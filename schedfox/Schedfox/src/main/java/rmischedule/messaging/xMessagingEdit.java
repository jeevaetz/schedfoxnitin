/**
 * Filename: xMessagingEdit.java Author: Jeffrey N. Davis Date Created:
 * 05/20/2010 Date Last Modified: 05/20/2010 Last Modified By: Jeffrey N. Davis
 * Purpose of File: File contains the xMessagingEdit class that is based off of
 * xEmployeeEdit and xClientEdit.
 */
//  package declaration
package rmischedule.messaging;

//  import declarations
import schedfoxlib.model.util.Record_Set;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.ImageIcon;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rmischedule.components.graphicalcomponents.GenericEditForm;
import rmischedule.components.graphicalcomponents.GenericListContainer;
import rmischedule.components.graphicalcomponents.GraphicalListComponent;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.data_connection.Connection;
import rmischedule.main.CompanyBranchMenuInterface;
import rmischedule.main.Main_Window;
import rmischedule.messaging.components.Email_Messaging;
import rmischedule.messaging.components.MessagingSubForm;
import rmischedule.messaging.components.Text_Messaging;
import rmischedule.messaging.datacomponents.MessagingFilterType;
import rmischedule.messaging.email.GeneralEmailMessageSend;
import rmischedule.messaging.sms.ScheduleSmSRequest;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.availability.Messaging_Availability;
import rmischedule.security.User;
import rmischedule.xprint.components.GetDatesForPrintDialog;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmailController;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.control.MessagingController;
import rmischeduleserver.mysqlconnectivity.queries.messaging.employee_messaging_list_query;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.Employee;
import schedfoxlib.model.MessagingCommunicationBatch;

/**
 * Class Name: xMessagingEdit Purpose of Class: This class forms the super class
 * for messaging components.
 */
public final class xMessagingEdit extends GenericEditForm implements CompanyBranchMenuInterface {

    private Connection myConnection;
    private String company;
    private String branch;
    private User user;
    private DShift myDShift;
    protected xMessagingEdit thisObject;
    private MessagingSubForm messagingForm;
    private boolean attach;
    private boolean addSchedText;
    private HashMap<Integer, Branch> branchCache = new HashMap<Integer, Branch>();
    private ArrayList<Integer> employeeIds;
    private MessagingFilterType noFilterType = new MessagingFilterType(this, "No Filter", MessagingFilterType.NO_FILTER) {
        @Override
        public void runFilter() {

        }
    };
    private MessagingFilterType lessThanFortyHours = new MessagingFilterType(this, "Under 40 Hours", MessagingFilterType.LESS_THAN_40) {
        @Override
        public void runFilter() {

        }
    };
    private MessagingFilterType availableFilterType = new MessagingFilterType(this, "Available Employees", MessagingFilterType.AVAILABLE) {
        @Override
        public void runFilter() {

        }
    };

    public xMessagingEdit() {
        //  set private variables of class
        myConnection = new Connection();
        thisObject = this;
        myDShift = null;
        this.messagingForm = new MessagingSubForm(this);
        super.addSubForm(this.messagingForm);
    }

    private void resetAfterSend(int form) {
        resetOnCardChange();
        messagingForm.resetAfterSend(form);
    }

    public void runOnClickUser(Object objectContained, boolean selected) {
        try {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            currentSelectedObject = objectContained;
            if (selectedIsMarkedDeleted()) {
                myDeleteIcon.setText(getUndeleteString(), new Font("Dialog", Font.BOLD, 14));
            } else {
                myDeleteIcon.setText(getDeletedString(), new Font("Dialog", Font.BOLD, 14));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }

    /**
     * Method Name: determineMessageType Purpose of Method: method determines if
     * the user wishes to send an email of SMS message when send is pressed
     * Arguments: none Returns: an int describing the message type
     * Preconditiosn: user has pressed send, message type needs to be determined
     * Postconditions: message type determined
     */
    private int determineMessageType() {
        int messageType = 0;
        if (messagingForm.getSelectedMessagingType().matches("SMS Messaging")) {
            messageType = 1;
        } else if (messagingForm.getSelectedMessagingType().matches("Email Messaging")) {
            messageType = 2;
        }
        return messageType;
    }

    /**
     * Method Name: sendSMS Purpose of Method: sends out an SMS message
     * Arguments: none Returns: void Prconditions: user wishes to send sms
     * message Postconditions: sms message sent
     */
    private void sendSMS() {
        try {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            Vector<GraphicalListComponent> myList = getVectorOfObjects();
            HashMap<String, SEmployee> myGoodSMS = new HashMap<String, SEmployee>();
            Iterator iter = myList.iterator();
            while (iter.hasNext()) {
                GraphicalListComponent comp = (GraphicalListComponent) iter.next();
                if (comp.AmISelected) {
                    SEmployee emp = null;
                    emp = (SEmployee) comp.getObject();
                    if (!emp.getCell().equals("")) {
                        myGoodSMS.put(emp.getCell(), emp);
                    }
                    if (!emp.getPhone().equals("")) {
                        myGoodSMS.put(emp.getPhone(), emp);
                    }
                    if (!emp.getPhone2().equals("")) {
                        myGoodSMS.put(emp.getPhone2(), emp);
                    }
                }
            }
            MessagingCommunicationBatch batch = new MessagingCommunicationBatch();
            try {
                batch.setAttachPdf(false);
                batch.setFromEmail("");
                batch.setIsemail(false);
                batch.setIssms(true);
                batch.setMessagingSouce(8);
                batch.setSubject("");
                batch.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                batch.setTimeSent(new Date(GenericController.getInstance(company).getCurrentTimeMillis()));
                MessagingController.getInstance(company).saveCommunicationBatch(batch);
            } catch (Exception exe) {
            }

            boolean needsDates = false;
            CompanyController companyController = new CompanyController();
            Company company = companyController.getCompanyById(Integer.parseInt(this.company));
            if (new EmailController(company, new Branch()).containsVerifyOrView(this.getMessage(false))) {
                needsDates = true;
            }
            Calendar startWeek = null;
            Calendar endWeek = null;
            if (needsDates) {
                Integer companyId = Integer.parseInt(myConnection.myCompany);
                Calendar[] getDates = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true).getDatesFromDialog(companyId);
                startWeek = getDates[0];
                endWeek = getDates[1];
            }

            ScheduleSmSRequest send = new ScheduleSmSRequest(
                    myGoodSMS, user, company.getCompId(), this.getMessage(false), startWeek, endWeek, this.myConnection, batch);
            JOptionPane.showMessageDialog(this,
                    "SMS messages successfully sent.",
                    "SMS Messaging", JOptionPane.OK_OPTION);

            resetAfterSend(0);
        } catch (Exception ex) {
            //  send failed, print stack and display error
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error:  SMS Messages did not send.  Error Code:  "
                    + ex.toString(), "SMS Messaging",
                    JOptionPane.ERROR_MESSAGE);
        } finally {
            //  reset cursor
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }

    /**
     * Method Name: sendEmail Purpose of Method: sends out an email message
     * Arguments: none Returns: void Prconditions: user wishes to send email
     * message Postconditions: email message sent
     */
    private void sendEmail() {
        try {
            Cursor hourglassCursor = new Cursor(Cursor.WAIT_CURSOR);
            setCursor(hourglassCursor);

            boolean includeSignature = messagingForm.getEmailForm().shouldIncludeSignature();

            ArrayList<SEmployee> emps = new ArrayList<SEmployee>();
            try {
                for (int i = 0; i < myListComponents.size(); i++) {
                    GraphicalListComponent comp = myListComponents.get(i);
                    SEmployee emp = (SEmployee) comp.getObject();
                    if (comp.AmISelected) {
                        emps.add(emp);
                    }
                }
            } catch (Exception exe) {
            }

            boolean hasLinks = false;
            String message = this.getMessage(true);
            if (message.contains(EmailController.VERIFY_URL.replaceAll("\\\\", "")) || message.contains(EmailController.VIEW_URL.replaceAll("\\\\", ""))) {
                hasLinks = true;
            }

            Branch branch = null;
            try {
                branch = branchCache.get(Integer.parseInt(this.myConnection.myBranch));
            } catch (Exception exe) {
            }
            if (addSchedText) {
                message += "[shifts]\r\n\r\n";
            }
            if (branch != null && includeSignature) {
                message += "\r\n\r\nIf you have any questions please contact:";
                message += "\r\n" + branch.getBranchInfo().getContactName();
                message += "\r\n" + branch.getBranchInfo().getContactEmail();
                message += "\r\n" + branch.getBranchInfo().getContactPhone();
            }

            message = message.replaceAll("\n", "<br/>");
            message = message.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");

            Date startDate = null;
            Date endDate = null;
            if (isAttach() || isAddSchedText() || hasLinks) {
                Integer companyId = Integer.parseInt(this.myConnection.myCompany);
                Calendar[] getDates = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true).getDatesFromDialog(companyId);
                startDate = getDates[0].getTime();
                endDate = getDates[1].getTime();
            }
            GeneralEmailMessageSend send = new GeneralEmailMessageSend(emps, this.myConnection, message, includeSignature, isAttach(), isAddSchedText(), hasLinks, startDate, endDate);

            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Emails have been sent!");
            resetAfterSend(1);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error:  Email Messages did not send.  Error Code:  "
                    + ex.toString(),
                    "Email Messaging", JOptionPane.ERROR_MESSAGE);
        } finally {
            Cursor normalCursor = new Cursor(Cursor.DEFAULT_CURSOR);
            setCursor(normalCursor);
        }
    }

    /**
     * Method Name: prepareDataObject Purpose of Method: checks the current tab,
     * loads the message into the data object, loads the subject into the data
     * object, user safety Arguments: none Returns: void Preconditions: user has
     * clicked send, data object not prepared, send user safety not run
     * Postconditions: user safety run, data object contains all information to
     * send
     */
    private void prepareDataObject(int form) {
        if (form == 1) {
            Text_Messaging smsForm = messagingForm.getSMSForm();
            StringBuffer message = new StringBuffer(0);
            message.append(smsForm.doOnSend());
        } else if (form == 2) {
            Email_Messaging emailForm = messagingForm.getEmailForm();
            StringBuffer subject = new StringBuffer(0);
            subject.append(emailForm.getSubject());
            StringBuffer message = new StringBuffer(0);
            message.append(emailForm.doOnSend());
            loadSubject(subject.toString());
        }
    }

    /**
     * Method Name: isSafe Purpose of Method: Runs a series of user safety
     * checks to ensure the user has completed all fields in order to send
     * Arguments: an int describing which form is selected Returns: a boolean
     * describing if the form passes the safety checks Preconditions: user
     * wishes to send, safety checks no performed Postconditions: safety checks
     * performed, boolean returned
     */
    private boolean isSafe(int form) {
        //  declaration of boolean to return
        boolean isSafe = false;

        //  safety checks for SMS tab
        if (form == 1) {
            //  get sms form
            Text_Messaging smsForm = messagingForm.getSMSForm();

            //  safety check for message
            boolean hasMessage = false;
            StringBuilder message = new StringBuilder(0);
            message.append(smsForm.doOnSend());
            if (message.length() > 0) {
                hasMessage = true;
            } else {
                JOptionPane.showMessageDialog(this, "Error:  You must type"
                        + " a message to send.", "SMS Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  safety check for recipients
            if (isSendListNull()) {
                JOptionPane.showMessageDialog(this, "Error:  You have no "
                        + "employees selected to message.", "SMS Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  compare sub safety checks
            if (hasMessage && !isSendListNull()) {
                isSafe = true;
            }
        } //  safety checks for Email tab
        else if (form == 2) {
            //  get email form
            Email_Messaging emailForm = messagingForm.getEmailForm();

            //  safety check for subject
            boolean hasSubject = false;
            StringBuffer subject = new StringBuffer(0);
            subject.append(emailForm.getSubject());
            if (subject.length() > 0) {
                hasSubject = true;
            } else {
                JOptionPane.showMessageDialog(this, "Error:  You must enter "
                        + "a subject for the email.", "Email Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  safety check for message
            boolean hasMessage = false;
            StringBuffer message = new StringBuffer(0);
            message.append(emailForm.doOnSend());
            if (message.length() > 0) {
                hasMessage = true;
            } else {
                JOptionPane.showMessageDialog(this, "Error:  You must type"
                        + " a message to send.", "Email Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  safety check for recipients
            if (isSendListNull()) {
                JOptionPane.showMessageDialog(this, "Error:  You have no "
                        + "employees selected to message.", "Email Messaging",
                        JOptionPane.ERROR_MESSAGE);
            }

            //  compare sub safety checks
            if (hasMessage && hasSubject && !isSendListNull()) {
                isSafe = true;
            }
        }

        //  return safety check
        return isSafe;
    }

    /**
     * Method Name: isSendListNull Purpose of Method: checks to make sure there
     * is an employee to be messaged Arguments: none Returns: a boolean
     * describing if the messaging list is null Preconditions: user pressed
     * send, need to determine if any employees are selected to send
     * Postcondition: check performed
     */
    private boolean isSendListNull() {
        boolean returnFlag = true;

        //  check to ensure someone has been selected
        int i = 0;
        while (i < getVectorOfObjects().size() && returnFlag) {
            GraphicalListComponent comp = getVectorOfObjects().get(i);
            if (getVectorOfObjects().get(i).isVisible() && comp.AmISelected) {
                returnFlag = false;
            } else {
                i++;
            }
        }
        return returnFlag;
    }

    /**
     * Method Name: loadMessage Purpose of Method: loads the message from the
     * argument into the data object Arguments: a string containing the message
     * Returns: void Preconditions: message known, not loaded into data object
     * Postconditions: message loaded into data object
     */
    private String getMessage(boolean isEmail) {
        if (isEmail) {
            return messagingForm.getEmailForm().doOnSend();
        } else {
            return messagingForm.getSMSForm().doOnSend();
        }
    }

    /**
     * Method Name: loadSubject Purpose of Method: loads the subject from the
     * argument into the data object Arguments: a string containing the subject
     * Returns: void Preconditions: subject known, not loaded into data object
     * Postconditions: messubjectsage loaded into data object
     */
    private void loadSubject(String subject) {
        //  iterate through vector, loading message
        for (int i = 0; i < getVectorOfObjects().size(); i++) {
            GraphicalListComponent comp = getVectorOfObjects().get(i);

            //  check to ensure object is visible and selected
            if (getVectorOfObjects().get(i).isVisible() && comp.AmISelected) {
                SEmployee tempData = (SEmployee) comp.getObject();
                tempData.setSubject(subject);
            }
        }
    }

    /**
     * Little method to clear the Filters
     */
    private void setupFilters() {
        this.messagingForm.clearFilters();
        this.messagingForm.addFilterType(noFilterType, false);
        this.messagingForm.addFilterType(lessThanFortyHours, true);
        this.messagingForm.addFilterType(availableFilterType, true);
    }

    /**
     * This method ensures that only people visible are selected.
     */
    private void ensureCorrectList() {
        for (GraphicalListComponent element : getVectorOfObjects()) {
            if (!element.isVisible()) {
                element.setSelected(false);
            }
        }
    }

    /**
     * Method Name: setUpToolBar Purpose of Method: sets up the JToolBar
     * myToolBar Arguments: none Returns: void Precondition: myToolBar
     * initialized in initComponents Postcondition: myToolBar set up with all
     * icons and proper handling
     */
    @Override
    protected void setUpToolBar() {
        //  create new icons
        mySendIcon = new myToolBarIcons();
        myResetIcon = new myToolBarIcons();
        myExitIcon = new myToolBarIcons();

        //  set icon text
        mySendIcon.setToolTipText("Send Message to Selected Employees");
        mySendIcon.setText(getSendString(), new Font("Dialog", Font.BOLD, 14));
        myResetIcon.setToolTipText("Reset all Fields");
        myResetIcon.setText(getResetString(), new Font("Dialog", Font.BOLD, 14));
        myExitIcon.setToolTipText("Exit this window");
        myExitIcon.setText(getExitString(), new Font("Dialog", Font.BOLD, 14));

        //  set icons
        mySendIcon.setIcon(Main_Window.Send_Message_Icon_36x36);
        myResetIcon.setIcon(Main_Window.Reset_Message_Icon_36x36);
        myExitIcon.setIcon(Main_Window.Exit_Message_Icon_36x36);

        //  add icons to tool bar in proper format
        myToolBar.add(mySendIcon);
        myToolBar.add(createSpacerPanel());
        myToolBar.add(myResetIcon);
        myToolBar.add(createSpacerPanel());
        myToolBar.add(myExitIcon);

        //  add mouse listeners
        /**
         * mySendIcon mouse listener implementation
         */
        mySendIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                if (isSafe(determineMessageType())) {
                    prepareDataObject(determineMessageType());

                    switch (determineMessageType()) {
                        case 1:
                            ensureCorrectList();
                            sendSMS();
                            break;
                        case 2:
                            ensureCorrectList();
                            sendEmail();
                            break;
                    }
                }
            }
        });
        /**
         * myResetIcon mouse listener implementation
         */
        myResetIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                messagingForm.showTemplateButton(false);
                reset(false);
            }
        });
        /**
         * myExitIcon mouse listener implementation
         */
        myExitIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                setVisible(false);
            }
        });

        //  set size of icons
        mySendIcon.setSize(new Dimension(120, 25));
        myResetIcon.setSize(new Dimension(120, 25));
        myExitIcon.setSize(new Dimension(120, 25));

        myDeleteIcon = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
            }
        };
    }

    @Override
    protected void showDeleted(boolean isPressedLocal) {
        selectAll(0, isPressedLocal);
    }

    @Override
    protected ImageIcon getDeletedUpIcon() {
        return Main_Window.SelectAllIcon;
    }

    @Override
    protected ImageIcon getDeletedDownIcon() {
        return Main_Window.DeselectAllIcon;
    }

    @Override
    public boolean getToggleDeleted() {
        return true;
    }

    @Override
    protected void promptUserToSave() {
        //Don't want to do anything
    }

    /**
     * Method Name: getSaveString Purpose of Method: returns the string to be
     * displayed on save icon Arguments: none Returns: a string describing the
     * text to be displayed on save icon Precondition: a piece of code desires
     * the text to be display on save icon, text already known by method
     * Postcondition: string returned containing text to be displayed on save
     * icon
     */
    @Override
    protected String getSaveString() {
        return "Save";
    }

    /**
     * Method Name: getSendString Purpose of Method: returns the string to be
     * displayed on send icon Arguments: none Returns: a string describing the
     * text to be displayed on send icon Precondition: a piece of code desires
     * the text to be display on send icon, text already known by method
     * Postcondition: string returned containing text to be displayed on send
     * icon
     */
    protected String getSendString() {
        return "Send";
    }

    /**
     * Method Name: getResetString Purpose of Method: returns the string to be
     * displayed on reset icon Arguments: none Returns: a string describing the
     * text to be displayed on reset icon Precondition: a piece of code desires
     * the text to be display on reset icon, text already known by method
     * Postcondition: string returned containing text to be displayed on reset
     * icon
     */
    protected String getResetString() {
        return "Reset";
    }

    /**
     * Method Name: getExitString Purpose of Method: returns the string to be
     * displayed on exit icon Arguments: none Returns: a string describing the
     * text to be displayed on exit icon Precondition: a piece of code desires
     * the text to be display on exit icon, text already known by method
     * Postcondition: string returned containing text to be displayed on exit
     * icon
     */
    protected String getExitString() {
        return "Exit";
    }

    /**
     * Method Name: createSpacePanel Purpose of Method: returns a "space" panel
     * to separate icons Arguments: none Returns: a JPanel for space creation
     * Precondition: spacer panel needed Postcondition: spacer panel created and
     * returned
     */
    private JPanel createSpacerPanel() {
        //  create spacer panel, set properties
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);

        //  return spacer panel
        return spacer;
    }

    /**
     * Override if you want to change the generic list container behaviour
     *
     * @return GenericListContainer
     */
    @Override
    protected GenericListContainer createListContainer() {
        GenericListContainer cont = new GenericListContainer(this);
        cont.setMutlipleSelection(true);
        cont.setUnSelectable(true);
        return cont;
    }

    /**
     * Method Name: initializeVariables Purpose of Method: initializes class
     * variables without the constructor calls due to getData() method being
     * called by super prior to class constructor being executed Arguments: none
     * Returns: void Precondition: variables set to null Postcondition: class
     * variables initialized
     */
    private void initializeVariables() {
        // initialize variables
        thisObject = this;
        myConnection = new Connection();
    }

    /**
     * Method Name: setInformation Purpose of Method: allows internal data to be
     * set by Main_Window without having to pass values through constructor,
     * calls getData to load data from db Arguments: Two strings, containing the
     * company name and branch Returns: void Precondition: initial variables
     * uninitialized, known outside of instance. Postcondition: initial
     * variables set
     */
    public void setInformation(String tempCompany, String tempBranch, User tempUser) {
        //  set user
        this.user = tempUser;
        this.employeeIds = null;

        //  set connections
        myConnection.setCompany(tempCompany);
        myConnection.setBranch(tempBranch);

        //  set company, branch
        company = tempCompany;
        branch = tempBranch;

        getData();

        this.setupFilters();
        messagingForm.showTemplateButton(false);

        messagingForm.getSMSForm().viewController();
        messagingForm.getEmailForm().viewController();
    }

    /**
     * Method Name: setInformation Purpose of Method: allows internal data to be
     * set by Schedule_Main_Window without having to pass values through
     * constructor, calls getData to load data from db Arguments: Two strings,
     * containing the company name and branch, a DShift Returns: void
     * Precondition: initial variables uninitialized, known outside of instance.
     * Postcondition: initial variables set
     */
    public void setInformation(String tempCompany, String tempBranch, User tempUser, DShift tempDShift, ArrayList<Integer> employeeIds) {
        this.user = tempUser;
        this.employeeIds = employeeIds;

        myConnection.setCompany(tempCompany);
        myConnection.setBranch(tempBranch);

        company = tempCompany;
        branch = tempBranch;

        this.myDShift = tempDShift;

        if (company.matches("2")) {
            messagingForm.setDefaultTemplateMessage();
        }
        getData();

        this.setupFilters();
        messagingForm.showTemplateButton(true);
    }

    /**
     * Method sets information when messaging all employees by.
     *
     * @param company
     */
    public void setInformation(String company, String branch, User user, boolean lol) {
        this.company = company;
        this.user = user;
        this.branch = branch;
        this.employeeIds = null;
        this.myConnection.setCompany(this.company);
        getVectorOfObjects().clear();
        this.getData();
        this.setupFilters();

        this.messagingForm.loadTemplates();
    }

    /**
     * Grabs our dshift
     *
     * @return DShift
     */
    public DShift getDShift() {
        return myDShift;
    }

    /**
     * Method Name: selectAll Purpose of Method: selects/unselects every box in
     * listview depending one if SMS/Email is selected Arguments: an int
     * describing which tab (text/email etc) is active, and a boolean describing
     * if the boxes should be selected or unselected Returns: void
     * Preconditions: Select All box either checked or unchecked, no actions
     * have been performed Postconditions: boxs selected/unselected TAB: 0 for
     * SMS, 1 for Email
     */
    public void selectAll(int tab, boolean isSelectAll) {
        for (int i = 0; i < getVectorOfObjects().size(); i++) {
            GraphicalListComponent comp = getVectorOfObjects().get(i);
            comp.setSelected(isSelectAll);
            //System.out.println("");
        }
    }

    /**
     * Method Name: informNoShift Purpose of Method: method generates a pop
     * window to inform the user that no shift is currently associated with the
     * messaging window. The shift is required to calculate the "sort by
     * availability" Arguments: a string containing the message, a string
     * containing the window title Returns: void Preconditions: user has
     * attempted to select "sort by availability" with no corresponding shift
     * Postcondition: user informed via jpop window that there is no shift to
     * run methods against
     */
    public void informNoShift(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title,
                JOptionPane.OK_OPTION);
    }

    //  overriden abstract method implementations
    /**
     * Method Name: getWindowTitle Purpose of Method: generates and returns the
     * name of the window title Arguments: none Returns: a string containing the
     * name of the window Precondition: name of window known internally by
     * object Postcondition: name of window returned Implements: getWindowTitle
     * from GenericTabbedEditForm
     *
     * @return
     */
    @Override
    public String getWindowTitle() {
        StringBuilder windowTitleReturn = new StringBuilder();
        windowTitleReturn.append("Employee Messaging For ");
        windowTitleReturn.append(Main_Window.parentOfApplication.getCompanyNameById(company));
        windowTitleReturn.append(" in ");
        windowTitleReturn.append(Main_Window.parentOfApplication.getBranchNameById(branch));
        return windowTitleReturn.toString();
    }

    @Override
    public void getData() {
        if (thisObject == null) {
            initializeVariables();
        }

        myListComponents = new Vector();
        myListContainer.removeAll();
        super.clearList();
        super.clearData();

        String messagingType = MessagingSubForm.SMS_MESSAGING;
        if (messagingForm != null) {
            messagingType = messagingForm.getSelectedMessagingType();
        }
        boolean isEmailMessaging = messagingType.equals(MessagingSubForm.EMAIL_MESSAGING);
        employee_messaging_list_query query
                = new employee_messaging_list_query();
        query.update(branch, isEmailMessaging, this.employeeIds);

        Record_Set rs = new Record_Set();

        if (getConnection().myCompany != null && getConnection().myCompany.trim().length() > 0) {
            try {
                getConnection().prepQuery(query);
                rs = myConnection.executeQuery(query);
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.populateList(rs, "", "");
        }
    }

    /**
     * Method Name: addMyMenu Purpose of Method: adds menus to xMessagingEdit
     * Form Arguments: a JMenuBar Returns: void Precondition: xMessagingEdit
     * form trying to load, needs submenus Postcondition: submenus added
     * Implements: addMyMenu from GenericEditForm
     *
     * @param myMenu
     */
    @Override
    public void addMyMenu(JMenuBar myMenu) {

    }

    @Override
    public void clickedMenu(String action, String companyName,
            String branchName, String companyId, String branchId) {
        myConnection.setBranch(branchId);
        myConnection.setCompany(companyId);

        //  set private variables
        branch = branchId;
        company = companyId;

        getData();
    }

    @Override
    protected void windowHidden() {
        reset(true);
    }

    public void resetSubForms() {
    }

    public DShift getParentDShift() {
        return this.myDShift;
    }

    public String getCompany() {
        return this.company;
    }

    public String getBranch() {
        return this.branch;
    }

    public User getUser() {
        return this.user;
    }

    //  setter methods
    public void resetDShift() {
        this.myDShift = null;
    }

    @Override
    public Connection getConnection() {
        return myConnection;
    }

    @Override
    public String getDisplayNameForObject(Object o) {
        SEmployee emp = (SEmployee) o;
        return emp.getName();
    }

    @Override
    public Object createObjectForList(Record_Set rs) {
        Employee myEmployee = new Employee(new Date(), rs);
        SEmployee aEmployee = new SEmployee(myEmployee, null,
                null, "false", "", new ArrayList<String>());
        try {
            aEmployee.setAllow_sms_messaging(rs.getBoolean("sms_messaging"));
        } catch (Exception e) {
            aEmployee.setAllow_sms_messaging(false);
        }

        return aEmployee;
    }

    @Override
    public String getMyIdForSave() {
        return "";
    }

    @Override
    public void deleteData() {
    }

    /**
     * Method Name: reset Purpose of Method: reset runs when the reset icon is
     * clicked in the tool bar Arguments: none Returns: void Precondition: user
     * has clicked reset button Postcondition: all fields reset
     */
    public void reset(boolean isClosing) {
        //  reset all selected to unselected
        for (int i = 0; i < getVectorOfObjects().size(); i++) {
            GraphicalListComponent comp = getVectorOfObjects().get(i);
            comp.setSelected(false);
        }

        // reset select/deselect icon
        myToggleDeletedIcon.setPressed(false);

        // reset subForms
        messagingForm.doOnReset(isClosing);
    }

    /**
     * Method Name: resetOnCardChange Purpose of Method: sets all selected
     * graphical list components to unselected when the card is changed by user
     * Arguments: none Returns: void Preconditions: user has changed cards,
     * employee list must be reset Postconditions: employee list reset
     */
    public void resetOnCardChange() {
        //  iterate through vector, setting all showing AND selected to
        //      unselected
        for (int i = 0; i < getVectorOfObjects().size(); i++) {
            GraphicalListComponent comp = getVectorOfObjects().get(i);
            if (comp.AmISelected) {
                comp.setSelected(false);
            }
        }

        //  reset icon
        myToggleDeletedIcon.setPressed(false);
    }

    /**
     * @return the attach
     */
    public boolean isAttach() {
        return attach;
    }

    /**
     * @param attach the attach to set
     */
    public void setAttach(boolean attach) {
        this.attach = attach;
    }

    /**
     * @return the addSchedText
     */
    public boolean isAddSchedText() {
        return addSchedText;
    }

    /**
     * @param addSchedText the addSchedText to set
     */
    public void setAddSchedText(boolean addSchedText) {
        this.addSchedText = addSchedText;
    }
};
