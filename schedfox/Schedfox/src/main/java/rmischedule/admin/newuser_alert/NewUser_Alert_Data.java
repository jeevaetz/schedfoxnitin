/**
 *  FileName:  NewUser_Alert_Data.java
 *  Created by:  Jeffrey N. Davis
 *  Date Created:  08/03/2010
 *  Purpose of File:  File contains a class designed to be a data component
 *      for NewUser_Alert
 *  Modification Information:
 */

//  package declaration
package rmischedule.admin.newuser_alert;

//  import declarations

/**
 *  Class Name:  NewUser_Alert_Data
 *  Purpose of Class:  a class designed to be a data component
 *      for NewUser_Alert
 */
public class NewUser_Alert_Data
{
    //  private variable declarations
    private int ssn;
    private StringBuffer firstName;
    private StringBuffer lastName;
    private StringBuffer company;
    private StringBuffer primaryEmail;
    private StringBuffer alternateEmail;
    private StringBuffer textNumber;
    private boolean isSendText;
    private boolean useAlternateEmail;
    private boolean useBothEmail;

    //  public method implementations
    /*
     *  Method Name:  NewUser_Alert_Data
     *  Purpose of Method:  creates a default instance of this object and
     *      initialized class variables
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, initial variables set
     */
    public NewUser_Alert_Data()
    {
        //  initialize class variables
        ssn = 0;
        firstName = new StringBuffer(0);
        lastName = new StringBuffer(0);
        company = new StringBuffer(0);
        primaryEmail = new StringBuffer(0);
        alternateEmail = new StringBuffer(0);
        textNumber = new StringBuffer(0);
        isSendText = false;
        useAlternateEmail = false;
        useBothEmail = false;
    }

    //  getters and setters
    public int getSSN()
    {
        return ssn;
    }

    public String getFirstName()
    {
        return firstName.toString();
    }

    public String getLastName()
    {
        return lastName.toString();
    }

    public String getCompany()
    {
        return company.toString();
    }

    public String getPrimaryEmail()
    {
        return primaryEmail.toString();
    }

    public String getAlternateEmail()
    {
        return alternateEmail.toString();
    }

    public String getTextNumber()
    {
        return textNumber.toString();
    }

    public boolean isIsSendText()
    {
        return isSendText;
    }

    public boolean isUseAlternateEmail()
    {
        return useAlternateEmail;
    }

    public boolean isUseBothEmail()
    {
        return useBothEmail;
    }

    public void setSSN(int tempSSN)
    {
        this.ssn = tempSSN;
    }

    public void setFirstName(String tempFirstName)
    {
        firstName.setLength(0);
        this.firstName.append(tempFirstName);
    }

    public void setLastName(String tempLastName)
    {
        this.lastName.setLength(0);
        this.lastName.append(tempLastName);
    }

    public void setCompany(String tempCompany)
    {
        this.company.setLength(0);
        this.company.append(tempCompany);
    }

    public void setPrimaryEmail(String tempPrimaryEmail)
    {
        this.primaryEmail.setLength(0);
        this.primaryEmail.append(tempPrimaryEmail);
    }

    public void setAlternateEmail(String tempAlternateEmail)
    {
        this.alternateEmail.setLength(0);
        this.alternateEmail.append(tempAlternateEmail);
    }

    public void setTextNumber(String tempTextNumber)
    {
        this.textNumber.setLength(0);
        this.textNumber.append(tempTextNumber);
    }

    public void setIsSendText(boolean tempIsSendText)
    {
        this.isSendText = tempIsSendText;
    }

    public void setUseAlternateEmail(boolean tempUseAlternateEmail)
    {
        this.useAlternateEmail = tempUseAlternateEmail;
    }

    public void setUseBothEmail(boolean tempUseBothEmail)
    {
        this.useBothEmail = tempUseBothEmail;
    }
};