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
public class check_employee_login_isnt_used_query extends GeneralQueryFormat {

    private String login;
    private String companyDB;

    public void update(String login, String companyDB) {
        this.login = login.trim();
        this.companyDB = companyDB;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(companyDB + ".employee ");
        sql.append("WHERE ");
        sql.append("employee_login = '" + this.login + "'");
        return sql.toString();
    }

}
