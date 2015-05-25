/*
 * get_user_access_security_query.java
 *
 * Created on April 19, 2005, 8:57 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_access_security_query extends GeneralQueryFormat {
    
    private String uid;
    
    /** Creates a new instance of get_user_access_security_query */
    public get_user_access_security_query() {
        myReturnString = new String();
    }
    
    public void update(String userId, String managementid) {
        uid = userId;
    }
    
    public String toString() {
        return "SELECT access.access_id as id, access.access_id, access.access_name as name, " +
                "CASE WHEN access.access_id IN ((SELECT access_id FROM " + getManagementSchema() + ".user_access WHERE user_access.user_id = " + uid + ")) " +
                "THEN 'true' ELSE 'false' END as access FROM access LEFT JOIN " + getManagementSchema() + ".user_access " +
                "ON access.access_id = user_access.access_id AND user_access.user_id = " + uid;
    }
    
    public boolean hasAccess() {
        return true;
    }       
}
