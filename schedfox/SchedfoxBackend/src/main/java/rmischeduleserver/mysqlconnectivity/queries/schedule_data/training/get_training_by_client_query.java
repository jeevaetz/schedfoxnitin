/*
 * get_training_by_client_query.java
 *
 * Created on June 1, 2005, 7:26 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class get_training_by_client_query extends GeneralQueryFormat {
    
         
//    select employee.employee_id, ( Select (SUM(Get_Difference(schedule.schedule_start, schedule.schedule_end))) 
//    FROM schedule where schedule.schedule_id = employee_trained.schedule_id) as total 
//            from employee LEFT JOIN employee_trained on employee_trained.employee_id = employee.employee_id where employee.branch_id = 3

    private String CID;
    private String EID;
    
    /** Creates a new instance of get_training_by_client_query */
    public get_training_by_client_query() {
        myReturnString = new String();
        CID = new String();
        EID = new String();
    }
    
    public void update(String eid, String cid) {
        CID = cid;
        EID = eid;
    }
    
    public String toString() {
        String clientString = new String();
        String employeeString = new String();
        if (CID.length() > 0) {
            clientString = " AND schedule.client_id = " + CID;
        }
        if (EID.length() > 0) {
            employeeString = " AND employee.employee_id = " + EID;
        }
        String retVal =  "SELECT DISTINCT employee.employee_id as id, " +
                "employee.employee_first_name as fname, " +
                "employee.employee_last_name as lname, " +
                "(CASE WHEN EXISTS (SELECT employee_trained.employee_id FROM " +
                "employee_trained WHERE employee_trained.employee_id = employee.employee_id " +
                "AND employee_trained.client_id = " + CID + " AND employee_trained.employee_is_done_training_this_shift = true) " +
                "THEN true ELSE false END) as override, " +
                "( SELECT (SUM(Get_Difference(schedule.schedule_start, schedule.schedule_end))) " +
                "FROM schedule WHERE schedule.employee_id = employee_trained.employee_id " + clientString + " AND schedule.schedule_date < DATE(NOW())) / 60  as tot, " +
                "(client.client_training_time) as ttime " +
                "FROM client, employee " +
                "LEFT JOIN employee_trained ON " +
                "employee_trained.employee_id = employee.employee_id " +
                "LEFT JOIN schedule ON " +
                "schedule.schedule_id = employee_trained.schedule_id " +
                "WHERE employee.branch_id = " + getBranch() + employeeString + 
                " AND client.client_id = " + CID + " AND employee.employee_is_deleted = 0";
        return retVal;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
