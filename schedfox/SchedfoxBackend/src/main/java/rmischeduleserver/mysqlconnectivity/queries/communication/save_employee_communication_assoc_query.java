/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.communication;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_communication_assoc_query extends GeneralQueryFormat {

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
        sql.append("DELETE FROM communication_link_to_client ");
        sql.append("WHERE ");
        sql.append("communication_source_id = ?;");
        sql.append("INSERT INTO communication_link_to_client ");
        sql.append("(client_id, communication_source_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?);");
        return sql.toString();
    }

}
