/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class CheckinNotifications {
    private Integer checkinNotificationId;
    private String shiftId;
    private Integer startTime;
    private Integer endTime;
    private String notificationSentTo;
    private Integer employeeId;
    private Integer userId;
    private Integer clientContactId;
    private Date notificationTime;
    private Boolean resolutionSent;
    private Date checkedInResolutionTime;
    
    private Date checkinLastUpdated;
    private Integer newEmpId;
    
    public CheckinNotifications() {
        
    }
    
    public CheckinNotifications(Record_Set rst) {
        try {
            checkinNotificationId = rst.getInt("checkin_notification_id");
        } catch (Exception exe) {}
        try {
            shiftId = rst.getString("shift_id");
        } catch (Exception exe) {}
        try {
            startTime = rst.getInt("start_time");
        } catch (Exception exe) {}
        try {
            endTime = rst.getInt("end_time");
        } catch (Exception exe) {}
        try {
            notificationSentTo = rst.getString("notification_sent_to");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            clientContactId = rst.getInt("client_contact_id");
        } catch (Exception exe) {}
        try {
            notificationTime = rst.getDate("notification_time");
        } catch (Exception exe) {}
        try {
            resolutionSent = rst.getBoolean("resolution_sent");
        } catch (Exception exe) {}
        try {
            checkinLastUpdated = rst.getTimestamp("checkin_last_updated");
        } catch (Exception exe) {}
        try {
            newEmpId = rst.getInt("emp_id");
        } catch (Exception exe) {}
    }

    /**
     * @return the checkinNotificationId
     */
    public Integer getCheckinNotificationId() {
        return checkinNotificationId;
    }

    /**
     * @param checkinNotificationId the checkinNotificationId to set
     */
    public void setCheckinNotificationId(Integer checkinNotificationId) {
        this.checkinNotificationId = checkinNotificationId;
    }

    /**
     * @return the shiftId
     */
    public String getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the startTime
     */
    public Integer getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Integer startTime) {
        this.startTime = startTime;
    }

    /**
     * @return the endTime
     */
    public Integer getEndTime() {
        return endTime;
    }

    /**
     * @param endTime the endTime to set
     */
    public void setEndTime(Integer endTime) {
        this.endTime = endTime;
    }

    /**
     * @return the notificationSentTo
     */
    public String getNotificationSentTo() {
        return notificationSentTo;
    }

    /**
     * @param notificationSentTo the notificationSentTo to set
     */
    public void setNotificationSentTo(String notificationSentTo) {
        this.notificationSentTo = notificationSentTo;
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
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the clientContactId
     */
    public Integer getClientContactId() {
        return clientContactId;
    }

    /**
     * @param clientContactId the clientContactId to set
     */
    public void setClientContactId(Integer clientContactId) {
        this.clientContactId = clientContactId;
    }

    /**
     * @return the notificationTime
     */
    public Date getNotificationTime() {
        return notificationTime;
    }

    /**
     * @param notificationTime the notificationTime to set
     */
    public void setNotificationTime(Date notificationTime) {
        this.notificationTime = notificationTime;
    }

    /**
     * @return the resolutionSent
     */
    public Boolean getResolutionSent() {
        return resolutionSent;
    }

    /**
     * @param resolutionSent the resolutionSent to set
     */
    public void setResolutionSent(Boolean resolutionSent) {
        this.resolutionSent = resolutionSent;
    }

    /**
     * @return the checkedInResolutionTime
     */
    public Date getCheckedInResolutionTime() {
        return checkedInResolutionTime;
    }

    /**
     * @param checkedInResolutionTime the checkedInResolutionTime to set
     */
    public void setCheckedInResolutionTime(Date checkedInResolutionTime) {
        this.checkedInResolutionTime = checkedInResolutionTime;
    }

    /**
     * @return the newEmpId
     */
    public Integer getNewEmpId() {
        return newEmpId;
    }

    /**
     * @param newEmpId the newEmpId to set
     */
    public void setNewEmpId(Integer newEmpId) {
        this.newEmpId = newEmpId;
    }
}
