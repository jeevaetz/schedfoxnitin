/*
 * get_user_info_query.java
 *
 * Created on April 19, 2005, 8:08 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_user_info_query extends GeneralQueryFormat {
    
    private String emp_id;
    
    /** Creates a new instance of get_user_info_query */
    public get_user_info_query() {
        myReturnString = new String();
    }
    
    public void update(String eid) {
        emp_id = eid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "Select user_id, user_md5, user_login, user_password, user_first_name, user_last_name, user_middle_initial, (CASE WHEN user_is_deleted = 0 THEN 'false' ELSE 'true' END) as user_is_deleted FROM " + getManagementSchema() +".user WHERE user_id = " + emp_id;
    }
    
}
