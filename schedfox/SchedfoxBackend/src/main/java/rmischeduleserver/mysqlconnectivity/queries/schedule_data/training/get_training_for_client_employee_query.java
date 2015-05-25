/*
 * get_training_for_client_employee_query.java
 *
 * Created on May 12, 2005, 10:01 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class get_training_for_client_employee_query extends GeneralQueryFormat {
    
    String cli_id;
    String start_date;
    
    /** Creates a new instance of get_training_for_client_employee_query */
    public get_training_for_client_employee_query() {
        myReturnString = new String();
    }
    
    public void update(String cid, String sdate) {
        cli_id = cid;
        start_date = sdate;
    }
    
    public String toString() {
        return  "SELECT client.client_id, " +
                "employee.employee_id, " +
                "employee_trained.schedule_id, " +
                "schedule.schedule_date, " +
                "Get_Difference(schedule.schedule_start, " +
                "schedule.schedule_end) as difference, " +
                "employee_is_done_training_this_shift " +
                "FROM client " +
                "LEFT JOIN " +
                "employee_trained on client.client_id " +
                "= employee_trained.client_id " +
                "LEFT JOIN employee on employee.employee_id = employee_trained.employee_id " +
                "LEFT JOIN schedule ON schedule.schedule_id = employee_trained.schedule_id " +
                "WHERE client.client_id = " +
                cli_id + " AND client.client_training_time > 0 AND schedule.schedule_is_deleted = 0 AND schedule_date < " +
                "'" + start_date + "';";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
