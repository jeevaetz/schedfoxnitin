/*
 * ExportEmployeeInfoQuery.java
 *
 * Created on June 28, 2005, 9:25 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.ExportCCS;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class ExportEmployeeInfoQuery extends GeneralQueryFormat {
    
    /** Creates a new instance of ExportEmployeeInfoQuery */
    public ExportEmployeeInfoQuery() {
        myReturnString = new String();
    }
 
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  "SELECT usked_emp_id as ueid, " +
                "employee_id as eid FROM " +
                "usked_employee;";
    }
    
}
