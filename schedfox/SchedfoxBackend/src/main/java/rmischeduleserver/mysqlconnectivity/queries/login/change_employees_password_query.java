/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.login;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class change_employees_password_query extends GeneralQueryFormat {

    private String loginName;
    private String password;
    private int employee_id;
    private String companyDB;

    public void update(String loginName, String password, int employee_id, String companyDB) {
        this.loginName = loginName;
        this.password = password;
        this.employee_id = employee_id;
        this.companyDB = companyDB;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("UPDATE " + companyDB + ".employee ");
        sql.append("SET ");
        sql.append("employee_login = '" + loginName + "', ");
        sql.append("employee_password = '" + password + "' ");
        sql.append("WHERE ");
        sql.append("employee_id = " + this.employee_id);
        return sql.toString();
    }
}
