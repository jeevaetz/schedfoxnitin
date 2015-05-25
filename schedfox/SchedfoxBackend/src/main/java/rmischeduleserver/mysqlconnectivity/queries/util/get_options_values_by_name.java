/*
 * get_options_values_by_name.java
 *
 * Created on September 1, 2005, 3:15 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_options_values_by_name extends GeneralQueryFormat {
    
    private String oName;
    
    /** Creates a new instance of get_options_values_by_name */
    public get_options_values_by_name() {
        myReturnString = new String();
    }
    
    public void update(String optionName) {
        oName = optionName;
    }
    
    public String toString() {
        return "SELECT options_value as val, options_management_id as id, options_id as optid FROM options WHERE options_name = '" + oName + "'"; 
    }
    
    public boolean hasAccess() {
        return true;
    }
}
