/*
 * availability_types_query.java
 *
 * Created on January 24, 2005, 9:22 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_types_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "SELECT " +
            "availability_type_val as text, " +
            "availability_type.availability_type_id as val " +
            "FROM " +
            "availability_type " +
            "WHERE " +
            "availability_type.availability_type_id BETWEEN 3 and 8"
            );
    
    /** Creates a new instance of availability_types_query */
    public availability_types_query() {
        myReturnString = new String();
    }
    
    public void update() {
        myReturnString = MY_QUERY;
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
