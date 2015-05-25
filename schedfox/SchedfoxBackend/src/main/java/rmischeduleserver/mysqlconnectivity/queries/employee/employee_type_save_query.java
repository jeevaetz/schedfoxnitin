/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class employee_type_save_query extends GeneralQueryFormat {

    private int employee_id;
    private ArrayList<Integer> types;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int employee_id, ArrayList<Integer> types) {
        this.employee_id = employee_id;
        this.types = types;
    }

    @Override
    public String toString() {
        String employeeTypesStr = "'{";
        for (int e = 0; e < types.size(); e++) {
            if (e > 0) {
                employeeTypesStr = employeeTypesStr + ",";
            }
            employeeTypesStr = employeeTypesStr + types.get(e);
        }
        employeeTypesStr = employeeTypesStr + "}'::integer[]";
        return "SELECT f_employee_type_save_query(" + this.employee_id + "," + employeeTypesStr + "," +
                types.size() + ");"; 
    }

}
