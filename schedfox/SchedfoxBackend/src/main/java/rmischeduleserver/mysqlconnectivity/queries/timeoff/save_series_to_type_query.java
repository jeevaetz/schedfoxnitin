/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import java.util.ArrayList;
import schedfoxlib.model.TimeOffSeries;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_series_to_type_query extends GeneralQueryFormat {

    private ArrayList<Integer> types;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM time_off_series_to_type ");
        sql.append("WHERE ");
        sql.append("time_off_series_id = ?;");
        for (int t = 0; t < types.size(); t++) {
            sql.append("INSERT INTO time_off_series_to_type ");
            sql.append("(time_off_type_id, time_off_series_id)");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        }
        return sql.toString();
    }

    public void update(ArrayList<Integer> types, TimeOffSeries accrual) {
        Object[] values = new Object[1 + (types.size() * 2)];
        values[0] = accrual.getTimeOffSeriesId();
        for (int t = 0; t < types.size(); t++) {
            values[(t * 2) + 1] = types.get(t);
            values[(t * 2) + 2] = accrual.getTimeOffSeriesId();
        }
        super.setPreparedStatement(values);

        this.types = types;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
