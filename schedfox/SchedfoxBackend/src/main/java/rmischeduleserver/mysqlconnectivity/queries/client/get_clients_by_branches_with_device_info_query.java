/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_clients_by_branches_with_device_info_query extends GeneralQueryFormat {
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client.*, latitude, longitude, client_equipment_id FROM client ");
        sql.append("LEFT JOIN address_geocode ON upper(trim(address_geocode.address1)) = upper(trim(client.client_address)) AND ");
        sql.append("    upper(trim(address_geocode.city)) = upper(trim(client.client_city)) AND upper(trim(address_geocode.state)) = upper(trim(client.client_state)) AND ");
        sql.append("    upper(trim(address_geocode.zip)) = upper(trim(client.client_zip)) AND upper(trim(address_geocode.address2)) = upper(trim(client.client_address2)) ");
        sql.append("LEFT JOIN client_equipment ON client_equipment.client_id = client.client_id AND client_equipment.active = true ");
        sql.append("WHERE ");
        sql.append("branch_id = ? AND ((? = false AND client.client_is_deleted != 1) OR (? = true))");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
