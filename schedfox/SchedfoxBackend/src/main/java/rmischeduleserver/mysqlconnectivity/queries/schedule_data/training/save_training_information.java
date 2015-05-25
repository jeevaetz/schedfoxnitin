/*
 * save_training_information.java
 *
 * Created on May 12, 2005, 10:29 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.training;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class save_training_information extends GeneralQueryFormat {
    
    String emp_id, cli_id, shi_id, tra_id;
    
    /** Creates a new instance of save_training_information */
    public save_training_information() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() {
        return UPDATE_SCHEDULE;
    }
    
    public void update(String eid, String cid, String sid, String traineid) {
        emp_id = eid;
        cli_id = cid;
        shi_id = sid;
        tra_id = traineid;
    }
    
    public String toString() {
        return "DELETE FROM employee_trained WHERE client_id = " 
               + cli_id + " AND schedule_id = " + shi_id + ";" +
               "INSERT INTO employee_trained (employee_id, client_id, schedule_id, " +
               "training_employee_id) VALUES (" + emp_id + ", " + cli_id + ", " + shi_id +
               ", " + tra_id + ")";
    }
    
}
