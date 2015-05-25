/*
 * ExportEmployeeInformationQuery.java
 *
 * Created on July 5, 2005, 9:26 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class ExportEmployeeInformationQuery extends GeneralQueryFormat {
    
    /** Creates a new instance of ExportEmployeeInformationQuery */
    public ExportEmployeeInformationQuery() {
        myReturnString = new String();
    }
    
    public void update() {
        
    }
    
    public String toString() {
        return "SELECT usked_emp_id as ueid, " +
                "employee.employee_id as eid, " +
                "employee.employee_first_name as fname, " +
                "employee.employee_last_name as lname, " +
                "employee.employee_middle_initial as mname, " +
                "employee.employee_ssn as ssn, " +
                "employee.employee_address as address, " +
                "employee.employee_address2 as address2, " +
                "employee.employee_city as city, " +
                "employee.employee_state as state, " +
                "employee.employee_zip as zip, " +
                "employee.employee_phone as phone, " +
                "employee.employee_phone2 as phone2, " +
                "employee.employee_cell as cell, " +
                "employee.employee_pager as pager, " +
                "employee.employee_email as email, " +
                "employee.employee_hire_date as hdate, " +
                "employee.employee_term_date as tdate, " +
                "employee.employee_is_deleted as isdel " +
                "FROM usked_employee LEFT JOIN employee ON " +
                "employee.employee_id = usked_employee.employee_id WHERE " +
                "employee.employee_id is not null";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
