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
public class get_security_for_employees_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("employee_security_group_id, ");
        sql.append("employee_security_group_name, ");
        sql.append("employee_security_is_deleted ");
        sql.append("FROM employee_security_group");
        return sql.toString();
    }

}
