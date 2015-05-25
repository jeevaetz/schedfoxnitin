/*
 * delete_undelete_management_query.java
 *
 * Created on November 28, 2005, 12:57 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class delete_undelete_management_query extends GeneralQueryFormat {
    
    private boolean isDel;
    private String MID;
    
    /** Creates a new instance of delete_undelete_management_query */
    public delete_undelete_management_query() {
        myReturnString = new String();
    }
    
    public void update(String mid, boolean isDeleted) {
        MID = mid;
        isDel = isDeleted;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        if(isDel) {
            return "UPDATE management_clients SET management_is_deleted = " + isDel + " WHERE management_id = " + MID + ";";
        }
        else {
            return "DELETE FROM management_clients WHERE management_id = " + MID + ";";
        }
            
        
    }
    
}
