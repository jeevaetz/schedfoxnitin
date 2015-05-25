/*
 * group_list_query.java
 *
 * Created on January 25, 2005, 10:57 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.management;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class group_list_query extends GeneralQueryFormat {
    
    /** Creates a new instance of group_list_query */
    public group_list_query() {
        myReturnString = new String();
    }
    
    public void update() {
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return " SELECT " +
               " groups_id as id, groups_name as name " +
               " FROM " +
               getManagementSchema() + ".groups " +
               " ORDER BY groups_name ";
    }
    
}
