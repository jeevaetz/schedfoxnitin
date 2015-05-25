/*
 * get_employee_notes_query.java
 *
 * Created on February 14, 2006, 2:23 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_employee_notes_query extends GeneralQueryFormat {
    
    private String eid;
    
    /** Creates a new instance of get_employee_notes_query */
    public get_employee_notes_query() {
    }
    
    public void update(String eid) {
        this.eid = eid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {

        return "SELECT * FROM f_get_employee_notes_query('" + eid + "');";
    }
}
