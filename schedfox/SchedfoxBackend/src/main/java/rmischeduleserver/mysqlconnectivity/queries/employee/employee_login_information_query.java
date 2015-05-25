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
public class employee_login_information_query extends GeneralQueryFormat {

    private int employee_id;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int employee_id) {
        this.employee_id = employee_id;
    }

    @Override
    public String toString() {

        return "SELECT * FROM f_employee_login_information_query(" + this.Company + ", " + employee_id + ");"; 
    }
}
