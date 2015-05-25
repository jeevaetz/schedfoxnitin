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
public class get_clients_by_login_info_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client.*, latitude, longitude FROM ");
        sql.append("client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(address_geocode.city) = upper(client.client_city) AND upper(address_geocode.state) = upper(client.client_state) AND ");
        sql.append("    upper(address_geocode.zip) = upper(client.client_zip) ");
        sql.append("WHERE ");
        sql.append("(branch_id = ? OR ? = -1) AND ");
        sql.append("upper(trim(cusername)) = upper(trim(?)) AND ");
        sql.append("upper(trim(cpassword)) = upper(trim(?)) ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
