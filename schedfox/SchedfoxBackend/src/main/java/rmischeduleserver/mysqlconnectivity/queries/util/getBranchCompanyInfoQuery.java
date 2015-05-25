/*
 * getBranchCompanyInfoQuery.java
 *
 * Created on July 6, 2005, 8:15 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class getBranchCompanyInfoQuery extends GeneralQueryFormat {
    
    private String branch;
    private String company;
    
    /** Creates a new instance of getBranchCompanyInfoQuery */
    public getBranchCompanyInfoQuery() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String co, String br) {
        branch = br;
        company = co;
    }
    
    public String toString() {
        return "SELECT (SELECT company_name from " + getManagementSchema() + ".company WHERE " +
                "company_id = " + company + ") as compname, " +
                "(SELECT branch_name from " + getManagementSchema() + ".branch WHERE " +
                "branch_id = " + branch + ") as branchname";
    }
    
}
