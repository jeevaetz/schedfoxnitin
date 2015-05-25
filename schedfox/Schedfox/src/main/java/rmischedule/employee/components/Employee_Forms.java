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
import javax.swing.table.DefaultTableModel;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.employee.data_components.Employee_Files_Data;
import rmischedule.employee.xEmployeeEdit;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import schedfoxlib.model.util.FileLoader;
import rmischedule.security.User;
import rmischedule.security.security_detail;
import rmischeduleserver.IPLocationFile;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_state_noncertification_query;
import rmischedule.forms.FormsClient;


/**
 *  Class Name:  Employee_Files
 *  Purpose of Class:  a class designed to sit within the employee
 *      edit window for file manipulation
 */
public class Employee_Forms extends GenericEditSubForm
{
    //  private variable declarations
     private JFileChooser fchooser;
     private xEmployeeEdit myParent;
     private ArrayList<Employee_Files_Data> fileArray;
     private Employee_File_Table fileTable;
     private Employee_Forms thisEF;
     private Object forms[][]=new Object[12][1];
     private String title[] = new String[] {"Forms"};
     private DefaultTableModel fileModel;



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
    private void addFile()
    {
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
    private String getCompanySchema()
    {
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
    private String getEmployeeId()
    {
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
    private void loadFiles()
    {

        forms[0][0]="Aetna";
        forms[1][0]="Avaliability";
        forms[2][0]="BackGroundCheck";
        forms[3][0]="CompanyVehicleUsage";
        forms[4][0]="Drugs And Alcohol";
        forms[5][0]="FrugFreeConsent";
        forms[6][0]="EmployeeNewHire";
        forms[7][0]="Form-9";
        forms[8][0]="Level2";
        forms[9][0]="Orientation";
        forms[10][0]="RegAndUniform";
        forms[11][0]="Registration";
        fileModel=new DefaultTableModel(forms,title);
    }

    public void printForms(){
        FormsClient fc = new FormsClient();
        fc.printForms("DrugAndAlchohol");
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
    private void refreshScreen()
    {
//        this.fileTable.setDataArray(fileArray);
        this.fileTable.fireTableDataChanged();
        this.jFileTableScrollPanel.validate();
        this.jFileTableScrollPanel.repaint();
    }


    private String getUserId()
    {
        User user = Main_Window.parentOfApplication.getUser();
        return user.getUserId();
    }

    //  public method implmentations
    public Employee_Forms(xEmployeeEdit main)
    {
        //  initialize class variables
        this.myParent = main;
        fileArray = new ArrayList<Employee_Files_Data>();
        fileTable = new Employee_File_Table(this);
        this.thisEF = this;

        //  initialize swing components
        initComponents();
        loadFiles();
        this.jFileTable.setModel(fileModel);
//        this.jFileTable.getColumnModel().getColumn(1).setCellRenderer(new MyCellRenderer());
//        this.jFileTable.getColumnModel().getColumn(2).setCellRenderer(new MyCellRenderer());
//        this.jFileTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//        this.jFileTable.getColumnModel().getColumn(0).setPreferredWidth(521);
//        this.jFileTable.getColumnModel().getColumn(1).setPreferredWidth(75);
//        this.jFileTable.getColumnModel().getColumn(2).setPreferredWidth(75);
//        this.jFileTable.addMouseListener(new MouseAdapter()
//        {
//            public void mouseClicked(MouseEvent e)
//            {
//                if (e.getClickCount() == 2)
//                {
//                    JTable target = (JTable)e.getSource();
//                    int row = target.getSelectedRow();
//                    int column = target.getSelectedColumn();
//                    if(column == 1)
//                        thisEF.openFile(row);
//                    else if(column == 2)
//                        thisEF.removeFile(row);
//                }
//            }
//        });

        
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

        jAddFilesButton.setText("Print");
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
       this.printForms();
    }//GEN-LAST:event_jAddFilesButtonActionPerformed

    //  abstract method implmentations
    @Override
    public GeneralQueryFormat getQuery(boolean isSelected)
    {
         /**
         * dummy query for testing
         */
        employee_state_noncertification_query query = new employee_state_noncertification_query();

        return query;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData)
    {
         /**
         * dummy query for testing
         */
        employee_state_noncertification_query query = new employee_state_noncertification_query();

        return query;
    }

    @Override
    public void loadData(Record_Set rs)
    {
        //  load files
        this.loadFiles();

        //  display information
        this.refreshScreen();
    }

    @Override
    public boolean needsMoreRecordSets()
    {
        return false;
    }

    @Override
    public String getMyTabTitle()
    {
        return "New Employee Forms";
    }

    @Override
    public JPanel getMyForm()
    {
        return this;
    }

    @Override
    public void doOnClear()
    {
        this.fileArray.clear();
        this.fileTable.clear();
    }

    @Override
    public boolean userHasAccess()
    {
        return true;
//        return Main_Window.mySecurity.checkSecurity(security_detail.MODULES.EMPLOYEE_EDIT);
    }

    public JTable getTable()
    {
        return this.jFileTable;
    }

    //  private class
    /**
     *  Class Name:  Employee_File_Table
     *  Purpose of Class:  class displays information relating to all files
     *      associated with an employee
     */
    private class Employee_File_Table extends AbstractTableModel 
    {
        //  private variable declaration
        ArrayList<File> dataArray;
        JLabel openLabel;
        JLabel removeLabel;
        Employee_Forms myParent;


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
         public Employee_File_Table(Employee_Forms main)
         {
            // initialize variables
            this.dataArray = new ArrayList<File>();
            myParent = main;
            openLabel = new JLabel();
            removeLabel = new JLabel();
         }


        public void setDataArray(ArrayList<File> argArray)
        {
            this.dataArray = null;
            this.dataArray = argArray;
        }

        public void clear()
        {
            this.dataArray.clear();
            this.fireTableDataChanged();
        }

        public int getRowCount()
        { return this.dataArray.size(); }
        public int getColumnCount() { return 3;}

        public Object getValueAt(int rowIndex, int columnIndex)
        {
            File data = dataArray.get(rowIndex);

            //  return value
            switch(columnIndex)
            {
                case 0:  return data.getName();
                case 1:  return openLabel;
                case 2:  return removeLabel;
                default:
                    return null;
            }
        }

        @Override
        public String getColumnName(int columnIndex)
        {
            String returnString = null;
            if (columnIndex == 0)
                returnString = "File Name";
            else if (columnIndex == 1)
                returnString = "Open File";
            else if (columnIndex == 2)
                returnString = "Remove File";

            return returnString;
        }
    }

    /**
     *  Class Name:  MyCellRenderer
     *  Purpose of Class:  defines how each cell will be rendered within the table
     */
    private class MyCellRenderer extends DefaultTableCellRenderer
    {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {

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
