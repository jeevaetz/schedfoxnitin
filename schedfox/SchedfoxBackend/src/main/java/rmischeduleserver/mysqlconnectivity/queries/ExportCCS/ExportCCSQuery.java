/*
 * ExportCCSQuery.java
 *
 * Created on January 19, 2005, 8:22 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class ExportCCSQuery extends GeneralQueryFormat{

    private static final String MY_QUERY = (
            "SELECT " +
            "schedule.schedule_id, " +
            "schedule.client_id as cid, " +
            "schedule.employee_id as eid, " +
            "schedule.schedule_start as start, " +
            "schedule.schedule_end as end,  " +
            "usked_employee.usked_emp_id as ueid, " +
            "usked_client.usked_cli_id as ucid, " +
            "schedule.schedule_date + 1 as date, " +
            "client.client_id, " +
            "client.client_name as cname, " +
            "usked_client.usked_ws_id as cwrk, " +
            "employee.employee_id, " +
            "employee.employee_last_name as ename " +
            "from schedule " +
            "LEFT JOIN " +
            "employee ON " +
            "employee.employee_id = schedule.employee_id " +
            "LEFT JOIN " +
            "client ON " +
            "client.client_id = schedule.client_id " +
            "LEFT JOIN " +
            "usked_client ON " +
            "usked_client.client_id = schedule.client_id " +
            "LEFT JOIN " +
            "usked_employee ON " +
            "usked_employee.employee_id = schedule.employee_id " +
            "WHERE " +
            "schedule.schedule_date >=");
    
    private String startDate;
    private String endDate;
    
    /** Creates a new instance of ExportCCSQuery */
    public ExportCCSQuery()  {
        myReturnString = new String();
    }
    
    public void update(String startdate, String enddate) {
        startDate = startdate;
        endDate = enddate;
    }
    
    public String toString() {
        return MY_QUERY + " '" + startDate + "' AND schedule.schedule_date <= '" + endDate +"' AND schedule.schedule_is_deleted = 0 AND client.client_is_deleted = 0";
    }
    
    public boolean hasAccess() {
        return true;
    }

    
}
