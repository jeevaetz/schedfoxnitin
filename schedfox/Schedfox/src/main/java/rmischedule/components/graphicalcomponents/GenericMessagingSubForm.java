/**
 *  Name of File:  GenericMessagingSubForm.java
 *  Author: Jeffrey N. Davis
 *  Date Created:  05/24/2010
 *  Date Last Modified:  05/24/2010
 *  Purpose of File:  provides an abstract class with new control functionality
 *      for messaging subforms
 */

//  package declaration
package rmischedule.components.graphicalcomponents;

//  import declarations
import java.util.Vector;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import rmischedule.messaging.datacomponents.Employee_Messaging_List_Data;
import rmischedule.messaging.datacomponents.Messaging_Filters_Data_Champion;
import rmischedule.schedule.components.DShift;
import rmischedule.schedule.components.availability.Messaging_Availability;


/**
 *  Class Name:  GenericMessagingSubForm
 *  Purpose of Class:  An abstract class for future extension for subforms
 *      related to messaging
 */
public abstract class GenericMessagingSubForm extends JPanel
{
    //  variable declarations
    protected GenericMessagingForm myParent;

   //  private method implementations

    //  protected method implementations
    /**
     *  Method Name:  selectAll
     *  Purpose of Method:  calls the GenericMessagingForm select method so
     *      that email/sms can be selected/unselected
     *  Arguments:  an int describing which tab (text/email etc) is active,
     *      and a boolean describing if the boxes should be selected or 
     *      unselected
     *  Returns:  void
     *  Preconditions:  Select All box either checked or unchecked, no actions
     *      have been performed
     *  Postconditions:  myParent called to perform the action
     *  TAB:  0 for SMS, 1 for Email
     */
    protected void selectAll(int tab, boolean isSelectAll)
    {
        //myParent.selectAll(tab, isSelectAll);
    }

    /**
     *  Method Name:  selectAvailable
     *  Purpose of Method:  calls the GenericMessagingForm select method so
     *      that email/sms can be selected/unselected
     *  Arguments:  an int describing which tab (text/email etc) is active,
     *      and a boolean describing if the boxes should be selected or
     *      unselected, the DShift for selecte shift
     *  Returns:  void
     *  Preconditions:  Select Available box either checked or unchecked,
     *      no actions have been performed
     *  Postconditions:  myParent called to perform the action
     *  TAB:  0 for SMS, 1 for Email
     */
    protected void selectAvailable(int tab, boolean isSelectAvailable,
        DShift myShift, String company, String branch,
        Messaging_Availability temp)
    {
        myParent.selectAvailable(tab, isSelectAvailable, myShift, company,
            branch, temp);
    }

    /**
     *  Method Name:  selectOvertime
     *  Purpose of Method:  calls the GenericMessagingForm select method so
     *      that email/sms can be selected/unselected
     *  Arguments:  an int describing which tab (text/email etc) is active,
     *      and a boolean describing if the boxes should be selected or
     *      unselected
     *  Returns:  void
     *  Preconditions:  Select Overtime box either checked or unchecked,
     *      no actions have been performed
     *  Postconditions:  myParent called to perform the action
     *  TAB:  0 for SMS, 1 for Email
     */
    protected void selectOvertime(int tabl, boolean isSelectOvertime,
        Messaging_Availability myAvailability)
    {
        myParent.selectOvertime(tabl, isSelectOvertime, myAvailability);
    }

    

    /**
     *  Method Name:  smsSearch
     *  Purpose of Method:  calls myParent smsSearch, passes along parameters
     *  Arguments:  an instance of Messaging_Availability, an instance of
     *      Messaging_Filters_Data, and a boolean describing whether to
     *      "check" the box
     *  Returns:  void
     *  Preconditions:  user has selected all filters for search, search not
     *      performed
     *  Postconditions:  paramaters passed to parent to perform search
     */
    protected void smsSearch(Messaging_Availability myAvailability,
        Messaging_Filters_Data_Champion filterData, boolean isSelected)
    {
        //myParent.smsSearch(myAvailability, filterData, isSelected);
    }

    /**
     *  Method Name:  emailSearch
     *  Purpose of Method:  calls myParent emailSearch, passes along parameters
     *  Arguments:  an instance of Messaging_Availability, an instance of
     *      Messaging_Filters_Data, and a boolean describing whether to
     *      "check" the box
     *  Returns:  void
     *  Preconditions:  user has selected all filters for search, search not
     *      performed
     *  Postconditions:  paramaters passed to parent to perform search
     */
    protected void emailSearch(Messaging_Availability myAvailability,
        Messaging_Filters_Data_Champion filterData, boolean isSelected)
    {
        //myParent.emailSearch(myAvailability, filterData, isSelected);
    }

    //  public method implementations
    /**
     *  Method Name:  GenericMessagingSubForm
     *  Purpose of Method:  constructor for GenericMessagingSubForm class.
     *      Creates a new instance of this class
     *  Arguments:  none
     *  Returns:  none
     *  Precondition:  object not created
     *  Postcondition:  object created
     */
    public GenericMessagingSubForm()
    {
        
    }
    
    /**
     *  Method name:  setMyParent
     *  Purpose of Method:  sets the parent of this subForm to the parent form
     *  Arguments:  an instance of GenericMessagingForm
     *  Return:  void
     *  Precondition:  parent not known internally
     *  Postcondition:  parent set
     */
    public void setMyParent(GenericMessagingForm parent)
    {
        myParent = parent;
    }

    //  abstract method declarations
    public abstract String getMyTabTitle();
    public abstract JPanel getMyForm();
    public abstract void doOnClear();
    public abstract boolean userHasAccess();
    public abstract void getSelected(Vector<Employee_Messaging_List_Data>
        tempVector);
    public abstract void doOnReset(boolean isClosing);
    public abstract String doOnSend();
    public abstract String getSubject();
};