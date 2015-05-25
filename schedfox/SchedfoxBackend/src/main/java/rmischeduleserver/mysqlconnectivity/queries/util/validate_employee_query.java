/*
 * validate_employee_query.java
 *
 * Created on February 23, 2006, 9:56 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author Ira Juneau
 */
public class validate_employee_query extends GeneralQueryFormat {
    
    private String lName;
    private String pw;
    
    /** Creates a new instance of validate_employee_query */
    public validate_employee_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String loginName, String pw) {
        lName = loginName;
        this.pw = pw;
    }
    
    public String toString() {
        return "set local search_path = 'control_db'; SELECT * FROM validate_employee('" + lName + "','" + pw + "');";
    }
    
}
