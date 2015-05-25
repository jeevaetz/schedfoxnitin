/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.gps;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.GPSCoordinate;

/**
 *
 * @author user
 */
public class save_gps_coordinates_query extends GeneralQueryFormat {

    private boolean isInsert;
    private GPSCoordinate gps;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(GPSCoordinate gps, boolean isInsert) {
        this.isInsert = isInsert;
        this.gps = gps;
        
        java.sql.Timestamp time = new java.sql.Timestamp(new java.util.Date().getTime());
        try {
            time = new java.sql.Timestamp(gps.getRecordedOn().getTime());
        } catch (Exception exe) {}
        
        Integer clientId = 0;
        try {
            clientId = gps.getClientId();
        } catch (Exception exe) {}
        
        super.setPreparedStatement(new Object[]{
            gps.getLatitude(), gps.getLongitude(), time, gps.getEmployeeId(), clientId,
            gps.getEquipmentId(), gps.getAccuracy(), gps.getSpeed(),
            gps.getGpsCoordinateId()
        });
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (this.isInsert) {
            sql.append("INSERT INTO ");
            sql.append("gps_coordinate ");
            sql.append("(latitude, longitude, recorded_on, employee_id, client_id, equipment_id, accuracy, speed, gps_coordinate_id)");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("gps_coordinate ");
            sql.append("SET ");
            sql.append("latitude = ?, longitude = ?, recorded_on = ?, employee_id = ?, client_id = ?, equipment_id = ?, accuracy = ?, speed = ? ");
            sql.append("WHERE ");
            sql.append("gps_coordinate_id = ?");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
