/*
 * avaialability_memo_insert_query.java
 *
 * Created on January 26, 2005, 3:08 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class avaialability_memo_insert_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "INSERT INTO availability_notes (availability_notes_day, " +
            " availability_notes_employee_id, availability_notes_notes)" +
            " values('"
            );
    
    /** Creates a new instance of avaialability_memo_insert_query */
    public avaialability_memo_insert_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public void update(String date, String empId, String Memo) {
        myReturnString = MY_QUERY + date + "', " + empId + ", '" + Memo + "')";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
