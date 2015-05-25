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
public class options_save_query extends GeneralQueryFormat {
    
    /** Creates a new instance of options_save_query */
    public options_save_query() {
        myReturnString = new String();
    }
    
    public void update(String OptionsVal, String OptionsName) {
        myReturnString = "Update " + getManagementSchema() + ".options set " + generateValueSQL(OptionsVal) + generateNameSQL(OptionsName);
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
        return myReturnString;
    }
    
    public String generateNameSQL(String name) {
        return " where options_name = '" + name + "'";
    }
     
}
