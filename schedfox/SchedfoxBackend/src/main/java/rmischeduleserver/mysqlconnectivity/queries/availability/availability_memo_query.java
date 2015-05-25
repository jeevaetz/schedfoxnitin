/*
 * availability_memo_query.java
 *
 * Created on January 25, 2005, 8:19 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_memo_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            " Select" +
            " avail_notes_day as date," +
            " avail_notes_notes as text " +
            " From availability_notes " +
            " Where" +
            " avail_notes_employee_id " +
            " = "
            );
    
    /** Creates a new instance of availability_memo_query */
    public availability_memo_query() {
        myReturnString = new String();
    }
    
    public void update(String EmployeeId) {
        myReturnString = MY_QUERY + EmployeeId;
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
