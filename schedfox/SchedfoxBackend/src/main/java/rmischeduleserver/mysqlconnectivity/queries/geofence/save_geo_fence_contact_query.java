/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.geofence;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.GeoFencingContact;

/**
 *
 * @author ira
 */
public class save_geo_fence_contact_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(GeoFencingContact fence) {
        if (fence.getGeoFencingContactId() != null) {
            isUpdate = true;
            super.setPreparedStatement(
                fence.getGeoFenceId(), fence.getContactName(), fence.getContactValue(), fence.getContactType(),
                fence.getActive(), fence.getGeoFencingContactId(), fence.getGeoFencingContactId()
            );
        } else {
            isUpdate = false;
            super.setPreparedStatement(
                fence.getGeoFenceId(), fence.getContactName(), fence.getContactValue(), fence.getContactType(),
                fence.getActive()
            );
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE geo_fencing_contact ");
            sql.append("SET ");
            sql.append("geo_fence_id = ?, contact_name = ?, contact_value = ?, contact_type = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("geo_fencing_contact_id = ?; ");
            sql.append("SELECT ? as myid; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append("geo_fencing_contact ");
            sql.append("(geo_fence_id, contact_name, contact_value, contact_type, active) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?); ");
            sql.append("SELECT currval('geo_fencing_contact_seq') as myid; ");
        }
        return sql.toString();
    }
}
