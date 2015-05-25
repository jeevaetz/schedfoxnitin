/*
 * availability_types_query.java
 *
 * Created on January 26, 2005, 4:31 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_types_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "Select availability_type_val, availability_type.availability_type_id from " +
            "availability_type where availability_type.availability_type_id between 3 and 8"
            );
    
    
    /** Creates a new instance of availability_types_query */
    public availability_types_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public void update() {
        myReturnString = MY_QUERY;
    }
    
}
