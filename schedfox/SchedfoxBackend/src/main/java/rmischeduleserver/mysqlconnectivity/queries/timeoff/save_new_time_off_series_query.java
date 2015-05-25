/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import schedfoxlib.model.TimeOffSeries;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_new_time_off_series_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(TimeOffSeries series) {
        super.setPreparedStatement(new Object[]{
                    series.getTimeOffSeriesId(), series.getTimeOffSeries(), series.getEmployeeTypeId(), series.getTimeOffTypeId(), series.getActive()
                });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ");
        sql.append("time_off_series ");
        sql.append("(time_off_series_id, time_off_series, employee_type_id, time_off_type_id, active) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?);");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
