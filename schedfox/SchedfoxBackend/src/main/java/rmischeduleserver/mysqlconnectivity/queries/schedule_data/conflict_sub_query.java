/*
 * conflict_query.java
 *
 * Created on April 8, 2005, 9:01 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class conflict_sub_query extends GeneralQueryFormat {
    
    private String sdate;
    private String edate;
    private String branch;
    private String empId = "";
    
    /** Creates a new instance of conflict_query */
    public conflict_sub_query() {
        myReturnString = new String();
    }
    
    public void update(String Sdate, String Edate, String Branch) {
        sdate = Sdate;
        edate = Edate;
        branch = Branch;
    }
    
    public void update(String Sdate, String Edate, String Branch, String emp) {
        update(Sdate, Edate, Branch);
        empId = " AND employee.employee_id = " + emp + " ";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String getSchedule() {
        return  "Select dow, " +
                "doy, " +
                "schedule.schedule_master_id as schedule_master_id, " +
                "schedule.schedule_id as schedule_id, " +
                "schedule.schedule_override as schedule_override, " +
                "client.client_id, " +
                "client.client_name, " +
                "schedule.employee_id as emp_id, " +
                "schedule.schedule_start as stime, " +
                "schedule.schedule_end as etime " +
                "FROM generate_date_series(date '" + sdate + "', date '" + edate + "')" +
                "JOIN schedule on schedule.schedule_date = doy " +
                "LEFT JOIN client on client.client_id = schedule.client_id " +
                " WHERE " + 
                "schedule.schedule_is_deleted = 0 " + empId;
    }
    
    public String getMasterSchedule() {
        return  "Select dow, " +
                "doy, " +
                "schedule_master.schedule_master_id as schedule_master_id, " +
                "0 as schedule_id, " +
                "0 as schedule_override, " +
                "client.client_id, " +
                "client.client_name, " +
                "schedule_master.employee_id as emp_id, " +
                "schedule_master.schedule_master_start as stime, " +
                "schedule_master.schedule_master_end as etime " +
                "FROM generate_date_series(date '" + sdate + "', date '" + edate + "') " +
                "JOIN schedule_master on  schedule_master_date_started <= doy " +
                "and schedule_master_date_ended > doy " +
                "and schedule_master_day = dow " +
                "And " +
                "    (schedule_master_id  not in  " +
                "      (Select schedule_master_id  " +
                "       From schedule  " +
                "       Where schedule_date = doy and schedule.schedule_override > 0 " +
                "      ) " +
                "    ) " +
                "LEFT JOIN client " +
                "on client.client_id = schedule_master.client_id WHERE " +
                "f_msched_overide(doy, schedule_master_id) = false "+ empId;
    }
    
    public String toString() {
        return "SELECT CONCAT(sched.dow, msched.dow) as dow," +
                "CONCAT(sched.stime, msched.stime) as stime," +
                "CONCAT(sched.etime, msched.etime) as etime," +
                "MAX(sched.schedule_id, msched.schedule_id) as schedule_id, " +
                "MAX(sched.schedule_master_id, msched.schedule_master_id) as schedule_master_id," +
                "CONCATCHAR(sched.client_name, msched.client_name) as client_name, " +
                "CONCATDATE(sched.doy, msched.doy) as doy, " +
                "CONCAT(sched.emp_id, msched.emp_id) as emp_id " +
                " FROM( " + getSchedule() + ") as sched FULL OUTER JOIN (" + 
                getMasterSchedule() + ") as msched on sched.doy = msched.doy AND " +
                "sched.schedule_master_id = msched.schedule_master_id";
    }
    
}
