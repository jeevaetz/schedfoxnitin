/*
 * GetBranchesQuery.java
 *
 * Created on January 19, 2005, 11:21 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.management;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class GetBranchesQuery extends GeneralQueryFormat {
    
    /** Creates a new instance of GetBranchesQuery */
    private static final String MY_QUERY = ("Select * from control_db.branch");
    
    private String returnString;
    
    /** Creates a new instance of GetCompaniesQuery */
    public GetBranchesQuery() {
        returnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY;
    }

    public void update(String val) {
    }
}
