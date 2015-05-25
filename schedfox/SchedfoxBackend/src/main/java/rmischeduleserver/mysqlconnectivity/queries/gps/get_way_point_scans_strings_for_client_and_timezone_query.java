/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.gps;

import java.text.SimpleDateFormat;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_way_point_scans_strings_for_client_and_timezone_query extends GeneralQueryFormat {
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String start, String end, Integer clientId, String timezone) {
        super.setPreparedStatement(timezone, timezone, start, end, clientId);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client_waypoint_scan_id, client_waypoint_scan.client_waypoint_id, user_id,  ");
        sql.append("((((date_scanned::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))::timestamp without time zone))::timestamp without time zone) as date_scanned ");
        sql.append("FROM ");
        sql.append("client_waypoint_scan ");
        sql.append("INNER JOIN client_waypoint ON client_waypoint.client_waypoint_id = client_waypoint_scan.client_waypoint_id ");
        sql.append("WHERE ");
        sql.append("DATE((((date_scanned::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))::timestamp without time zone))::timestamp without time zone) BETWEEN DATE(?) AND DATE(?) AND client_id = ? ");
        sql.append("ORDER BY ");
        sql.append("date_scanned ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
