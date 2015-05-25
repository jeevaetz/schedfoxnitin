/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_max_scans_for_shift_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client_waypoint.client_waypoint_id, waypoint_name, waypoint_data, MAX(date_scanned) as max_date ");
        sql.append("FROM ");
        sql.append("mobile_form_fillout ");
        sql.append("INNER JOIN client_waypoint ON client_waypoint.client_id = mobile_form_fillout.client_id ");
        sql.append("INNER JOIN client_waypoint_scan ON client_waypoint_scan.client_waypoint_id = client_waypoint.client_waypoint_id AND client_waypoint_scan.date_scanned <= mobile_form_fillout.last_modified ");
        sql.append("WHERE ");
        sql.append("mobile_form_fillout.mobile_form_fillout_id = ? AND client_waypoint_scan.date_scanned > mobile_form_fillout.date_entered - interval '1 day' ");
        sql.append("GROUP BY ");
        sql.append("client_waypoint.client_waypoint_id, waypoint_name, waypoint_data ");
        return sql.toString();
    }
 
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
