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
public class get_all_branches_for_company_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("control_db.branch ");
        sql.append("INNER JOIN control_db.branch_info ON branch_info.branch_id = branch.branch_id ");
        sql.append("INNER JOIN control_db.management_clients ON management_clients.management_id = branch.branch_management_id ");
        sql.append("INNER JOIN control_db.company ON company.company_management_id = management_clients.management_id ");
        sql.append("WHERE ");
        sql.append("company.company_id = ? ");
        sql.append("ORDER BY branch_name; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
