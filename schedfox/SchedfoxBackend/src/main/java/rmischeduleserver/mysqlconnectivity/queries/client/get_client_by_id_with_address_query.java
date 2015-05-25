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
public class get_client_by_id_with_address_query extends GeneralQueryFormat {

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
        sql.append("SELECT client.*, latitude, longitude ");
        sql.append("FROM client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(trim(address_geocode.city)) = upper(trim(client.client_city)) AND upper(trim(address_geocode.state)) = upper(trim(client.client_state)) AND ");
        sql.append("    upper(trim(address_geocode.zip)) = upper(trim(client.client_zip)) AND upper(trim(address_geocode.address2)) = upper(trim(client.client_address2)) "); 
        sql.append("WHERE ");
        sql.append("client_id = ?");
        return sql.toString();
    }

}
