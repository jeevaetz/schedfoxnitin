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
public class load_last_modified_clients_query extends GeneralQueryFormat {

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
        sql.append("SELECT * FROM client ");
        sql.append("LEFT JOIN usked_client ON usked_client.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("client.branch_id = ? AND ");
        sql.append("client.client_last_updated >= NOW() - interval '1 minute' * ?;");
        return sql.toString();
    }
}
