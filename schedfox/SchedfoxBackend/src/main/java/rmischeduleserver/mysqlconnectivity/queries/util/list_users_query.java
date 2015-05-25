/*
 * list_users_query.java
 *
 * Created on April 18, 2005, 3:38 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class list_users_query extends GeneralQueryFormat {
    
    private boolean showDel;
    private String managementId;
    private String userId;
    
    /** Creates a new instance of list_users_query */
    public list_users_query() {
        myReturnString = new String();
        showDel = false;
    }
    
    public void update(boolean showDeleted, String manageId) {
        showDel = showDeleted;
        managementId = manageId;
    }
    
    public void update(boolean showDeleted, String manageId, String id) {
        showDel = showDeleted;
        managementId = manageId;
        userId = id;
    }
    
    public String toString() {
        String del = " WHERE user_id = user_id ";
        String user = "";
        if (userId != null) {
            user = " AND user_id = " + userId;
        }
        String manageSQL = new String();
        if (!showDel) {
            del = del + " AND user_is_deleted = 0";
        }
        
        manageSQL = " AND ";
        
        manageSQL = manageSQL + " user_management_id = " + managementId;
        
        return "Select user_id, user_md5, user_login, user_password, user_management_id, user_first_name, user_last_name, user_middle_initial, user_is_deleted, user_email as email, can_view_ssn FROM " +
               getManagementSchema() + "." + getDriver().getTableName("user") + del + manageSQL + user + " Order by user_first_name, user_last_name";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
