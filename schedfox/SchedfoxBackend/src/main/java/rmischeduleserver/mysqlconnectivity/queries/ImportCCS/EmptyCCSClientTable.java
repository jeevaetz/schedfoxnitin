/*
 * EmptyCCSClientTable.java
 *
 * Created on January 20, 2005, 1:03 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class EmptyCCSClientTable extends GeneralQueryFormat {

    private static final String MY_QUERY = (
            "Delete * From  usked_client"
            );
    
    /** Creates a new instance of EmptyCCSClientTable */
    public EmptyCCSClientTable() {
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY;
    }
    
}
