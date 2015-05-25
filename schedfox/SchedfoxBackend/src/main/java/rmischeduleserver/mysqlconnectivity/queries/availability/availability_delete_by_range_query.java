/*
 * availability_delete_by_range_query.java
 *
 * Created on January 26, 2005, 3:18 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_delete_by_range_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "DELETE FROM availability " +
            " WHERE " +
            " employee_id = "
    );
    
    /** Creates a new instance of availability_delete_by_range_query */
    public availability_delete_by_range_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String EmpId, String StartDate, String EndDate, String Row) {
        String shiftQuery = new String("");
        if (Row.length() > 0) {
            shiftQuery = " and availability_shift = '" + Row + "'";
        }
        myReturnString = MY_QUERY + EmpId + " and '" + StartDate + "' <= availability_day_of_year and " +
                         "'" + EndDate + "' >= availability_day_of_year" + shiftQuery;
    }
           
    
}
