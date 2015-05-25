/*
 * active_employees_query.java
 *
 * Created on October 6, 2005, 3:22 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class active_employees_query extends GeneralQueryFormat {
     
    private boolean fetchWeeklyGross;
    private boolean fetchYearGross;
    
    /** Creates a new instance of active_employees_query */
    public active_employees_query() {

    }
    
    public void update(boolean fetchWeeklyGross, boolean fetchYearGross) {
        this.fetchWeeklyGross = fetchWeeklyGross;
        this.fetchYearGross = fetchYearGross;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("employee.employee_last_name as lname, employee.employee_first_name as fname, employee.employee_ssn as ssn, ");
        sql.append("employee.employee_address as address1, employee.employee_address2 as address2, employee.employee_city as city, ");
        sql.append("employee.employee_birthdate as dob, employee.gender, employee.employee_state as state, employee.employee_zip as zip, ");
        sql.append("employee.employee_phone as phone1, employee.employee_phone2 as phone2, employee.employee_cell as cell, ");
        sql.append("employee.employee_pager as pager, employee.employee_email as email, employee.employee_hire_date as hire, ");
        sql.append("employee.employee_term_date as term ");
        if (fetchWeeklyGross) {
            sql.append(", (SELECT gross_pay FROM employee_payments WHERE employee_payments.employee_id = employee.employee_id AND date_of_trans < DATE(?) ORDER BY date_of_trans DESC LIMIT 1) as week_gross ");
        } else {
            sql.append(", ? as mydate ");
        }
        if (fetchYearGross) {
            sql.append(", (SELECT SUM(COALESCE(gross_pay, 0)) FROM employee_payments WHERE employee_payments.employee_id = employee.employee_id AND date_of_trans BETWEEN NOW() - interval '1 year' AND NOW()) as year_gross ");
        }
        sql.append("FROM employee ");
        sql.append("WHERE ");
        sql.append("(employee.branch_id = ? OR ? = -1) AND (");
        sql.append("employee.employee_is_deleted = 0 OR ");
        sql.append("(employee.employee_is_deleted = 1 AND ");
        sql.append("employee_term_date > (DATE(?) - 15)) AND ");
        sql.append("employee_term_date < (DATE(?) + 100)) ");
        sql.append("ORDER by employee.employee_last_name");
        return sql.toString();
    }
    
}
