/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_branches_for_user_company_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT branch.* FROM ");
        sql.append("user_branch_company ");
        sql.append("INNER JOIN branch ON branch.branch_id = user_branch_company.branch_id ");
        sql.append("WHERE ");
        sql.append("user_id = ? AND company_id = ? ");
        return sql.toString();
    }

}
