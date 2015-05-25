/**
 *  FileName:  DynamicFieldAdditionalFileType.java
 *  Date Created:  12/15/2010
 *  @author Jeffrey N. Davis
 *  Last Revision:
 */
//  package definition
package rmischedule.components;

//  import definitions
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.beans.PropertyVetoException;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import rmischedule.components.graphicalcomponents.GenericTabbedEditForm;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.misc.AdditionalFieldsLoader;
import rmischedule.security.User;
import rmischedule.xadmin.model.DynamicFieldValue;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_additional_field_required_query;

/**
 *  Class Name:  DynamicFieldAdditionalFileType
 *  @author jdavis
 *  Purpose of Class:  a form containing the front end and its associated actions
 *      for handling files associated with additional fields
 */
public class DynamicFieldAdditionalFileType extends javax.swing.JPanel implements DynamicFieldTypeInterface
{
    //  private variable declarations
    private JFileChooser fChooser;
    private GenericTabbedEditForm myParent;
    private DynamicFieldValue value;
    private boolean hasFile;
    private boolean isRequired;
        
    //  private method implementations
    /**
     *  Method Name:  getFileName
     *  Purpose of Method:  constructs the file name to save on server
     *  @param initialFileName - the initial fileName to parse the extension
     *  @returns fileName - a correctly formatted filename
     */
    private String getFileName(String initialFileName)
    {
        StringBuffer fileName = new StringBuffer();

        fileName.append(this.getFieldType());
        fileName.append("_");
        fileName.append(this.getEmployeeId());
        fileName.append("_");

        Calendar rightNow = Calendar.getInstance();
        int DAY_OF_MONTH = rightNow.get(Calendar.DAY_OF_MONTH);
        int YEAR = rightNow.get(Calendar.YEAR);
        int MONTH = rightNow.get(Calendar.MONTH) + 1;

        fileName.append(MONTH);
        fileName.append(DAY_OF_MONTH);
        fileName.append(YEAR);

        Pattern p = Pattern.compile("\\.");
        String[] splitString = p.split(initialFileName);
        
        for(int idx = 1;idx <= (splitString.length - 1);idx ++)
        {
            fileName.append(".");
            fileName.append(splitString[idx]);
        }
        

        return fileName.toString();
    }

    /** Method Name:  saveFile
     *  Purpose of Method:  talks to AdditionalFieldsLoader to save a file
     *      to the ImageServer
     *  @see <code>AdditionalFieldsLoader.saveFile()</code> for communication with ImageServer
     */
    private void saveFile()
    {
       if (fChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                try
                {
                    //  set cursor
                    Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
                    setCursor(hourglassCursor);

                    //  save file
                    boolean isSuccess = AdditionalFieldsLoader.saveFile(this.getCompanySchema(),
                        this.getEmployeeId(), this.getFieldType(), fChooser.getSelectedFile(),
                        this.getFileName(fChooser.getSelectedFile().toString()));
                    if(isSuccess)
                        JOptionPane.showMessageDialog(this, "The file was saved successfully to the server.",
                            "Save file to server", JOptionPane.INFORMATION_MESSAGE);
                    else
                        JOptionPane.showMessageDialog(this, "Error:  file was not saved to server.",
                            "FileServer Error", JOptionPane.ERROR_MESSAGE);
                }
                catch(Exception ex)
                {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "There was an error saving the file.  Please contact Schedfox administrators.",
                        "FileServer Error", JOptionPane.ERROR_MESSAGE);
                }
                finally
                {
                    //  check for file
                    this.checkForFile();

                    //  reset cursor
                    Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
                    setCursor(normalCursor);
                }   //  end finally
            }   //  end if
    }   //  end method

    /**
     *  Purpose of Method:  opens an instance of <code>Employee_View_Additional_Fields_Files</code>
     *      and initializes it
     */
    private void openEmployeeViewAdditionalFieldsFilesWindow()
    {
        if(!this.hasFile)
            JOptionPane.showMessageDialog(this, "No file(s) present.", "View File(s)", JOptionPane.ERROR_MESSAGE);
        else
        {
            if( Main_Window.additionalFieldsFilesWindow == null)
            {
                Main_Window.additionalFieldsFilesWindow = new Employee_View_Additional_Fields_Files();
                Main_Window.parentOfApplication.desktop.add(Main_Window.additionalFieldsFilesWindow);
                Main_Window.additionalFieldsFilesWindow.setVisible(true);
                try
                {
                    Main_Window.additionalFieldsFilesWindow.setSelected(true);
                }
                catch(PropertyVetoException ex)
                {
                    Logger.getLogger(DynamicFieldAdditionalFileType.class.getName()).log(Level.SEVERE, null, ex);
                    ex.printStackTrace();
                }
                Main_Window.additionalFieldsFilesWindow.setParent(this);
                Main_Window.additionalFieldsFilesWindow.initialize();
            }
            else
            {
                Main_Window.additionalFieldsFilesWindow.setVisible(true);
                Main_Window.additionalFieldsFilesWindow.setParent(this);
                Main_Window.additionalFieldsFilesWindow.initialize();
            }
        }
    }

    /**
     *  Purpose of Method:  sets appropriate icons to visible/invisible
     */
    private void setIcons()
    {
        //  set required
        if(this.isRequired)
        {
            this.jRequiredLabel.setText("Required");
            this.jRequiredLabel.setToolTipText("Field Required.");
            this.jRequiredLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        }
        else
        {
            this.jRequiredLabel.setText("Optional");
            this.jRequiredLabel.setToolTipText("Field NOT Required.");
        }
        this.jRequiredLabel.setVisible(true);

        //  set present
        if(this.hasFile)
        {
            jPresentLabel.setText("File present.");
            jPresentLabel.setForeground(Color.BLACK);
        }
        else
        {
            jPresentLabel.setText("File not present!");
            jPresentLabel.setForeground(Color.RED);
        }
        this.repaint();
        this.revalidate();
    }

    /**
     *  Purpose of Method:  determines if this field is required, sets class boolean
     */
    private void determineIfFieldRequired()
    {
        Record_Set rs = new Record_Set();
        Connection myConnection = new Connection();
        myConnection.setCompany(myParent.getConnection().myCompany);
        myConnection.setBranch(myParent.getConnection().myBranch);

        employee_additional_field_required_query query = new employee_additional_field_required_query(this.getFieldType());
        myConnection.prepQuery(query);

        try
        {
            rs = myConnection.executeQuery(query);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }

        if(rs.length() > 0)
        {
            this.isRequired = rs.getBoolean("is_required");
        }
    }

    /**
     *  Purpose of Method:  calls AdditioanlFieldsLoader to check and see if
     *      a file is present for this additioanl field; sets appropriate icons
     *      after determination
     */
    private void checkForFile()
    {
        this.hasFile = AdditionalFieldsLoader.checkForFile(this.getCompanySchema(),
            this.getEmployeeId(), this.getFieldType());

        this.setIcons();
    }

    //  protected method implementations
    /**
     *  Method Name:  getCompanySchema
     *  Purpose of Method:  method determines the company schema, then returns
     *      it
     *  @return companySchema - a string containing the company schema
    */
    protected String getCompanySchema()
    {
        StringBuffer companySchema = new StringBuffer();

        String currentCompanyId = myParent.getConnection().myCompany;
        Company comp = Main_Window.parentOfApplication.getCompanyById(currentCompanyId);
        companySchema.append(comp.getDB().trim());

        return companySchema.toString().trim();
    }

    /**
     *  Method Name:  getEmployeeId
     *  Purpose of Method:  method determines the employee_id, then returns it
     *  @return empId -   a string containing the employee id
     *  Preconditions:  employee id unknown
    */
    protected String getEmployeeId()    {return this.myParent.getMyIdForSave();}

    /**
     *  Method Name:  getFieldType
     *  Purpose of Method:  determines what type of additional field this
     *      object is, returns it as a string
     *  @return fieldType - a string representing what type of field this object is
     */
    protected String getFieldType() {return value.getFieldDefObj().getName();}

    /**
     *  Purpose of Method:  returns the proper name of the current company
     *  @return companyName - a string representing the proper name of the current company
     */
    protected String getCompanyName()
    {
        Company company = Main_Window.parentOfApplication.getCompanyById(myParent.getConnection().myCompany);
        
        return company.getName();
    }

    /**
     *  Purpose of Method:  returns the company for connection
     *  @return company - a string representing the company for setting <code>Connection</code>
     */
    protected String getCompanyForConnection()  {return myParent.getConnection().myCompany;}

    /**
     *  Purpose of Method:  returns the branch for connection
     *  @return branch - a string representing the branch for setting <code>Connection</code>
     */
    protected String getBranchForConnection()   {return myParent.getConnection().myBranch;}

    /**
     *  Purpose of Method:  hits Main_Window to determine current user_id, returns is
     *  @return userId - a string representing the current user_id
     */
    protected String getUserId()
    {
        User user = Main_Window.parentOfApplication.getUser();
        return user.getUserId();
    }

    /**
     *  Purpose of Method:  reloads this object, reloads <code>Employee_View_Additional_Fields_Files</code>
     */
    protected void reload()
    {
        Main_Window.additionalFieldsFilesWindow.setVisible(false);
        this.checkForFile();
        if( this.hasFile )
            this.openEmployeeViewAdditionalFieldsFilesWindow();
    }

    //  public method implementations
    /**
     * Creates new form DynamicFieldAdditionalFileType
     * @param enabled - a boolean describing if this field is enabled
     * @param myParent - the instance of xEmployeeEdit that created this object
     */
    public DynamicFieldAdditionalFileType(boolean enabled, GenericTabbedEditForm myParent)
    {
        //  initialize class variables
        this.myParent = myParent;
        this.hasFile = false;
        this.isRequired = false;
        
        initComponents();

        //  setup file chooser
        this.fChooser = new JFileChooser();
        this.fChooser.setDialogTitle("Select a file to save for this field.");
        this.fChooser.setAcceptAllFileFilterUsed(true);
        this.fChooser.setMultiSelectionEnabled(false);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jRequiredLabel = new javax.swing.JLabel();
        jPresentLabel = new javax.swing.JLabel();
        jUploadButton = new javax.swing.JButton();
        jViewFilesButton = new javax.swing.JButton();

        setMaximumSize(new java.awt.Dimension(32767, 30));
        setMinimumSize(new java.awt.Dimension(0, 30));
        setPreferredSize(new java.awt.Dimension(500, 30));
        setLayout(new java.awt.GridBagLayout());

        jRequiredLabel.setMaximumSize(new java.awt.Dimension(75, 14));
        jRequiredLabel.setMinimumSize(new java.awt.Dimension(75, 14));
        jRequiredLabel.setPreferredSize(new java.awt.Dimension(75, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.weightx = 0.25;
        gridBagConstraints.weighty = 0.1;
        add(jRequiredLabel, gridBagConstraints);

        jPresentLabel.setMaximumSize(new java.awt.Dimension(100, 14));
        jPresentLabel.setMinimumSize(new java.awt.Dimension(100, 14));
        jPresentLabel.setPreferredSize(new java.awt.Dimension(100, 14));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.weightx = 0.25;
        add(jPresentLabel, gridBagConstraints);

        jUploadButton.setText("Upload");
        jUploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jUploadButtonActionPerformed(evt);
            }
        });
        add(jUploadButton, new java.awt.GridBagConstraints());

        jViewFilesButton.setText("View File(s)");
        jViewFilesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jViewFilesButtonActionPerformed(evt);
            }
        });
        add(jViewFilesButton, new java.awt.GridBagConstraints());
    }// </editor-fold>//GEN-END:initComponents

    private void jUploadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jUploadButtonActionPerformed
        this.saveFile();
    }//GEN-LAST:event_jUploadButtonActionPerformed

    private void jViewFilesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jViewFilesButtonActionPerformed
        this.openEmployeeViewAdditionalFieldsFilesWindow();
    }//GEN-LAST:event_jViewFilesButtonActionPerformed

    public String getValue() {
        return "getValue method called";
    }

    public void setValue(DynamicFieldValue value) {
        this.value = value;
        this.determineIfFieldRequired();
        this.checkForFile();
    }

    public JComponent getComponent()    {return this;}


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jPresentLabel;
    private javax.swing.JLabel jRequiredLabel;
    private javax.swing.JButton jUploadButton;
    private javax.swing.JButton jViewFilesButton;
    // End of variables declaration//GEN-END:variables
};
