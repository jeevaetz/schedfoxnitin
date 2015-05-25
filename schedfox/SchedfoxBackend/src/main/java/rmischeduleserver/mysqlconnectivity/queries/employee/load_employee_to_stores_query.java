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
public class load_employee_to_stores_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public String toString() {
        return "SELECT * FROM f_load_employee_to_stores_query();";
    }

}
