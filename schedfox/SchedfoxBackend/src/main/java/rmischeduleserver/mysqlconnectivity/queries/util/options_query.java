/*
 * options_query.java
 *
 * Created on January 21, 2005, 10:54 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class options_query extends GeneralQueryFormat {
    
    /** Creates a new instance of options_query */
    public options_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return "SELECT DISTINCT " +
                " options_name  as name_field, " +
                " options_type  as type_field, " +
                " options_category as category_field, " +
                " options_user_access as access_field, " +
                " options_display as display_field " +
                " FROM " + this.getManagementSchema() + ".options ";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
