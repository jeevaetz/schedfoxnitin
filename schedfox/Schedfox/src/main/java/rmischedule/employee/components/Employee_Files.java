/**
 *  Filename:  Employee_Files.java
 *  Author:  Jeffrey Davis
 *  Date Created:  09/23/2010
 *  Modifications:
 *  Purpose of File:  file contains a class designed to sit within the employee
 *      edit window for file manipulation
 */
//  package declaration
package rmischedule.employee.components;

//  import declarations
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.employee.data_components.Employee_Files_Data;
import rmischedule.employee.xEmployeeEdit;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.FileLoader;
import rmischedule.security.User;
import rmischeduleserver.IPLocationFile;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_noncertification_query;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;

/**
 *  Class Name:  Employee_Files
 *  Purpose of Class:  a class designed to sit within the employee
 *      edit window for file manipulation
 */
public class Employee_Files extends GenericEditSubForm {
    //  private variable declarations

    private JFileChooser fchooser;
    private xEmployeeEdit myParent;
    private ArrayList<Employee_Files_Data> fileArray;
    private Employee_File_Table fileTable;
    private Employee_Files thisEF;

    //  private method implementations
    /**
     *  Method Name:  addFile
     *  Purpose of Method:  asks the user to select a file to save, then
     *      calls FileLoader to execute
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has clicked "Add File" Button, no action performed
     *  Postconditions:  file to save selected, FileLoader executed to save,
     *      user informed if save pass/failed
     */
    private void addFile() {
        if (fchooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                //  set cursor
                Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                setCursor(hourglassCursor);

                //  check for collisions
                if (!this.saveHasNamingCollision(fchooser.getSelectedFile())) {
                    this.saveFileToServer(fchooser.getSelectedFile());
                    this.loadFiles();
                    this.refreshScreen();
                } else {
                    // ask user if they wish to overwrite
                    int response = JOptionPane.showConfirmDialog(this, "A file with "
                            + "the same name has been detected on the server.  Do you "
                            + "wish to overwrite?", "Overwrite existing file?", JOptionPane.YES_NO_OPTION);
                    if (response == 0) {
                        //  overwrite file
                        this.saveFileToServer(fchooser.getSelectedFile());
                        this.loadFiles();
                        this.refreshScreen();
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "There was an error saving your file.",
                        "FileServer Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                //  reset cursor
                Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                setCursor(normalCursor);
            }   // finally
        }   //  if
    }   //  method

    /**
     *  Method Name:  getCompanySchema
     *  Purpose of Method:  method determines the company schema, then returns
     *      it
     *  Arguments:  none
     *  Returns:  a string containing the company schema
     *  Preconditions:  company schema unknown
     *  Postconditions:  company schema known, returned
     */
    private String getCompanySchema() {
        StringBuffer companySchema = new StringBuffer();

        String currentCompanyId = myparent.getConnection().myCompany;
        Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);
        companySchema.append(comp.getDB().trim());

        return companySchema.toString().trim();
    }

    /**
     *  Method Name:  getEmployeeId
     *  Purpose of Method:  method determines the employee_id, then returns it
     *  Arguments:  none
     *  Returns:  a string containing the employee id
     *  Preconditions:  employee id unknown
     *  Postconditions:  employee id known, returned
     */
    private String getEmployeeId() {
        return this.myParent.getMyIdForSave();
    }

    /**
     *  Method Name:  loadFiles
     *  Purpose of Method:  hits the FileServer for all files for an employee,
     *      then loads them into an internal data structure
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  files on fileserver, not known internally
     *  Postconditions:  files loaded from file server and into data structure
     */
    private void loadFiles() {
        Runnable fetchDocuments = new Runnable() {

            public void run() {
                final ArrayList<String> fileLocations = FileLoader.getFileNames(getCompanySchema(),
                        getEmployeeId(), "employee_additional_files");
                //load files
                ArrayList<File> filesFromServer = new ArrayList<File>();
                if (fileLocations.size() > 0) {
                    filesFromServer = FileLoader.getFiles(fileLocations);
                }
                //Load into internal data structure
                fileArray.clear();
                if (filesFromServer.size() == fileLocations.size()) {
                    for (int idx = 0; idx < filesFromServer.size(); idx++) {
                        File tempFile = filesFromServer.get(idx);
                        Employee_Files_Data tempData = new Employee_Files_Data(
                                getFileName(fileLocations.get(idx)), tempFile.getAbsolutePath(),
                                tempFile);
                        fileArray.add(tempData);
                    }
                }
            }
        };
        new Thread(fetchDocuments).start();
    }

    /**
     *  Method Name:  refreshScreen
     *  Purpose of Method:  method parses out information to be shown in the gui,
     *      then updates the screen
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  data to be display known internally, not displayed in
     *      gui
     *  Postconditions:  relevant data displayed on screen
     */
    private void refreshScreen() {
        //  update label
        this.jNumFilesLabel.setText("Number of Employee files:  " + fileArray.size());
        this.jInformationPanel.validate();
        this.jInformationPanel.repaint();

        //  update table
        this.fileTable.setDataArray(fileArray);
        this.fileTable.fireTableDataChanged();
        this.jFileTableScrollPanel.validate();
        this.jFileTableScrollPanel.repaint();
    }

    /**
     *  Method Name:  getFileName
     *  Purpose of Method:  method takes an absolute path name, and returns
     *      the "fileName"; the last part of the path
     *  Arguments:  a string containing the full file path
     *  Returns:  a string containing the file name
     *  Preconditions:  full file path known, file name unknown
     *  Postconditions:  file name known, returned
     */
    private String getFileName(String argFilePath) {
        //  split path
        StringTokenizer st = new StringTokenizer(argFilePath, IPLocationFile.getIMAGE_SERVER_FILE_SEPARATOR());
        ArrayList<String> path = new ArrayList<String>();
        while (st.hasMoreTokens()) {
            path.add(st.nextToken());
        }
        //  get filename
        return path.get(path.size() - 1);
    }

    /**
     *  Method Name:  editFile
     *  Purpose of Method:  opens a new Desktop window to edit a selected file
     *  Arguments:  an int describing the row
     *  Returns:  void
     *  Preconditions:  user has clicked the edit icon for a file, file not opened
     *  Postconditions:  file opened in new window
     */
    private void openFile(int idx) {
        //  set cursor
        Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
        setCursor(hourglassCursor);

        //  open file via desktop
        try {
            Desktop.getDesktop().edit(fileArray.get(idx).getFile());
        } catch (IOException ex) {
            Logger.getLogger(Employee_Files.class.getName()).log(Level.SEVERE, null, ex);
        }

        //  reset cursor
        Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
        setCursor(normalCursor);
    }

    /**
     *  Method Name:  removeFile
     *  Purpose of Method:  method calls FileLoader to remove a file
     *  Arguments:  an int describing the position in the array for file removal
     *  Returns:  void
     *  Preconditions:  user has asked to remove a file, file not removed
     *  Postconditions:  file removed
     */
    private void removeFile(int idx) {
        // ask user if they wish to overwrite
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this file?",
                "Remove file?", JOptionPane.YES_NO_OPTION);
        if (response == 0) {
            //  set cursor
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            boolean isSuccessfull = FileLoader.removeAdditionalFile(this.getCompanySchema(), "remove_additional_employee_files",
                    "_", this.getEmployeeId(), this.getUserId(), (String) this.fileTable.getValueAt(idx, 0));

            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);

            //  display success
            if (isSuccessfull) {
                JOptionPane.showMessageDialog(this, "The file was successfully removed.",
                        "Remove File", JOptionPane.INFORMATION_MESSAGE);

                //  reload/refresh screen
                this.loadFiles();
                this.refreshScreen();
            } else {
                JOptionPane.showMessageDialog(this, "ERROR:  The file was not removed.",
                        "Remove File", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     *  Method Name:  saveHasNamingCollision
     *  Purpose of Method:  checks to see if the file to save is already present
     *      via name on file server.
     *  Arguments:  a file to check against
     *  Returns:  a boolean describing if the file has collisions
     *  Preconditions:  user wishes to save a file, collision status unknown
     *  Postconditions:  collisions tested, returned
     */
    private boolean saveHasNamingCollision(File argFile) {
        boolean hasCollision = false;

        //  get list of file names from server
        ArrayList<String> fileLocations = new ArrayList<String>();
        fileLocations = FileLoader.getFileNames(this.getCompanySchema(),
                this.getEmployeeId(), "employee_additional_files");
        String argFileName = argFile.getName();

        // check file names from server against local filename
        for (int idx = 0; idx < fileLocations.size(); idx++) {
            String fileNameFromServer = this.getFileName(fileLocations.get(idx));
            if (argFileName.matches(fileNameFromServer)) {
                hasCollision = true;
            }
        }

        return hasCollision;
    }

    /**
     *  Method Name:  saveFileToServer
     *  Purpose of Method:  saves the file stored in the fchooser to the server
     *  Arguments:  a file to save to server
     *  Returns:  void
     *  Preconditions:  user has selected to save a file to server, file not saved
     *  Postconditions:  file saved to server
     */
    private void saveFileToServer(File fileToSave) {
        boolean isSuccesfull = FileLoader.saveAdditionalFile("employee_additional_files",
                this.getCompanySchema(), this.getEmployeeId(), fileToSave.getName(), fileToSave);

        //  tell user if succesful
        if (isSuccesfull) {
            JOptionPane.showMessageDialog(this, "The file was saved successfully to the server.",
                    "Save file to server", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error:  file was not saved to server.",
                    "Save file to server", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *  Method Name:  getUserId
     *  Purpose of Method:  determines the user id, returns it as a string
     *  Arguments:  none
     *  Returns:  a string containing the user id
     *  Preconditions:  user id unknown
     *  Postconditions:  user id known and returned
     */
    private String getUserId() {
        User user = Main_Window.parentOfApplication.getUser();
        return user.getUserId();
    }

    //  public method implmentations
    public Employee_Files(xEmployeeEdit main) {
        //  initialize class variables
        this.myParent = main;
        fileArray = new ArrayList<Employee_Files_Data>();
        fileTable = new Employee_File_Table(this);
        this.thisEF = this;

        //  initialize swing components
        initComponents();
        this.jFileTable.getColumnModel().getColumn(1).setCellRenderer(new MyCellRenderer());
        this.jFileTable.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        this.jFileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.jFileTable.getColumnModel().getColumn(0).setPreferredWidth(521);
        this.jFileTable.getColumnModel().getColumn(1).setPreferredWidth(75);
        this.jFileTable.getColumnModel().getColumn(2).setPreferredWidth(75);
        this.jFileTable.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    JTable target = (JTable) e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if (column == 1) {
                        thisEF.openFile(row);
                    } else if (column == 2) {
                        thisEF.removeFile(row);
                    }
                }
            }
        });


        //  setup file chooser
        this.fchooser = new JFileChooser();
        this.fchooser.setDialogTitle("Select a file to save for this employee.");
        this.fchooser.setAcceptAllFileFilterUsed(true);
        this.fchooser.setMultiSelectionEnabled(false);
    }

    //  java swing code
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jBasePanel = new javax.swing.JPanel();
        jControlPanel = new javax.swing.JPanel();
        jAddFilePanel = new javax.swing.JPanel();
        jAddFilesButton = new javax.swing.JButton();
        jInformationPanel = new javax.swing.JPanel();
        jNumFilesLabel = new javax.swing.JLabel();
        jTablePanel = new javax.swing.JPanel();
        jFileTableScrollPanel = new javax.swing.JScrollPane();
        jFileTable = new javax.swing.JTable();

        setLayout(new java.awt.BorderLayout());

        jBasePanel.setLayout(new java.awt.BorderLayout());

        jControlPanel.setLayout(new java.awt.GridLayout(0, 2));

        jAddFilePanel.setLayout(new java.awt.GridBagLayout());

        jAddFilesButton.setText("Add File");
        jAddFilesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jAddFilesButtonActionPerformed(evt);
            }
        });
        jAddFilePanel.add(jAddFilesButton, new java.awt.GridBagConstraints());

        jControlPanel.add(jAddFilePanel);

        jInformationPanel.setLayout(new java.awt.GridBagLayout());
        jInformationPanel.add(jNumFilesLabel, new java.awt.GridBagConstraints());

        jControlPanel.add(jInformationPanel);

        jBasePanel.add(jControlPanel, java.awt.BorderLayout.PAGE_START);

        jTablePanel.setLayout(new java.awt.BorderLayout());

        jFileTable.setModel(fileTable);
        jFileTableScrollPanel.setViewportView(jFileTable);

        jTablePanel.add(jFileTableScrollPanel, java.awt.BorderLayout.CENTER);

        jBasePanel.add(jTablePanel, java.awt.BorderLayout.CENTER);

        add(jBasePanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jAddFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jAddFilesButtonActionPerformed
        this.addFile();
    }//GEN-LAST:event_jAddFilesButtonActionPerformed

    //  abstract method implmentations
    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        return new GenericQuery("SELECT NOW()");
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return new GenericQuery("SELECT NOW()");
    }

    @Override
    public void loadData(Record_Set rs) {
        this.loadFiles();
        this.refreshScreen();
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Files";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
        this.fileArray.clear();
        this.fileTable.clear();
    }

    @Override
    public boolean userHasAccess() {
        return true;
//        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

    public JTable getTable() {
        return this.jFileTable;
    }

    //  private class
    /**
     *  Class Name:  Employee_File_Table
     *  Purpose of Class:  class displays information relating to all files
     *      associated with an employee
     */
    private class Employee_File_Table extends AbstractTableModel {
        //  private variable declaration

        ArrayList<Employee_Files_Data> dataArray;
        JLabel openLabel;
        JLabel removeLabel;
        Employee_Files myParent;

        //  private method implementations
        //  public method implementation
        /**
         *  Method Name:  Employee_File_Table
         *  Purpose of Method:  creates a default instance of this class
         *  Arguments:  none
         *  Returns:  none
         *  Preconditions:  object DNE
         *  Postconditions:  object exists, variables initialized
         */
        public Employee_File_Table(Employee_Files main) {
            // initialize variables
            this.dataArray = new ArrayList<Employee_Files_Data>();
            myParent = main;
            openLabel = new JLabel();
            removeLabel = new JLabel();
        }

        public void setDataArray(ArrayList<Employee_Files_Data> argArray) {
            this.dataArray = null;
            this.dataArray = argArray;
        }

        public void clear() {
            this.dataArray.clear();
            this.fireTableDataChanged();
        }

        public int getRowCount() {
            return this.dataArray.size();
        }

        public int getColumnCount() {
            return 3;
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            Employee_Files_Data data = dataArray.get(rowIndex);

            //  return value
            switch (columnIndex) {
                case 0:
                    return data.getFileName();
                case 1:
                    return openLabel;
                case 2:
                    return removeLabel;
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int columnIndex) {
            String returnString = null;
            if (columnIndex == 0) {
                returnString = "File Name";
            } else if (columnIndex == 1) {
                returnString = "Open File";
            } else if (columnIndex == 2) {
                returnString = "Remove File";
            }

            return returnString;
        }
    }

    /**
     *  Class Name:  MyCellRenderer
     *  Purpose of Class:  defines how each cell will be rendered within the table
     */
    private class MyCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

            if (column == 1) {
                ((JLabel) value).setIcon(Main_Window.Edit_Notes_Icon);
                ((JLabel) value).setHorizontalAlignment(JLabel.CENTER);
            } else if (column == 2) {
                ((JLabel) value).setIcon(Main_Window.Delete24x24);
                ((JLabel) value).setHorizontalAlignment(JLabel.CENTER);
            }

            return ((JLabel) value);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jAddFilePanel;
    private javax.swing.JButton jAddFilesButton;
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JPanel jControlPanel;
    private javax.swing.JTable jFileTable;
    private javax.swing.JScrollPane jFileTableScrollPanel;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jNumFilesLabel;
    private javax.swing.JPanel jTablePanel;
    // End of variables declaration//GEN-END:variables
};
