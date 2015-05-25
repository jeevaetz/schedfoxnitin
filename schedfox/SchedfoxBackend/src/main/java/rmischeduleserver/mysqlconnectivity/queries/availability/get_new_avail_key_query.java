/*
 * get_new_avail_key_query.java
 *
 * Created on September 15, 2005, 4:45 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 * Small query to return newest avail key, after an insert...
 */
public class get_new_avail_key_query extends GeneralQueryFormat {
    
    /** Creates a new instance of get_new_avail_key_query */
    /** Creates a new instance of availability_save_query */
    public get_new_avail_key_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return "SELECT MAX(availability.avail_id) as avail_id FROM availability; ";
    }

    public boolean hasAccess() {
        return true;
    }
    
}
