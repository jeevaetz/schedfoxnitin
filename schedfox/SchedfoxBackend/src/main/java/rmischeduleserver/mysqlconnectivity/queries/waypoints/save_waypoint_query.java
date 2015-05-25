/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.waypoints;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.WayPoint;

/**
 *
 * @author user
 */
public class save_waypoint_query extends GeneralQueryFormat {

    private boolean isInsert = false;
    
    public void update(WayPoint wayPoint, boolean isInsert) {
        this.isInsert = isInsert;
        super.setPreparedStatement(new Object[]{wayPoint.getClientId(), wayPoint.getWaypointName(), 
            wayPoint.getWaypointData(), wayPoint.getActive().booleanValue(), 
            wayPoint.getLatitude(), wayPoint.getLongitude(), wayPoint.getWaypointType(),
            wayPoint.getClientWaypointId()});
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
            sql.append("client_waypoint ");
            sql.append("(client_id, waypoint_name, waypoint_data, active, latitude, longitude, waypoint_type, client_waypoint_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("client_waypoint ");
            sql.append("SET ");
            sql.append("client_id = ?, waypoint_name = ?, waypoint_data = ?, active = ?, latitude = ?, longitude = ?, waypoint_type = ? ");
            sql.append("WHERE ");
            sql.append("client_waypoint_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
