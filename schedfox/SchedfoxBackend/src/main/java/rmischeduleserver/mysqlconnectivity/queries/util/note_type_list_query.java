/*
 * note_type_list_query.java
 *
 * Created on January 24, 2005, 8:24 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class note_type_list_query extends GeneralQueryFormat {
    
    /** Creates a new instance of note_type_list_query */
    public note_type_list_query() {
        this.myReturnString =   "SELECT note_type_id   as id,  " +
                                      " note_type_name as name " +
                                "FROM note_type "                +
                                "ORDER BY note_type_name;";
    }

    public boolean hasAccess() { return true; }
}
