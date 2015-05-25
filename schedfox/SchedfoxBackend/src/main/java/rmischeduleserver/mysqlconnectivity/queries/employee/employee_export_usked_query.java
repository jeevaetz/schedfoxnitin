/*
 * employee_export_usked_query.java
 *
 * Created on February 14, 2005, 8:15 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_export_usked_query extends GeneralQueryFormat {

    private String eid;
    
    /** Creates a new instance of employee_export_usked_query */
    public employee_export_usked_query() {
        myReturnString = new String();
    }
    
    public void update(String Eid) {
        eid = Eid;
    }
    
    public String toString() {
        return "SELECT * FROM f_employee_export_usked_query(" + eid + ");";
    }
    
    public boolean hasAccess() {
        return true;
    }
    

    
}
