/*
 * schedule_get_sched_id_query.java
 *
 * Created on January 21, 2005, 1:33 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class schedule_get_sched_id_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "Select schedule_id as id from schedule " +
            "Where " +
            " employee_id      = ");
    
    /** Creates a new instance of schedule_get_sched_id_query */
    public schedule_get_sched_id_query() {
        myReturnString = new String();
    }

    public void update(String emp_id, String cli_id, String date, String start, String end, String day, String type, String group) {
        myReturnString = MY_QUERY + emp_id + " And  client_id = " + cli_id +
                         " And schedule_date = '" + date + "' And schedule_start = " + start +
                         " And schedule_end = " + end + " And schedule_day = " + day;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return myReturnString;
    }
    
}
