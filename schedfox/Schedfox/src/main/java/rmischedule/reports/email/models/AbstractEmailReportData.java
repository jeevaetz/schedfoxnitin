/*  package declaration */
package rmischedule.reports.email.models;

/*  import declarations */

/**
 *  An abstract class containing the majority of code needed for EmailReportData
 *  @author Jeffrey N. Davis
 *  @see rmischedule.reports.email.controllers.EmailReportController
 *  @since 04/22/2011
 */
public abstract class AbstractEmailReportData 
{
    /*  object variable declarations    */
    private String email;
    private String firstName;
    private String lastName;
    private String branch;
    private String city;
    private String state;
    private static final int STRINGBUILDER_LARGE_LENGTH = 35;
    private static final int STRINGBUILDER_SMALL_LENGTH = 25;
    private static final char TAB = '\t';
    
    /*  object instantiation code   */
    /**
     *  Default instantiation of this object.
     */
    protected AbstractEmailReportData() {}
    
    /** 
     *  Constructs and returns a report line with all data formatted.
     *  @return commonReportLine a StringBuilder representing the common report line
     */ 
    protected StringBuilder getCommonReportLine()
    {
        StringBuilder commonReportLine = new StringBuilder();
        commonReportLine.setLength( 0 );
        
        StringBuilder nameSb = new StringBuilder();
        nameSb.append( "Name:  ");
        nameSb.append( this.firstName );
        nameSb.append(" ");
        nameSb.append( this.lastName );
        this.fillReportLine(STRINGBUILDER_SMALL_LENGTH, nameSb);
        StringBuilder emailSb = new StringBuilder();
        emailSb.append( "Email:  " );
        emailSb.append( this.email );
        this.fillReportLine(STRINGBUILDER_LARGE_LENGTH, emailSb);
        StringBuilder branchSb = new StringBuilder();
        branchSb.append( "Branch:  " );
        branchSb.append( this.branch );
        this.fillReportLine(STRINGBUILDER_SMALL_LENGTH, branchSb);
        StringBuilder citySb = new StringBuilder();
        citySb.append( "City:  " );
        citySb.append( this.city );
        this.fillReportLine(STRINGBUILDER_SMALL_LENGTH, citySb);
        StringBuilder stateSb = new StringBuilder();
        stateSb.append( "State:  " );
        stateSb.append( this.state );
        this.fillReportLine(STRINGBUILDER_SMALL_LENGTH, stateSb);
        
        commonReportLine.append( nameSb );
        commonReportLine.append ( TAB );
        commonReportLine.append( emailSb );
        commonReportLine.append ( TAB );
        commonReportLine.append ( branchSb );
        commonReportLine.append ( TAB );
        commonReportLine.append ( citySb );
        commonReportLine.append ( TAB );
        commonReportLine.append ( stateSb );
        commonReportLine.append ( TAB );
                
        return commonReportLine;
    }
    
    /**
     *  Method fills the void of a {@code StringBuilder} with whitespace
     *      instead of manually using {@code StringBuilder.setLength()} to ensure
     *      proper character formatting
     * @param length
     * @param sb 
     */
    protected void fillReportLine ( int length, StringBuilder sb )
    {
        sb.trimToSize();
        for ( int idx = sb.length(); idx < length; idx ++ )
            sb.append( " " );
        sb.trimToSize();
    }
    

    
    protected abstract String getReportLine();
    
    /*  mutator methods    */
    protected String getBranch()   {  return branch;  }
    protected String getCity() {  return city;  }
    protected String getEmail()    {  return email;  }
    protected String getFirstName()    {  return firstName;  }
    protected String getLastName()  {  return lastName;  }
    protected String getState()    {  return state;  }
    protected int getSbLargeLength()    {  return STRINGBUILDER_LARGE_LENGTH;  }
    protected int getSbSmallLength()    {  return STRINGBUILDER_SMALL_LENGTH;  }
    protected char getTab() {  return TAB;  }
    
    protected void setBranch ( String branch ) {  this.branch = branch;  }
    protected void setCity ( String city ) {  this.city = city;  }
    protected void setEmail ( String email )   {  this.email = email;  }
    protected void setFirstName ( String firstName )   {  this.firstName = firstName;  }
    protected void setLastName ( String lastName ) {  this.lastName = lastName;  }
    protected void setState ( String state )   {  this.state = state;  }
};
