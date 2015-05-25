/*
 * availability_memo_delete.java
 *
 * Created on January 26, 2005, 3:02 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_memo_delete_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "DELETE FROM availability_notes WHERE "
            );
    
    /** Creates a new instance of availability_memo_delete */
    public availability_memo_delete_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String EmpId, String date) {
        myReturnString = MY_QUERY + EmpId + " = availability_notes_employee_id " +
                         " AND availability_notes_day = '" + date + "'";
        
    }
    
    
    
}
