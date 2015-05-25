/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee.security;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_security_group_for_employee_query extends GeneralQueryFormat {

    private String employee_id;
    private String sec_group;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String employee_id, String sec_group) {
        this.employee_id = employee_id;
        this.sec_group = sec_group;
    }

    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("DELETE FROM ");
        sql.append("employee_to_security_groups WHERE employee_id = " + employee_id + ";");
        sql.append("INSERT INTO ");
        sql.append("employee_to_security_groups ");
        sql.append("(employee_id,security_group_id) VALUES ");
        sql.append("(" + this.employee_id + "," + this.sec_group + ")");
        return sql.toString();
    }

}
