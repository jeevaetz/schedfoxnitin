/*  package declaration */
package rmischedule.reports.email.models;

/*  import declarations */
import javax.swing.JRadioButton;

/**
 *
 * @author jdavis
 */
public final class BranchEmailReportData extends JRadioButton
{
    /*  object variable declarations    */
    private final int companyId;
    private final int branchId;
    private final String branchName;
    
    /*  object instantiation code   */
    /**
     *  Default instantiation of this object is not allowed
     *  @throws UnsupportedOperationException
     */
    private BranchEmailReportData()
    {
        throw new UnsupportedOperationException ( "Default instantiation of the object BranchEmailReportData is not allowed." );
    }
    
    /**
     *  Creates an instance of this object.
     *  @param companyId an int describing the companyId
     *  @param branchId an int describing the branchId
     *  @param branchName a String describing the branchName
     */
    public BranchEmailReportData ( int companyId, int branchId, String branchName )
    {
        this.companyId = companyId;
        this.branchId = branchId;
        this.branchName = branchName;
    }
    
    /*  private method implementations  */
    
    
    /*  getter methods  */
    public String getBranchName()   {  return this.branchName;  }
    public int getBranchId()    {  return this.branchId; }
    public int getCompanyId()   {  return this.companyId;  }
};
