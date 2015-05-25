/*
 * management_list_query.java
 *
 * Created on January 25, 2005, 10:05 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class management_list_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            " SELECT " +
            " management_clients.management_id as id, " +
            " management_clients.management_client_name as name " +
            " FROM control_db.management_clients ORDER BY name;"
            );
    
    /** Creates a new instance of management_list_query */
    public management_list_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update() {
        myReturnString = MY_QUERY;
    }
    
    public String toString() {
        return myReturnString;
    }
    
}
