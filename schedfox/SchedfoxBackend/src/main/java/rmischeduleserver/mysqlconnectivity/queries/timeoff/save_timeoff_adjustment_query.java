/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import schedfoxlib.model.TimeOffAdjustment;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_timeoff_adjustment_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(TimeOffAdjustment adjustment) {
        if (adjustment.getTimeOffAdjustmentId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                        adjustment.getEmployeeId(), adjustment.getDateAdded(),
                        adjustment.getAdjustedBy(), adjustment.getAdjustmentNotes(),
                        adjustment.getAdjustment(), adjustment.getTimeOffTypeId()
                    });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{
                        adjustment.getEmployeeId(), adjustment.getDateAdded(),
                        adjustment.getAdjustedBy(), adjustment.getAdjustmentNotes(),
                        adjustment.getAdjustment(), adjustment.getTimeOffTypeId(),
                        adjustment.getTimeOffAdjustmentId()
                    });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO time_off_adjustment ");
            sql.append("(employee_id, date_added, adjusted_by, adjustment_notes, adjustment, time_off_type_id) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE time_off_adjustment ");
            sql.append("SET ");
            sql.append("employee_id = ?, date_added = ?, adjusted_by = ?, ");
            sql.append("adjustment_notes = ?, adjustment = ?, time_off_type_id = ? ");
            sql.append("WHERE ");
            sql.append("time_off_adjustment_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
