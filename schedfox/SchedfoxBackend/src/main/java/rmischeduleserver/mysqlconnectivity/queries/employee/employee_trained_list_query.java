/*
 * employee_trained_list_query.java
 *
 * Created on January 24, 2005, 3:33 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_trained_list_query extends GeneralQueryFormat {
    
    private String EID;
    
    /** Creates a new instance of employee_trained_list_query */
    public employee_trained_list_query() {
        myReturnString = new String();
    }
    
    public void update(String employeeId) {
        EID = employeeId;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {

        return "SELECT * FROM f_employee_trained_list_query(" + EID + "," + getBranch() + ");"; 
    }
    
}
