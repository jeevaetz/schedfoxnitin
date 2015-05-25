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
public class get_security_settings_for_emp_query extends GeneralQueryFormat {

    private String employee_id;
    private String corp_db;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String emp, String corp_db) {
        this.employee_id = emp;
        this.corp_db = corp_db;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT employee_security_group_settings.* ");
        sql.append("FROM " + corp_db + ".employee_security_group ");
        sql.append("INNER JOIN " + corp_db + ".employee_to_security_groups ON ");
        sql.append("    employee_security_group.employee_security_group_id = employee_to_security_groups.security_group_id ");
        sql.append("INNER JOIN " + corp_db + ".employee_security_group_settings ON ");
        sql.append("    employee_security_group_settings.employee_security_group_id = employee_security_group.employee_security_group_id ");
        sql.append("WHERE ");
        sql.append("employee_to_security_groups.employee_id = " + this.employee_id);
        return sql.toString();
    }

}
