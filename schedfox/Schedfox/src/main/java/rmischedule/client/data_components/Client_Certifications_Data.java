/**
 *  Filename:  Client_Certifications_Data.java
 *  Author:  Jeffrey N. Davis
 *  Date Created:  06/06/2010
 *  Date Last Modified:  06/07/2010
 *  Last Modified by:  Jeffrey N. Davis
 *  Purpose of File:  contains the data components needed for the
 *      Client_Certifications class
*/

//  package declaration
package rmischedule.client.data_components;

//  import declarations

/**
 *  Class Name:  client_Certifications_Data
 *  Purpose of Class:  contains the simple data components required for the
 *      Client_Certifications class
 */
public class Client_Certifications_Data
{
    //  private variable declarations
    boolean isRequired;
    StringBuffer certificationId;
    StringBuffer certificationName;
    StringBuffer description;
    
    //  public method implementations
    /**
     *  Method Name:  Client_Certifications_Data
     *  Purpose of Method:  creates a default instance of this class
     *  Arguments:  none
     *  Returns:  none
     *  Preconditions:  object DNE
     *  Postconditions:  object exists, class variables initialized
     */
    public Client_Certifications_Data()
    {
        //  initialize variables
        isRequired = false;
        certificationId = new StringBuffer();
        certificationName = new StringBuffer();
        description = new StringBuffer();
    }
    
    //  getter methods
    public boolean getIsRequried()
    {
        return this.isRequired;
    }

    public String getCertificationName()
    {
        return this.certificationName.toString();
    }

    public String getDescription()
    {
        return this.description.toString();
    }

    public String getCertificationId()
    {
        return this.certificationId.toString();
    }

    //  setter methods
    public void setIsRequired(boolean tempIsRequired)
    {
        this.isRequired = tempIsRequired;
    }

    public void setCertificationName(String tempCertificationName)
    {
        this.certificationName.append(tempCertificationName);
    }

    public void setDescription(String tempDescription)
    {
        this.description.append(tempDescription);
    }

    public void setCertificationId(String tempCertificationId)
    {
        this.certificationId.append(tempCertificationId);
    }
};