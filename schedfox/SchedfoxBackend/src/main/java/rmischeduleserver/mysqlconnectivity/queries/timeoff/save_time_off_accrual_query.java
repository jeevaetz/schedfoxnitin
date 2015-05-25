/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import schedfoxlib.model.TimeOffAccrual;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_time_off_accrual_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(TimeOffAccrual timeoff) {
        if (timeoff.getTimeOffAccrualId() == null || timeoff.getTimeOffAccrualId() == 0) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                timeoff.getStartInterval(), timeoff.getEndInterval(),
                timeoff.getTimeOffInterval(), timeoff.getDays(), timeoff.getActive(),
                timeoff.isTimeOffAccrual(),
                timeoff.getTimeOffSeriesId()
                });
        } else {
            super.setPreparedStatement(new Object[] {
                timeoff.getStartInterval(), timeoff.getEndInterval(),
                timeoff.getTimeOffInterval(), timeoff.getDays(), timeoff.getActive(),
                timeoff.isTimeOffAccrual(),
                timeoff.getTimeOffSeriesId(), timeoff.getTimeOffAccrualId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO time_off_accrual ");
            sql.append("(start_interval, end_interval, time_off_interval, days, active, reset_accrual, time_off_series_id) ");
            sql.append("VALUES ");
            sql.append("(?::interval, ?::interval, ?::interval, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE time_off_accrual ");
            sql.append("SET ");
            sql.append("start_interval = ?::interval, end_interval = ?::interval, ");
            sql.append("time_off_interval = ?::interval, days = ?, active = ?, reset_accrual = ?, time_off_series_id = ? ");
            sql.append("WHERE ");
            sql.append("time_off_accrual_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
