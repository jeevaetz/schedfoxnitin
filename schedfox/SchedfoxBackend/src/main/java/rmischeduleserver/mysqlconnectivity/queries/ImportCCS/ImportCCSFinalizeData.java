/*
 * ImportCCSFinalizeData.java
 *
 * Created on January 27, 2005, 12:58 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ImportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class ImportCCSFinalizeData extends GeneralQueryFormat{
    
    /** Creates a new instance of ImportCCSFinalizeData */
    public ImportCCSFinalizeData() {
        myReturnString = new String();
    }
    
    public String toString() {
        return "UPDATE schedule SET schedule_day = 7 WHERE schedule_day = 0;" +
                "INSERT INTO schedule_master (client_id, employee_id, schedule_master_day, schedule_master_start," +
                "schedule_master_end, schedule_master_date_started, schedule_master_date_ended, schedule_master_group, " +
                "schedule_master_shift) SELECT schedule.client_id as cid, schedule.employee_id, schedule.schedule_day, " +
                "schedule.schedule_start, schedule.schedule_end, schedule.schedule_date + 1, '2100-01-01', 0, 0 FROM schedule " +
                "LEFT JOIN client ON client.client_id = schedule.client_id " +
                "WHERE schedule.schedule_date > (CASE WHEN ((SELECT max(schedule_date) FROM schedule WHERE schedule.client_id = client.client_id AND " +
                "client.branch_id = " + getBranch() + ") - INTERVAL '7 DAY') > (Date(NOW()) - integer '10') THEN " +
                "((SELECT max(schedule_date) FROM schedule WHERE schedule.client_id = client.client_id AND " +
                "client.branch_id = " + getBranch() + ") - INTERVAL '7 DAY') ELSE (Date(NOW()) - integer '10') END) " +
                "AND schedule.schedule_date < (CASE WHEN ((SELECT max(schedule_date) FROM schedule WHERE schedule.client_id = client.client_id AND " +
                "client.branch_id = " + getBranch() + ") + INTERVAL '1 DAY') > (Date(NOW()) - integer '10') THEN " +
                "((SELECT max(schedule_date) FROM schedule WHERE schedule.client_id = client.client_id AND " +
                "client.branch_id = " + getBranch() + ") + INTERVAL '1 DAY') ELSE (Date(NOW()) - integer '10') END);" +
                "UPDATE employee SET employee_is_deleted = 1 WHERE employee_id NOT IN (SELECT DISTINCT employee_id FROM schedule_master) AND " +
                "employee_id NOT IN (SELECT DISTINCT employee_id FROM schedule WHERE schedule_date > date(NOW()) - integer '30');" +
                "UPDATE client SET client_is_deleted = 1 WHERE client_id NOT IN (SELECT DISTINCT client_id FROM schedule_master) AND " +
                "client_id NOT IN (SELECT DISTINCT client_id FROM schedule WHERE schedule_date > date(NOW()) - integer '30');";
    }
    
    public boolean hasAccess() {
        return true;
    }    
}
