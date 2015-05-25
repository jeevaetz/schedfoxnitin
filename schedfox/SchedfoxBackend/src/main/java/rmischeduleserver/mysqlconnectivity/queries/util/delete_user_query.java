/*
 * delete_user_query.java
 *
 * Created on April 19, 2005, 9:08 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class delete_user_query extends GeneralQueryFormat {
    
    private String uid;
    
    /** Creates a new instance of delete_user_query */
    public delete_user_query() {
        myReturnString = new String();
    }
    
    public void update(String userid) {
        uid = userid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "UPDATE " + getManagementSchema() + ".user SET user_is_deleted = (CASE WHEN user_is_deleted = 1 THEN 0 ELSE 1 END) WHERE " +
               "user_id = " + uid;
    }
    
}
