/*
 * employee_check_if_ssn_exists_query.java
 *
 * Created on May 23, 2005, 1:54 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class employee_check_if_ssn_exists_query extends GeneralQueryFormat {
    
    private String Social;
    
    /** Creates a new instance of employee_check_if_ssn_exists_query */
    public employee_check_if_ssn_exists_query() {
        myReturnString = new String();
    }
    
    public void update(String ssn) {
        Social = ssn;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return "SELECT * FROM f_employee_check_if_ssn_exists_query('" + Social + "');";
    }
          
    
}
