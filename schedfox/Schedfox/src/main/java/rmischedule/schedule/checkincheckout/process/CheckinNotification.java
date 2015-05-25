/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.schedule.checkincheckout.process;

import java.util.Date;

/**
 *
 * @author ira
 */
public class CheckinNotification {
    private Integer checkinNotificationId;
    private String shiftId;
    private Integer startTime;
    private Integer endTime;
    private String notificationSentTo;
    private Integer employeeId;
    private Integer userId;
    private Integer clientContactId;
    private Date notificationTime;
    
    public CheckinNotification() {
        
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
}
