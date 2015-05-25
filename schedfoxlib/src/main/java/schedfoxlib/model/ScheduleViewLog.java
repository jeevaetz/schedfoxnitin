/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ScheduleViewLog implements Serializable {
    private Integer scheduleViewLogId;
    private Integer viewType;
    private String remoteAddress;
    private Boolean isMobile;
    private Date viewTime;
    private Boolean fromUrlShorteningService;
    private Integer employeeId;
    private Date startDate;
    private Date endDate;
    
    public static int VIEW_SCHEDULE = 1;
    public static int CONFIRM_SCHEDULE = 2;
    
    public ScheduleViewLog() {
        
    }
    
    public ScheduleViewLog(Record_Set rst) {
        try {
            scheduleViewLogId = rst.getInt("schedule_view_log_id");
        } catch (Exception exe) {}
        try {
            viewType = rst.getInt("view_type");
        } catch (Exception exe) {}
        try {
            remoteAddress = rst.getString("remote_address");
        } catch (Exception exe) {}
        try {
            isMobile = rst.getBoolean("is_mobile");
        } catch (Exception exe) {}
        try {
            viewTime = rst.getDate("view_time");
        } catch (Exception exe) {}
        try {
            fromUrlShorteningService = rst.getBoolean("from_url_shortening_service");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            startDate = rst.getDate("start_date");
        } catch (Exception exe) {}
        try {
            endDate = rst.getDate("end_date");
        } catch (Exception exe) {}
    }

    /**
     * @return the scheduleViewLogId
     */
    public Integer getScheduleViewLogId() {
        return scheduleViewLogId;
    }

    /**
     * @param scheduleViewLogId the scheduleViewLogId to set
     */
    public void setScheduleViewLogId(Integer scheduleViewLogId) {
        this.scheduleViewLogId = scheduleViewLogId;
    }

    /**
     * @return the viewType
     */
    public Integer getViewType() {
        return viewType;
    }

    /**
     * @param viewType the viewType to set
     */
    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    /**
     * @return the remoteAddress
     */
    public String getRemoteAddress() {
        return remoteAddress;
    }

    /**
     * @param remoteAddress the remoteAddress to set
     */
    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    /**
     * @return the isMobile
     */
    public Boolean getIsMobile() {
        return isMobile;
    }

    /**
     * @param isMobile the isMobile to set
     */
    public void setIsMobile(Boolean isMobile) {
        this.isMobile = isMobile;
    }

    /**
     * @return the viewTime
     */
    public Date getViewTime() {
        return viewTime;
    }

    /**
     * @param viewTime the viewTime to set
     */
    public void setViewTime(Date viewTime) {
        this.viewTime = viewTime;
    }

    /**
     * @return the fromUrlShorteningService
     */
    public Boolean getFromUrlShorteningService() {
        return fromUrlShorteningService;
    }

    /**
     * @param fromUrlShorteningService the fromUrlShorteningService to set
     */
    public void setFromUrlShorteningService(Boolean fromUrlShorteningService) {
        this.fromUrlShorteningService = fromUrlShorteningService;
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
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
