/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class client_save_problem_contact_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM client_to_user_contact WHERE client_id = ?;");
        sql.append("INSERT INTO client_to_user_contact ");
        sql.append("(client_id, user_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?);");
        return sql.toString();
    }

    public String toString() {
        return "";
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
