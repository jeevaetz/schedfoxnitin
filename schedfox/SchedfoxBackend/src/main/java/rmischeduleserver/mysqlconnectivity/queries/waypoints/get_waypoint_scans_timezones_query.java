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
public class get_waypoint_scans_timezones_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client_waypoint_scan.client_waypoint_scan_id, client_waypoint_scan.client_waypoint_id, user_id, ");
        sql.append("((date_scanned::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))) as date_scanned ");
        sql.append("FROM ");
        sql.append("client_waypoint_scan ");
        sql.append("INNER JOIN client_waypoint ON client_waypoint.client_waypoint_id = client_waypoint_scan.client_waypoint_id ");
        sql.append("WHERE ");
        sql.append("client_waypoint_scan.client_waypoint_id = ? AND ");
        sql.append("DATE(date_scanned::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?)) BETWEEN DATE(?) AND DATE(?) ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
