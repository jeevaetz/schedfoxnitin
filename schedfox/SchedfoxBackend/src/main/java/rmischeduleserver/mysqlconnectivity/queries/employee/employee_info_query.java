/*
 * employee_info_query.java
 *
 * Created on January 24, 2005, 2:49 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_info_query extends GeneralQueryFormat {
    
    private String EmployeeId;
    private String deleted;
    
    /** Creates a new instance of employee_info_query */
    public employee_info_query() {
        myReturnString = new String();
    }

    @Override
    public String toString() {
        return "SELECT * FROM f_employee_info_query(" + EmployeeId + "," + (deleted.length() > 0) + ");";
    }
    
    public void update(String employeeId, String Deleted) {
        EmployeeId = employeeId;
        deleted = Deleted;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
