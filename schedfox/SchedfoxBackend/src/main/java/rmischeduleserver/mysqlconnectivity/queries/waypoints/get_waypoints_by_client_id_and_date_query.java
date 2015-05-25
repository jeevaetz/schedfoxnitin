/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.waypoints;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_waypoints_by_client_id_and_date_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client_waypoint_scan.* FROM ");
        sql.append("client_waypoint_scan ");
        sql.append("INNER JOIN client_waypoint ON client_waypoint.client_waypoint_id = client_waypoint_scan.client_waypoint_id ");
        sql.append("WHERE ");
        sql.append("client_id = ? AND date_scanned BETWEEN ? AND ? ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
