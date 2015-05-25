/*
 * employee_note_list_query.java
 *
 * Created on January 26, 2005, 4:03 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author jason.allen
 */
public class employee_note_list_query  extends GeneralQueryFormat{
    
    private String eid;
    
    /** Creates a new instance of employee_note_list_query */
    public employee_note_list_query() {
        myReturnString = "";
    }
    
    public void update(String Eid){
        eid = Eid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {

        return "SELECT * FROM f_employee_note_list_query(" + eid + ");"; 
    }
}
