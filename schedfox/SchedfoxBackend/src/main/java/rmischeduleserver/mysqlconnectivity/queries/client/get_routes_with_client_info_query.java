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
public class get_routes_with_client_info_query extends GeneralQueryFormat {

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
        sql.append("SELECT ");
        sql.append("client_route.*, client_route_to_client.client_id ");
        sql.append("FROM ");
        sql.append("client_route ");
        sql.append("LEFT JOIN client_route_to_client ON client_route_to_client.route_id = client_route.client_route_id ");
        sql.append("LEFT JOIN client ON client.client_id = client_route_to_client.client_id ");
        sql.append("ORDER BY route_name, client_name ");
        return sql.toString();
    }

}
