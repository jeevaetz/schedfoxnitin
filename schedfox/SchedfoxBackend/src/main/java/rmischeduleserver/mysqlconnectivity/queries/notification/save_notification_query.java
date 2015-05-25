/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.notification;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.NotificationSent;

/**
 *
 * @author ira
 */
public class save_notification_query extends GeneralQueryFormat {

    private boolean isInsert = false;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(NotificationSent notificationSent) {
        if (notificationSent.getNotificationSentId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{notificationSent.getEmployeeId(), 
                notificationSent.getNotificationType()});
        } else {
            super.setPreparedStatement(new Object[]{notificationSent.getEmployeeId(), 
                notificationSent.getNotificationType(), notificationSent.getNotificationSentId()});
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("notification_sent ");
            sql.append("(employee_id, notification_type) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("notification_sent ");
            sql.append("SET ");
            sql.append("employee_id = ?, notification_type = ? ");
            sql.append("WHERE ");
            sql.append("notification_sent_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
