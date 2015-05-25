/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.admin;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_companies_accessible_for_management_co_query extends GeneralQueryFormat {

    private int user_id;

    public void update(int user_id) {
        this.user_id = user_id;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT company.*, (CASE WHEN pg_namespace.nspname IS NULL THEN 1 ELSE 0 END) as is_deleted ");
        sql.append("FROM ");
        sql.append(this.getManagementSchema() + ".user ");
        sql.append("INNER JOIN " + this.getManagementSchema() + ".management_clients ON management_clients.management_id = \"user\".user_management_id ");
        sql.append("INNER JOIN " + this.getManagementSchema() + ".company ON company.company_management_id = management_clients.management_id OR (management_id = 0) ");
        sql.append("INNER JOIN pg_namespace ON pg_namespace.nspname = company.company_db ");
        sql.append("WHERE user_id = ").append(user_id).append(" ");
        sql.append("ORDER BY company_name");
        return sql.toString();
    }

}
