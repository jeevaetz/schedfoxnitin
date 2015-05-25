/*
 * EmptyCCSEmployeeTable.java
 *
 * Created on January 20, 2005, 1:06 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class EmptyCCSEmployeeTable extends GeneralQueryFormat {
        private static final String MY_QUERY = (
            "Delete * From  usked_employee"
            );
    /** Creates a new instance of EmptyCCSEmployeeTable */
    public EmptyCCSEmployeeTable() {
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY;
    }    
}
