/*
 * client_employees_banned_list_query.java
 *
 * Created on May 24, 2005, 9:55 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class client_employees_banned_list_query extends GeneralQueryFormat {
    
    private String cust_id;
    private boolean showAllEmps;
    
    /** Creates a new instance of client_employees_banned_list_query */
    public client_employees_banned_list_query() {
        myReturnString = new String();
    }
    
    public void update(String cid, boolean showAll) {
        cust_id = cid;
        showAllEmps = showAll;
    }
    
    public String toString() {
        StringBuilder myString = new StringBuilder();
        myString.append("Select employee.employee_id as eid, " +
                "employee.employee_last_name as fname," +
                "employee.employee_first_name as lname, " +
                "COALESCE(COALESCE (employee_phone, employee_phone2), employee_cell) as phone, " +
                "(CASE WHEN EXISTS (SELECT 1 FROM employee_banned WHERE " +
                "employee_banned.employee_id = employee.employee_id AND " +
                "employee_banned.client_id = " + cust_id + ") THEN 'true' ELSE 'false' END) as isBanned " +
                "FROM employee WHERE employee.branch_id = " + getBranch());
        if (!showAllEmps) {
            myString.append(" AND (EXISTS (SELECT 1 FROM employee_banned WHERE " +
                "employee_banned.employee_id = employee.employee_id AND " +
                "employee_banned.client_id = " + cust_id + "))");
        }
        myString.append(" Order by upper(employee.employee_last_name)");
        return myString.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
