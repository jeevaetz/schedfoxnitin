/**
 *  FileName:  Employee_View_AdditonalFields_Files.java
 *  @author Jeffrey Davis
 *  Date Created:  01/04/2011
 *  Modifications:
 */

//  package declaration
package rmischedule.components;

//  import declarations
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.misc.AdditionalFieldsLoader;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_name_additional_field_query;

/**
 *  Purpose of Class:  class displays a JInternalFrame form to display all files
 *      associated with with a specific DynamicFieldAdditionalFileType.  Users
 *      are also allowed to download and remove files form this form
 *  @author Jeffrey Davis
 *  @see <code>Employee_Files</code> for code model
 */
public class Employee_View_Additional_Fields_Files extends javax.swing.JInternalFrame
{
    //  private variable declarations
    private Additional_Fields_File_Table fileTable;
    private DynamicFieldAdditionalFileType myParent;
    private Employee_View_Additional_Fields_Files thisEVAFF;

    //  private method implmentations
    /**
     *  Purpose of Method:  sets all information labels
     *  @param numFiles - an integer describing the number of files
     */
    private void setLabels(int numFiles)
    {
        //  set employee label
        this.jEmployeeNameLabel.setText("Employee:  " + this.getEmployeeName());

        //  set company label
        this.jCompanyNameLabel.setText("Company:  " + this.myParent.getCompanyName());

        //  set field type label
        this.jFieldTypeLabel.setText("Additional Field Type:  " + this.myParent.getFieldType());

        //  set number of files label
        this.jNumberFilesLabel.setText("Number of File(s):  " + numFiles);
    }

    /**
     *  Purpose of Method:  hits the DB to determine the name of the employee,
     *      formats the name, returns it
     *  @return employeeName - a string representing the properly formatted employee name
     */
    private String getEmployeeName()
    {
        StringBuilder employeeName = new StringBuilder();

        //  setup connection, record set, query
        Record_Set rs = new Record_Set();
        Connection myConnection = new Connection();
        myConnection.setCompany(this.myParent.getCompanyForConnection());
        myConnection.setBranch(this.myParent.getBranchForConnection());
        employee_name_additional_field_query query = new employee_name_additional_field_query(this.myParent.getEmployeeId());
        myConnection.prepQuery(query);

        //  connect to database
        try
        {
            rs = myConnection.executeQuery(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        
        //  parse record set
        if(rs.length() > 0)
        {
            String firstName = rs.getString("first");
            String lastName = rs.getString("last");
            
            employeeName.append(firstName);
            employeeName.append(" ");
            employeeName.append(lastName);
        }

        return employeeName.toString();
    }

    /**
     *  Purpose of Method:  calls AdditionalFieldsLoader to retrieve file from
     *      image server, displays it via <code>Desktop.getDesktop()</code>
     *  @param rowIndex - an int describing which row in the table has been selected
     *  @see <code>AdditionalFieldsLoader.getFile()</code> for communication with Image Server
     */
    private void openFile(int rowIndex)
    {
        //  get file name to retrieve
        String fileName = this.fileTable.getFileName(rowIndex);

        //  get file from server
        File retrievedFile = AdditionalFieldsLoader.getFile(myParent.getCompanySchema(),
            myParent.getEmployeeId(), myParent.getFieldType(), fileName);

        //  display if present
        if(retrievedFile == null)
            JOptionPane.showMessageDialog(this,
                "There was an error downloading the file.  Please contact Schedfox Administrators.",
                "Error downloading File.", JOptionPane.ERROR_MESSAGE);
        else
        {
            //  set cursor
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            //  open file via desktop
            try
            {
                Desktop.getDesktop().edit(retrievedFile);
            }
            catch (IOException ex)
            {
                Logger.getLogger(DynamicFieldAdditionalFileType.class.getName()).log(Level.SEVERE, null, ex);
            }

            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }

    /**
     *  Purpose of Method:  calls AdditionalFieldsLoader to remove the selected file,
     *      informs user if successfull
     *  @param row - an int describing which row in the table has been selected
     *  @see <code>AdditionalFieldsLoader.removeFile()</code> for communication with ImageServer
     */
    private void removeFile(int row)
    {
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to remove this file?",
                "Remove file?", JOptionPane.YES_NO_OPTION);
        if ( response == 0)
        {
             //  set cursor
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            //  call AdditionalFieldsLoader
           // public static boolean removeFile(String companySchema, String key, String fieldType,
        //String userId, String fileName)
            boolean removeSuccessfull = AdditionalFieldsLoader.removeFile(
                myParent.getCompanySchema(),
                myParent.getEmployeeId(),
                myParent.getFieldType(),
                myParent.getUserId(),
                this.fileTable.getFileName(row));

            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);

            //  display success
            if(removeSuccessfull)
            {
                JOptionPane.showMessageDialog(this, "The file was successfully removed.",
                    "Remove File", JOptionPane.INFORMATION_MESSAGE);

                //  update if file present
                myParent.reload();
            }
            else
                JOptionPane.showMessageDialog(this, "ERROR:  The file was not removed.  Please contact Schedfox administrators.",
                    "Remove File", JOptionPane.ERROR_MESSAGE);
        }
    }


    /** Creates new default form Employee_View_Additional_Fields_Files */
    public Employee_View_Additional_Fields_Files()
    {
        this.fileTable = new Additional_Fields_File_Table(this);
        this.myParent = null;
        this.thisEVAFF = this;
        initComponents();
        

        this.jFileTable.getColumnModel().getColumn(1).setCellRenderer(new MyCellRenderer());
        this.jFileTable.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
        this.jFileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        this.jFileTable.getColumnModel().getColumn(0).setPreferredWidth(665);
        this.jFileTable.getColumnModel().getColumn(1).setPreferredWidth(85);
        this.jFileTable.getColumnModel().getColumn(2).setPreferredWidth(85);
        this.jFileTable.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                if (e.getClickCount() == 2)
                {
                    JTable target = (JTable)e.getSource();
                    int row = target.getSelectedRow();
                    int column = target.getSelectedColumn();
                    if(column == 1)
                        thisEVAFF.openFile(row);
                    else if(column == 2)
                        thisEVAFF.removeFile(row);
                }
            }
        });       
    }

    /**
     *  Purpose of Method:  sets <code>DynamicFieldAdditionalFileType myParent</code>
     *  @param myParent - the instance of <code>DynamicFieldAdditionalFileType</code>
     *      that has called this object
     */
    public void setParent(DynamicFieldAdditionalFileType myParent)
    {
        this.myParent = null;
        this.myParent = myParent;
    }

    /**
     *  Purpose of Method:  initializes this class to display relevanet data
     *      specific to <code>myParent</code>
     */
    public void initialize()
    {
        this.setTitle("File(s) for " + this.myParent.getFieldType());
        this.fileTable.clear();
        ArrayList<String> fileNames = AdditionalFieldsLoader.getFileNames(
            this.myParent.getCompanySchema(),
            this.myParent.getEmployeeId(),
            this.myParent.getFieldType(),
            "employee_additional_fields_files");

        this.setLabels(fileNames.size());

        this.fileTable.setFileNamesArray(fileNames);
    }

    //  private inner class
    /**
     *  Class Name:  Additional_Fields_File_Table
     *  Purpose of Class:  class displays information relation to all files
     *      associated with an additional filed per employee
     */
    private final class Additional_Fields_File_Table extends AbstractTableModel
    {
        //  private variable declarations
        private Employee_View_Additional_Fields_Files myParent;
        private ArrayList<String> fileNamesArray;
        private JLabel openLabel;
        private JLabel removeLabel;

        //  public method implementations
        /*  Create default instance of class    */
        public Additional_Fields_File_Table()
        {
            this.myParent = null;
            this.fileNamesArray = null;
            this.openLabel = null;
            this.removeLabel = null;
        }

        /**
         *  Purpose of Method:  creates an instance of this class
         *  @param myParent - the instance of <code>Employee_View_Additional_Fields_Files</code>
         *      that calls this class
         */
        public Additional_Fields_File_Table(Employee_View_Additional_Fields_Files myParent)
        {
            this.myParent = myParent;
            this.fileNamesArray = new ArrayList<String>();
            this.openLabel = new JLabel();
            this.removeLabel = new JLabel();
        }

        /**
         *  Purpose of Method:  resets table by clearing <code>fileNamesArray</code>,
         *      then calling <code>fireTableDataChanged</code> to clear GUI
         */
        public void clear()
        {
            this.fileNamesArray.clear();
            this.fireTableDataChanged();
        }

        /**
         *  Purpose of Method:  sets inner class <code>fileNamesArray</code>
         *  @param fileNamesArray - an array of strings representing the file names
         *      to be displayed
         */
        public void setFileNamesArray(ArrayList<String> fileNamesArray)
        {
            this.fileNamesArray = null;
            this.fileNamesArray = fileNamesArray;
        }

        /**
         *  Purpose of Method:  determines the file name asked for by the param,
         *      returns it
         *  @param rowIndex - an int describing the row
         *  @return fileName - a string describing the file name
         */
        public String getFileName(int rowIndex) {return this.fileNamesArray.get(rowIndex);}


        /*  Overridden methods to setup Table   */
        @Override
        public int getRowCount()
        {
            if(this.fileNamesArray == null)
                return 0;
            else
                return this.fileNamesArray.size();
        }

        @Override
        public int getColumnCount() {return 3;}

        @Override
        public Object getValueAt(int rowIndex, int columnIndex)
        {
            switch( columnIndex)
            {
                case 0: return this.fileNamesArray.get(rowIndex);
                case 1: return this.openLabel;
                case 2: return this.removeLabel;
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int columnIndex)
        {
            StringBuilder columnName = new StringBuilder();

            if (columnIndex == 0)
                columnName.append("File Name");
            else if (columnIndex == 1)
                columnName.append("Open File");
            else if (columnIndex == 2)
                columnName.append("Remove File");

            return columnName.toString();
        }
    }

        /**
         * Class Name:  MyCellRenderer
         * Purpose of Class:  defines how each cell will be rendered within the table
        */
        private class MyCellRenderer extends DefaultTableCellRenderer
        {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column)
            {
                if(column == 1)
                {
                    ((JLabel)value).setIcon(Main_Window.Edit_Notes_Icon);
                    ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
                }
                else if(column == 2)
                {
                    ((JLabel)value).setIcon(Main_Window.Delete24x24);
                    ((JLabel)value).setHorizontalAlignment(JLabel.CENTER);
                }

                return ((JLabel) value);
            }
        }
    

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
        jEmployeeCompanyPanel = new javax.swing.JPanel();
        jEmployeeNamePanel = new javax.swing.JPanel();
        jEmployeeNameLabel = new javax.swing.JLabel();
        jCompanyNamePanel = new javax.swing.JPanel();
        jCompanyNameLabel = new javax.swing.JLabel();
        jInformationPanel = new javax.swing.JPanel();
        jFieldTypePanel = new javax.swing.JPanel();
        jFieldTypeLabel = new javax.swing.JLabel();
        jNumberFilesPanel = new javax.swing.JPanel();
        jNumberFilesLabel = new javax.swing.JLabel();
        jTabelPanel = new javax.swing.JPanel();
        jFileTabelScrollPane = new javax.swing.JScrollPane();
        jFileTable = new javax.swing.JTable();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setNormalBounds(new java.awt.Rectangle(0, 0, 57, 0));
        setPreferredSize(new java.awt.Dimension(226, 67));

        jBasePanel.setLayout(new java.awt.BorderLayout());

        jControlPanel.setLayout(new java.awt.GridLayout(1, 2));

        jEmployeeCompanyPanel.setLayout(new java.awt.GridLayout(2, 1));

        jEmployeeNamePanel.setLayout(new java.awt.GridBagLayout());
        jEmployeeNamePanel.add(jEmployeeNameLabel, new java.awt.GridBagConstraints());

        jEmployeeCompanyPanel.add(jEmployeeNamePanel);

        jCompanyNamePanel.setLayout(new java.awt.GridBagLayout());
        jCompanyNamePanel.add(jCompanyNameLabel, new java.awt.GridBagConstraints());

        jEmployeeCompanyPanel.add(jCompanyNamePanel);

        jControlPanel.add(jEmployeeCompanyPanel);

        jInformationPanel.setLayout(new java.awt.GridLayout(2, 1));

        jFieldTypePanel.setLayout(new java.awt.GridBagLayout());
        jFieldTypePanel.add(jFieldTypeLabel, new java.awt.GridBagConstraints());

        jInformationPanel.add(jFieldTypePanel);

        jNumberFilesPanel.setLayout(new java.awt.GridBagLayout());
        jNumberFilesPanel.add(jNumberFilesLabel, new java.awt.GridBagConstraints());

        jInformationPanel.add(jNumberFilesPanel);

        jControlPanel.add(jInformationPanel);

        jBasePanel.add(jControlPanel, java.awt.BorderLayout.PAGE_START);

        jTabelPanel.setLayout(new java.awt.BorderLayout());

        jFileTable.setModel(fileTable);
        jFileTabelScrollPane.setViewportView(jFileTable);

        jTabelPanel.add(jFileTabelScrollPane, java.awt.BorderLayout.CENTER);

        jBasePanel.add(jTabelPanel, java.awt.BorderLayout.CENTER);

        getContentPane().add(jBasePanel, java.awt.BorderLayout.CENTER);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-853)/2, (screenSize.height-531)/2, 853, 531);
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jBasePanel;
    private javax.swing.JLabel jCompanyNameLabel;
    private javax.swing.JPanel jCompanyNamePanel;
    private javax.swing.JPanel jControlPanel;
    private javax.swing.JPanel jEmployeeCompanyPanel;
    private javax.swing.JLabel jEmployeeNameLabel;
    private javax.swing.JPanel jEmployeeNamePanel;
    private javax.swing.JLabel jFieldTypeLabel;
    private javax.swing.JPanel jFieldTypePanel;
    private javax.swing.JScrollPane jFileTabelScrollPane;
    private javax.swing.JTable jFileTable;
    private javax.swing.JPanel jInformationPanel;
    private javax.swing.JLabel jNumberFilesLabel;
    private javax.swing.JPanel jNumberFilesPanel;
    private javax.swing.JPanel jTabelPanel;
    // End of variables declaration//GEN-END:variables
};
