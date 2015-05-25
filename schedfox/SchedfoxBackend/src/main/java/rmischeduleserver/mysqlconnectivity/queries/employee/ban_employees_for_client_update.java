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
public class ban_employees_for_client_update extends GeneralQueryFormat {
    
    private ArrayList EmployeeId;
    private String ClientId;
    private String isBanned;
    
    /** Creates a new instance of employee_banned_update */
    public ban_employees_for_client_update() {
        myReturnString = new String();
    }
    
    public void update(ArrayList employeeId, String clientId) {
        ClientId = clientId;
        EmployeeId = employeeId;
    }

    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_BANNED;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        String employeeids = "'{";
        for (int e = 0; e < EmployeeId.size(); e++) {
            if (e > 0) {
                employeeids = employeeids + ",";
            }
            employeeids = employeeids + EmployeeId.get(e);
        }
        employeeids = employeeids + "}'::integer[]";

        String clientId = "null";
        try {
            clientId = Integer.parseInt(ClientId) + "";
        } catch (Exception e) {}

        return "SELECT f_ban_employees_for_client_update(" + clientId + "," +
                employeeids + "," + EmployeeId.size() + ");";
    }
    
}
