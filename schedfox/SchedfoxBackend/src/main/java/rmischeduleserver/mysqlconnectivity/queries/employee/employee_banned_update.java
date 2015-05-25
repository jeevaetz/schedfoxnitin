/*
 * employee_banned_update.java
 *
 * Created on January 25, 2005, 9:16 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author ira
 */
public class employee_banned_update extends GeneralQueryFormat {
    
    private ArrayList EmployeeId;
    private String ClientId;
    private String isBanned;
    
    /** Creates a new instance of employee_banned_update */
    public employee_banned_update() {
        myReturnString = new String();
    }
    
    public void update(ArrayList employeeId, String clientId) {
        ClientId = clientId;
        EmployeeId = employeeId;
    }

    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        String employeeIdsStr = "'{";
        for (int e = 0; e < EmployeeId.size(); e++) {
            if (e > 0) {
                employeeIdsStr = employeeIdsStr + ",";
            }
            employeeIdsStr = employeeIdsStr + EmployeeId.get(e);
        }
        employeeIdsStr = employeeIdsStr + "}'::integer[]";
        return "SELECT f_employee_banned_update(" + ClientId + "," + employeeIdsStr + "," +
                EmployeeId.size() + ");"; 
    }
    
}
