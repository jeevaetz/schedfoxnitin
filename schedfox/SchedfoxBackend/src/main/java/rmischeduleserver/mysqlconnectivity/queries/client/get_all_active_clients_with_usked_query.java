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
public class get_all_active_clients_with_usked_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client.*, COALESCE(usked_client.usked_cli_id, '') as usked_identifier FROM ");
        sql.append("client ");
        sql.append("LEFT JOIN usked_client ON usked_client.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
