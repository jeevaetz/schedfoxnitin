/*
 * new_assemble_complete_schedule_query.java
 *
 * Created on May 27, 2005, 1:06 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 * 
 * Ok first of all this is the main query for our entire scheduling program in reality...you will notice one hell
 * of alot of protected methods here, that is so you can create a sub class of this class, and override these methods 
 * to allow it to return just about anything you could hope for based off of the schedule, for example you can override the
 * getMasterEmpString() to return only temp shifts by putting a return " schedule_master.employee_id < 0 " which will 
 * always be false so only temp schedules are returned...
 */
public class new_assemble_complete_schedule_query extends GeneralQueryFormat {
    private String ClientsToInclude;
    private String EmployeesToInclude;
    public String Sdate;
    public String Edate;
    private String Type;
    private String lu;
    protected boolean ShowDeleted;
    private boolean showOpenEmps;
    protected String emps = "";
    protected String empsM = "";
    
    /** Creates a new instance of new_assemble_complete_schedule_query */
    public new_assemble_complete_schedule_query() {
        myReturnString = new String();
        showOpenEmps = false;
    }
    
    /**
     * Out update method
     */
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
    
    /**
     * Overload to change what fields we want to sort by
     */
    protected String orderBy() {
        return " Order by 18, 19";
    }
    
    /**
     * Overload with return "> 0"; if you want to return info for all branches like in the checkin
     */
    protected String generateBranch() {
        return " = " + getBranch() + " ";
    }
    
    /**
     * Overload in case you want to return even masters that are overloaded...
     */
    protected String genOverwriteField() {
        return "And " +
                "    (schedule_master_id not in  " +
                "      (Select schedule_master_id  " +
                "       From schedule  " +
                "       Where schedule_date = a and schedule.schedule_override > 0 " +
                "      ) " +
                "    ) ";
    }
    
    protected String getMasterEmpString() {
        if (showOpenEmps) {
            return "OR (schedule_master.employee_id = 0 AND cli.branch_id " + generateBranch() + ") ";
        }
        return "";
    }
    
    /**
     * 
     */
    protected String getTempEmpString() {
        if (showOpenEmps) {
            return "OR (schedule.employee_id = 0 AND cli.branch_id " + generateBranch() + ") ";
        }
        return "";
    }
    
    /**
     * Overload if you want to change what the join is on...shouldn't be touched for most things...
     */
    protected String getMasterJoinDateString() {
        return "";
    }
    
    protected String getTempJoinDateString() {
        return "  WHERE schedule_date between '" + Sdate +"' and '" + Edate +"' AND ";
    }
    
    /**
     * Defines what fields to return overload to change what fields you want returned from the 
     * master schedule table, remember must have same amount of fields in both temp and master...
     */
    protected String getMasterFields() {
        return  "    schedule_master_start                  as start_time,  " +
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
                "   (CASE WHEN (a < schedule_master_date_started OR a > schedule_master_date_ended) THEN 1 ELSE 0 END) as isDeleted,  " +
                "   schedule_master.schedule_master_pay_opt  as pay_opt,  " +
                "   schedule_master.schedule_master_bill_opt as bill_opt, " +
                "   schedule_master.rate_code_id             as rate_code_id, " +
                "   (CASE WHEN client.client_name is NULL THEN cli.client_name ELSE " +
                "   client.client_name || ' ' || cli.client_name END)as cname, " +
                "    (employee.employee_last_name || ', ' || employee.employee_first_name)  as ename, " +
                "    0                                      as trainerid," +
                "    '' as trainer ";
    }
    
    /**
     * Defines what fields to return overload to change what fields you want returned from the 
     * schedule table, remember must have same amount of fields in both temp and master...
     */
    protected String getTempFields() {
        return  "    schedule_start               as start_time,  " +
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
                "    schedule_pay_opt             as pay_opt,  " +
                "    schedule_bill_opt            as bill_opt, " +
                "   schedule.rate_code_id          as rate_code_id, " +
                "   (CASE WHEN client.client_name is NULL THEN cli.client_name ELSE " +
                "   client.client_name || ' ' || cli.client_name END)as cname, " +
                "    (employee.employee_last_name || ', ' || employee.employee_first_name)  as ename, " +
                "    employee_trained.training_employee_id as trainerid, " +
                "   (SELECT (employee.employee_last_name || ', ' || employee.employee_first_name) FROM employee WHERE" +
                "   employee.employee_id = employee_trained.training_employee_id) as trainer ";
    }
    
    public String toString() {
        String date = " generate_date_series((CASE WHEN '" + Sdate + "' < date(NOW()) THEN date(NOW()) ELSE date '" + Sdate +"' END), " +
                      "  (CASE WHEN '" + Edate + "' < date(NOW()) THEN date(NOW()) ELSE date '" + Edate + "' END)) as s(a) ";

        empsM = getMasterEmpString();
        emps = getTempEmpString();
        
        return "( " +
                "  SELECT  " +
                getMasterFields() +
                "  FROM   " +
                date  +
                "  LEFT JOIN " +
                "  schedule_master ON schedule_master_day = dow AND " +
                "  schedule_master_date_ended > '" + Sdate + "' AND schedule_master_date_started <= '" + Edate + "' " +
                "  LEFT JOIN " +
                "  client as cli ON " +
                "  cli.client_id = schedule_master.client_id " +
                "  LEFT JOIN " +
                "  client ON " +
                "  client.client_id = cli.client_worksite " +
                "  LEFT JOIN " + 
                "  employee ON " + 
                "  employee.employee_id = schedule_master.employee_id " +
                "  WHERE " + getMasterJoinDateString() +
                "    (((schedule_master.employee_id in  " + 
                "      (Select employee_id  " + 
                "        From employee  " + 
                "        Where  " + 
                "          branch_id " + generateBranch() + " And " + 
                generateDeletedSQL(ShowDeleted) + 
                "    ) "  + empsM + ")  " + genOverwriteField() + generateToIncludeSQL(ClientsToInclude,   "schedule_master.client_id") +
                generateToIncludeSQL(EmployeesToInclude, "schedule_master.employee_id") + generateOtherMasterSQL() +
                generateLastUpdateSQL(lu, "schedule_master_last_updated") + groupBy() +
                ")UNION( " +
                "  SELECT  " +
                getTempFields() +
                "  FROM   " +
                "    schedule  " +
                "  LEFT JOIN " +
                "  client as cli ON " +
                "  cli.client_id = schedule.client_id " +
                "  LEFT JOIN " +
                "  client ON " +
                "  client.client_id = cli.client_worksite " +
                "  LEFT JOIN " +
                "  employee ON " +
                "  employee.employee_id = schedule.employee_id " +
                "  LEFT JOIN " +
                "  employee_trained ON employee_trained.schedule_id = schedule.schedule_id " +
                getTempJoinDateString() +
                "    (( " + generateMoreDeletedSQL() +
                "    (schedule.employee_id in  " +
                "      (Select employee_id  " +
                "        From employee  " +
                "        Where  " +
                "          branch_id " + generateBranch() + " And " +
                generateDeletedSQL(ShowDeleted) +
                "   )  " + emps  + ")" + generateTypeSQL(Type) + generateToIncludeSQL(ClientsToInclude,   "schedule.client_id") +
                generateToIncludeSQL(EmployeesToInclude, "schedule.employee_id") + 
                generateLastUpdateSQL(lu, "schedule_last_updated") + groupBy() +
                ") " + this.orderBy();
    }
    
//    public String toString() {
//        
//    }
//    
    protected String groupBy() {
        return "";
    }
    
    protected String generateOtherMasterSQL() {
        return "";
    }
    

    
    public void showOpenShifts() {
        showOpenEmps = true;
    }
    
    protected String generateDeletedSQL(boolean showDeleted) {
        return " employee_is_deleted = 0 OR employee_is_deleted = 1)) ";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * Generates Type SQL
     */
    protected String generateTypeSQL(String type) {
        if (type.length() > 0) {
            return " AND ( schedule_type = " + type  + ") ";
        }
        return " ";
    }
    
    /**
     * Generates Last Updated SQL, is protected so child classes can use to modify this class behaviour
     */
    protected String generateLastUpdateSQL(String dt, String fieldToCheck) {
        if (dt.length() > 0) {
            return " AND ( " + fieldToCheck + " >= '" + dt  + "') ";
        }
        return "";
    }
    
    /**
     * Takes comma delimited List and generates SQL for it....fun fun
     */
    protected String generateToIncludeSQL(String listToInclude, String fieldToCheck) {
        String returnString = "";
        if (listToInclude.length() > 0) {
            returnString = " AND " + fieldToCheck + " IN (" + listToInclude + ") ";
        }
        return returnString;
    }    
    
    protected String generateMoreDeletedSQL() {
        return " ";
    }
}
