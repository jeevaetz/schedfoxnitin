/*
 * get_group_access_security_query.java
 *
 * Created on April 21, 2005, 7:38 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class get_group_access_security_query extends GeneralQueryFormat {
    
    private String uid;
    
    /** Creates a new instance of get_user_access_security_query */
    public get_group_access_security_query() {
        myReturnString = new String();
    }
    
    public void update(String gId) {
        uid = gId;
    }
    
    public String toString() {
        return "SELECT groups_access.access_id as id, groups.groups_name " +
                "FROM groups LEFT JOIN " + getManagementSchema() + ".groups_access " +
                "ON groups.groups_id = groups_access.groups_id WHERE groups_access.groups_id = " + uid;
    }
    
    public boolean hasAccess() {
        return true;
    }       
}
