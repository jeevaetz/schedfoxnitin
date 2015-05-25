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
public class fallback_to_employee_login_query extends GeneralQueryFormat {

    private String prefix;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(String prefix) {
        this.prefix = prefix;
    }

    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("company_id, ");
        sql.append("company_db ");
        sql.append("FROM ");
        sql.append(this.getManagementSchema() + ".company ");
        sql.append("WHERE ");
        sql.append("UPPER(company_db) = UPPER('" + this.prefix + "')");
        return sql.toString();
    }

}
