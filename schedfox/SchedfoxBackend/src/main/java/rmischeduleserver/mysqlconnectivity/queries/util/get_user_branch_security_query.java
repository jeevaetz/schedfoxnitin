/*
 * get_user_branch_security_query.java
 *
 * Created on April 19, 2005, 8:20 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_branch_security_query extends GeneralQueryFormat {
    
    private String uid;
    private String management;
    
    /** Creates a new instance of get_user_branch_security_query */
    public get_user_branch_security_query() {
        myReturnString = new String();
    }
    
    public void update(String userId, String managementId) {
        uid = userId;
        management = managementId;
    }
    
    
    public String toString() {
        String manageSQL = new String();
        if (!management.equals("0")) {
            manageSQL = " WHERE branch_management_id = " + management;
        }
        return "SELECT branch.branch_id as id, branch.branch_id, branch.branch_name as name, " +
                "CASE WHEN branch.branch_id IN ((SELECT branch_id FROM " + getManagementSchema() + ".user_branch WHERE user_branch.user_id = " + uid + ")) " +
                "THEN 'true' ELSE 'false' END as access FROM branch LEFT JOIN " + getManagementSchema() + ".user_branch " +
                "ON branch.branch_id = user_branch.branch_id AND user_branch.user_id = " + uid + manageSQL + " Order By branch.branch_name";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
