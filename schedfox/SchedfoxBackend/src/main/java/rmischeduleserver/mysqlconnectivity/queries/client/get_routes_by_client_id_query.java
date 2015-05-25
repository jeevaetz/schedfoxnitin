/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_routes_by_client_id_query extends GeneralQueryFormat {
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client_route.* ");
        sql.append("FROM ");
        sql.append("client_route_to_client ");
        sql.append("INNER JOIN client_route ON client_route.client_route_id = client_route_to_client.route_id ");
        sql.append("WHERE ");
        sql.append("client_route_to_client.client_id = ? ");
        sql.append("ORDER BY ");
        sql.append("route_name ");
        return sql.toString();
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
