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
public class get_clients_id_by_branch_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client.*, latitude, longitude FROM client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(trim(address_geocode.city)) = upper(trim(client.client_city)) AND upper(trim(address_geocode.state)) = upper(trim(client.client_state)) AND ");
        sql.append("    upper(trim(address_geocode.zip)) = upper(trim(client.client_zip)) AND upper(trim(address_geocode.address2)) = upper(trim(client.client_address2)) ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 AND branch_id = ? ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
