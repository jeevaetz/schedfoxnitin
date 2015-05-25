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
public class get_coordinates_for_client_with_accuracy_and_timezone_query extends GeneralQueryFormat {

    private int numberParameters = 0;
    private Integer[] equipmentId;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Date start, Date end, Integer[] equipmentId, Integer accuracy, String timezone) {
        numberParameters = equipmentId.length;
        SimpleDateFormat databaseFormat = new SimpleDateFormat("yyyy-MM-dd");
        
        Object[] params = new Object[equipmentId.length + 6];
        params[0] = timezone;
        params[1] = timezone;
        params[2] = databaseFormat.format(start);
        params[3] = databaseFormat.format(end);
        for (int e = 0; e < equipmentId.length; e++) {
            params[e + 4] = equipmentId[e];
        }
        params[params.length - 2] = accuracy;
        params[params.length - 1] = timezone;
        super.setPreparedStatement(params);
        this.equipmentId = equipmentId;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("gps_coordinate_id, latitude, longitude, ");
        sql.append("((recorded_on::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))) as recorded_on, ");
        sql.append("employee_id, equipment_id, accuracy, speed ");
        sql.append("FROM ");
        sql.append("gps_coordinate ");
        sql.append("WHERE ");
        sql.append("DATE((((recorded_on::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))::timestamp without time zone))::timestamp without time zone) BETWEEN DATE(?) AND DATE(?) AND equipment_id IN ");
        sql.append("(");
        for (int n = 0; n < numberParameters; n++) {
            if (n != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") AND accuracy <= ? ");
        sql.append("ORDER BY ");
        sql.append("((recorded_on::timestamp at time zone(current_setting('TIMEZONE')) AT time zone(?))) ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
