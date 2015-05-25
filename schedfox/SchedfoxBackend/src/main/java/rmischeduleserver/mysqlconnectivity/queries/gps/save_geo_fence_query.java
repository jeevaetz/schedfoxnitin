/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.gps;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.GeoFencing;

/**
 *
 * @author ira
 */
public class save_geo_fence_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(GeoFencing geoFence) {
        if (geoFence.getGeoFencingId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                geoFence.getGeoFenceName(), geoFence.getGeoFenceAddedBy(), geoFence.getClientId(), geoFence.getActive(), geoFence.getGeoFenceType()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                geoFence.getGeoFenceName(), geoFence.getGeoFenceAddedBy(), geoFence.getClientId(), geoFence.getActive(), geoFence.getGeoFenceType(),
                geoFence.getGeoFencingId(), geoFence.getGeoFencingId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing ");
            sql.append("(geo_fence_name, geo_fence_added_by, client_id, active, geo_fence_type) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?);");
            sql.append("SELECT currval('geo_fencing_seq') as myid; ");
        } else {
            sql.append("UPDATE ");
            sql.append("geo_fencing ");
            sql.append("SET ");
            sql.append("geo_fence_name = ?, geo_fence_added_by = ?, client_id = ?, active = ?, geo_fence_type = ? ");
            sql.append("WHERE ");
            sql.append("geo_fencing_id = ?;");
            sql.append("SELECT ? as myid; ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
