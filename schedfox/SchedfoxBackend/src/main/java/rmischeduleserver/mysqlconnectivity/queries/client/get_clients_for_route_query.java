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
public class get_clients_for_route_query extends GeneralQueryFormat {

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
        sql.append("SELECT DISTINCT client.*, address_geocode.* FROM ");
        sql.append("client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(address_geocode.city) = upper(client.client_city) AND upper(address_geocode.state) = upper(client.client_state) AND ");
        sql.append("    upper(address_geocode.zip) = upper(client.client_zip) ");
        sql.append("INNER JOIN client_route_to_client ON client_route_to_client.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("client_route_to_client.route_id = ? AND client_is_deleted != 1 ");
        sql.append("ORDER BY ");
        sql.append("client_name ");
        return sql.toString();
    }

}
