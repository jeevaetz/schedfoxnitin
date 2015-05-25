/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class employee_list_types_query extends GeneralQueryFormat {

    public int employeeid;

    public employee_list_types_query() {
        
    }

    public void update(int employeeid) {
        this.employeeid = employeeid;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {

        return "SELECT * FROM f_employee_list_types_query(" + employeeid +");"; 
    }
}
