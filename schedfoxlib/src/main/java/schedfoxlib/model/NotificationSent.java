/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class NotificationSent {
    
    public static int OUTSTANDING_BALANCE = 1;
    
    private Integer notificationSentId;
    private Integer employeeId;
    private Integer notificationType;
    private Date notificationSent;
    
    public NotificationSent() {
        
    }

    public NotificationSent(Record_Set rst) {
        try {
            notificationSentId = rst.getInt("notification_sent_id");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            notificationType = rst.getInt("notification_type");
        } catch (Exception exe) {}
        try {
            notificationSent = rst.getTimestamp("notification_sent");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the notificationSentId
     */
    public Integer getNotificationSentId() {
        return notificationSentId;
    }

    /**
     * @param notificationSentId the notificationSentId to set
     */
    public void setNotificationSentId(Integer notificationSentId) {
        this.notificationSentId = notificationSentId;
    }

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the notificationType
     */
    public Integer getNotificationType() {
        return notificationType;
    }

    /**
     * @param notificationType the notificationType to set
     */
    public void setNotificationType(Integer notificationType) {
        this.notificationType = notificationType;
    }

    /**
     * @return the notificationSent
     */
    public Date getNotificationSent() {
        return notificationSent;
    }

    /**
     * @param notificationSent the notificationSent to set
     */
    public void setNotificationSent(Date notificationSent) {
        this.notificationSent = notificationSent;
    }
}
