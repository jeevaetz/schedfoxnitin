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
public class employee_to_client_query extends GeneralQueryFormat {

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

        return "SELECT * FROM f_employee_to_client_query(" + this.employee_id +");"; 
    }

}
