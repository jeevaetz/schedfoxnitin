/**
 *  Filename:  Text_Messaging.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/21/2010
 *  Date last modified:  05/21/2010
 *  Last modified by:  Jeffrey N. Davis
 *  Pupose of File:  File contains the Text_Messaging class which is designed
 *      to send out a text message to any employees within a particular branch
 *      of a company
 *  NOTE:  Design is currently one way.  Once all forms of messaging are
 *      implemented, a two way design to receieve a response from the employee
 *      will be added.
 */
//  package declaration
package rmischedule.messaging.components;

//  import declarations
import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.messaging.datacomponents.Employee_Messaging_List_Data;
import rmischedule.messaging.xMessagingEdit;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.SShift;
import schedfoxlib.model.ShiftTypeClass;
import rmischedule.schedule.components.availability.Messaging_Availability;
import rmischedule.templates.controllers.TemplateController;
import rmischedule.templates.interfaces.TemplateSystemInterface;
import rmischedule.templates.models.InitializeTemplateSystem;
import rmischedule.templates.models.TemplateComboBoxModel;
import schedfoxlib.model.TemplateData;
import rmischedule.templates.view.TemplateDiagForm;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_messaging_company_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_user_name_messaging_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.sms_template_message_query;

/**
 *  Class Name:  Text_Messaging
 *  Purpose of Class:  Class is a tabbed form within "Employee Messaging" tab
 *      that is designed specifically to send out text messages to employees.
 *      See NOTE
 */
public final class Text_Messaging extends JPanel implements TemplateSystemInterface {
    //  private variable declarations

    private xMessagingEdit myParent;
    private StringBuffer messageList;
    private static final String CHAMPION_DEFAULT_TEMPLATE = "[COMPANY] wants you to "
            + "work on [DATE] FROM [SHIFT TIME].  Please call [USER] at 972-235-8844.";
    //  private method implementations

    /**
     *  Method Name:  determineMessageIndex
     *  Purpose of Method:  takes the parents DShift, and determines the
     *      appropriate messageIndex to create the query
     *  Arguments:  none
     *  Returns:  and int describing the correct index
     *  Preconditions:  index in DB unknown
     *  Postconditions:  index known, returned
     */
    private int determineMessageIndex() {
        //  int to return
        int returnIndex = 0;

        //  determine index
        //  test for conflict
        ShiftTypeClass myShiftClass = getMyParent().getParentDShift().getType();

        if (getMyParent().getParentDShift().hasConflict()) {
            returnIndex = 3;
        } else if (getMyParent().getParentDShift().isOpenShift()) {
            returnIndex = 1;
        } else if (myShiftClass.isShiftType(ShiftTypeClass.SHIFT_UNCONFIRMED)) {
            returnIndex = 2;
        }

        //  return index
        return returnIndex;
    }

    public void loadTemplates() {
        LinkedHashMap<Integer, TemplateData> templates = TemplateController.getInstance().reloadTemplates(myParent.getCompany(), this.getTemplateType());
        TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
        this.jTemplateNameComboBox.setModel(templateComboBoxModel);
    }
    
    /**
     *  Method Name:  formatTemplateMessage
     *  Purpose of Method:  takes the message from the DB and formats it
     *      for proper display
     *  Arguments:  a String containing the templated message
     *  Returns:  a formatted string containing the message
     *  Preconditions:  templated message pulled from DB, not yet formatted
     *  Postconditions:  message formatted
     *  Template Index:
     *      $ = Company Name
     *      % = Date
     *      & = Branch
     *      # = time
     *      @ = user
     */
    private String formatTemplateMessage(String templateMessage) {
        //  variable for string manipulation and retunr
        StringBuffer formatString = new StringBuffer();
        StringBuffer returnString = new StringBuffer();
        formatString.append(templateMessage);
        formatString.trimToSize();

        // assign substitute strings
        StringBuffer company = new StringBuffer();
        StringBuffer branch = new StringBuffer();
        StringBuffer userName = getUserName();
        getCompanyBranchName(company, branch);

        String date = getMyParent().getParentDShift().getDateString() + " ";
        String time = getMyParent().getParentDShift().getFormattedStartTime()
                + " to " + getMyParent().getParentDShift().getFormattedEndTime();

        //  insert company name where $ is located
        for (int i = 0; i < formatString.length(); i++) {
            //  traverse stringbuffer, looking for '$'
            if (formatString.charAt(i) == '$') {
                //  get replacement string
                int j = i + 1;
                int k = 0;
                boolean flag = false;
                while (!flag && j < formatString.length()) {
                    if (formatString.charAt(j) == '$') {
                        k = j + 2;
                        flag = true;
                    } else {
                        j++;
                    }
                }
                formatString.replace(i, k, company.toString());
                formatString.trimToSize();
            }
        }

        //  insert date where % is located
        for (int i = 0; i < formatString.length(); i++) {
            //  traverse stringbuffer, looking for '$'
            if (formatString.charAt(i) == '%') {
                //  get replacement string
                int j = 0;
                for (int k = i + 1; k < formatString.length(); k++) {
                    if (formatString.charAt(k) == '%') {
                        j = k + 2;
                    }
                }
                formatString.replace(i, j, date);
                formatString.trimToSize();
            }
        }

        //  insert branch name where & is located
        for (int i = 0; i < formatString.length(); i++) {
            //  traverse stringbuffer, looking for '$'
            if (formatString.charAt(i) == '&') {
                //  get replacement string
                int j = 0;
                for (int k = i + 1; k < formatString.length(); k++) {
                    if (formatString.charAt(k) == '&') {
                        j = k + 2;
                    }
                }
                formatString.replace(i, j, branch.toString());
                formatString.trimToSize();
            }
        }

        //  insert time where # is located
        for (int i = 0; i < formatString.length(); i++) {
            //  traverse stringbuffer, looking for '$'
            if (formatString.charAt(i) == '#') {
                //  get replacement string
                int j = 0;
                for (int k = i + 1; k < formatString.length(); k++) {
                    if (formatString.charAt(k) == '#') {
                        j = k + 1;
                    }
                }
                formatString.replace(i, j, time);
                formatString.trimToSize();
            }
        }

        //  insert user where @ is located
        for (int i = 0; i < formatString.length(); i++) {
            //  traverse stringbuffer, looking for '$'
            if (formatString.charAt(i) == '@') {
                //  get replacement string
                int j = 0;
                for (int k = i + 1; k < formatString.length(); k++) {
                    if (formatString.charAt(k) == '@') {
                        j = k + 2;
                    }
                }
                formatString.replace(i, j, userName.toString());
                formatString.trimToSize();
            }
        }

        //  return formatted message
        returnString.append(formatString);
        return returnString.toString();
    }

    /**
     *  Method Name:  getCompanyBranchName
     *  Purpose of Method:  hits the DB and returns the name of the company,
     *      branch associated with the company_id, branch_id
     *  Arguments:  two strings to format for company/branch
     *  Returns:  void
     *  Preconditions:  ids known, data from DB not known
     *  Postconditions:  DB information retrieved, strings formatted
     */
    private void getCompanyBranchName(StringBuffer company, StringBuffer branch) {
        //  declare Record_Set to return, initialize stringbuffers
        Record_Set rs = null;
        company.setLength(0);
        branch.setLength(0);

        //  create, prep query
        get_messaging_company_branch_query query = new get_messaging_company_branch_query();
        query.addCompanyBranchId(getMyParent().getCompany(), getMyParent().getBranch());
        getMyConnection().prepQuery(query);

        //  execute query
        try {
            rs = getMyConnection().executeQuery(query);

            //  format StringBuffers
            company.append(rs.getString("company_name"));
            company.append(" ");
            branch.append(rs.getString("branch_name"));
            branch.append(" ");
            company.trimToSize();
            branch.trimToSize();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *  Method Name:  getUserName
     *  Purpose of Method:  hits the DB to get the username, formats the name
     *  Arguments:  none
     *  Returns:  a StringBuffer with the properly formatted name
     *  Preconditions:  userId known by parent, formatted name required for
     *      templated message
     *  Postconditions:  DB hit, user name known, format complete for name
     */
    private StringBuffer getUserName() {
        //  declare stringbuffer to format, return
        StringBuffer name = new StringBuffer();

        //  get userId
        Integer userId = new Integer(getMyParent().getUser().getUserId());



        //  declare, prep query
        get_user_name_messaging_query query = new get_user_name_messaging_query(userId);
        getMyConnection().prepQuery(query);

        //  execute query, format name
        try {
            //  declare Record_Set, execute query
            Record_Set rs = new Record_Set();
            rs = getMyConnection().executeQuery(query);

            //  format name
            name.append(rs.getString("first_name"));
            name.append(" ");
            name.append(rs.getString("last_name"));
            name.append(" ");
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        //  return properly formatted name
        name.trimToSize();
        return name;
    }

    /**
     *  Class Name:  Text_Messaging
     *  Purpose of Method:  Constructor for Email_Messaging class
     *  Arguments:  an instance of xMessagingEdit
     *  Returns:  none
     *  Precondition:  Object not created
     *  Postcondition:  instance of object created, initial variables set
     */
    public Text_Messaging(xMessagingEdit main) {
        //  assign values to class private variables
        myParent = main;
        messageList = new StringBuffer();

        //filterData = new Messaging_Filters_Data_Champion(this);

        //  initializa Swing components
        initComponents();
        addTxtCharCount();

    }

    /**
     *  Method Name:  generateTemplatedMessage
     *  Purpose of Method:  called by parent to load templated message
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  this object exists, no templated message exists
     *  Postconditions:  templated message created, set to messagePane
     */
    public void generateTemplatedMessage() {
        //  create, prep query, create Record_Set
        sms_template_message_query query = new sms_template_message_query();
        query.addTemplateId(determineMessageIndex());
        getMyConnection().setCompany(getMyParent().getCompany());
        getMyConnection().setBranch(getMyParent().getBranch());
        getMyConnection().prepQuery(query);

        Record_Set rs = new Record_Set();

        messagePane.setText("");

        //  execute query if messageIndex exists
        if (determineMessageIndex() > 0) {
            try {
                rs = getMyConnection().executeQuery(query);
                messagePane.setText(formatTemplateMessage(
                        rs.getString("message_sms_text")));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        this.setTemplate(CHAMPION_DEFAULT_TEMPLATE);
    }

    //  abstract method implementations

    /*
     *  Method Name:  getMyTabTitle
     *  Purpose of Method:  returns the tab panel title
     *  Arguments:  none
     *  Returns:  a string containing the name of the tab
     *  Postcondition:  Tab title known internally, not known outside of class
     *  Postcondition:  tab title returned
     *  Implements:  getMyTabTitle from GenericEditSubForm
     */
    //@Override
    public String getMyTabTitle() {
        return "Text";
    }

    public void getSelected(Vector<Employee_Messaging_List_Data> tempVector) {
        //  ensure messageList is empty
        messageList.setLength(0);

        //  run through vector, searching for employees to message
        for (int i = 0; i < tempVector.size(); i++) {
            if (tempVector.get(i).getSendSms()) {
                messageList.append("<");
                messageList.append(tempVector.get(i).getLastName());
                messageList.append(", ");
                messageList.append(tempVector.get(i).getFirstName());
                messageList.append(" ");
                messageList.append(tempVector.get(i).getMiddleInitial());
                messageList.append(">;  ");
            }
        }

        //  update toPane
        // toTextPane.setText(messageList.toString());
    }

    /**
     *  Method Name:  doOnReset
     *  Purpose of Method:  resets all fields to blank if the reset button
     *      has been pushed
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  user has pushed reset, forms need to be reset
     *  Postcondition:  all forms reset
     *  Overrids:  doOnReset from GenericSubMessagingForm
     */
    public void doOnReset(boolean isClosing) {
        //  delete message
        messagePane.setText(null);
    }

    /**
     *  Method Name:  resetAfterSend
     *  Purpose of Method:  resets the fields after a send
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  send completed succesfully, form must be rest
     *  Postconditions:  all fields reset
     */
    public void resetAfterSend() {
        //  delete message
        messagePane.setText(null);
    }

    /**
     *  Method Name:  doOnSend
     *  Purpose of Method:  returns the message entered in the message pane
     *      so that a message can be sent
     *  Arguments:  none
     *  Returns:  a string containing the message to be sent
     *  Precondition:  message known by class, desired elsewhere
     *  Postcondition:  message returned
     */
    public String doOnSend() {
        return messagePane.getText();
    }

    /**
     *  Method Name:  getSubject
     *  Purpose of Method:  returns the string written into the subject panel
     *  Arguments:  none
     *  Returns:  a string
     *  Preconditions:  another piece of the program requires the subject
     *  Postconditiosn:  subject returned
     */
    public String getSubject() {
        return null;
    }

    /**
     *  Method Name:  cancelPressed
     *  Purpose of Method:  resets all buttons and selections from employee
     *      list; method is called specifically from Messaging_Sort_Paramaters
     *      when the cancel button is pressed in that window
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has pressed cancel button in
     *      Messagin_Sort_Parameters window, GUI needs to be updated in
     *      messaging window
     *  Postconditions:  gui updated
     */
    public void cancelPressed() {
        //  ensure no employees are selected in employee list
        myParent.selectAll(0, false);
    }

    /**
     *  Method Name:  okPressed
     *  Purpose of Method:  gets the proper filter information from the
     *      Messaging_Filter_Window, and begins the process of searching
     *      the employee lists with the appropriate filters
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  user has pressed the ok buttong in
     *      Messaging_Filters_Window, employee search needs to be started
     *  Postcondition:  search complete, appropriate methods called to update
     *      GUI
     */
    public void okPressed() {
        //  determine proper filter
//        filterData.determineFilter(lessThan40ToggleButton.isSelected(),
//                noConflictToggleButton.isSelected());
//
//        //  hit DB for armed/unarmed
//        filterData.hitDBEmployeeArmed();
    }

    public void loadDefaultTemplate() {
        setTemplate(CHAMPION_DEFAULT_TEMPLATE);
    }

    public void showTemplateButton(boolean flag)
    {
       
    }

    //  Java Swing code
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        primaryPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTemplateNameComboBox = new javax.swing.JComboBox();
        jPanel2 = new javax.swing.JPanel();
        jEditTemplateButton = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel10 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelCharCount = new javax.swing.JLabel();
        jPanel11 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagePane = new javax.swing.JTextArea();
        messagePanel = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        sortPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(460, 300));
        setLayout(new java.awt.BorderLayout());

        primaryPanel.setLayout(new javax.swing.BoxLayout(primaryPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel1.setMaximumSize(new java.awt.Dimension(33084, 28));
        jPanel1.setMinimumSize(new java.awt.Dimension(343, 28));
        jPanel1.setPreferredSize(new java.awt.Dimension(910, 28));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(100, 50));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 12));
        jPanel3.setPreferredSize(new java.awt.Dimension(100, 12));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Template");
        jPanel3.add(jLabel6);

        jPanel1.add(jPanel3);

        jTemplateNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTemplateNameComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(jTemplateNameComboBox);

        jPanel2.setMaximumSize(new java.awt.Dimension(100, 12));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 12));

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        jPanel1.add(jPanel2);

        jEditTemplateButton.setText("Edit Templates");
        jEditTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditTemplateButtonActionPerformed(evt);
            }
        });
        jPanel1.add(jEditTemplateButton);

        primaryPanel.add(jPanel1);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jPanel9.setMaximumSize(new java.awt.Dimension(100, 32767));
        jPanel9.setPreferredSize(new java.awt.Dimension(100, 256));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.Y_AXIS));

        jLabel1.setText("Message");
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel9.add(jLabel1);

        jPanel10.setMaximumSize(new java.awt.Dimension(100, 32767));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel10);

        jLabel3.setText("Char Count");
        jPanel9.add(jLabel3);

        jLabelCharCount.setText("0");
        jPanel9.add(jLabelCharCount);

        jPanel11.setMaximumSize(new java.awt.Dimension(100, 32767));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 50, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 106, Short.MAX_VALUE)
        );

        jPanel9.add(jPanel11);

        jPanel4.add(jPanel9);

        jPanel5.setLayout(new java.awt.GridLayout());

        messagePane.setColumns(20);
        messagePane.setLineWrap(true);
        messagePane.setRows(5);
        messagePane.setWrapStyleWord(true);
        jScrollPane1.setViewportView(messagePane);

        jPanel5.add(jScrollPane1);

        jPanel4.add(jPanel5);

        primaryPanel.add(jPanel4);

        messagePanel.setMaximumSize(new java.awt.Dimension(32767, 35));
        messagePanel.setPreferredSize(new java.awt.Dimension(910, 35));
        messagePanel.setLayout(new javax.swing.BoxLayout(messagePanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(150, 32767));

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 150, Short.MAX_VALUE)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 35, Short.MAX_VALUE)
        );

        messagePanel.add(jPanel6);

        jLabel4.setText("If message passes 140 characters, it will be split into muliple texts.");
        messagePanel.add(jLabel4);

        primaryPanel.add(messagePanel);

        sortPanel.setMaximumSize(new java.awt.Dimension(32767, 80));
        sortPanel.setPreferredSize(new java.awt.Dimension(910, 80));

        jLabel2.setText("[verify_url]");

        jLabel5.setText("[view_url]");

        javax.swing.GroupLayout sortPanelLayout = new javax.swing.GroupLayout(sortPanel);
        sortPanel.setLayout(sortPanelLayout);
        sortPanelLayout.setHorizontalGroup(
            sortPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, sortPanelLayout.createSequentialGroup()
                .addContainerGap(446, Short.MAX_VALUE)
                .addGroup(sortPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addComponent(jLabel5))
                .addGap(108, 108, 108))
        );
        sortPanelLayout.setVerticalGroup(
            sortPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(sortPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        primaryPanel.add(sortPanel);

        add(primaryPanel, java.awt.BorderLayout.CENTER);
    }// </editor-fold>//GEN-END:initComponents

    private void jEditTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTemplateButtonActionPerformed
        this.editTemplateAction();
    }//GEN-LAST:event_jEditTemplateButtonActionPerformed

    private void jTemplateNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTemplateNameComboBoxActionPerformed
        this.templateSelectionAction();
    }//GEN-LAST:event_jTemplateNameComboBoxActionPerformed
    /*
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        //  ensure no multiple shifts
        if (!ensureNoMultipleShifts()) {
            JOptionPane.showMessageDialog(this, "Error:  You cannot use templates when multiple shifts are selected", "SMS Messaging", JOptionPane.OK_OPTION);

        } else {
            Text_Messaging_Template temp = new Text_Messaging_Template(this);
            Main_Window.parentOfApplication.desktop.add(temp);
            try {
                temp.setSelected(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Text_Messaging.class.getName()).log(Level.SEVERE, null, ex);
            }
            temp.repaint();
      
        }
    }*/
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jEditTemplateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCharCount;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JComboBox jTemplateNameComboBox;
    private javax.swing.JTextArea messagePane;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JPanel primaryPanel;
    private javax.swing.JPanel sortPanel;
    // End of variables declaration//GEN-END:variables

    /**
     *  Vu's character count additions
     */
    private void addTxtCharCount() {
        JTextFieldLimit doc = new JTextFieldLimit(275);
        this.messagePane.setDocument(doc);
        doc.addDocumentListener((DocumentListener) new MyDocumentListener(this.jLabelCharCount));

    }

    /**
     * @return the myParent
     */
    public xMessagingEdit getMyParent() {
        return myParent;
    }

    /**
     * @return the myConnection
     */
    public Connection getMyConnection() {
        return myParent.getConnection();
    }

    public void setTemplate(String template) {
        //replace company and branch
        StringBuffer company = new StringBuffer();
        StringBuffer branch = new StringBuffer();
        this.getCompanyBranchName(company, branch);
        template = template.replace(Text_Messaging_Template.COMPANY_TAG, company.toString().trim());
        template = template.replace(Text_Messaging_Template.BRANCH_TAG, branch.toString().trim());

        //get parent shift if is applicaable otherwise get user input
        template = template.replace(Text_Messaging_Template.USER_TAG,
                this.myParent.getUser().getFirstName() + " " + this.myParent.getUser().getLastName());
        DShift temp = this.myParent.getDShift();
        if (temp != null) {
            template = template.replace(Text_Messaging_Template.SHIFT_TIME_TAG,
                    temp.getFormattedStartTime() + "-" + temp.getFormattedEndTime());
            template = template.replace(Text_Messaging_Template.DATE_TAG, temp.getDateString());
        } else {
            /*
            //case when no shift associated
            //replace date tag if present
            if (template.lastIndexOf(Text_Messaging_Template.DATE_TAG) != -1) {
                String userInput = JOptionPane.showInputDialog(this, "Please enter appropiate text to replace the [Date] Tag"
                        + ", if left blank then the slot will be removed");
                if (userInput == null) {
                    userInput = " ";
                }
                template = template.replace(Text_Messaging_Template.DATE_TAG, userInput);
            }
            //replace shift tag if it is there
            if (template.lastIndexOf(Text_Messaging_Template.SHIFT_TIME_TAG) != -1) {
                String userInput = JOptionPane.showInputDialog(this, "Please enter appropiate text to replace the [Shift] Tag"
                        + ", if left blank then the slot will be removed");
                if (userInput == null) {
                    userInput = " ";
                }
                template = template.replace(Text_Messaging_Template.SHIFT_TIME_TAG, userInput);
            }*/
        }
        //set text into message pane
        this.messagePane.setText(template);
    }

    
    @Override
    public void setVisible(boolean flag) {

        super.setVisible(flag);
        /*if (this.myParent.getDShift() == null) {
            this.jButton1.setVisible(false);
        } else {
            this.jButton1.setVisible(true);
        }*/
    }

    /*  Implementation of {@code TemplateSystem}    */
    public void viewController()
    {
        LinkedHashMap<Integer, TemplateData> templates = templateController.reloadTemplates(this.myParent.getCompany(), this.getTemplateType());
        this.messagePane.setText( "" );

        /*  set drop down menu visible/invisible, data model    */
        if (templates.isEmpty() )
            this.jTemplateNameComboBox.setVisible( false );
        else
        {
            this.jTemplateNameComboBox.setVisible( true );
            TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
            this.jTemplateNameComboBox.setModel( templateComboBoxModel );

            if( templateController.hasNewSave() )
            {
                this.messagePane.setText( templateController.getNewTemplateValue() );
                this.jTemplateNameComboBox.setSelectedIndex( templateController.getNewTemplateIndex() );
                templateController.resetAfterSave();
            }
            else
                this.jTemplateNameComboBox.setSelectedIndex( 0 );

            this.jTemplateNameComboBox.revalidate();
            this.jTemplateNameComboBox.repaint();
        }
    }

    public InitializeTemplateSystem initTemplateSystem()
    {
        String compId = this.myParent.getCompany();
        String branchId = this.myParent.getBranch();
        int templateType = this.getTemplateType();
        String incomingText = this.messagePane.getText();
        
        InitializeTemplateSystem init =  ( this.isTemplateNew() )
            ? new InitializeTemplateSystem.Builder( compId, branchId,templateType, templateController  ).incomingText(incomingText).build()
            : new InitializeTemplateSystem.Builder( compId, branchId, templateType, templateController ).build();
        
        return init;
    }

    public int getTemplateType() {  return 2;}

    public void initParentTemplateController() {
        String company = this.myParent.getCompany();
        String branch = this.myParent.getBranch();
        templateController.init(this.myParent.getCompany(),this.getTemplateType());
    }

    public void editTemplateAction()
    {
        TemplateDiagForm templateForm = new TemplateDiagForm();
        templateForm.init( this.initTemplateSystem() );
        templateForm.pack();
        templateForm.setVisible( true );
        this.viewController();
    }

    public void templateSelectionAction() {
        TemplateData selectedElement = (TemplateData) this.jTemplateNameComboBox.getSelectedItem();
        this.messagePane.setText( selectedElement.getTemplateValue() );
    }

    private boolean isTemplateNew()
    {
        if ( this.messagePane.getText().length() == 0 )
            return false;

        boolean hasMatch = false;
        String outgoingText = this.messagePane.getText();
        LinkedHashMap<Integer, TemplateData> map = templateController.getTemplates();
        Collection<TemplateData> collection = map.values();
        for ( TemplateData element:  collection )
        {
            if ( outgoingText.equalsIgnoreCase( element.getTemplateValue() ))
                hasMatch = true;
        }

        return hasMatch ? false : true;
    }
    /*  Implementation of {@code TemplateSystem complete}*/

    //Document listener gives a char count and displays on a label
    private class MyDocumentListener implements DocumentListener {

        JLabel lblCharCount;

        MyDocumentListener(JLabel lbl) {
            this.lblCharCount = lbl;
        }

        public void insertUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        public void removeUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        public void changedUpdate(DocumentEvent e) {
            displayEditInfo(e);
        }

        private void displayEditInfo(DocumentEvent e) {
            //grabs the difference doc and displays length
            Document document = (Document) e.getDocument();
            this.lblCharCount.setText(Integer.toString(document.getLength()));
        }
    }

    public class JTextFieldLimit extends PlainDocument {

        private int limit;
        // optional uppercase conversion
        private boolean toUppercase = false;

        JTextFieldLimit(int limit) {
            super();
            this.limit = limit;
        }

        JTextFieldLimit(int limit, boolean upper) {
            super();
            this.limit = limit;
            toUppercase = upper;
        }

        @Override
        public void insertString(int offset, String str, AttributeSet attr)
                throws BadLocationException {
            if (str == null) {
                return;
            }

            if ((getLength() + str.length()) <= limit) {
                if (toUppercase) {
                    str = str.toUpperCase();
                }
                super.insertString(offset, str, attr);
            } else {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication.desktop, "Maximum characters is " + this.limit + " and has been reached");
            }
        }

        public int getLimit() {
            return this.limit;
        }
    }
};
