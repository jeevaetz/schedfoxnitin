//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations

/**
 *  This object is designed to hold specific contact information about a client contact.
 *      This object is held in a data structure by {@code ClientOfBranchContactData}.
 *  <p><b>NOTE: </b> this object uses a {@code static Builder} object for construction
 *      due to the number of parameters.  Proper construction is checked;
 *      if object is not valid, a {@code RuntimeOperationException} is thrown
 *      detailing the issue.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.models.ClientContactData
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @since 03/24/2011
 */
public final class ClientContactData implements Comparable<ClientContactData>
{
    /*  private variable declarations   */
    private final int clientContactId;
    private final int clientId;
    private final boolean isContactPrimary;
    private final String clientContactTitle;
    private final String contactFirstName;
    private final String contactLastName;
    private final String contactPhone;
    private final String contactCell;
    private final String contactEmailAddress;

    /*  object construction code    */
    /**
     *  This static object is used to create an instance of {@code ClientContactData}.
     *      It allows for parameter telescoping.
     *  <p><b>NOTE:  </b> both field {@code clientId } have been set to "required"
     *      as there isn't a valid baseline to check for validity.  All other "optional"
     *      <i>(these fields are <b>NOT</b> optional, they are simply telescoped)</i>
     *      will be checked for validity by {@code ClientContactData}.
    */
    public static class Builder
    {
        /*  private variable declarations   */
        /*  required parameters */
        private final int clientContactId;
        private final boolean isContactPrimary;
        /*  technically optional parameters, though this will be checked for validity   */
        private int clientId = 0;
        private String clientContactTitle = "";
        private String contactFirstName = "";
        private String contactLastName = "";
        private String contactPhone = "";
        private String contactCell = "";
        private String contactEmailAddress = "";

        /**
         * Default construction of this object is not allowed.
         * @throws UnsupportedOperationException
        */
        private Builder()
            {  throw new UnsupportedOperationException( "Default construction of this object is not allowed." ); }

       /**
        *   Constructs an instance of this object
        *   @param clientContactId an int describing the clientContactId
        *   @param isContactPrimary a boolean describing if the contact is the primary contact
        */
        public Builder ( int clientContactId, boolean isContactPrimary )
            { this.clientContactId = clientContactId; this.isContactPrimary = isContactPrimary; }

        public Builder clientId ( int val )
            { this.clientId = val; return this; }
        public Builder clientContactTitle ( String val )
            { this.clientContactTitle = val; return this; }
        public Builder contactFirstName ( String val )
            { this.contactFirstName = val;  return this; }
        public Builder contactLastName ( String val )
            { this.contactLastName = val;  return this; }
        public Builder contactPhone ( String val )
            { this.contactPhone = val;  return this; }
        public Builder contactCell ( String val )
            { this.contactCell = val;  return this; }
        public Builder contactEmailAddress ( String val )
            { this.contactEmailAddress = val;  return this; }

        public ClientContactData build()
            { return new ClientContactData ( this ); }
    }

    /**
     *  Default construction of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private ClientContactData()
        { throw new UnsupportedOperationException( "Default construction of this object is not allowed." ); }

    /**
     *  Initializes this object by assigning member variables to corresponding
     *      {@code Builder} variables.  After assignment, each variable is checked
     *      for validity
     *  @param builder an instance of {@code Builder}
     */
    private ClientContactData ( Builder builder )
    {
        /*  set object variables    */
        this.clientContactId = builder.clientContactId;
        this.clientId = builder.clientId;
        this.isContactPrimary = builder.isContactPrimary;
        this.clientContactTitle = builder.clientContactTitle;
        this.contactFirstName = builder.contactFirstName;
        this.contactLastName = builder.contactLastName;
        this.contactPhone = builder.contactPhone;
        this.contactCell = builder.contactCell;
        this.contactEmailAddress = builder.contactEmailAddress;
    }

    /*  public method implementation    */
    /*  mutators    */
    public int getClientContactId() {  return clientContactId; }
    public String getClientContactTitle() { return clientContactTitle; }
    public int getClientId() { return clientId; }
    public String getContactCell() { return contactCell; }
    public String getContactEmailAddress() { return contactEmailAddress; }
    public String getContactFirstName() { return contactFirstName; }
    public String getContactLastName() { return contactLastName; }
    public String getContactPhone() { return contactPhone; }
    public boolean isIsContactPrimary() { return isContactPrimary; }

    @Override
    public String toString()
        { return "Contact Name:  " + this.contactFirstName + " " + this.contactLastName + ".  Client Contact Id: " + this.clientContactId; }

    @Override
    public int compareTo ( ClientContactData t )
    {
        //  check last names
        int lastName = String.CASE_INSENSITIVE_ORDER.compare( this.contactLastName, t.contactLastName );
        if ( lastName != 0 )
            return lastName;

        //  check first names
        int firstName = String.CASE_INSENSITIVE_ORDER.compare( this.contactFirstName, t.contactFirstName );
        if ( firstName != 0 )
            return firstName;

        //  check clientContactId
        return this.clientContactId - t.clientContactId;
    }

    @Override
    public boolean equals ( Object obj )
    {
        /*
         *  Method checks clientContactId -> clientContactLastName -> clientContactFirstName
         */
        if ( !( obj instanceof ClientContactData ) )
            return false;

        final ClientContactData other = ( ClientContactData ) obj;

        return ( this.clientContactId == other.clientContactId   //  test clientContactId
                && ( this.contactLastName == null ? other.contactLastName == null : this.contactLastName.equals( other.contactLastName ) )  //  test client contact last name
                && ( this.contactFirstName == null ? other.contactFirstName == null : this.contactFirstName.equals( other.contactFirstName ) )  //  test client contact first name
                );
    }

    @Override
    public int hashCode()
    {
        int hash = 71;

        hash = 71 * hash + this.clientContactId;
        hash = 71 * hash + ( this.contactLastName != null ? this.contactLastName.hashCode() : 0 );
        hash = 71 * hash + ( this.contactFirstName != null ? this.contactFirstName.hashCode() : 0 );

        return hash;
    }
};
