/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_assigned_types_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT time_off_type.* FROM ");
        sql.append("time_off_series ");
        sql.append("INNER JOIN time_off_series_to_type ON ");
        sql.append("    time_off_series_to_type.time_off_series_id = time_off_series.time_off_series_id ");
        sql.append("INNER JOIN time_off_type ON ");
        sql.append("    time_off_type.time_off_type_id = time_off_series_to_type.time_off_type_id ");
        sql.append("WHERE ");
        sql.append("time_off_series_to_type.time_off_series_id = ?; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
