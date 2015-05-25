/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;

import schedfoxlib.model.CheckIn;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_checkin_obj_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(CheckIn checkin) {
        if (checkin.getCheckinId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                checkin.getPersonCheckedIn(), checkin.getTimeStamp(),
                checkin.getPersonCheckedOut(), checkin.getTimeStampOut(),
                checkin.getCheckInDate(), checkin.getEmployeeId(), checkin.getShiftId(),
                checkin.getStartTime(), checkin.getEndTime()
            });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{
               // checkin.getPersonCheckedIn(), checkin.getTimeStamp(),
                checkin.getPersonCheckedOut(), checkin.getTimeStampOut(),
                //checkin.getCheckInDate(), checkin.getEmployeeId(), checkin.getShiftId(),
                //checkin.getStartTime(), checkin.getEndTime(),
                checkin.getCheckinId()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("checkin ");
            sql.append("(");
            sql.append(" person_checked_in, time_stamp, ");
            sql.append(" person_checked_out, time_stamp_out, ");
            sql.append(" checkin_date, employee_id, shift_id, ");
            sql.append(" checkin_last_updated, start_time, end_time ");
            sql.append(")");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, NOW(), ?, ?);");
        } else {
            sql.append("UPDATE checkin SET ");
            //sql.append("person_checked_in = ?, time_stamp = ?, ");
            sql.append("person_checked_out = ?, time_stamp_out = ?, ");
            //sql.append("checkin_date = ?, employee_id = ?, shift_id = ?, ");
            sql.append("checkin_last_updated = NOW()");
            sql.append("WHERE ");
            sql.append("checkin_id = ?;");
        }
        return sql.toString();
    }


}
