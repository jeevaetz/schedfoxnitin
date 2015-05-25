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
public class grab_security_for_employee_query extends GeneralQueryFormat {

    private int employee_id;

    public grab_security_for_employee_query() {

    }

    public void update(int employee_id) {
        this.employee_id = employee_id;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("employee_security_group.employee_security_group_id as id, ");
        sql.append("employee_security_group.employee_security_group_name as name, ");
        sql.append("(CASE WHEN employee_to_security_groups.security_group_id IS NULL THEN false ELSE true END) as isselected ");
        sql.append("FROM ");
        sql.append("employee_security_group ");
        sql.append("LEFT JOIN employee_to_security_groups ON ");
        sql.append("employee_to_security_groups.security_group_id = employee_security_group.employee_security_group_id AND ");
        sql.append("employee_to_security_groups.employee_id = " + employee_id + " ");
        sql.append("WHERE ");
        sql.append("employee_security_is_deleted != true");
        return sql.toString();
    }

}
