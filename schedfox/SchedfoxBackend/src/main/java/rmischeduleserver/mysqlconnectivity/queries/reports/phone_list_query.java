/*
 * phone_list_query.java
 *
 * Created on June 16, 2005, 10:47 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class phone_list_query extends GeneralQueryFormat {
    
    private String b, c;
    
    /** Creates a new instance of phone_list_query */
    public phone_list_query() {
        myReturnString = new String();
    }
    
    public void update(String branch, String company) {
        b = branch;
        c = company;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "Select employee.employee_last_name as lname," +
                "employee.employee_first_name as fname," +
                "employee.employee_phone as phone, " +
                "employee.employee_phone2 as phone2, " +
                "employee.employee_cell as cell, " +
                "employee.employee_pager as pager FROM " +
                "employee WHERE employee.branch_id = " + getBranch() + 
                " AND employee_is_deleted = 0 " +
                " ORDER by employee.employee_last_name";
    }
    
}
