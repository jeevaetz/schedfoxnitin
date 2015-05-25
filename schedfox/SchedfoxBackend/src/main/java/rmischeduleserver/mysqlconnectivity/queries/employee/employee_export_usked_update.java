/*
 * employee_export_usked_update.java
 *
 * Created on February 14, 2005, 8:27 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_export_usked_update extends GeneralQueryFormat {

    private String eid;
    private String usked_id;
    
    /** Creates a new instance of employee_export_usked_update */
    public employee_export_usked_update() {
        myReturnString = new String();
    }
    
    public void update(String Usked_id, String Eid) {
        eid = Eid;
        usked_id = Usked_id;
    }
    
    public String toString() {
        return "SELECT f_employee_export_usked_update(" + eid + ",'" + usked_id + "'," + getBranch() + ");";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
