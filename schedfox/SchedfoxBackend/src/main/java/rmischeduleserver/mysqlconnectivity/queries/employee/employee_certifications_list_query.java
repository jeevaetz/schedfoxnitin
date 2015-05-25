/*
 * employee_certifications_list_query.java
 *
 * Created on May 26, 2005, 11:48 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class employee_certifications_list_query extends GeneralQueryFormat {
    
    private String emp_id;
    private boolean showAll;
    
    /** Creates a new instance of employee_certifications_list_query */
    public employee_certifications_list_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String eid, boolean printAll) {
        emp_id = eid;
        showAll = printAll;
    }
    
    public String toString() {
        return "SELECT * FROM f_employee_certifications_list_query(" +
                emp_id + "," + showAll + "," + getBranch() + ");";
    }
    
}
