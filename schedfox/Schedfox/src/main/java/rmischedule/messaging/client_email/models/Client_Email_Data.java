//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations

/**
 *  This method is a simple data object to hold and individual "email" to be
 *      sent by {@code Client_Email_Controller}.
 *  <p><b>NOTE:  </b> validity of this email object is checked by {@code Client_Email_Controller}.
 *      This object will not be constructed unless all variables are in a valid
 *      state.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @since 03/25/2011
 */
public final class Client_Email_Data implements Comparable<Client_Email_Data>
{
    /*  class variable declarations */
    private String emailSubject;
    private String emailBody;
    private String emailAddress;
    private String clientName;
    private String contactName;
    private Boolean isSelected;

    /*  object construction code    */
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private Client_Email_Data()
    {
        throw new UnsupportedOperationException( "Default instantiation of Client_Email_Data is not allowed." );
    }

    /**
     *  Constructs this object in a valid state.
     *  @param emailSubject a string representing the subject of the email
     *  @param emailBody a string representing the body of the email
     *  @param emailAddress a string representing the email address to send to
     *  @param clientName a string representing the client name
     *  @param contactName a string representing the contact name
     */
    public Client_Email_Data ( String emailSubject, String emailBody, String emailAddress,
            String clientName, String contactName )
    {
        this.emailSubject = emailSubject;
        this.emailBody = emailBody;
        this.emailAddress = emailAddress;
        this.clientName = clientName;
        this.contactName = contactName;
        
        isSelected = true;
    }

    /*  public method implementations   */
    /*  accessors    */
    public String getEmailAddress() { return emailAddress; }
    public String getEmailBody() { return emailBody; }
    public String getEmailSubject() { return emailSubject; }
    public String getClientName() {  return clientName;  }
    public String getContactName() {  return contactName;  }
        
    /*  methods common to all objects   */
    @Override
    public String toString()
        { return "Email Addres:  " + this.emailAddress + "  Email Subject:  " + this.emailSubject; }

    @Override
    public boolean equals ( Object obj )
    {
        /*
         *  Method checks emailAddress -> emailSubject -> emailBody
         */
        if ( !( obj instanceof Client_Email_Data ) )
            return false;

        final Client_Email_Data other = ( Client_Email_Data ) obj;

        return ( this.emailAddress == null ? other.emailAddress == null : this.emailAddress.equals( other.emailAddress ) ) // test emailAddress
                && ( this.emailSubject == null ? other.emailSubject == null : this.emailSubject.equals( other.emailSubject) ) // test emailSubject
                && ( this.emailBody == null ? other.emailBody == null : this.emailBody.equals( other.emailBody ));   //  test embailBody
    }

    @Override
    public int hashCode()
    {
        int hash = 71;

        hash = 71 * hash + ( this.emailAddress != null ? this.emailAddress.hashCode() : 0 );
        hash = 71 * hash + ( this.emailSubject != null ? this.emailSubject.hashCode() : 0 );
        hash = 71 * hash + ( this.emailBody != null ? this.emailBody.hashCode() : 0 );

        return hash;
    }

    @Override
    public int compareTo ( Client_Email_Data other )
    {
        return String.CASE_INSENSITIVE_ORDER.compare( this.emailAddress, other.emailAddress );
    }

    /**
     * @return the isSelected
     */
    public Boolean getIsSelected() {
        return isSelected;
    }

    /**
     * @param isSelected the isSelected to set
     */
    public void setIsSelected(Boolean isSelected) {
        this.isSelected = isSelected;
    }
};
