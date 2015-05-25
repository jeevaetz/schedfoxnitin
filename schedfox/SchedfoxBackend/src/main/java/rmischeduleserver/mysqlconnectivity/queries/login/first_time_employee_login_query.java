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
public class first_time_employee_login_query extends GeneralQueryFormat {
    private String companyDB;
    private String lastName;
    private String SSN;
    private int employee_id;

    public first_time_employee_login_query() {
        companyDB = "";
        lastName = "";
        SSN = "";
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String companyDB, String lastName, String SSN, int employeeId) {
        this.companyDB = companyDB.trim().replaceAll("'", "''");
        this.lastName = lastName.trim().replaceAll("'", "''");
        this.SSN = SSN.trim().replaceAll("'", "''");
        this.employee_id = employeeId;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("employee_id, ");
        sql.append("employee_login, ");
        sql.append("employee_password ");
        sql.append("FROM ");
        sql.append(companyDB + ".employee ");
        sql.append("WHERE ");
        sql.append("UPPER(employee_last_name) = UPPER('" + lastName + "') AND ");
        sql.append("(");
        sql.append("employee_ssn = '" + SSN + "' OR ");
        sql.append("(employee_id = " + employee_id + " AND " + employee_id + " != 0) ");
        sql.append(") AND ");
        //sql.append("is_login_available != false AND ");
        sql.append("employee_is_deleted = 0;");
        return sql.toString();
    }

}
