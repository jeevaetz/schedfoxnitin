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
public class get_security_settings_for_emp_group_query extends GeneralQueryFormat {

    private String groupId;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String groupId) {
        this.groupId = groupId;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();

        sql.append("SELECT * FROM ");
        sql.append("employee_security_group_settings ");
        sql.append("WHERE ");
        sql.append("employee_security_group_id = " + groupId);

        return sql.toString();
    }

}
