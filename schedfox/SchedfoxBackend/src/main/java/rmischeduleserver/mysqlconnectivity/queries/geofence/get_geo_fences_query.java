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
public class get_geo_fences_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("geo_fencing ");
        sql.append("WHERE ");
        sql.append("client_id = ? AND active = true; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
