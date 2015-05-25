/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.waypoints;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientWaypointScan;

/**
 *
 * @author user
 */
public class save_waypoint_scan_query extends GeneralQueryFormat {

    private boolean isInsert = false;
    
    public void update(ClientWaypointScan wayPoint, boolean isInsert) {
        this.isInsert = isInsert;
        super.setPreparedStatement(new Object[]{wayPoint.getUserId(), wayPoint.getClientWaypointId(), 
            wayPoint.getClientWaypointScanId()});
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("client_waypoint_scan ");
            sql.append("(user_id, client_waypoint_id, client_waypoint_scan_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("client_waypoint_scan ");
            sql.append("SET ");
            sql.append("user_id = ?, client_waypoint_id = ? ");
            sql.append("WHERE ");
            sql.append("client_waypoint_scan_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
