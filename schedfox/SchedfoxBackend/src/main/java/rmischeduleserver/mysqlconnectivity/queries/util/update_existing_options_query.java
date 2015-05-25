/*
 * options_save_query.java
 *
 * Created on January 21, 2005, 12:51 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class update_existing_options_query extends GeneralQueryFormat {
    
    private String myVal;
    private String myId;
    
    /** Creates a new instance of options_save_query */
    public update_existing_options_query() {
        myReturnString = new String();
    }
    
    public void update(String OptionsVal, String id) {
        myId = id;
        myVal = OptionsVal;
   }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String generateValueSQL(String val) {
        if (val.length() > 0) {
            return "options_value 	= 	'" + val + "' ";
        }
        return "";
    }
    
    public String toString() {
        return  "UPDATE " + getManagementSchema() + ".options SET options_value =  '" + myVal + "' WHERE options_id = " + myId;
    }
    
    public String generateNameSQL(String name) {
        return " where options_name = '" + name + "'";
    }
    
}
