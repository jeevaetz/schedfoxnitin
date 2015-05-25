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
public class login_as_client_query extends GeneralQueryFormat {

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
        sql.append(companySchema + ".client ");
        sql.append("WHERE ");
        sql.append("    upper(client.cusername) = upper('" + userName.trim() + "') ");
        sql.append("    AND ");
        sql.append("    upper(client.cpassword) = upper('" + password.trim() + "') ");
        sql.append("    AND client_is_deleted = 0; ");
        return sql.toString();
    }
    
}
