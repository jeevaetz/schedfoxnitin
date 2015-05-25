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
public class grab_employee_target_hours_query extends GeneralQueryFormat {

    private String employee_id;

    @Override
    public boolean hasAccess() {
        String co = this.getCompany();
        return true;
    }

    public void update(String emp_id) {
        this.employee_id = emp_id;
    }

    public String toString() {
        return "SELECT * FROM f_grab_employee_target_hours_query(" + employee_id + ");";
    }
}
