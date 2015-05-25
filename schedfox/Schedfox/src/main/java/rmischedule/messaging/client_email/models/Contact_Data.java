//  package declaration
package rmischedule.messaging.client_email.models;

//  import declarations

/**
 *  A simple data object designed to hold valid email addresses, and
 *      relevant contact information about them.
 *  <p><b>NOTE: </b> this object uses a {@code static Builder} object for construction
 *      due to the number of parameters.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.messaging.client_email.controllers.Client_Email_Controller
 *  @since 03/25/2011
 */
public final class Contact_Data implements Comparable<Contact_Data>
{
    /* object variable definitions  */
    private final String emailAddress;
    private final String branchName;
    private final String clientName;
    private final String activeClient;
    private final String contactPrimary;
    private final String clientPhone;
    private final String contactPhone;
    private final String contactTitle;
    private final String contactName;

    /*  object construction code    */
    /**
     *  This static object is used to create an instance of {@code Invalid_EmailData}.
     *      It allows for parameter telescoping.
     *  <p><b>NOTE:  </b> the only required field is {@code emailAddress}.  All additional
     *      fields are useful; however if they aren't assigned they default to "UNKNOWN".
     */
    public static class Builder
    {
        /*  variable declarations   */
        /*  required parameters  */
        private final String emailAddress;
        /*  optional parameters */
        private String branchName = "UNKNOWN";
        private String clientName = "UNKNOWN";
        private String activeClient = "UNKNOWN";
        private String contactPrimary = "UNKNOWN";
        private String clientPhone = "UNKNOWN";
        private String contactPhone = "UNKNOWN";
        private String contactTitle = "UNKNOWN";
        private String contactName = "UNKNOWN";

        /**
          *  Default instantiation of this object is not allowed.
          *  @throws UnsupportedOperationException
         */
        private Builder ()
        {
            throw new UnsupportedOperationException( "Default instantiation of Client_Email_Data is not allowed." );
        }

        /**
         *  Constructs an instance of this object
         *  @param emailAddress a string representing the invalid email Address
         */
        public Builder ( String emailAddress )
            { this.emailAddress = emailAddress; }

        public Builder branchName ( String val )
            { this.branchName = val; return this; }
        public Builder clientName ( String val )
            { this.clientName = val; return this; }
        public Builder activeClient ( String val )
            { this.activeClient = val; return this; }
        public Builder contactPrimary ( String val )
            { this.contactPrimary = val; return this; }
        public Builder clientPhone ( String val )
            { this.clientPhone = val; return this; }
        public Builder contactPhone ( String val )
            { this.contactPhone = val; return this; }
        public Builder contactTitle ( String val )
            { this.contactTitle = val; return this; }
        public Builder contactName ( String val )
            { this.contactName = val; return this; }

        public Contact_Data build()
            { return new Contact_Data ( this ); }
    }

    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private Contact_Data()
    {
        throw new UnsupportedOperationException( "Default instantiation of Client_Email_Data is not allowed." );
    }

    /**
     *  Initializes this object by assigning member variables to corresponding
     *      {@code Builder} variables.
     *  @param builder an instance of {@code Builder}
     */
    private Contact_Data ( Builder builder )
    {
        /*  set object vaiable  */
        this.emailAddress = builder.emailAddress;
        this.branchName = builder.branchName;
        this.clientName = builder.clientName;
        this.activeClient = builder.activeClient;
        this.contactPrimary = builder.contactPrimary;
        this.clientPhone = builder.clientPhone;
        this.contactPhone = builder.contactPhone;
        this.contactTitle = builder.contactTitle;
        this.contactName = builder.contactName;
    }

    /*  public method implementations   */
    /*  accessors   */
    public String getActiveClient() { return activeClient; }
    public String getBranchName() { return branchName; }
    public String getClientName() { return clientName; }
    public String getClientPhone() { return clientPhone; }
    public String getContactName() { return contactName; }
    public String getContactPhone() { return contactPhone; }
    public String getContactPrimary() { return contactPrimary; }
    public String getContactTitle() { return contactTitle; }
    public String getEmailAddress() { return emailAddress; }

    /*  methods common to all objects   */
    @Override
    public String toString()
    {
        return "Branch:  " + this.branchName + "  Client:  " + this.clientName +
            "  Email Address:  " + emailAddress + "  Contact Name:  " + this.contactName;
    }

    @Override
    public int compareTo ( Contact_Data other )
    {
        //  check branch
        int branch = String.CASE_INSENSITIVE_ORDER.compare( this.branchName, other.branchName );
        if ( branch != 0 )
            return branch;

        //  check client
        int client = String.CASE_INSENSITIVE_ORDER.compare( this.clientName, other.clientName );
        if ( client != 0 )
            return client;

        //  check name
        int name = String.CASE_INSENSITIVE_ORDER.compare( this.contactName, other.contactName );
        if ( name != 0 )
            return name;

        //  check email address
        return String.CASE_INSENSITIVE_ORDER.compare( this.emailAddress, other.emailAddress );
    }

    @Override
    public boolean equals ( Object obj )
    {
        /*
         *  Method checks emailAddres -> contactName -> clientName -> contactTitle
         *      -> contactPhone -> clientPhone -> activeClient -> contactPrimary ->
         *      branchName
         */
        if ( !( obj instanceof Contact_Data ) )
            return false;

        final Contact_Data other = ( Contact_Data ) obj;

        return (
                   ( this.emailAddress == null ? other.emailAddress == null : this.emailAddress.equals( other.emailAddress ) ) //  test emailAddress
                && ( this.contactName == null ? other.contactName == null : this.contactName.equals( other.contactName ) ) //  test contactName
                && ( this.clientName == null ? other.clientName == null : this.clientName.equals( other.clientName ) ) //  test clientName
                && ( this.contactTitle == null ? other.contactTitle == null : this.contactTitle.equals( other.contactTitle ) ) //  test contactTitle
                && ( this.contactPhone == null ? other.contactPhone == null : this.contactPhone.equals( other.contactPhone ) ) //  test contactPhone
                && ( this.clientPhone == null ? other.clientPhone == null : this.clientPhone.equals( other.clientPhone ) ) //  test clientPhone
                && ( this.activeClient == null ? other.activeClient == null : this.activeClient.equals( other.activeClient ) ) //  test activeClient
                && ( this.contactPrimary == null ? other.contactPrimary == null : this.contactPrimary.equals( other.contactPrimary ) ) //  test contactPrimary
                && ( this.branchName == null ? other.branchName == null : this.branchName.equals( other.branchName ) ) //  test branchName
                );
    }

    @Override
    public int hashCode()
    {
        int hash = 71;

        hash = 71 * hash + ( this.emailAddress != null ? this.emailAddress.hashCode() : 0 );
        hash = 71 * hash + ( this.contactName != null ? this.contactName.hashCode() : 0 );
        hash = 71 * hash + ( this.clientName != null ? this.clientName.hashCode() : 0 );
        hash = 71 * hash + ( this.clientName != null ? this.clientName.hashCode() : 0 );
        hash = 71 * hash + ( this.contactTitle != null ? this.contactTitle.hashCode() : 0 );
        hash = 71 * hash + ( this.contactPhone != null ? this.contactPhone.hashCode() : 0 );
        hash = 71 * hash + ( this.clientPhone != null ? this.clientPhone.hashCode() : 0 );
        hash = 71 * hash + ( this.activeClient != null ? this.activeClient.hashCode() : 0 );
        hash = 71 * hash + ( this.contactPrimary != null ? this.contactPrimary.hashCode() : 0 );
        hash = 71 * hash + ( this.branchName != null ? this.branchName.hashCode() : 0 );

        return hash;
    }
};
