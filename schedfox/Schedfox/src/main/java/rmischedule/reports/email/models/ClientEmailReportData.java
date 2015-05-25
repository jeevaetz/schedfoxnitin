/*  package declaration */
package rmischedule.reports.email.models;

/*  import declarations */

/**
 *  A data object designed to hold Email Report Data related to the Client.  It
 *      extends {@code AbstractEmailReportData} which contains the lions share
 *      of the data, which is similar to {@code EmployeeEmailReportData}
 *  <p><b>NOTE:  </b> this class uses a a static {@code Builder} object due
 *      to the number of parameters.  <b>ALL</b> parameters are required, none
 *      are optional.  Parameter validity is checked on instantiation, and
 *      a {@code RuntimeException} is thrown detailing the cause of the fault
 *      if a parameter is invalid.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.reports.email.controllers.EmailReportController
 *  @see rmischedule.reports.email.models.AbstractEmailReportData
 *  @since 04/22/2011
 */
public final class ClientEmailReportData extends AbstractEmailReportData implements Comparable<ClientEmailReportData>
{
    /*  object variable declarations    */
    private boolean isActive;
    private boolean isPrimary;
    private String clientName;
    private String contactTitle;

    /*  object instantiation code   */
    /**
     *  This static object is used to create an instance of {@code ClientEmailReportData}.
     *      It allows for parameter telescoping.
     *  <p><b>NOTE: </b> though parameters are telescoped for readability, <b>ALL</b>
     *      fields are required, and checked for validity.
     */
    public static class Builder
    {
        /*  variable declarations   */
        private String email;
        private String firstName;
        private String lastName;
        private String branch;
        private String city;
        private String state;
        private boolean isActive;
        private boolean isPrimary;
        private String clientName;
        private String contactTitle;
        
        /**
         *  Instantiates this object
         */
        public Builder()    {}
        
        public Builder email ( String val )
            {  this.email = val;  return this;  }
        public Builder firstName ( String val )
            {  this.firstName = val;  return this;  }
        public Builder lastName ( String val )
            {  this.lastName = val;  return this;  }
        public Builder branch ( String val )
            {  this.branch = val;  return this;  }
        public Builder city ( String val )
            {  this.city = val;  return this;  }
        public Builder state ( String val )
            {  this.state = val;  return this;  }
        public Builder isActive ( boolean val )
            {  this.isActive = val;  return this;  }
        public Builder isPrimary ( boolean val )
            {  this.isPrimary = val;  return this;  }
        public Builder clientName ( String val )
            {   this.clientName = val;  return this;  }
        public Builder contactTitle ( String val )
            {  this.contactTitle = val;  return this;  }
        
        public ClientEmailReportData build()
            {  return new ClientEmailReportData ( this );  }
    }
    
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private ClientEmailReportData()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object ClientEmailReportData is not allowed." );
    }
    
    /**
     *  Instantiates this object by assigning member variables to corresponding {@code Builder}
     *      variables. After assignment, each variable is checked for validity.
     *  @param builder an instance of {@code Builder}
     */
    private ClientEmailReportData ( Builder builder )
    {
        super.setEmail( builder.email );
        super.setFirstName( builder.firstName );
        super.setLastName( builder.lastName );
        super.setBranch( builder.branch );
        super.setCity( builder.city );
        super.setState( builder.state );
        this.isActive = builder.isActive;
        this.isPrimary = builder.isPrimary;
        this.clientName = builder.clientName;
        this.contactTitle = builder.contactTitle;
        
        try
        {
            this.areParamsValid();
        }
        catch ( RuntimeException ex )
        {
            throw new IllegalArgumentException ( "Object was not constructed in a valid state.  One or more params are null." );
        }
    }
    
    /**
     *  Checks to ensure each param is invalid.  
     *  @throws RuntimeException if any parameter is invalid
     */
    private void areParamsValid() throws RuntimeException
    {
        boolean isValid = true;
        
        if ( super.getEmail() == null )
            isValid = false;
        if ( super.getFirstName() == null )
            isValid = false;
        if ( super.getLastName() == null )
            isValid = false;
        if ( super.getBranch() == null )
            isValid = false;
        if ( super.getCity() == null )
            isValid = false;
        if ( super.getState() == null )
            isValid = false;
        if ( this.clientName == null )
            isValid = false;
        if ( this.contactTitle == null )
            isValid = false;
        
        if ( !isValid )
            throw new RuntimeException();
    }
    
    @Override
    public String getReportLine() 
    {
        StringBuilder reportLine = new StringBuilder();
        
        StringBuilder clientNameSb = new StringBuilder();
        clientNameSb.append( "Client Name:  " );
        clientNameSb.append( this.clientName );
        super.fillReportLine( super.getSbSmallLength(), clientNameSb);
        StringBuilder contactTitleSb = new StringBuilder();
        contactTitleSb.append ( "Contact Title:  " );
        contactTitleSb.append( this.contactTitle );
        super.fillReportLine(super.getSbSmallLength(), contactTitleSb);
        StringBuilder isActiveSb = new StringBuilder();
        isActiveSb.append ( "Active:  ");
        String activeString = this.isActive ? "ACTIVE" : "INACTIVE";
        isActiveSb.append ( activeString );
        super.fillReportLine( super.getSbSmallLength(), isActiveSb );
        StringBuilder isPrimarySb = new StringBuilder();
        isPrimarySb.append ( "Primary:  " );
        String primaryString = this.isPrimary ? "PRIMARY" : "NOT-PRIMARY";
        isPrimarySb.append( primaryString );
        super.fillReportLine( super.getSbSmallLength(), isPrimarySb);
        
        reportLine.append( super.getCommonReportLine() );
        reportLine.append( clientNameSb );
        reportLine.append( super.getTab() );
        reportLine.append( contactTitleSb );
        reportLine.append( super.getTab() );
        reportLine.append( isActiveSb );
        reportLine.append( super.getTab() );
        reportLine.append( isPrimarySb );       
                
        return reportLine.toString();
    }
    
    @Override
    public String getBranch()   {  return super.getBranch();  }
    @Override
    public String getCity() {  return super.getCity();  }
    @Override 
    public String getFirstName()    {  return super.getFirstName();  }
    @Override
    public String getLastName() {  return super.getLastName();  }
    @Override
    public String getEmail()    {  return super.getEmail();  }
    
    
    /*  methods common to all objects   */
    @Override
    public String toString()
    {
       return "Branch:  " + super.getBranch() + "  Client:  " + this.clientName +
            "  Email Address:  " + super.getEmail() + "  Contact Name:  " + super.getFirstName() 
            + " " + super.getLastName();
    }
    
    @Override
    public int compareTo ( ClientEmailReportData other )
    {
        /*  check branch    */
        int branch = String.CASE_INSENSITIVE_ORDER.compare( super.getBranch(), other.getBranch() );
        if ( branch != 0 )
            return branch;
        
        /*  check city  */
        int city = String.CASE_INSENSITIVE_ORDER.compare ( super.getCity(), other.getCity() );
        if ( city != 0 )
            return city;
        
        /*  check clientName    */
        int client = String.CASE_INSENSITIVE_ORDER.compare ( this.clientName, other.clientName );
        if ( client != 0 )
            return client;
        
        /*  check lastName  */
        int last = String.CASE_INSENSITIVE_ORDER.compare ( super.getLastName(), other.getLastName() );
        if ( last != 0 )
            return last;
        
        /*  check firstName */
        int first = String.CASE_INSENSITIVE_ORDER.compare ( super.getLastName(), other.getLastName() );
        if ( first != 0 )
            return first;
        
        /*  check email */
        return String.CASE_INSENSITIVE_ORDER.compare ( super.getEmail(), other.getEmail() );
    }
    
    @Override
    public boolean equals ( Object obj )
    {
        /*
         *  Method checks:  email -> lastName -> firstName -> branch -> city ->
         *      state -> clientName
         */
        if ( !( obj instanceof ClientEmailReportData ) )
            return false;
        
        final ClientEmailReportData other = ( ClientEmailReportData ) obj;
        
        return (
                    ( super.getEmail() == null ? other.getEmail() == null : super.getEmail().equals( other.getEmail() ) ) // test email
                &&  ( super.getLastName() == null ? other.getLastName() == null : super.getLastName().equals( other.getLastName() ) ) // test last name
                &&  ( super.getFirstName() == null ? other.getFirstName() == null : super.getFirstName().equals( other.getFirstName() ) )  // test first name
                &&  ( super.getBranch() == null ? other.getBranch() == null : super.getBranch().equals( other.getBranch() ) )  // test branch
                &&  ( super.getCity() == null ? other.getCity() == null : super.getCity().equals( other.getCity() ) )  // test city
                &&  ( super.getState() == null ? other.getState() == null : super.getState().equals( other.getState() ) ) // test state
                &&  ( this.clientName == null ? other.clientName == null : this.clientName.equals( other.clientName ) ) // test client name
                );
    }
    
    @Override
    public int hashCode()
    {
        int hash = 71;
        
        hash = 71 * hash + ( super.getEmail() != null ? super.getEmail().hashCode() : 0 );
        hash = 71 * hash + ( super.getLastName() != null ? super.getLastName().hashCode() : 0 );
        hash = 71 * hash + ( super.getFirstName() != null ? super.getFirstName().hashCode() : 0 );
        hash = 71 * hash + ( super.getBranch() != null ? super.getBranch().hashCode() : 0 );
        hash = 71 * hash + ( super.getCity() != null ? super.getCity().hashCode() : 0 );
        hash = 71 * hash + ( super.getState() != null ? super.getState().hashCode() : 0 );
        hash = 71 * hash + ( this.clientName != null ? this.clientName.hashCode() : 0 );        
        
        return hash;
    }
};
