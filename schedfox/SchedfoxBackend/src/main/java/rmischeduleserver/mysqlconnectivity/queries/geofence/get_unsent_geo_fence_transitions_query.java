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
public class get_unsent_geo_fence_transitions_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("geo_fencing_transition ");
        sql.append("INNER JOIN geo_fencing ON geo_fencing.geo_fencing_id = geo_fencing_transition.geo_fence_id ");
        sql.append("WHERE ");
        sql.append("alert_sent_on IS NULL AND geo_fencing.geo_fence_type = geo_fencing_transition.transition_type OR (geo_fencing.geo_fence_type = 3) ");
        sql.append("AND geo_fencing_transition.transition_date > NOW() - interval '30 minutes' ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
