/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.CheckinNotifications;

/**
 *
 * @author ira
 */
public class save_checkin_notification_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(CheckinNotifications schedData) {
        if (schedData.getCheckinNotificationId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                schedData.getShiftId(), schedData.getStartTime(), schedData.getEndTime(), schedData.getNotificationSentTo(), schedData.getEmployeeId(), schedData.getUserId(),
                schedData.getClientContactId(), schedData.getResolutionSent(), schedData.getCheckedInResolutionTime()
            });
        } else {
            java.sql.Timestamp checkinResolution = null;
            try {
                checkinResolution = new java.sql.Timestamp(schedData.getCheckedInResolutionTime().getTime());
            } catch (Exception exe) {}
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                schedData.getShiftId(), schedData.getStartTime(), schedData.getEndTime(), schedData.getNotificationSentTo(), schedData.getEmployeeId(), schedData.getUserId(),
                schedData.getClientContactId(), schedData.getResolutionSent(), checkinResolution, schedData.getCheckinNotificationId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("checkin_notifications ");
            sql.append("(shift_id, start_time, end_time, notification_sent_to, employee_id, user_id, client_contact_id, resolution_sent, checked_in_resolution_time) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("checkin_notifications ");
            sql.append("SET ");
            sql.append("shift_id = ?, start_time = ?, end_time = ?, notification_sent_to = ?, employee_id = ?, ");
            sql.append("user_id = ?, client_contact_id = ?, resolution_sent = ?, checked_in_resolution_time = ? ");
            sql.append("WHERE ");
            sql.append("checkin_notification_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
