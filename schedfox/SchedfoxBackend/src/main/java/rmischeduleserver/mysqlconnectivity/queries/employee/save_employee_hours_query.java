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
public class save_employee_hours_query extends GeneralQueryFormat {

    private int employee_id;
    private int hoursV;
    private int hoursW;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int emp_id, int hoursForVacation, int hoursForWeek) {
        this.employee_id = emp_id;
        this.hoursV = hoursForVacation;
        this.hoursW = hoursForWeek;
    }

    public String toString() {

        return "SELECT f_save_employee_hours_query(" + hoursW + "," + hoursV + "," + employee_id + ");";
    }

}
