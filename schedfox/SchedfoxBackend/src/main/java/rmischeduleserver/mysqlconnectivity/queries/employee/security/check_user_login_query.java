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
public class check_user_login_query extends GeneralQueryFormat {

    private Integer employeeId;
    private String newLoginName;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Integer employee_id, String login_name) {
        this.employeeId = employee_id;
        this.newLoginName = login_name;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT employee_id ");
        sql.append("FROM employee ");
        sql.append("WHERE ");
        sql.append("(   ");
        sql.append("    employee_login = '" + newLoginName + "' OR ");
        sql.append("    (employee_login = '' AND employee_ssn = '" + newLoginName + "')");
        sql.append(") ");
        if (this.employeeId != null) {
            sql.append("AND employee_id != " + employeeId);
        }
        return sql.toString();
    }

}
