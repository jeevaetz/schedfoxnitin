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
public class save_user_branch_company_query extends GeneralQueryFormat {
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO control_db.user_branch_company ");
        sql.append("(user_id, company_id, branch_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?);");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
