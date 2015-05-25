/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_users_by_company_query extends GeneralQueryFormat {

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
        sql.append("SELECT DISTINCT \"user\".* ");
        sql.append("FROM ");
        sql.append("control_db.user ");
        sql.append("INNER JOIN control_db.user_branch_company ON user_branch_company.user_id = \"user\".user_id ");
        sql.append("WHERE ");
        sql.append("company_id = ? AND user_is_deleted != 1 ");
        sql.append("ORDER BY user_first_name, user_last_name ");
        return sql.toString();
    }
}
