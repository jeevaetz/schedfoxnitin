/*
 * over_under_report_query.java
 *
 * Created on August 31, 2005, 2:24 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
/**
 *
 * @author Ira Juneau
 */
public class over_under_report_query extends generic_assemble_schedule_query {
    
    private String Sdate;
    private boolean showOvertimeOnly = false;
    
    /** Creates a new instance of over_under_report_query */
    public over_under_report_query() {
        myReturnString = new String();
        super.setSelectedFields(super.myOverUnderReportFields);
    }
    
    public void setOvertimeOnly(boolean showOvertimeOnly) {
        this.showOvertimeOnly = showOvertimeOnly;
    }
    
    public void update(String sdate, String edate) {
        super.update("", "", sdate,edate, "", "", false);
        Sdate = sdate;
        super.ShowDeleted = false;
    }
    
    protected String getFinalSelectFields() {
        return " eid, ename, phone, days_employed ";
    }
    
    public String additionalFields() {
        return "(SUM ((CASE WHEN end_time < start_time THEN end_time + 1440 ELSE end_time END) - start_time)) ";
    }
    
    /**
     * Overload to do funky things like a join on this or so on...
     */
    @Override
    protected String getFinalSelectStatement() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("employee_phone as phone, (" + additionalFields() + " - (40 * 60)) as overtime, ");
        sql.append("employee_id as eid, (date_part('day', NOW() - employee.employee_hire_date)) as days_employed, ");
        sql.append("(CASE WHEN (" + additionalFields() + ") IS NULL THEN 0 ELSE (" + additionalFields() +  ") END) as total_time, ");
        sql.append("(employee.employee_last_name || ', ' || employee.employee_first_name) as ename ");
        sql.append("FROM employee ");
        sql.append("LEFT JOIN assemble_schedule(DATE('" + Sdate + "'), DATE('" + Edate + "'), " + this.getBranch() + ") ON assemble_schedule.eid = employee.employee_id ");
        sql.append(generateCompleteWhereClause());
        sql.append(generateOrderBy() + ";");
        return sql.toString();

    }
    
    public String orderBy() {
        return " (SUM ((CASE WHEN end_time < start_time THEN end_time + 1440 ELSE end_time END) - start_time)) ";
    }
    
    public String generateOrderBy() {
        String retVal = " WHERE employee.branch_id = " + getBranch() + " AND (isdeleted = '0' OR isdeleted IS NULL) AND employee_is_deleted = 0 AND ((\"date\" < '" + Edate + "' AND \"date\" >= '" + Sdate + "') OR \"date\" IS NULL) ";
        retVal += " GROUP BY employee.employee_id, employee_last_name, employee_first_name, employee_phone, days_employed, employee_hire_date  ";
        if (showOvertimeOnly) {
            retVal += " HAVING (" + additionalFields() + " - (40 * 60)) > 0 ";
        }
        return retVal + " ORDER BY (SUM ((CASE WHEN end_time < start_time THEN end_time + 1440 ELSE end_time END) - start_time)) ";
    }
}
