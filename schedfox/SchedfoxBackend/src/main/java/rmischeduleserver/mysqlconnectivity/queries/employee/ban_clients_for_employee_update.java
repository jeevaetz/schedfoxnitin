/*
 * employee_banned_update.java
 *
 * Created on January 25, 2005, 9:16 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 * @author ira
 */
public class ban_clients_for_employee_update extends GeneralQueryFormat {
    
    private ArrayList ClientId;
    private String EmployeeId;
    private String isBanned;
    
    /** Creates a new instance of employee_banned_update */
    public ban_clients_for_employee_update() {
        myReturnString = new String();
    }
    
    public void update(ArrayList CliId, String EmpId) {
        EmployeeId = EmpId;
        ClientId = CliId;
    }

    public int getUpdateStatus() { 
        return GeneralQueryFormat.UPDATE_BANNED;                 
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        String clientIdsStr = "'{";
        for (int e = 0; e < ClientId.size(); e++) {
            if (e > 0) {
                clientIdsStr = clientIdsStr + ",";
            }
            clientIdsStr = clientIdsStr + ClientId.get(e);
        }
        clientIdsStr = clientIdsStr + "}'::integer[]";

        String emps = "null";
        try {
            emps = Integer.parseInt(EmployeeId) + "";
        } catch (Exception e) {}

        return "SELECT f_ban_clients_for_employee_update(" + emps + "," +  clientIdsStr + "," + ClientId.size() + ");";
    }
    
}
