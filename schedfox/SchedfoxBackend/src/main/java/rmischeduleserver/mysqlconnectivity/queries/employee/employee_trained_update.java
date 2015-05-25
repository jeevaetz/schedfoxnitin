/*
 * employee_trained_update.java
 *
 * Created on January 25, 2005, 8:04 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_trained_update extends GeneralQueryFormat {
    
    /** Creates a new instance of employee_trained_update */
    public employee_trained_update() {
        myReturnString = new String();
    }

    public void update(String EmployeeId, String ClientId, String isTrained) {

        myReturnString = "SELECT f_employee_trained_update(" + EmployeeId +
                ", " + ClientId + ", " + isTrained + ");";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return myReturnString;
    }
    
}
