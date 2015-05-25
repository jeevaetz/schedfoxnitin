/*
 * create_new_option_query.java
 *
 * Created on September 1, 2005, 3:56 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author Ira Juneau
 */
public class create_new_option_query extends GeneralQueryFormat {
    
    private String manageId;
    private String val;
    private String id;
    
    /** Creates a new instance of create_new_option_query */
    public create_new_option_query() {
        myReturnString = new String();
    }
    
    private String genSQL(String field) {
        return "(SELECT " + field + " FROM options WHERE options_id = " + id + " LIMIT 1)";
    }
    
    public void update(String ManagementId, String value, String myid) {
        manageId = ManagementId;
        val = value;
        id = myid;
    }
    
    public String toString() {
        return "DELETE FROM options WHERE options_name = " + genSQL("options_name") + " AND options_management_id = '" + manageId +"';" +
                "INSERT INTO options (options_name, options_type, options_value, options_category, options_user_access, options_display," +
                "options_management_id) VALUES (" + genSQL("options_name") + "," + genSQL("options_type") + ", '" +
                val + "' ," + genSQL("options_category") + "," + genSQL("options_user_access") + "," + genSQL("options_display") + ",'"
                 + manageId + "')";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
