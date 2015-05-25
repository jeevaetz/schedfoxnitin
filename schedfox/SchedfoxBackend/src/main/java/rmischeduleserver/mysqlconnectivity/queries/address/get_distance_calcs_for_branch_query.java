/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.address;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_distance_calcs_for_branch_query extends GeneralQueryFormat {

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
        sql.append("SELECT employee_id, client_id, travel_duration, travel_distance FROM ");
        sql.append("employee ");
        sql.append("INNER JOIN address_geocode ON upper(address_geocode.address1) = upper(employee.employee_address) AND upper(address_geocode.address2) = upper(employee.employee_address2) AND ");
	sql.append( "upper(address_geocode.city) = upper(employee.employee_city) AND upper(address_geocode.state) = upper(employee.employee_state) AND  ");
        sql.append("	upper(address_geocode.zip) = upper(employee.employee_zip) ");
        sql.append("INNER JOIN address_geocode_distance ON address_geocode_distance.address_geocode1 = address_geocode.address_geocode_id ");
        sql.append("INNER JOIN address_geocode as cli_geocode ON address_geocode_distance.address_geocode2 = cli_geocode.address_geocode_id ");
        sql.append("INNER JOIN client ON upper(cli_geocode.address1) = upper(client.client_address) AND upper(cli_geocode.address2) = upper(client.client_address2) AND ");
        sql.append("	upper(cli_geocode.city) = upper(client.client_city) AND upper(cli_geocode.state) = upper(client.client_state) AND  ");
        sql.append("	upper(cli_geocode.zip) = upper(client.client_zip) ");
        sql.append("WHERE ");
        sql.append("employee.branch_id = ? AND employee_is_deleted != 1");
        return sql.toString();
    }
    
}
