
/**
 *  Filename:  Employee_Messaging_List_Data.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  05/27/2010
 *  Modified On/By:  06/25/2010, Jeffrey Davis
 *      Reason for Modifications:  added isOvertimeHit, isAvailable for     
 *          selection purposes
 *  Purpose of File:  contains a class which is a data component for the
 *      employee_messaging_list
 */

//  package declaration
package rmischedule.messaging.datacomponents;

//  import declarations

import org.apache.commons.validator.EmailValidator;


/**
 *  Class Name:  Employee_Messaging_List_Data
 *  Purpose of Class:  a data component for the employee_messaging_list
 */
public class Employee_Messaging_List_Data implements Comparable
{
    //  variable declarations
    private StringBuffer employeeId;
    private StringBuffer branchId;
    private StringBuffer firstName;
    private StringBuffer lastName;
    private StringBuffer middleInitial;
    private StringBuffer employeeCell;
    private StringBuffer emailPrimary;
    private StringBuffer emailAlternate;
    private StringBuffer emailMessagingType;
    private boolean canEmailMessage;
    private boolean canSmsMessage;
    private boolean canMessage;
    private boolean sendEmail;
    private boolean sendSms;
    private boolean isAvailable;
    private boolean isOvertimeHit;
    private boolean isArmed;
    private boolean isTrained;
    private boolean isNewEmployee;

    /**
     *  Declarations of Vu's data pieces
     */
    private StringBuffer msg;
    private String companyId;
    private int messageType;
    private int user_sent_id;
    private int messaging_mod_id;
    private String emailSubject;
    /**
     *  addition of Vu's data pieces complete
     */

    //  private method implemenations
    /**
     *  Method Name:  formatCellNumber
     *  Purpose of Method:  ensures that the cell phone pulled from the DB is
     *      in the proper format with no hyphens
     *  Arguments:  the cell pulled from the DB
     *  Returns:  the formatted cell phone number
     *  Preconditions:  the cell phone number may or may not be formatted
     *      correctly
     *  PostConditions:  cell phone number properly formatted, returned
     */
    private String formatCellNumber(String cellNumber)
    {
        //  declare stringbuffer to alter, return stringbuffer
        StringBuffer cellToManipulate = new StringBuffer();
        StringBuffer returnString = new StringBuffer();

        //  search through for hypens
        cellToManipulate.append(cellNumber);
        cellToManipulate.trimToSize();
        for(int i = 0;i < cellToManipulate.length();i ++)
        {
            char stringChar = cellToManipulate.charAt(i);
            if(Character.isDigit(stringChar))
                returnString.append(stringChar);
        }

        //  return the formatted cell number
        return returnString.toString();
    }

    //  public method implementations
    /**
     *  Method Name:  Employee_Messaging_List_Data
     *  Purpose of Method:  creates a default instance of this class and
     *      initializes variables
     *  Arguments:  none
     *  Returns:  none
     *  Precondtion:  object DNE
     *  Postcondition:  object exists, variables initialized
     */
    public Employee_Messaging_List_Data()
    {
        //  initialize class variables
        employeeId = new StringBuffer();
        branchId = new StringBuffer();
        firstName = new StringBuffer();
        lastName = new StringBuffer();
        middleInitial = new StringBuffer();
        employeeCell = new StringBuffer();
        emailPrimary = new StringBuffer();
        emailAlternate = new StringBuffer();
        emailMessagingType = new StringBuffer();

        /**
         *  Vu's data components set to values discussed
         */
        msg = new StringBuffer();
        messageType = 0;
        companyId = null;
        user_sent_id = 0;
        messaging_mod_id = 0;
        /**
         *  Vu's addition complete
         */

        canEmailMessage = false;
        canSmsMessage = false;
        canMessage = false;
        sendEmail = false;
        sendSms = false;
        isAvailable = true;
        isOvertimeHit = false;
        isArmed = false;
        isTrained = false;
        isNewEmployee = false;
    }

   /**
     *  Method Name:  Employee_Messaging_List_Data
     *  Purpose of Method:  creates an instance of this class and sets the class
     *      variables equal to the arguments
     *  Arguments:
     *  Returns:  none
     *  Precondtion:  object DNE
     *  Postcondition:  object exists, variables set to arguments
     */
    public Employee_Messaging_List_Data(String tempEmpId, String tempBranchId,
        String tempFirstName, String tempLastname, String tempMiddleInitial,
        String tempEmpCell, String tempEmailPrimary, String tempEmailAlternate,
        String tempEmailMessaging, boolean tempSmsMessaging,
        int tempMessageType, String tempCompanyId, int tempUserSentId,
        int tempMessagingModId)
    {
        //  initialize variables
        this.employeeId = new StringBuffer();
        this.branchId = new StringBuffer();
        this.firstName = new StringBuffer();
        this.lastName = new StringBuffer();
        this.middleInitial = new StringBuffer();
        this.employeeCell = new StringBuffer();
        this.emailPrimary = new StringBuffer();
        this.emailAlternate = new StringBuffer();
        this.emailMessagingType = new StringBuffer();

        //  set class variables to arguments
        this.employeeId.append(tempEmpId);
        this.branchId.append(tempBranchId);
        this.firstName.append(tempFirstName);
        this.lastName.append(tempLastname);
        this.middleInitial.append(tempMiddleInitial);
        this.employeeCell.append(formatCellNumber(tempEmpCell));
        this.emailPrimary.append(tempEmailPrimary);
        this.emailAlternate.append(tempEmailAlternate);
        this.emailMessagingType.append(tempEmailMessaging);
        this.canSmsMessage = tempSmsMessaging;
        this.sendEmail = false;
        this.sendSms = false;
        this.isAvailable = true;
        this.isOvertimeHit = false;
        this.isArmed = false;
        this.isTrained = false;
        this.isNewEmployee = false;

         /**
         *  Vu's data components set to values discussed
         */
        this.msg = new StringBuffer();
        this.messageType = tempMessageType;
        this.companyId = tempCompanyId;
        this.user_sent_id = tempUserSentId;
        this.messaging_mod_id = tempMessagingModId;
        /**
         *  Vu's addition complete
         */

        //  determine if this employee can be messaged
        determineCanEmailMessage();
        determineCanMessage();
    }

    /**
     *  Method Name:  determineCanMessage
     *  Purpose of Method:  determines whether this employee can be messaged,
     *      then sets canMessage appropriately
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  information known as to whether employee can be
     *      messaged, not calculated yet
     *  Postconditions:  calculations performed, canMessage set
     */
    public void determineCanMessage()
    {

        if((canEmailMessage == true) || (canSmsMessage == true))
            this.canMessage = true;
        else
            this.canMessage = false;
    }

    /**
     *  Method Name:  determineCanEmailMessage
     *  Purpose of Method:  determines whether this employee can be messaged
     *      via email, then sets canEmailMessage appropriately
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  information known as to whether employee can be
     *      email messaged, not calculated yet
     *  Postconditions:  calculations performed, canEmailMessage set
     */
    public void determineCanEmailMessage()
    {
        //  get instance of email validator, declare flags to run
        EmailValidator myValidator = EmailValidator.getInstance();
        boolean primaryFlag = false;
        boolean alternateFlag = false;
        
        //  check if primary email is valid
        if(this.emailPrimary.length() > 0)
        {
            if(!myValidator.isValid(emailPrimary.toString()))
                emailPrimary.setLength(0);
            else
                primaryFlag = true;
        }
        
        //  check if alternate email is valid
        if(this.emailAlternate.length() > 0)
        {
            if(!myValidator.isValid(emailAlternate.toString()))
                emailAlternate.setLength(0);
            else
                alternateFlag = true;
        }

        //  determine canEmail
        if(!primaryFlag && !alternateFlag)
            this.canEmailMessage = false;
        else
            this.canEmailMessage = true;
    }

    /**
     *  Method Name:  determineCanSmsMessage
     *  Purpose of Method:  checks the phone number associated with with the
     *      employee to ensure it is in valid format; will override a "true"
     *      in DB if number is not in proper format
     *  Arguments:  none
     *  Returns:  void
     *  Preconditions:  class knows cell number, unknown if valid
     *  Postconditions:  number checked, canSms set
     */
    public void determineCanSmsMessage()
    {
        if(employeeCell.length() == 10)
            this.canSmsMessage = true;
        else
            this.canSmsMessage = false;
    }

    public int compareTo(Object o)
    {
        //  declare value to return
        String thisString = toString();
        int returnValue = thisString.compareToIgnoreCase(o.toString());

        //  return comparison
        return returnValue;
    }

    public String toString()
    {
        //  declare stringbuffer to manipulate and return
        StringBuffer returnString = new StringBuffer();

        //  build string
        returnString.append(this.lastName.toString());
        returnString.append(", ");
        returnString.append(this.firstName.toString());
        returnString.append(" ");
        returnString.append(this.middleInitial.toString());

        //  return string
        return returnString.toString();
    }

    //  getter methods
    public String getEmployeeId()
    {
        return employeeId.toString();
    }

    public String getBranchId()
    {
        return branchId.toString();
    }

    public String getFirstName()
    {
        return firstName.toString();
    }

    public String getLastName()
    {
        return lastName.toString();
    }

    public String getMiddleInitial()
    {
        return middleInitial.toString();
    }

    public String getEmployeeCell()
    {
        return employeeCell.toString();
    }

    public String getEmailPrimary()
    {
        return emailPrimary.toString();
    }

    public String getEmailAlternate()
    {
        return emailAlternate.toString();
    }

    public String getEmailMessagingType()
    {
        return emailMessagingType.toString();
    }

    public boolean getCanEmailMessage()
    {
        return this.canEmailMessage;
    }

    public boolean getCanSmsMessage()
    {
        return canSmsMessage;
    }

    public boolean getCanMessage()
    {
        return this.canMessage;
    }

    public boolean getSendEmail()
    {
        return this.sendEmail;
    }

    public boolean getSendSms()
    {
        return this.sendSms;
    }

    public boolean getIsAvailable()
    {
        return this.isAvailable;
    }

    public boolean getIsOvertimeHit()
    {
        return this.isOvertimeHit;
    }

    public boolean getIsArmed()
    {
        return this.isArmed;
    }

    public boolean getIsTrained()
    {
        return this.isTrained;
    }

    public boolean getIsNewEmployee()
    {
        return this.isNewEmployee;
    }

    /**
     *  getters for Vu's additional data pieces
     */
    public String getMessage()
    {
        this.msg.trimToSize();
        return msg.toString();
    }

    public int getMessageType()
    {
        return this.messageType;
    }

    public String getCompanyId()
    {
        return this.companyId;
    }

    public int getUserSentId()
    {
        return this.user_sent_id;
    }

    public int getMessagingModId()
    {
        return this.messaging_mod_id;
    }
    /**
     *  Vu's getter methods complete
     */

    //  setter methods
     public void setEmployeeId(String tempEmployeeId)
    {
        this.employeeId.append(tempEmployeeId);
    }

    public void setBranchId(String tempBranchId)
    {
        this.branchId.append(tempBranchId);
    }

    public void setFirstName(String tempFirstName)
    {
        this.firstName.append(tempFirstName);
    }

    public void setLastName(String tempLastName)
    {
        this.lastName.append(tempLastName);
    }

    public void setMiddleInitial(String tempMiddleInitial)
    {
        this.middleInitial.append(tempMiddleInitial);
    }

    public void setEmployeeCell(String tempEmployeeCell)
    {
        this.employeeCell.append(formatCellNumber(tempEmployeeCell));
    }

    public void setEmailPrimary(String tempEmailPrimary)
    {
       this.emailPrimary.append(tempEmailPrimary);
    }

    public void setEmailAlternate(String tempEmailAlternate)
    {
       this.emailAlternate.append(tempEmailAlternate);
    }

    public void setEmailMessagingType(String tempEmailMessagingType)
    {
       this.emailMessagingType.append(tempEmailMessagingType);
    }

    public void setCanSmsMessage(boolean tempCanSmsMessage)
    {
       this.canSmsMessage = tempCanSmsMessage;
    }

    public void setSendEmail(boolean tempSendEmail)
    {
        this.sendEmail = tempSendEmail;
    }

    public void setSendSms(boolean tempSendSms)
    {
        this.sendSms = tempSendSms;
    }

    public void setIsAvailable(boolean tempIsAvailable)
    {
        this.isAvailable = tempIsAvailable;
    }

    public void setIsOvertimeHit(boolean tempIsOvertimeHit)
    {
        this.isOvertimeHit = tempIsOvertimeHit;
    }

    public void setIsArmed(boolean tempIsArmed)
    {
        this.isArmed = tempIsArmed;
    }

    public void setIsTrained(boolean tempIsTrained)
    {
        this.isTrained = tempIsTrained;
    }

    public void setIsNewEmployee(boolean tempIsNewEmployee)
    {
        this.isNewEmployee = tempIsNewEmployee;
    }

    /**
     *  Vu's setter methods
     */
    public void setMessage(String tempMessage)
    {
        this.msg.setLength(0);
        this.msg.append(tempMessage);
    }

    public void setMessageType(int tempMessageType)
    {
        this.messageType = tempMessageType;
    }

    public void setCompanyId(String tempCompanyId)
    {
        this.companyId = null;
        this.companyId = tempCompanyId;
    }

    public void setUserSentId(int tempUserSentId)
    {
        this.user_sent_id = tempUserSentId;
    }

    public void setMessagingModId(int tempMessagingModId)
    {
        this.messaging_mod_id = tempMessagingModId;
    }

    /**
     * @return the emailSubject
     */
    public String getEmailSubject() {
        return emailSubject;
    }

    /**
     * @param emailSubject the emailSubject to set
     */
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }
    /**
     *  Vu's getter methods complete
     */
};