/**
 *  Name of File:  GenericMessagingForm.java
 *  Author: Jeffrey N. Davis
 *  Date Created:  05/24/2010
 *  Date Last Modified:  05/24/2010
 *  Purpose of File:  provides an abstract class with new control functionality
 *      for messaging
 */
//  package declaration
package rmischedule.components.graphicalcomponents;

//  import declarations
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;
import javax.swing.JComponent;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rmischedule.components.List_View;
import rmischedule.main.Main_Window;
import rmischedule.messaging.datacomponents.Employee_Messaging_List_Data;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.*;
import rmischedule.messaging.datacomponents.Messaging_Filters_Data_Champion;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.SShift;
import rmischedule.schedule.components.availability.Messaging_Availability;
import rmischeduleserver.mysqlconnectivity.queries.messaging.messaging_availability_query;

/**
 *  Class Name:  GenericMessagingForm
 *  Purpose of Class:  an abstract class for future extension designed for
 *      messaging
 */
public abstract class GenericMessagingForm extends GenericEditForm {
    //  variable declarations

    protected Vector<GenericMessagingSubForm> mySubPanels;
    protected Vector<Employee_Messaging_List_Data> employeeListVector =
            new Vector<Employee_Messaging_List_Data>();
    protected Internal_List_View myListView;
    //  icon declarations
    protected myToolBarIcons myEmployeeMessagingHeaderPanelIcon;
    //protected myToolBarIcons mySaveIcon;
    protected myToolBarIcons mySendIcon;
    protected myToolBarIcons myResetIcon;
    protected myToolBarIcons myExitIcon;

    private GenericListContainer listContainer;

    //  public method implementations
    /**
     *  Method Name:  GenericMessagingForm
     *  Purpose of Method:  Creates an instance of the abstract class
     *      GenericMessagingForm
     */
    public GenericMessagingForm() {
        //  initialize Components from Swing
        initComponents();

        listContainer = new GenericListContainer(this);

        //  set initial variables
        mySubPanels = new Vector<GenericMessagingSubForm>();
        myListView = new Internal_List_View();
        

        //  add List_View to panel
        listViewPanel.add(myListView.getMyForm());

        //  call abstract methods from extensions
        addMyMenu(myMenu);
        //getData();

        //  hardcoded setup methods
        setUpToolBar();
        setUpEmployeeMessagingHeaderPanel();

        //  get/set window title
        setTitle(getWindowTitle());
    }
    
    //  private method implementations
    /**
     *  Method Name:  createSpacePanel
     *  Purpose of Method:  returns a "space" panel to separate icons
     *  Arguments:  none
     *  Returns:  a JPanel for space creation
     *  Precondition:  spacer panel needed
     *  Postcondition:  spacer panel created and returned
     */
    private JPanel createSpacerPanel() {
        //  create spacer panel, set properties
        JPanel spacer = new JPanel();
        spacer.setOpaque(false);

        //  return spacer panel
        return spacer;
    }

    /**
     *  Method Name:  loadEmployeeListVector
     *  Purpose of Method:  fills the employeeListVector with the data obtained
     *      by subclasses
     *  Arguments:  a record_set
     *  Returns:  void
     *  Preconditions:  relevant data already obtained from database, internal
     *      vector not loaded
     *  Postconditions:  employeeListVector loaded
     */
    private void loadEmployeeListVector(Record_Set rs, String company) {
        //  ensure that the vector is cleared for no duplicate entries
        employeeListVector =
            new Vector<Employee_Messaging_List_Data>();

//        GraphicalListComponent myComp = new
//                GraphicalListComponent(myVectorOfObjects.get(i), myListContainer, getDisplayNameForObject(myVectorOfObjects.get(i)));

        //  iterate through record set
        do {
            //  create new data object, create new object to hold record set
            //      information
            Employee_Messaging_List_Data tempData = new Employee_Messaging_List_Data();

            //  set data inside tempData from current record in record set
            tempData.setEmployeeId(rs.getString("empid"));
            tempData.setBranchId(rs.getString("branchid"));
            tempData.setFirstName(rs.getString("firstname"));
            tempData.setLastName(rs.getString("lastname"));
            tempData.setMiddleInitial(rs.getString("middleinitial"));
            tempData.setEmployeeCell(rs.getString("empcell"));
            tempData.setEmailPrimary(rs.getString("email0"));
            tempData.setEmailAlternate(rs.getString("email1"));
            tempData.setEmailMessagingType(rs.getString("emailmessaging"));
            tempData.setCanSmsMessage(rs.getBoolean("smsmessaging"));
            tempData.setCompanyId(company);

            //  determine if this employee can be email messaged, and then
            //      messaged by SMS or email
            tempData.determineCanEmailMessage();
            tempData.determineCanSmsMessage();
            tempData.determineCanMessage();

            //  add data object to vector
            employeeListVector.add(tempData);
        } while (rs.moveNext());
    }

    /**
     *  Method Name:  setCheckBoxAvailable
     *  Purpose of Method:  runs through the vector, calling the list_View to
     *      alter the check box for anyone based on availability
     *  Arguments:  an into describing which tab, value to set box
     *  Returns:  void
     *  Preconditions:  data set, GUI needs to be updated
     *  Postconditions:  GUI updated
     */
    private void setCheckBoxAvailable(int tab, boolean isSelected)
    {
        //  iterate through vector
        int count = 0;
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  check for tab
            switch(tab)
            {
                //  SMS tab
                case 0:  if(employeeListVector.get(i).getCanSmsMessage())
                         {
                            if(employeeListVector.get(i).getIsAvailable() && !employeeListVector.get(i).getIsOvertimeHit())
                            {
                                //  format name for comparison
                                StringBuffer name = new StringBuffer();
                                name.append(employeeListVector.get(i).
                                    getLastName());
                                name.append(", ");
                                name.append(employeeListVector.get(i).
                                    getFirstName());
                                name.append(" ");
                                name.append(employeeListVector.get(i).
                                    getMiddleInitial());

                                //  call listView to alter boolean box
                                myListView.changeSelected(name.toString(),
                                    0, isSelected);

                                count ++;
                            }
                         }
                         break;

                //  email tab
                case 1:  System.out.println("Email tab not implemented yet");
                         break;
            }

        }
        System.out.println("Size of count after GenericMessaging selectAvailableBox method:  " + count);
    }

    /**
     *  Method Name:  setCheckBoxOvertime
     *  Purpose of Method:  runs through the vector, calling the list_View to
     *      alter the check box for anyone based on overtime
     *  Arguments:  an int describing which tab, value to set box
     *  Returns:  void
     *  Preconditions:  data set, GUI needs to be updated
     *  Postconditions:  GUI updated
     */
    private void setCheckBoxOvertime(int tab, boolean isSelected)
    {
        int count = 0;
        //  iterate through vector
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  check for tab
            switch(tab)
            {
                //  SMS tab
                case 0:  if(employeeListVector.get(i).getCanSmsMessage())
                         {
                            if(employeeListVector.get(i).getIsAvailable())
                            {
                                //  format name for comparison
                                StringBuffer name = new StringBuffer();
                                name.append(employeeListVector.get(i).
                                    getLastName());
                                name.append(", ");
                                name.append(employeeListVector.get(i).
                                    getFirstName());
                                name.append(" ");
                                name.append(employeeListVector.get(i).
                                    getMiddleInitial());

                                //  call listView to alter boolean box
                                myListView.changeSelected(name.toString(),
                                    0, isSelected);
                                count ++;
                            }
                         }
                         break;

                //  email tab
                case 1:  System.out.println("Email tab not implemented yet");
                         break;
            }

        }
        System.out.println("Size of count after Generic Messaging selectOvertimeCheckBox:  " + count);
    }

    /**
     *  Method Name:  determineOvertimeHit
     *  Purpose of Method:  determines if the selected shifts would force
     *      an employee into overtime, then sets the data component inside
     *      Employee_Messaging_List_Data accordingly
     *  Arguments:  Messaging_Availability instance
     *  Returns:  none
     *  Preconditions:  data known to calculate overtime, calculations not
     *      performed
     *  Postconditions:  calculations performed, data components set accordingly
     */
    private void determineOvertimeHit(Messaging_Availability myAvailability)
    {
        int count = 0;
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }

                //  sum overtime from selected shifts
                Vector<SShift> sShiftVector = myAvailability.getSShiftOfSelectedShifts();
                double totalHoursFromSShiftsVector = 0;
                for(int k = 0;k < sShiftVector.size();k ++)
                    totalHoursFromSShiftsVector += sShiftVector.get(k).myShift.getNoHoursDouble();

                //  determine total number of hours
                SEmployee tempSEmployee = sEmpVector.get(j);
                int weekId = sShiftVector.get(0).myShift.getWeekNo();
                double totalHours = tempSEmployee.getHoursWorkedForWeek(weekId) + totalHoursFromSShiftsVector;
                if(totalHours > 40.0)
                {
                    count ++;
                    employeeListVector.get(i).setIsOvertimeHit(true);
                }
                    else
                    employeeListVector.get(i).setIsOvertimeHit(false);
            }
        }
        System.out.println("Size of count after GenericMessaging determineOvertime:  " + count);
    }

    /**
     *  Method Name:  determineIsArmed
     *  Purpose of Method:  compares Employee_Messaging_List_Data to SEmpolyee
     *      and determines whether the employee in EMLD isArmed, then sets
     *      the boolean value accordingly
     *  Arguments:  an instance of Messaging_Availability
     *  Returns:  void
     *  Preconditions:  Messaging_Availability knows whether emp is armed,
     *      internal data strucuture does not
     *  Postconditions:  internal data structure updated with proper data
     */
    private void determineIsArmed(Messaging_Availability myAvailability)
    {
        
    }

    //  protected method implementations
    /**
     *  Method Name:  setUpToolBar
     *  Purpose of Method:  sets up the JToolBar myToolBar
     *  Arguments:  none    
     *  Returns:  void
     *  Precondition:  myToolBar initialized in initComponents
     *  Postcondition:  myToolBar set up with all icons and proper handling
     */
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
         *  mySendIcon mouse listener implementation
         */
        mySendIcon.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                saveData();
                send();
            }
        });
        /**
         *  myResetIcon mouse listener implementation
         */
        myResetIcon.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                reset(false);
            }
        });
        /**
         *  myExitIcon mouse listener implementation
         */
        myExitIcon.addMouseListener(new java.awt.event.MouseAdapter() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent event) {
                reset(true);
                setVisible(false);
            }
        });

        //  set size of icons
        mySendIcon.setSize(new Dimension(120, 25));
        myResetIcon.setSize(new Dimension(120, 25));
        myExitIcon.setSize(new Dimension(120, 25));
    }

    /**
     *  Method Name:  getSaveString
     *  Purpose of Method:  returns the string to be displayed on save icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on save icon
     *  Precondition:  a piece of code desires the text to be display on save
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on save
     *      icon
     */
    protected String getSaveString() {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Save");

        //  return string
        return returnString.toString();
    }

    /**
     *  Method Name:  getSendString
     *  Purpose of Method:  returns the string to be displayed on send icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on send icon
     *  Precondition:  a piece of code desires the text to be display on send
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on send
     *      icon
     */
    protected String getSendString() {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Send");

        //  return string
        return returnString.toString();
    }

    /**
     *  Method Name:  getResetString
     *  Purpose of Method:  returns the string to be displayed on reset icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on reset icon
     *  Precondition:  a piece of code desires the text to be display on reset
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on reset
     *      icon
     */
    protected String getResetString() {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Reset");

        //  return string
        return returnString.toString();
    }

    /**
     *  Method Name:  getExitString
     *  Purpose of Method:  returns the string to be displayed on exit icon
     *  Arguments:  none
     *  Returns:  a string describing the text to be displayed on exit icon
     *  Precondition:  a piece of code desires the text to be display on exit
     *      icon, text already known by method
     *  Postcondition:  string returned containing text to be displayed on exit
     *      icon
     */
    protected String getExitString() {
        //  create, set text to be returned
        StringBuffer returnString = new StringBuffer();
        returnString.append("Exit");

        //  return string
        return returnString.toString();
    }

    /**
     *  Method Name:  setUpEmployeeMessagingHeaderPanel
     *  Purpose of Method:  sets up the Jpanel employeeMessagingHeaderPanel
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  employeeMessagingHeaderPanel initialized in
     *      initComponents
     *  Postcondition:  employeeMessagingHeaderPanel set up with all icons
     */
    protected void setUpEmployeeMessagingHeaderPanel() {
        myEmployeeMessagingHeaderPanelIcon = new myToolBarIcons();
        myEmployeeMessagingHeaderPanelIcon.setIcon(
                Main_Window.Employee_Message_Icon_227x23);
        employeeMessagingHeaderPanel.add(myEmployeeMessagingHeaderPanelIcon);
    }

    /**
     *  Method Name:  populateList
     *  Purpose of Method:  allows the extensions to populate the List_View
     *  Arguments:  a record_set conataining the relevant information
     *  Returns: void
     *  Precondtion:  extensions have needed information
     *  Postcondition:  myListView populated
     */
    protected void populateList(Record_Set rs, String company) {
        //  load internal vector
        loadEmployeeListVector(rs, company);

        //  load List_View
        myListView.populate(employeeListVector);
    }

    /**
     *  Method Name:  loadMessage
     *  Purpose of Method:  loads the message into the data components
     *  Arguments:  a string representing the message, two booleans describing
     *      the type of message
     *  Returns:  void
     *  Preconditions:  message known by child, needs to be loaded into data
     *      components
     *  Postconditions:  message set within data components
     */
    protected void loadMessage(String message, boolean isEmail, boolean isSMS) {
        //  determine whether message is email or SMS
        if (isEmail)
        {
            //  iterate through vector, loading message into data components
            for(int i = 0;i < employeeListVector.size();i ++)
            {
                //  check to see if employee is to be messaged via email
                if(employeeListVector.get(i).getSendEmail())
                    employeeListVector.get(i).setMessage(message);
            }
        }
        else if (isSMS)
        {
            //  iterate through vector, loading message into data components
            for (int i = 0; i < employeeListVector.size(); i++)
            {
                //  check to see if employee is to be messaged via sms
                if (employeeListVector.get(i).getSendSms())
                    employeeListVector.get(i).setMessage(message);
            }
        }
    }

    /**
     *  Method Name:  loadSubject
     *  Purpose of Method:  loads the subject into the data components
     *  Arguments:  a string representing the subject
     *  Returns:  void
     *  Preconditions:  subject known by child, needs to be loaded into data
     *      components
     *  Postconditions:  subject set within data components
     */
    protected void loadSubject(String subject)
    {
        //  iterate through vector, loading message into data components
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            //  check to see if employee is to be messaged via email
            if(employeeListVector.get(i).getSendEmail())
                employeeListVector.get(i).setEmailSubject(subject);
        }
    }


    /**
     *  Method Name:  getEmployeeListVector
     */
    protected Vector<Employee_Messaging_List_Data> getEmployeeListVector() {
        return this.employeeListVector;
    }

     /**
     *  Method Name:  isSMSListNull
     *  Purpose of Method:  checks to make sure there is an employee to be
     *      messaged
     *  Arguments:  none
     *  Returns:  a boolean describing if the SMS messaging List is null
     *  Preconditions:  user pressed send, need to determine if any employees
     *      are selected to send
     *  Postcondition:  check performed
     */
    protected boolean isSMSListNull()
    {
        //  declaration of flag to return
        boolean returnFlag = true;

        //  check to ensure someone has been selected
        int i = 0;
        while(i < employeeListVector.size() && returnFlag)
        {
            if(!employeeListVector.get(i).getSendSms())
                i ++;
            else
                returnFlag = false;
        }
    
        //  return tested flag
        return returnFlag;
    }

    /**
     *  Method Name:  isEmailListNull
     *  Purpose of Method:  checks to make sure there is an employee to be
     *      messaged
     *  Arguments:  none
     *  Returns:  a boolean describing if the email messaging List is null
     *  Preconditions:  user pressed send, need to determine if any employees
     *      are selected to send
     *  Postcondition:  check performed
     */
    protected boolean isEmailListNull()
    {
        //  declaration of flag to return
        boolean returnFlag = true;

        //  check to ensure someone has been selected
        int i = 0;
        while(i < employeeListVector.size() && returnFlag)
        {
            if(!employeeListVector.get(i).getSendEmail())
                i ++;
            else
                returnFlag = false;
        }
        //  return check
        return returnFlag;
    }


    /**
     *  Method Name: addSubForm
     *  Purpose of Method:  adds a subForm to the the form
     *  Arguments:  an instance of GenericMessagingSubForm
     *  Returns:  void
     *  Preconditions:  subForm created elsewhere, needs to be added to form
     *  Postcondition:  subForm added to panel, added to internal data structure
     *      designed to hold subForms
     */
    public void addSubForm(GenericMessagingSubForm myNewForm) {
        //  add new form to panel, internal data structure
        getParentPanel().add(myNewForm.getMyForm());
        mySubPanels.add(myNewForm);

        //  set parent
        myNewForm.setMyParent(this);
    }

    /**
     *  Method Name:  getParentPanel
     *  Purpose of Method:  returns the nonListMainPanel
     *  Arguments:  none
     *  Returns:  the JComponent nonListMainPanel
     *  Preconditions:  nonListMainPanel desired by another piece of code
     *  Postconditions:  nonListMainPanel returned
     * @return
     */
    public JComponent getParentPanel() {
        return nonListMainPanel;
    }

    /**
     *  Method Name:  saveData
     *  Purpose of Method:  saveData runs when the Save icon is clicked in the
     *      tool bar.
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  user has clicked save icon
     *  Postcondition:
     */
    public void saveData() {
        //  update internal data structure to reflect changes made in List_View
        myListView.updateUserAlteredListView(employeeListVector);

        //  iterate through subPanels, calling each panel's saveData
        for (int i = 0; i < mySubPanels.size(); i++) {
            mySubPanels.get(i).getSelected(employeeListVector);
        }
    }

    /**
     *  Method Name:  send
     *  Purpose of Method:  send runs when the Send icon is clicked in the tool
     *      bar
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  user has clicked send button
     *  Postcondition:
     */
    /* public void send()
    {
    boolean isEmailTab = false;
    boolean isSmsTab = false;

    Object temp = nonListMainPanel.getClass();
    System.out.println(temp.toString());



    }*/
    /**
     *  Method Name:  reset
     *  Purpose of Method:  reset runs when the reset icon is clicked in the
     *      tool bar
     *  Arguments:  none
     *  Returns:  void
     *  Precondition:  user has clicked reset button
     *  Postcondition:  List_View altered to initial status, all TO panes
     *      cleared, all messaging fields cleared.
     */
    public void reset(boolean isClosing) {
        //  reset sendEmail, sendSms to false for all data components in vector
        for (int i = 0; i < employeeListVector.size(); i++) {
            //  test to see if employee can be messaged, if not loop falls
            //      through
            if (employeeListVector.get(i).getCanMessage()) {
                //  reset each canEmail, canSms to false
                employeeListVector.get(i).setSendEmail(false);
                employeeListVector.get(i).setSendSms(false);
                employeeListVector.get(i).setIsAvailable(true);
            }
        }

        //  clear List_View, rebuild
        myListView.resetListView();
        myListView.populate(employeeListVector);

        //  run through subPanels calling doOnReset
        for (int i = 0; i < mySubPanels.size(); i++) {
            mySubPanels.get(i).doOnReset(isClosing);
        }
    }

    /**
     *  Method Name:  selectAvailable
     *  Purpose of Method:  selects/unselects every box in listview depending
     *      one if SMS/Email is selected
     *  Arguments:  an int describing which tab (text/email etc) is active,
     *      and a boolean describing if the boxes should be selected or
     *      unselected
     *  Returns:  void
     *  Preconditions:  Select Available box either checked or unchecked, no
     *      actions have been performed
     *  Postconditions:  boxs selected/unselected
     *  TAB:  0 for SMS, 1 for Email
     */
    public void selectAvailable(int tab, boolean isSelectAvailable,
        DShift myShift, String company, String branch,
        Messaging_Availability myAvailability)
    {
        //  determine overtime
        determineOvertimeHit(myAvailability);

        int count = 0;
        //  determine availability
        for(int i = 0;i < employeeListVector.size();i ++)
        {
            if(employeeListVector.get(i).getCanMessage())
            {
                //  get matching SEmployee object from myAvailability
                Vector<SEmployee> sEmpVector = myAvailability.getFullEmployeeList();
                Integer employeeListVectorEmpId = new Integer(employeeListVector.get(i).getEmployeeId());
                boolean isFound = false;
                int j = 0;
                while(j < sEmpVector.size() && !isFound)
                {
                    if(sEmpVector.get(j).getId() == employeeListVectorEmpId)
                        isFound = true;
                    else
                        j ++;
                }
                
                //  determine availability per SShift (via DShift)
                SEmployee tempSEmployee = sEmpVector.get(j);
                Vector<SShift> sShiftVector = myAvailability.getSShiftOfSelectedShifts();
                //  if k = x after loop, employee is available
                int k = sShiftVector.size();
                int x = 0;
                boolean flag = true;
                while(x < sShiftVector.size() && flag)
                {
                    if (sShiftVector.get(x).myShift instanceof DShift) {
                        DShift testDShift = (DShift)sShiftVector.get(x).myShift;
                        if(tempSEmployee.isAvailable(testDShift)) {
                            x ++;
                        } else {
                            flag = false;
                        }
                    }
                }

                if(k == x)
                {
                    count ++;
                    employeeListVector.get(i).setIsAvailable(true);
                }
                     else
                    employeeListVector.get(i).setIsAvailable(false);
             }
         }

        System.out.println("Size of count after genericMessagingIsAvailable:  " + count);
        //  check which tab to perform actions on
        switch(tab)
        {
            //  SMS tab
            case 0:  if(isSelectAvailable)
                        setCheckBoxAvailable(0, true);
                                          
                     // unselect each box
                     else
                        setCheckBoxAvailable(0,false);
                     break;
            //  email tab
            case 1:  System.out.println("Select Available not implemented for email tab yet.");
                     break;
        }
    }

    /**
     *  Method Name:  selectOvertime
     *  Purpose of Method:  selects/unselects every box in listview depending
     *      one if SMS/Email is selected
     *  Arguments:  an int describing which tab (text/email etc) is active,
     *      and a boolean describing if the boxes should be selected or
     *      unselected
     *  Returns:  void
     *  Preconditions:  Select Overtime box either checked or unchecked, no
     *      actions have been performed
     *  Postconditions:  boxs selected/unselected
     *  TAB:  0 for SMS, 1 for Email
     */
    public void selectOvertime(int tab, boolean isSelectOvertime,
        Messaging_Availability myAvailability)
    {
        determineOvertimeHit(myAvailability);

        //  check which tab to perform actions on
        switch(tab)
        {
            //  SMS tab
            case 0:  if(isSelectOvertime)
                        setCheckBoxOvertime(0,true);
                     else
                        setCheckBoxOvertime(0, false);
                     break;
            //  email tab
            case 1:  System.out.println("Select Overtime not implemented for email tab yet.");
                     break;
        }
    }

    //  abstract method declarations
    public abstract String getWindowTitle();

    public abstract void addMyMenu(JMenuBar myMenu);

    public abstract void getData();

    public abstract void send();

    //  internal List_View class for the ability to return a JPanel
    protected class Internal_List_View extends JPanel {

        private List_View listView;

        /**
         *  Method Name:  Internal_List_View
         *  Purpose of Method:  creates instance of Internal_Liew_View
         *  Arguments:  none
         *  Returns:  none
         *  Precondition:  object DNE
         *  Postcondition:  object exists, initial variables set
         */
        public Internal_List_View() {
            //  set initial variables
            listView = new List_View();

            //  initialize List_View components
            //  initialize List_View
            setLayout(new BorderLayout());
            listView.addColumn("Email",
                    List_View.BOOLEAN, true, true, 30);
            listView.addColumn("SMS", List_View.BOOLEAN, true, true, 30);
            listView.addColumn("Employee", List_View.STRING, false, true, 100);
            listView.buildTable();
            listView.maximizeTable();

            //  set list to be sorted by store services, not boolean value
            //  sort not connected, so only sorts by column and not entire row
            //listView.sort(1);

            //  add the formatted tab to panel, data loaded serparately
            add(listView.myScrollPane, BorderLayout.CENTER);
        }

        /**
         *  Method Name:  getMyForm
         *  Purpose of Method:  returns this class as a JPanel
         *  Arguments:  none
         *  Returns:  this class as a JPanel
         *  Precondition:  another piece of code requires this class
         *  Postcondition:  this class returned
         */
        public JPanel getMyForm() {
            return this;
        }

        /**
         *  Method Name:  populate
         *  Purpose of Method:  populates the list_view with data
         *  Arguments:  the employeeListVector containg all relevant data
         *  Returns:  void
         *  Preconditions:  data contained in vector, list_view not
         *      populated
         *  Postconditions:  list_view populated
         */
        public void populate(Vector<Employee_Messaging_List_Data> listViewVector) {
            //  ensure listView is empty for no duplicate entries
            listView.clearRows();

            //  sort Vector to ensure proper listView fill
            Collections.sort(listViewVector);

            //  iterate through the the vector, load into List_View
            for (int i = 0; i < listViewVector.size(); i++) {
                //  create temporary data object, set to data in current
                //      position of vector
                Employee_Messaging_List_Data tempData = new Employee_Messaging_List_Data();
                tempData = listViewVector.get(i);

                //  determine if employee can be message, if so, populate
                //      list_view with that data
                if (tempData.getCanMessage())
                {
                    StringBuffer formattedName = new StringBuffer();
                    formattedName.append(tempData.getLastName());
                    formattedName.append(", ");
                    formattedName.append(tempData.getFirstName());
                    formattedName.append(" ");
                    formattedName.append(tempData.getMiddleInitial());

                    //  create new object to stuff into list_view, set the
                    //      elements
                    Object[] nextRow = new Object[3];
                    //  set email to uneditible until email functionality added
                    if(tempData.getCanEmailMessage())
                        nextRow[0] = true;
                    else
                        nextRow[0] = false;
                    if(tempData.getCanSmsMessage())
                        nextRow[1] = true;
                    else
                        nextRow[1] = false;
                    //nextRow[1] = tempData.getCanSmsMessage();
                    nextRow[2] = formattedName.toString();

                    //  add object to list_view
                    listView.addRowSetEnabledBooleans(nextRow);
                }
            }

            //  let the program know the data inside the List_View has changed
            listView.fireTableDataChanged();
        }

        /**
         *  Method Name:  updateUserAlteredListView
         *  Puprose of Method:  crawls through the List_View, searching for
         *      any changes made by the user, then alters the argument
         *      accordingly
         *  Arguments:  a vector containing the data components the List_View
         *      initially pulled data from
         *  Returns:  void
         *  Precondition:  internal data structure not updated to List_View
         *      changes
         *  Postcondition:  internal data strucuture altered to reflect
         *      modifications within List_View
         */
        public void updateUserAlteredListView(
                Vector<Employee_Messaging_List_Data> myVector) {

            //  iterate through list_view, altering vector as needed
            for (int i = 0; i < listView.getRowCount(); i++) {
                //  get data from each row in table
                Object tempCanEmail = listView.getValueAt(i, 0);
                Object tempCanSms = listView.getValueAt(i, 1);
                Object tempName = listView.getValueAt(i, 2);

                //  run through vector, updating as needed
                for (int j = 0; j < myVector.size(); j++) {
                    //  retrieve data held in position j in temporary data
                    //      object
                    Employee_Messaging_List_Data tempEMLD = myVector.get(j);

                    //  search for matching data object within vector, if
                    //      nothing matches, loop falls through
                    if (tempEMLD.getCanMessage()) {
                        //  format name within data object to match List_View
                        //      for comparison
                        StringBuffer formattedName = new StringBuffer();
                        formattedName.append(tempEMLD.getLastName());
                        formattedName.append(", ");
                        formattedName.append(tempEMLD.getFirstName());
                        formattedName.append(" ");
                        formattedName.append(tempEMLD.getMiddleInitial());

                        //  compare formattedName with List_View name for
                        //      possible alteration
                        if (formattedName.toString().matches(
                                tempName.toString())) {
                            //  update vector
                            if (tempCanEmail.toString().matches("true")) {
                                myVector.get(j).setSendEmail(true);
                            } else {
                                myVector.get(j).setSendEmail(false);
                            }
                            if (tempCanSms.toString().matches("true")) {
                                myVector.get(j).setSendSms(true);
                            } else {
                                myVector.get(j).setSendSms(false);
                            }

                        }
                    }
                }
            }
        }

        /** Method Name:  resetListView
         *  Purpose of Method:  clears out the List_View
         *  Arguments:  none
         *  Returns:  void
         *  Precondition:  List_View altered, needs to be reset
         *  Postcondition:  List_View cleared
         */
        public void resetListView() {
            listView.clearRows();
        }

        /**
         *  Method Name:  changeSelected
         *  Purpose of Method:  changes the value of a boolean box on the
         *      screen
         *  Arguments:  a string containing a properly formatted name to
         *      compare against, an int describing whether it is email or sms
         *      column to be changed, and a boolean describing whether to
         *      select or unselect the box
         *  Returns:  void
         *  Preconditions:  a boolean box value has been requested to change
         *      by another piece of the program
         *  Postconditions:  box value changed
         */
        public void changeSelected(String nameToCompare, int tab,
            boolean isSelected)
        {
            //  iterate through list_view, looking for name
            for(int i = 0;i < listView.getRowCount();i ++)
            {
                Object tempName = listView.getValueAt(i, 2);
                if(nameToCompare.matches(tempName.toString()))
                {
                    Object setBox = isSelected;
                    //  switch statement for different tabs
                    switch(tab)
                    {
                        case 0:  listView.setValueAt(setBox, i, 1);
                                 break;
                        case 1:  listView.setValueAt(setBox, i, 0);
                                 break;
                    }
                }
            }

            //  tell program table data changed
            listView.fireTableDataChanged(false);
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

        listMainPanel = new javax.swing.JPanel();
        employeeMessagingHeaderPanel = new javax.swing.JPanel();
        listViewPanel = new javax.swing.JPanel();
        nonListMainPanel = new javax.swing.JPanel();
        subFormPanel = new javax.swing.JPanel();
        myToolBar = new javax.swing.JToolBar();
        myMenu = new javax.swing.JMenuBar();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setResizable(true);
        setPreferredSize(new java.awt.Dimension(226, 67));
        setVisible(true);

        listMainPanel.setMaximumSize(new java.awt.Dimension(32767, 32791));
        listMainPanel.setMinimumSize(new java.awt.Dimension(10, 24));
        listMainPanel.setPreferredSize(new java.awt.Dimension(225, 0));
        listMainPanel.setLayout(new javax.swing.BoxLayout(listMainPanel, javax.swing.BoxLayout.Y_AXIS));

        employeeMessagingHeaderPanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        employeeMessagingHeaderPanel.setMinimumSize(new java.awt.Dimension(15, 24));
        employeeMessagingHeaderPanel.setPreferredSize(new java.awt.Dimension(15, 24));
        employeeMessagingHeaderPanel.setLayout(new java.awt.GridLayout(1, 0));
        listMainPanel.add(employeeMessagingHeaderPanel);

        listViewPanel.setPreferredSize(new java.awt.Dimension(850, 443));
        listViewPanel.setLayout(new java.awt.GridLayout(1, 0));
        listMainPanel.add(listViewPanel);

        getContentPane().add(listMainPanel, java.awt.BorderLayout.WEST);

        nonListMainPanel.setMaximumSize(new java.awt.Dimension(32767, 32791));
        nonListMainPanel.setMinimumSize(new java.awt.Dimension(10, 34));
        nonListMainPanel.setPreferredSize(new java.awt.Dimension(10, 34));
        nonListMainPanel.setLayout(new javax.swing.BoxLayout(nonListMainPanel, javax.swing.BoxLayout.Y_AXIS));

        subFormPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        subFormPanel.setLayout(new java.awt.GridLayout(1, 0));
        nonListMainPanel.add(subFormPanel);

        myToolBar.setRollover(true);
        myToolBar.setMargin(new java.awt.Insets(0, 10, 0, 0));
        myToolBar.setMaximumSize(new java.awt.Dimension(1000, 34));
        myToolBar.setMinimumSize(new java.awt.Dimension(10, 34));
        myToolBar.setPreferredSize(new java.awt.Dimension(10, 34));
        nonListMainPanel.add(myToolBar);
        myToolBar.getAccessibleContext().setAccessibleParent(nonListMainPanel);

        getContentPane().add(nonListMainPanel, java.awt.BorderLayout.CENTER);
        setJMenuBar(myMenu);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-700)/2, (screenSize.height-400)/2, 700, 400);
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel employeeMessagingHeaderPanel;
    private javax.swing.JPanel listMainPanel;
    private javax.swing.JPanel listViewPanel;
    private javax.swing.JMenuBar myMenu;
    private javax.swing.JToolBar myToolBar;
    private javax.swing.JPanel nonListMainPanel;
    protected javax.swing.JPanel subFormPanel;
    // End of variables declaration//GEN-END:variables
};
