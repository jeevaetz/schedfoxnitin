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
public class get_coordinates_for_client_id_with_accuracy_and_timezone_query extends GeneralQueryFormat {
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String start, String end, Integer clientId, String timezone) {
        Object[] params = new Object[7];
        params[0] = timezone;
        params[1] = clientId;
        params[2] = clientId;
        params[3] = start + " 00:00:00";
        params[4] = timezone;
        params[5] = end + " 23:59:59";
        params[6] = timezone;
        super.setPreparedStatement(params);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("gps_coordinate_id, latitude, longitude, ");
        sql.append("((recorded_on::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))) as recorded_on, ");
        sql.append("employee_id, gps_coordinate.equipment_id, accuracy, speed, COALESCE(gps_coordinate.client_id, client_equipment.client_id) as client_id ");
        sql.append("FROM ");
        sql.append("gps_coordinate ");
        sql.append("INNER JOIN client_equipment ON client_equipment.client_equipment_id = gps_coordinate.equipment_id AND ");
        sql.append("    ((gps_coordinate.client_id = ?) OR ((gps_coordinate.client_id IS NULL OR gps_coordinate.client_id = 0) AND client_equipment.client_id = ?))");
        sql.append("WHERE ");
        sql.append("recorded_on BETWEEN (?::timestamp at time zone(?)) AND (?::timestamp at time zone(?)) ");
        sql.append("ORDER BY ");
        sql.append("gps_coordinate.recorded_on ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
