/**
 * Filename: Text_Messaging.java Author: Jeffrey N. Davis Date Created:
 * 05/21/2010 Date last modified: 05/21/2010 Last modified by: Jeffrey N. Davis
 * Pupose of File: File contains the Text_Messaging class which is designed to
 * send out a text message to any employees within a particular branch of a
 * company NOTE: Design is currently one way. Once all forms of messaging are
 * implemented, a two way design to receieve a response from the employee will
 * be added.
 */
//  package declaration
package rmischedule.messaging.components;

//  import declarations
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.data_connection.Connection;
import schedfoxlib.model.Company;
import rmischedule.main.Main_Window;
import rmischedule.messaging.datacomponents.Employee_Messaging_List_Data;
import rmischedule.messaging.xMessagingEdit;
import rmischedule.schedule.components.DShift;
import schedfoxlib.model.ShiftTypeClass;
import rmischedule.security.security_detail;
import rmischedule.templates.controllers.TemplateController;
import rmischedule.templates.interfaces.TemplateSystemInterface;
import rmischedule.templates.models.InitializeTemplateSystem;
import rmischedule.templates.models.TemplateComboBoxModel;
import schedfoxlib.model.TemplateData;
import rmischedule.templates.view.TemplateDiagForm;
import rmischedule.utility.WordDocumentListener;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_messaging_company_branch_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_user_name_messaging_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.sms_template_message_query;
import rmischeduleserver.mysqlconnectivity.queries.util.GenericQuery;

/**
 * Class Name: Text_Messaging Purpose of Class: Class is a tabbed form within
 * "Employee Messaging" tab that is designed specifically to send out text
 * messages to employees. See NOTE
 */
public class Email_Messaging extends GenericEditSubForm implements TemplateSystemInterface {
    //  private variable declarations

    private xMessagingEdit myParent;
    private StringBuffer messageList;
    private StringBuffer message;
    private Company myCompany;
    private String defaultTemplate = "[COMPANY] wants you to work on [DATE] at[BRANCH] between [SHIFT TIME].  "
            + "Please call [USER] at [COMPANY] if youare available.";
    //  private method implementations

    //  public method implementations
    /**
     * Class Name: Text_Messaging Purpose of Method: Constructor for
     * Email_Messaging class Arguments: an instance of xMessagingEdit Returns:
     * none Precondition: Object not created Postcondition: instance of object
     * created, initial variables set
     */
    public Email_Messaging(xMessagingEdit main) {
        //  assign values to class private variables
        myParent = main;
        messageList = new StringBuffer();
        message = new StringBuffer();
        // filterData = new Messaging_Filters_Data_Champion(this);

        //  initializa Swing components
        initComponents();
        addTxtCharCount();
    }

    /**
     * Method Name: determineMessageIndex Purpose of Method: takes the parents
     * DShift, and determines the appropriate messageIndex to create the query
     * Arguments: none Returns: and int describing the correct index
     * Preconditions: index in DB unknown Postconditions: index known, returned
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

    /**
     * Method Name: formatTemplateMessage Purpose of Method: takes the message
     * from the DB and formats it for proper display Arguments: a String
     * containing the templated message Returns: a formatted string containing
     * the message Preconditions: templated message pulled from DB, not yet
     * formatted Postconditions: message formatted Template Index: $ = Company
     * Name % = Date & = Branch # = time
     *
     * @ = user
     */
    private String formatTemplateMessage(String templateMessage) {
        //  variable for string manipulation and retunr
        StringBuffer formatString = new StringBuffer();
        StringBuilder returnString = new StringBuilder();
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
     * Method Name: getCompanyBranchName Purpose of Method: hits the DB and
     * returns the name of the company, branch associated with the company_id,
     * branch_id Arguments: two strings to format for company/branch Returns:
     * void Preconditions: ids known, data from DB not known Postconditions: DB
     * information retrieved, strings formatted
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
     * Method Name: getUserName Purpose of Method: hits the DB to get the
     * username, formats the name Arguments: none Returns: a StringBuffer with
     * the properly formatted name Preconditions: userId known by parent,
     * formatted name required for templated message Postconditions: DB hit,
     * user name known, format complete for name
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
     * Method Name: generateTemplatedMessage Purpose of Method: called by parent
     * to load templated message Arguments: none Returns: void Preconditions:
     * this object exists, no templated message exists Postconditions: templated
     * message created, set to messagePane
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
        this.setTemplate(defaultTemplate);
    }

    //  abstract method implementations
    @Override
    public String getMyTabTitle() {
        return "Email";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {
    }

    @Override
    public boolean userHasAccess() {
        return Main_Window.parentOfApplication.checkSecurity(
                security_detail.MODULES.EMPLOYEE_EDIT);
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
     * Method Name: doOnReset Purpose of Method: resets all fields to blank if
     * the reset button has been pushed Arguments: none Returns: void
     * Precondition: user has pushed reset, forms need to be reset
     * Postcondition: all forms reset Overrids: doOnReset from
     * GenericSubMessagingForm
     */
    public void doOnReset(boolean isClosing) {
        //  delete message, delete subject
        messagePane.setText(null);
        subjectTextField1337.setText(null);
        jCheckBoxSchedule.setSelected(false);
        this.jCheckBoxSchedule.setSelected(false);
        this.jCheckBoxAttachment.setEnabled(false);
        this.jCheckBoxText.setEnabled(false);
        //  inform user of reset if reset button pressed
        //  if shifts selected, tell them they must reselect
        if (!isClosing) {
            JOptionPane.showMessageDialog(this, "All fields reset.",
                    "Messaging", JOptionPane.OK_OPTION);
            //  reset parent DShift to null
            getMyParent().resetDShift();
        }
    }

    /**
     * Method Name: resetAfterSend Purpose of Method: resets the fields after a
     * send Arguments: none Returns: void Preconditions: send completed
     * succesfully, form must be rest Postconditions: all fields reset
     */
    public void resetAfterSend() {
        //  delete message, delete subject, reset check box
        messagePane.setText(null);
        subjectTextField1337.setText(null);
        jCheckBoxSchedule.setSelected(false);
        this.jCheckBoxAttachment.setSelected(false);
        this.jCheckBoxAttachment.setEnabled(false);
        this.jCheckBoxText.setSelected(false);
        this.jCheckBoxText.setEnabled(false);
    }

    public boolean shouldIncludeSignature() {
        return signatureLineChk.isSelected();
    }

    /**
     * Method Name: doOnSend Purpose of Method: returns the message entered in
     * the message pane so that a message can be sent Arguments: none Returns: a
     * string containing the message to be sent Precondition: message known by
     * class, desired elsewhere Postcondition: message returned
     */
    public String doOnSend() {
        return messagePane.getText();
    }

    /**
     * Method Name: getSubject Purpose of Method: returns the string written
     * into the subject panel Arguments: none Returns: a string Preconditions:
     * another piece of the program requires the subject Postconditiosn: subject
     * returned
     */
    public String getSubject() {
        return subjectTextField1337.getText();
    }

    /**
     * Method Name: cancelPressed Purpose of Method: resets all buttons and
     * selections from employee list; method is called specifically from
     * Messaging_Sort_Paramaters when the cancel button is pressed in that
     * window Arguments: none Returns: void Preconditions: user has pressed
     * cancel button in Messagin_Sort_Parameters window, GUI needs to be updated
     * in messaging window Postconditions: gui updated
     */
    public void cancelPressed() {

        //  ensure no employees are selected in employee list
        myParent.selectAll(0, false);
    }

    public boolean includeSchedule() {
        return this.jCheckBoxSchedule.isSelected();
    }

    //  Java Swing code
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSeparator1 = new javax.swing.JSeparator();
        primaryPanel = new javax.swing.JPanel();
        messagePanel = new javax.swing.JPanel();
        firstRowPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jTemplateNameComboBox = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        jEditTemplateButton = new javax.swing.JButton();
        secondRowPanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        subjectTextField1337 = new javax.swing.JTextField();
        thirdRowPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabelCharCount = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        messagePane = new javax.swing.JTextArea();
        sortPanel = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        jCheckBoxSchedule = new javax.swing.JCheckBox();
        jCheckBoxAttachment = new javax.swing.JCheckBox();
        jCheckBoxText = new javax.swing.JCheckBox();
        signatureLineChk = new javax.swing.JCheckBox();
        jPanel8 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(495, 300));
        setLayout(new java.awt.GridLayout(1, 0));

        primaryPanel.setPreferredSize(new java.awt.Dimension(511, 874));
        primaryPanel.setLayout(new javax.swing.BoxLayout(primaryPanel, javax.swing.BoxLayout.Y_AXIS));

        messagePanel.setLayout(new javax.swing.BoxLayout(messagePanel, javax.swing.BoxLayout.Y_AXIS));

        firstRowPanel.setLayout(new javax.swing.BoxLayout(firstRowPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setMaximumSize(new java.awt.Dimension(100, 50));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 12));
        jPanel2.setPreferredSize(new java.awt.Dimension(100, 12));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Template");
        jPanel2.add(jLabel6);

        firstRowPanel.add(jPanel2);

        jTemplateNameComboBox.setMaximumSize(new java.awt.Dimension(32767, 28));
        jTemplateNameComboBox.setMinimumSize(new java.awt.Dimension(200, 28));
        jTemplateNameComboBox.setPreferredSize(new java.awt.Dimension(200, 28));
        jTemplateNameComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTemplateNameComboBoxActionPerformed(evt);
            }
        });
        firstRowPanel.add(jTemplateNameComboBox);

        jPanel1.setMaximumSize(new java.awt.Dimension(100, 12));
        jPanel1.setMinimumSize(new java.awt.Dimension(100, 12));
        jPanel1.setPreferredSize(new java.awt.Dimension(100, 12));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        firstRowPanel.add(jPanel1);

        jEditTemplateButton.setText("Edit Templates");
        jEditTemplateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jEditTemplateButtonActionPerformed(evt);
            }
        });
        firstRowPanel.add(jEditTemplateButton);

        messagePanel.add(firstRowPanel);

        secondRowPanel.setLayout(new javax.swing.BoxLayout(secondRowPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Subject:");
        jLabel5.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(100, 16));
        secondRowPanel.add(jLabel5);

        subjectTextField1337.setMaximumSize(new java.awt.Dimension(2147483647, 28));
        subjectTextField1337.setMinimumSize(new java.awt.Dimension(6, 28));
        subjectTextField1337.setPreferredSize(new java.awt.Dimension(6, 28));
        subjectTextField1337.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                subjectTextField1337ActionPerformed(evt);
            }
        });
        secondRowPanel.add(subjectTextField1337);

        messagePanel.add(secondRowPanel);

        thirdRowPanel.setLayout(new javax.swing.BoxLayout(thirdRowPanel, javax.swing.BoxLayout.X_AXIS));

        jPanel3.setMaximumSize(new java.awt.Dimension(100, 32815));
        jPanel3.setMinimumSize(new java.awt.Dimension(100, 148));
        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel4.setMaximumSize(new java.awt.Dimension(100, 32767));
        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.X_AXIS));

        jLabel1.setText("Message:");
        jLabel1.setMaximumSize(new java.awt.Dimension(100, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(100, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(100, 16));
        jPanel4.add(jLabel1);

        jPanel3.add(jPanel4);

        jPanel7.setMaximumSize(new java.awt.Dimension(100, 32767));

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel7);

        jPanel6.setLayout(new java.awt.GridLayout(2, 0));

        jLabel3.setText("Char count");
        jPanel6.add(jLabel3);

        jLabelCharCount.setFont(new java.awt.Font("sansserif", 1, 12)); // NOI18N
        jLabelCharCount.setText("0");
        jLabelCharCount.setMaximumSize(new java.awt.Dimension(700, 24));
        jLabelCharCount.setMinimumSize(new java.awt.Dimension(7, 24));
        jLabelCharCount.setPreferredSize(new java.awt.Dimension(7, 24));
        jPanel6.add(jLabelCharCount);

        jPanel3.add(jPanel6);

        jPanel5.setMaximumSize(new java.awt.Dimension(100, 32767));

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 79, Short.MAX_VALUE)
        );

        jPanel3.add(jPanel5);

        thirdRowPanel.add(jPanel3);

        messagePane.setColumns(20);
        messagePane.setLineWrap(true);
        messagePane.setRows(5);
        messagePane.setWrapStyleWord(true);
        messagePane.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                messagePaneKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(messagePane);

        thirdRowPanel.add(jScrollPane1);

        messagePanel.add(thirdRowPanel);

        primaryPanel.add(messagePanel);

        sortPanel.setMaximumSize(new java.awt.Dimension(6000, 110));
        sortPanel.setMinimumSize(new java.awt.Dimension(100, 50));
        sortPanel.setPreferredSize(new java.awt.Dimension(511, 110));
        sortPanel.setLayout(new javax.swing.BoxLayout(sortPanel, javax.swing.BoxLayout.LINE_AXIS));

        rightPanel.setMaximumSize(new java.awt.Dimension(350, 110));
        rightPanel.setMinimumSize(new java.awt.Dimension(350, 75));
        rightPanel.setPreferredSize(new java.awt.Dimension(350, 75));
        rightPanel.setLayout(new javax.swing.BoxLayout(rightPanel, javax.swing.BoxLayout.Y_AXIS));

        jCheckBoxSchedule.setText("Include Schedule?");
        jCheckBoxSchedule.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxScheduleActionPerformed(evt);
            }
        });
        rightPanel.add(jCheckBoxSchedule);

        jCheckBoxAttachment.setText("Attach as a PDF?");
        jCheckBoxAttachment.setEnabled(false);
        jCheckBoxAttachment.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxAttachmentActionPerformed(evt);
            }
        });
        rightPanel.add(jCheckBoxAttachment);

        jCheckBoxText.setText("Attach at end of message as Text?");
        jCheckBoxText.setEnabled(false);
        jCheckBoxText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxTextActionPerformed(evt);
            }
        });
        rightPanel.add(jCheckBoxText);

        signatureLineChk.setSelected(true);
        signatureLineChk.setText("Generate Signature Line at end of Email");
        signatureLineChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signatureLineChkActionPerformed(evt);
            }
        });
        rightPanel.add(signatureLineChk);

        sortPanel.add(rightPanel);

        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.Y_AXIS));

        jLabel2.setText("[view_url]");
        jPanel8.add(jLabel2);

        jPanel9.setMaximumSize(new java.awt.Dimension(32767, 10));

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 260, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );

        jPanel8.add(jPanel9);

        jLabel4.setText("[verify_url]");
        jPanel8.add(jLabel4);

        sortPanel.add(jPanel8);

        primaryPanel.add(sortPanel);

        add(primaryPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void subjectTextField1337ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_subjectTextField1337ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_subjectTextField1337ActionPerformed

    private void jCheckBoxScheduleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxScheduleActionPerformed
        // TODO add your handling code here:
        if (jCheckBoxSchedule.isSelected()) {
            this.jCheckBoxAttachment.setEnabled(true);
            this.jCheckBoxText.setEnabled(true);
            this.jCheckBoxAttachment.setSelected(true);
            this.jCheckBoxText.setSelected(false);
            this.myParent.setAttach(true);
        } else {
            this.jCheckBoxAttachment.setEnabled(false);
            this.jCheckBoxText.setEnabled(false);
            this.jCheckBoxAttachment.setSelected(false);
            this.jCheckBoxText.setSelected(false);
            this.myParent.setAttach(false);
            this.myParent.setAddSchedText(false);
        }
    }//GEN-LAST:event_jCheckBoxScheduleActionPerformed

    private void jCheckBoxAttachmentActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxAttachmentActionPerformed
        // TODO add your handling code here:
        this.attachMentLogic();
    }//GEN-LAST:event_jCheckBoxAttachmentActionPerformed

    private void jCheckBoxTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBoxTextActionPerformed
        // TODO add your handling code here:
        this.attachMentLogic();
    }//GEN-LAST:event_jCheckBoxTextActionPerformed

    private void jEditTemplateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jEditTemplateButtonActionPerformed
        this.editTemplateAction();
    }//GEN-LAST:event_jEditTemplateButtonActionPerformed

    private void jTemplateNameComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTemplateNameComboBoxActionPerformed
        this.templateSelectionAction();
    }//GEN-LAST:event_jTemplateNameComboBoxActionPerformed

    private void messagePaneKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_messagePaneKeyTyped
        jLabelCharCount.setText(Integer.toString(messagePane.getText().length()));
    }//GEN-LAST:event_messagePaneKeyTyped

    private void signatureLineChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signatureLineChkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_signatureLineChkActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel firstRowPanel;
    private javax.swing.JCheckBox jCheckBoxAttachment;
    private javax.swing.JCheckBox jCheckBoxSchedule;
    private javax.swing.JCheckBox jCheckBoxText;
    private javax.swing.JButton jEditTemplateButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabelCharCount;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JComboBox jTemplateNameComboBox;
    private javax.swing.JTextArea messagePane;
    private javax.swing.JPanel messagePanel;
    private javax.swing.JPanel primaryPanel;
    private javax.swing.JPanel rightPanel;
    private javax.swing.JPanel secondRowPanel;
    private javax.swing.JCheckBox signatureLineChk;
    private javax.swing.JPanel sortPanel;
    private javax.swing.JTextField subjectTextField1337;
    private javax.swing.JPanel thirdRowPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * Vu's character count additions
     */
    private void addTxtCharCount() {

        JTextFieldLimit doc = new JTextFieldLimit(50);
        this.subjectTextField1337.setDocument(doc);
        doc = new JTextFieldLimit(1000);
        this.messagePane.setDocument(doc);
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

    public void loadTemplates() {
        LinkedHashMap<Integer, TemplateData> templates = TemplateController.getInstance().reloadTemplates(myParent.getCompany(), this.getTemplateType());
        TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
        this.jTemplateNameComboBox.setModel(templateComboBoxModel);
    }

    void setTemplate(String template) {
        //replace company and branch
        StringBuffer company = new StringBuffer();
        StringBuffer branch = new StringBuffer();
        this.getCompanyBranchName(company, branch);
        template = template.replace(Text_Messaging_Template.COMPANY_TAG, company.toString().trim());
        template = template.replace(Text_Messaging_Template.BRANCH_TAG, branch.toString().trim());

        //get parent shift if is applicaable otherwise get user input
        template = template.replace(Text_Messaging_Template.USER_TAG,
                this.myParent.getUser().getFirstName() + " " + this.myParent.getUser().getLastName());
        DShift temp = this.myParent.getParentDShift();
        if (temp != null) {
            template = template.replace(Text_Messaging_Template.SHIFT_TIME_TAG,
                    temp.getFormattedStartTime() + "-" + temp.getFormattedEndTime());
            template = template.replace(Text_Messaging_Template.DATE_TAG, temp.getDateString());
        } else {
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
            }
        }
        //set text into message pane
        this.messagePane.setText(template);
    }

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
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    public void replaceWithTemplate(String subject, String message) {
        this.subjectTextField1337.setText(subject);
        this.messagePane.setText(message);
    }

    private void attachMentLogic() {
        if ((!this.jCheckBoxAttachment.isSelected() && !this.jCheckBoxText.isSelected())) {
            this.jCheckBoxSchedule.setSelected(false);
            this.jCheckBoxScheduleActionPerformed(null);
        } else {
            this.myParent.setAttach(this.jCheckBoxAttachment.isSelected());
            this.myParent.setAddSchedText(this.jCheckBoxText.isSelected());
        }

    }

    /*  Template System Implementation  */
    public void viewController() {
        LinkedHashMap<Integer, TemplateData> templates = templateController.reloadTemplates(myParent.getCompany(), this.getTemplateType());
        this.subjectTextField1337.setText("");
        this.messagePane.setText("");

        /*  set drop down menu visible/invisible, data model    */
        if (templates.isEmpty()) {
            this.jTemplateNameComboBox.setVisible(false);
        } else {
            this.jTemplateNameComboBox.setVisible(true);
            TemplateComboBoxModel templateComboBoxModel = new TemplateComboBoxModel(templates);
            this.jTemplateNameComboBox.setModel(templateComboBoxModel);

            if (templateController.hasNewSave()) {
                this.subjectTextField1337.setText(templateController.getNewTemplateName());
                this.messagePane.setText(templateController.getNewTemplateValue());
                this.jTemplateNameComboBox.setSelectedIndex(templateController.getNewTemplateIndex());
                templateController.resetAfterSave();
            } else {
                this.jTemplateNameComboBox.setSelectedIndex(0);
            }

            this.jTemplateNameComboBox.revalidate();
            this.jTemplateNameComboBox.repaint();
        }
    }

    public InitializeTemplateSystem initTemplateSystem() {
        String compId = this.myParent.getCompany();
        String branchId = this.myParent.getBranch();
        int templateType = this.getTemplateType();
        String incomingText = this.messagePane.getText();

        InitializeTemplateSystem init = (this.isTemplateNew())
                ? new InitializeTemplateSystem.Builder(compId, branchId, templateType, templateController).incomingText(incomingText).build()
                : new InitializeTemplateSystem.Builder(compId, branchId, templateType, templateController).build();

        return init;
    }

    public int getTemplateType() {
        return 3;
    }

    public void editTemplateAction() {
        TemplateDiagForm templateForm = new TemplateDiagForm();
        templateForm.init(this.initTemplateSystem());
        templateForm.pack();
        templateForm.setVisible(true);
        this.viewController();
    }

    public void templateSelectionAction() {
        TemplateData selectedElement = (TemplateData) this.jTemplateNameComboBox.getSelectedItem();
        try {
            this.subjectTextField1337.setText(selectedElement.getTemplateName());
            this.messagePane.setText(selectedElement.getTemplateValue());
        } catch (Exception e) {
        }
    }

    private boolean isTemplateNew() {
        if (this.messagePane.getText().length() == 0) {
            return false;
        }

        boolean hasMatch = false;
        String outgoingText = this.messagePane.getText();
        LinkedHashMap<Integer, TemplateData> map = templateController.getTemplates();
        Collection<TemplateData> collection = map.values();
        for (TemplateData element : collection) {
            if (outgoingText.equalsIgnoreCase(element.getTemplateValue())) {
                hasMatch = true;
            }
        }

        return hasMatch ? false : true;
    }
    /*  Template System Implementation complete */

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

            str = str.replaceAll("“", "\"");
            str = str.replaceAll("‘", "'");
            if (toUppercase) {
                str = str.toUpperCase();
            }
            super.insertString(offset, str, attr);
        }

        public int getLimit() {
            return this.limit;
        }
    }
};
