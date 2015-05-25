/*
 * employee_delete_query.java
 *
 * Created on January 26, 2005, 1:26 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_delete_query extends GeneralQueryFormat {
    
    private String empId;
    private String isDeleted;
    private String tdate;
    
    /** Creates a new instance of employee_delete_query */
    public employee_delete_query() {
        myReturnString = new String();        
    }
    
    
    public int getUpdateStatus(){
        return UPDATE_SCHEDULE;
    }

    public String toString() {
        return "SELECT f_employee_delete_query(" + empId + ",'" + tdate + "'::date," + isDeleted.equals("0") + ");";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String IsDeleted, String EmpId, String termDate) {
        empId     = EmpId;
        isDeleted = IsDeleted;
        tdate     = termDate;
    }
    
}
