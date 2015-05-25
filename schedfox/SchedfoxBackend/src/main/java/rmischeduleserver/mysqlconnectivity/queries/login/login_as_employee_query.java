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
public class login_as_employee_query extends GeneralQueryFormat {

    private String companySchema;
    private String userName;
    private String password;

    public void update(String company_schema, String userName, String password) {
        this.companySchema = company_schema;
        this.userName = userName;
        this.password = password;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM ");
        sql.append(companySchema + ".employee ");
        sql.append("WHERE ");
        sql.append("    upper(employee.employee_login) = upper('" + userName.trim() + "') ");
        sql.append("    AND ");
        sql.append("    upper(employee.employee_password) = upper('" + password.trim() + "') ");
        sql.append("    AND is_login_available = true ");
        sql.append("    AND is_login_available != false ");
        sql.append("    AND employee_is_deleted = 0; ");
        return sql.toString();
    }
    
}
