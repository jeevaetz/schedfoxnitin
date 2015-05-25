/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.geofence;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class mark_geo_fence_transition_as_sent_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append("geo_fencing_transition ");
        sql.append("SET ");
        sql.append("alert_sent_on = NOW() ");
        sql.append("WHERE ");
        sql.append("geo_fencing_transition_id = ?; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
