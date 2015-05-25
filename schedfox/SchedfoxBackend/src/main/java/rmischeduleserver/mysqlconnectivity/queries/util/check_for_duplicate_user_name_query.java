/*
 * check_for_duplicate_user_name_query.java
 *
 * Created on November 28, 2005, 1:27 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class check_for_duplicate_user_name_query extends GeneralQueryFormat {
    
    private String lname;
    private String id;
    
    /** Creates a new instance of check_for_duplicate_user_name_query */
    public check_for_duplicate_user_name_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String loginName, String myId) {
        lname = loginName;
        id = myId;
    }
    
    public String toString() {
        return "SELECT * FROM control_db.user LEFT JOIN management_clients on management_clients.management_id = " +
                "control_db.user.user_management_id WHERE control_db.user.user_login = '" + lname + "' AND user_id != " + id + " AND " +
                "user_is_deleted = 0 AND management_clients.management_is_deleted = FALSE;"; 
    }
    
}
