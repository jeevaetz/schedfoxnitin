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
public class get_available_series_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT time_off_series.* ");
        sql.append("FROM time_off_series ");
        sql.append("LEFT JOIN time_off_series_to_type ON ");
        sql.append("    time_off_series_to_type.time_off_series_id = time_off_series.time_off_series_id AND ");
        sql.append("    time_off_series_to_type.time_off_type_id = ? ");
        sql.append("WHERE ");
        sql.append("time_off_series_to_type.time_off_type_id IS NULL;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
