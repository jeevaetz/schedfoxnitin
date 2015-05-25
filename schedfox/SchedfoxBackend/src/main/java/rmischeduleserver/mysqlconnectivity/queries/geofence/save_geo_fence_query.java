/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.geofence;

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

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(GeoFencing fence, boolean isUpdate) {
        this.isUpdate = isUpdate;
        if (!isUpdate) {
            super.setPreparedStatement(new Object[]{
                fence.getGeoFenceName(), fence.getClientId(), fence.getGeoFenceAddedBy(), fence.getGeoFenceType(),
                fence.getGeoFencingId()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                fence.getGeoFenceName(), fence.getClientId(), fence.getGeoFenceType(), fence.getGeoFencingId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE geo_fencing ");
            sql.append("SET ");
            sql.append("geo_fence_name = ?, client_id = ?, geo_fence_type = ? ");
            sql.append("WHERE ");
            sql.append("geo_fencing_id = ?; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing ");
            sql.append("(geo_fence_name, client_id, geo_fence_added_by, geo_fence_type, geo_fencing_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?); ");
        }
        return sql.toString();
    }
}
