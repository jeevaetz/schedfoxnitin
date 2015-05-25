/*
 * new_assemble_complete_schedule_query.java
 *
 * Created on May 27, 2005, 1:06 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class assemble_schedule_for_export_query extends GeneralQueryFormat {
    private String ClientsToInclude;
    private String EmployeesToInclude;
    private String Sdate;
    private String Edate;
    private String Type;
    private String lu;
    private boolean ShowDeleted;
    private boolean showOpenEmps;
    private boolean isCheckInData;
    
    /** Creates a new instance of new_assemble_complete_schedule_query */
    public assemble_schedule_for_export_query() {
        myReturnString = new String();
        showOpenEmps = false;
    }
    
    public String toString() {
        String emps = "";
        String empsM = "";
        String date = " generate_date_series((CASE WHEN '" + Sdate + "' < date(NOW()) THEN date(NOW()) ELSE date '" + Sdate +"' END), " +
                      "  (CASE WHEN '" + Edate + "' < date(NOW()) THEN date(NOW()) ELSE date '" + Edate + "' END)) as s(a)";
        //if (showOpenEmps) {
            empsM = "OR (schedule_master.employee_id = 0 AND schedule_master_date_started <= a AND schedule_master_date_ended >= a ) ";
            emps =  "OR (schedule.employee_id = 0 AND (schedule_date between '" + Sdate +"' and '" + Edate +"') AND schedule_is_deleted = 0) ";
        //}
        
        if (isCheckInData) {
            date = "generate_date_series(Date(NOW()) - integer '1',  Date(NOW()) + integer '1') as s(a)";
        }
        return "( " +
                "  SELECT  " +
                "    schedule_master_start                  as start_time,  " +
                "    (text(schedule_master.schedule_master_id * -1) || '/' || a)    as sid, " +
                "    schedule_master.schedule_master_shift     as smid, " +
                "    schedule_master_end                    as end_time,  " +
                "    schedule_master_last_updated           as lu,  " +
                "    schedule_master_group                  as gp, " +
                "    schedule_master.employee_id            as eid,  " +
                "    schedule_master.client_id              as cid,  " +
                "    dow                                    as dow," +
                "    0                                      as type, " +
                "    schedule_master_date_started           as sdate,  " +
                "    schedule_master_date_ended             as edate,  " +
                "    a   as \"date\", " +
                "    '0'                                    as isDeleted,  " +
                "    client.client_name                     as cname, " +
                "    schedule_master_pay_opt              as pay_opt,  " +
                "    schedule_master_bill_opt             as bill_opt, " +
                "   schedule_master.rate_code_id          as rate_code_id, " +
                "   rate_code.usked_rate_code             as urc, " + 
                "    (employee.employee_last_name || ', ' || employee.employee_first_name)  as ename " +
                "  FROM   " +
                date  +
                "  LEFT JOIN " +
                "  schedule_master ON schedule_master_day = dow AND " +
                "  schedule_master_date_started <= a AND schedule_master_date_ended >= a " +
                "  LEFT JOIN " +
                "  client ON " +
                "  client.client_id = schedule_master.client_id " +
                "  LEFT JOIN " +
                "  employee ON " +
                "  employee.employee_id = schedule_master.employee_id " +
                "  LEFT JOIN " +
                "  rate_code ON " +
                "  rate_code.rate_code_id = schedule_master.rate_code_id " +
                "  WHERE " +
                "    (((schedule_master.employee_id in  " +
                "      (Select employee_id  " +
                "        From employee  " +
                "        Where  " +
                "          branch_id = " + getBranch() + " And " +
                generateDeletedSQL(true) + 
                "    ) " + empsM + ") And " +
                "    (schedule_master_id  not in  " +
                "      (Select schedule_master_id  " +
                "       From schedule  " +
                "       Where schedule_date = a and schedule.schedule_override > 0 " +
                "      ) " +
                "    ) " + generateToIncludeSQL(ClientsToInclude,   "schedule_master.client_id") +
                generateToIncludeSQL(EmployeesToInclude, "schedule_master.employee_id") + 
                generateLastUpdateSQL(lu, "schedule_master_last_updated") +
                //"  Order by client_id, employee.employee_id" +
                " AND client.branch_id = " + getBranch() + ")UNION( " +
                "  SELECT  " +
                "    schedule_start               as start_time,  " +
                "    text(schedule.schedule_id )  as sid, " +
                "    schedule_master_id           as smid, " +
                "    schedule_end                 as end_time,  " +
                "    schedule_last_updated        as lu,  "    +
                "    schedule_group               as gp, " +
                "    schedule.employee_id         as eid,  "   +
                "    schedule.client_id           as cid,  "   +
                "    schedule_day                 as dow,  "   +
                "    schedule_type                as type, "   +
                "    '1100-10-10'                 as sdate,  " +
                "    '2100-10-10'                 as edate,  " +
                "    schedule_date                as \"date\",  " +
                "    schedule_is_deleted          as isDeleted,  " +
                "    client.client_name                     as cname, " +
                "    schedule_pay_opt             as pay_opt,  " +
                "    schedule_bill_opt            as bill_opt, " +
                "   schedule.rate_code_id          as rate_code_id, " +
                "   rate_code.usked_rate_code             as urc, " + 
                "    (employee.employee_last_name || ', ' || employee.employee_first_name)  as ename " +
                "  FROM   " +
                "    schedule  " +
                "  LEFT JOIN " +
                "  client ON " +
                "  client.client_id = schedule.client_id " +
                "  LEFT JOIN " +
                "  employee ON " +
                "  employee.employee_id = schedule.employee_id " +
                "  LEFT JOIN " +
                "  rate_code ON " +
                "  rate_code.rate_code_id = schedule.rate_code_id " +
                "  WHERE " +
                "    (((schedule_date between '" + Sdate +"' and '" + Edate +"') And " + generateMoreDeletedSQL() +
                "    NOT (schedule_is_deleted = 1) And " +
                "    (schedule.employee_id in  " +
                "      (Select employee_id  " +
                "        From employee  " +
                "        Where  " +
                "          branch_id = " + getBranch() + " And " +
                generateDeletedSQL(false) +
                "   )  " + emps + generateToIncludeSQL(ClientsToInclude,   "schedule.client_id") +
                generateToIncludeSQL(EmployeesToInclude, "schedule.employee_id") + ")" +
                generateLastUpdateSQL(lu, "schedule_last_updated") +
                generateTypeSQL(Type) + //")" +
                " AND client.branch_id = " + getBranch() + ") Order by 15, 16 "
                ;
    }
    
    public void update(String clientsToInclude, String employeesToInclude, String sdate, String edate, String type, String lastupdated, boolean showDeleted) {
        ClientsToInclude = clientsToInclude;
        EmployeesToInclude = employeesToInclude;
        Sdate = sdate;
        Edate = edate;
        Type = type;
        lu = lastupdated;
        ShowDeleted = showDeleted;
    }
    
    /**
     * Used by our hearbeat...
     */
    public void updateTime(String LU) {
        lu = LU;
    }
    
    public void showOpenShifts() {
        showOpenEmps = true;
    }
    
    public void isCheckInData() {
        isCheckInData = true;
    }
    
    private String generateDeletedSQL(boolean isMaster) {
        String date = " schedule_date ";
        if (isMaster) {
            date = " a ";
        }
        return " ((employee_is_deleted = 0 OR (employee_is_deleted = 1 AND employee_term_date >= " + date + ")) AND client_is_deleted = 0))) ";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * Generates Type SQL
     */
    private String generateTypeSQL(String type) {
        if (type.length() > 0) {
            return " AND ( schedule_type = " + type  + ") ";
        }
        return " ";
    }
    
    /**
     * Generates Type SQL
     */
    private String generateLastUpdateSQL(String dt, String fieldToCheck) {
        if (dt.length() > 0) {
            return " AND ( " + fieldToCheck + " >= '" + dt  + "') ";
        }
        return "";
    }
    
    /**
     * Takes comma delimited List and generates SQL for it....fun fun
     */
    private String generateToIncludeSQL(String listToInclude, String fieldToCheck) {
        String returnString = "";
        if (listToInclude.length() > 0) {
            returnString = " AND " + fieldToCheck + " IN (" + listToInclude + ") ";
        }
        return returnString;
    }    
    
    private String generateMoreDeletedSQL() {
        if (!ShowDeleted) {
            return " NOT (schedule_is_deleted = 1 AND" +
                   "    schedule_override = 1) AND ";
        } else {
            return " ";
        }
    }
}
