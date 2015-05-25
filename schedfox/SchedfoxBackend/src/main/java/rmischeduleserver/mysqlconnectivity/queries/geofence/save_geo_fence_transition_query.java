/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.geofence;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.GeoFencingTransition;

/**
 *
 * @author ira
 */
public class save_geo_fence_transition_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(GeoFencingTransition trans) {
        if (trans.getGeoFencingTransitionId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                trans.getGeoFenceId(), trans.getTransitionType(), trans.getAlertSentOn(), trans.getClientEquipmentId(), trans.getGpsCoordinateId()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                trans.getGeoFenceId(), trans.getTransitionType(), trans.getAlertSentOn(), trans.getClientEquipmentId(), trans.getGpsCoordinateId(),
                trans.getGeoFencingTransitionId(), trans.getGeoFencingTransitionId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing_transition ");
            sql.append("(geo_fence_id, transition_type, alert_sent_on, client_equipment_id, gps_coordinate_id) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?);");
            sql.append("SELECT currval('geo_fencing_transition_seq') as myid; ");
        } else {
            sql.append("UPDATE ");
            sql.append("geo_fencing_transition ");
            sql.append("SET ");
            sql.append("geo_fence_id = ?, transition_type = ?, alert_sent_on = ?, client_equipment_id = ?, gps_coordinate_id = ? ");
            sql.append("WHERE ");
            sql.append("geo_fencing_transition_id = ?; ");
            sql.append("SELECT ? as myid; ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
