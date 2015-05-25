/*
 * get_training_for_schedule_query.java
 *
 * Created on June 21, 2005, 11:21 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_training_for_schedule_query extends GeneralQueryFormat {
    
    private String endWeek;
    private String begWeek;
    
    /** Creates a new instance of get_training_for_schedule_query */
    public get_training_for_schedule_query() {
        myReturnString = new String();
    }
    
    public void update(String bw, String ew) {
        begWeek = bw;
        endWeek = ew;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT employee_trained.employee_id as eid," +
                "(employee.employee_last_name || ', ' || employee.employee_first_name) as name, " +
                "employee_trained.client_id as cid, " +
                "employee_trained.schedule_id as sid, " +
                "employee_trained.training_employee_id as teid, " +
                "employee_trained.employee_is_done_training_this_shift as done FROM " +
                "employee_trained LEFT JOIN client ON " +
                "client.client_id = employee_trained.client_id " +
                "LEFT JOIN schedule ON " +
                "schedule.schedule_id = employee_trained.schedule_id AND " +
                "schedule.schedule_date BETWEEN '" + begWeek + "' AND '" + endWeek + "' " +
                "LEFT JOIN employee ON " +
                "employee.employee_id = employee_trained.employee_id WHERE " +
                "client.branch_id = " + getBranch();
    }
    
}
