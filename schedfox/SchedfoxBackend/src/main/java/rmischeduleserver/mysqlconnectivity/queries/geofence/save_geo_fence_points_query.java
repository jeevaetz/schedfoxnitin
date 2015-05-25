/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.geofence;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.GeoFencingPoints;

/**
 *
 * @author ira
 */
public class save_geo_fence_points_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(GeoFencingPoints point) {
        if (point.getGeoFencingPointId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                point.getGeoFencingId(), point.getLatitude(), point.getLongitude(), point.getPointCount()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                point.getGeoFencingId(), point.getLatitude(), point.getLongitude(), point.getPointCount(),
                point.getGeoFencingPointId(), point.getGeoFencingPointId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing_points ");
            sql.append("(geo_fencing_id, latitude, longitude, point_count) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?);");
            sql.append("SELECT currval('geo_fencing_points_seq') as myid; ");
        } else {
            sql.append("UPDATE ");
            sql.append("geo_fencing_points ");
            sql.append("SET ");
            sql.append("geo_fencing_id = ?, latitude = ?, longitude = ?, point_count = ? ");
            sql.append("WHERE ");
            sql.append("geo_fencing_point_id = ?; ");
            sql.append("SELECT ? as myid; ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
