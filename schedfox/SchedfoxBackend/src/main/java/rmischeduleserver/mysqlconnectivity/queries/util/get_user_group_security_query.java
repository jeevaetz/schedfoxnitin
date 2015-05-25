/*
 * get_user_group_security_query.java
 *
 * Created on April 19, 2005, 8:54 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_group_security_query extends GeneralQueryFormat {
    
    private String uid;
    private String management;
    
    /** Creates a new instance of get_user_group_security_query */
    public get_user_group_security_query() {
        myReturnString = new String();
    }
    
    public void update(String userId, String managementid) {
        uid = userId;
        management = managementid;
    }
    
    public String toString() {
        String manageSQL = new String();
        if (!management.equals("0")) {
            manageSQL = " WHERE groups_management_id = " + management + " OR groups_management_id = 0";
        } else {
            manageSQL = " WHERE groups_management_id = 0";
        }
        return "SELECT groups.groups_id as id, groups.groups_id, groups.groups_name as name, " +
                "CASE WHEN groups.groups_id IN ((SELECT groups_id FROM " + getManagementSchema() + ".user_groups WHERE user_groups.user_id = " + uid + ")) " +
                "THEN 'true' ELSE 'false' END as access FROM groups LEFT JOIN " + getManagementSchema() + ".user_groups " +
                "ON groups.groups_id = user_groups.groups_id AND user_groups.user_id = " + uid + manageSQL;
    }
    
    public boolean hasAccess() {
        return true;
    } 
    
}
