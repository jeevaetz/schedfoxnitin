/*
 * get_groups_management_query.java
 *
 * Created on September 6, 2005, 9:21 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class get_groups_management_query extends GeneralQueryFormat {
    
    private String manageID;
    
    /** Creates a new instance of get_branch_by_management_query */
    public get_groups_management_query() {
        myReturnString = new String();
    }
    
    public void update(String manageId) {
        manageID = manageId;
    }
    
    public String toString() {
        String sql = new String();
        if (!manageID.equals("0")) {
            sql = " WHERE groups_management_id = " + manageID + " OR groups_management_id =  0";
        } else {
            sql = " WHERE groups_management_id =  0";
        }
        return "SELECT groups_id, groups_name, groups_management_id FROM " + getManagementSchema() + 
               ".groups " + sql;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
