/*
 * get_time_query.java
 *
 * Created on February 15, 2005, 10:49 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_time_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY_POSTGRES = (
            "Select Now() as time"
            );

    private static final String MY_QUERY_ORACLE = (
            "SELECT CURRENT_TIMESTAMP as time FROM dual"
            );

    /** Creates a new instance of get_time_query */
    public get_time_query() {
        myReturnString = MY_QUERY_POSTGRES;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
