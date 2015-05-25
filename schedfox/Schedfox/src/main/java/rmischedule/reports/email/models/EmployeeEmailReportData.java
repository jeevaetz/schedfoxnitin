/*  package declaration */
package rmischedule.reports.email.models;

/*  import declarations */

/**
 *  A data object designed to hold Email Report Data related to the Employee.  It
 *      extends {@code AbstractEmailReportData} which contains the lions share
 *      of the data, which is similar to {@code ClientEmailReportData}
 *  <p><b>NOTE:  </b> this class uses a a static {@code Builder} object due
 *      to the number of parameters.  <b>ALL</b> parameters are required, none
 *      are optional.  Parameter validity is checked on instantiation, and
 *      a {@code RuntimeException} is thrown detailing the cause of the fault
 *      if a parameter is invalid.
 *  @author Jeffrey N. Davis
 *  @see rmischedule.reports.email.controllers.EmailReportController
 *  @see rmischedule.reports.email.models.AbstractEmailReportData
 *  @since 04/25/2011
 */
public final class EmployeeEmailReportData extends AbstractEmailReportData implements Comparable<EmployeeEmailReportData>
{
    /*  object variable declarations    */
    private final String address;
    private final String phone;
    private final String cell;
    private final boolean isActive;
    
    /*  object instantiation code   */
    /**
     *  This static object is used to create an instance of {@code EmployeeEmailReportData}.
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
        private String address;
        private String phone;
        private String cell;
        private boolean isActive;
        
        /**
         *  Instantiates this object.
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
        public Builder address ( String val )
            {  this.address = val;  return this;  }
        public Builder phone ( String val )
            {  this.phone = val;  return this;  }
        public Builder cell ( String val )
            {  this.cell = val;  return this;  }
        public Builder isActive ( boolean val )
            {  this.isActive = val;  return this;  }
        
        public EmployeeEmailReportData build()
            {  return new EmployeeEmailReportData ( this );  }
    }
    
    /**
     *  Default instantiation of this object is not allowed.
     *  @throws UnsupportedOperationException
     */
    private EmployeeEmailReportData()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object EmployeeEmailReportData is not allowed." );
    }
    
    /**
     *  Instantiates this object by assigning member variables to corresponding {@code Builder}
     *      variables. After assignment, each variable is checked for validity.
     *  @param builder an instance of {@code Builder}
     */
    private EmployeeEmailReportData ( Builder builder )
    {
        super.setEmail( builder.email );
        super.setFirstName( builder.firstName );
        super.setLastName( builder.lastName );
        super.setBranch( builder.branch );
        super.setCity( builder.city );
        super.setState( builder.state );
        this.address = builder.address;
        this.phone = builder.phone;
        this.cell = builder.cell;
        this.isActive = builder.isActive;
        
        try
        {
            this.areParamsValid();
        }
        catch ( RuntimeException ex )
        {
            throw new IllegalArgumentException( "Object was not constructed in a valid state.");
        }
    }
    
    /**
     *  Checks to ensure each param is invalid.  
     *  @throws RuntimeException if any parameter is invalid
     */
    private void areParamsValid()  throws RuntimeException
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
        
        if ( !isValid )
            throw new RuntimeException();
    }    
    
    
    @Override
    public String getReportLine() 
    {
        StringBuilder reportLine = new StringBuilder();
                
        StringBuilder addressSb = new StringBuilder();
        addressSb.append( "Address:  " );
        addressSb.append( this.address );
        super.fillReportLine( super.getSbLargeLength(), addressSb);
        StringBuilder phoneSb = new StringBuilder();
        phoneSb.append( "Phone:  " );
        phoneSb.append( this.phone );
        super.fillReportLine( super.getSbSmallLength(), phoneSb);
        StringBuilder cellSb = new StringBuilder();
        cellSb.append( "Cell:  " );
        cellSb.append ( this.cell );
        super.fillReportLine( super.getSbSmallLength(), cellSb );
        String active = this.isActive ? "ACTIVE" : "INVACTIVE";
        StringBuilder activeSb = new StringBuilder();
        activeSb.append( "Active:  ");
        activeSb.append( active );
        super.fillReportLine( super.getSbSmallLength(), activeSb );
        
        reportLine.append( super.getCommonReportLine() );
        reportLine.append( addressSb );
        reportLine.append( super.getTab() );
        reportLine.append( phoneSb );
        reportLine.append( super.getTab() );
        reportLine.append( cellSb );
        reportLine.append( super.getTab() );
        reportLine.append( activeSb );
        
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
    public boolean isActive()   {  return this.isActive;  }
    
    /*  methods common to all objects   */
    @Override
    public String toString()
    {
        return "Branch:  " + super.getBranch() + "  Email Address:  " + super.getEmail() 
            + "  Contact Name:  " + super.getFirstName() + " " + super.getLastName();
    }
    
    @Override
    public int compareTo ( EmployeeEmailReportData other )
    {
          /*  check branch    */
        int branch = String.CASE_INSENSITIVE_ORDER.compare( super.getBranch(), other.getBranch() );
        if ( branch != 0 )
            return branch;
        
        /*  check city  */
        int city = String.CASE_INSENSITIVE_ORDER.compare ( super.getCity(), other.getCity() );
        if ( city != 0 )
            return city;
        
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
         *  Method checks email -> last name -> first name -> branch -> 
         *      state -> address
         */
        if ( !(obj instanceof EmployeeEmailReportData) )
            return false;
        
        final EmployeeEmailReportData other = (EmployeeEmailReportData) obj;
        
        return (
                    ( super.getEmail() == null ? other.getEmail() == null : super.getEmail().equals( other.getEmail() ) ) // test email
                &&  ( super.getLastName() == null ? other.getLastName() == null : super.getLastName().equals( other.getLastName() ) ) // test last name
                &&  ( super.getFirstName() == null ? other.getFirstName() == null : super.getFirstName().equals( other.getFirstName() ) )  // test first name
                &&  ( super.getBranch() == null ? other.getBranch() == null : super.getBranch().equals( other.getBranch() ) )  // test branch
                &&  ( super.getCity() == null ? other.getCity() == null : super.getCity().equals( other.getCity() ) )  // test city
                &&  ( super.getState() == null ? other.getState() == null : super.getState().equals( other.getState() ) ) // test state
                &&  ( this.address == null ? other.address == null : this.address.equals( other.address) )    // test address
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
        hash = 71 * hash + ( this.address != null ? this.address.hashCode() : 0 );
        
        return hash;        
    }    
};
