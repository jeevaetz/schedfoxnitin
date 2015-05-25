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
public class get_assigned_timeoff_series_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("time_off_series ");
        sql.append("INNER JOIN time_off_type ON time_off_type.time_off_type_id = time_off_series.time_off_type_id ");
        sql.append("WHERE ");
        sql.append("employee_type_id = ? AND UPPER(timeoff) = UPPER(?) ");
        sql.append("ORDER BY ");
        sql.append("time_off_series;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
