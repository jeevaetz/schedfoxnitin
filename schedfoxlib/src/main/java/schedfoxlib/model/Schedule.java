/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;


/**
 *
 * @author user
 */
public class Schedule implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer scheduleId;
    private int clientId;
    private int employeeId;
    private int scheduleOverride;
    private Date scheduleDate;
    private int scheduleStart;
    private int scheduleEnd;
    private int scheduleDay;
    private int scheduleWeek;
    private int scheduleType;
    private int scheduleMasterId;
    private long scheduleGroup;
    private Date scheduleLastUpdated;
    private short scheduleIsDeleted;
    private String schedulePayOpt;
    private String scheduleBillOpt;
    private int rateCodeId;
    private Integer lastUserChanged;
    private Integer historyLinkId;

    public Schedule() {
    }

    public Schedule(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public Schedule(Integer scheduleId, int clientId, int employeeId, int scheduleOverride, Date scheduleDate, int scheduleStart, int scheduleEnd, int scheduleDay, int scheduleWeek, int scheduleType, int scheduleMasterId, long scheduleGroup, Date scheduleLastUpdated, short scheduleIsDeleted, int rateCodeId) {
        this.scheduleId = scheduleId;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.scheduleOverride = scheduleOverride;
        this.scheduleDate = scheduleDate;
        this.scheduleStart = scheduleStart;
        this.scheduleEnd = scheduleEnd;
        this.scheduleDay = scheduleDay;
        this.scheduleWeek = scheduleWeek;
        this.scheduleType = scheduleType;
        this.scheduleMasterId = scheduleMasterId;
        this.scheduleGroup = scheduleGroup;
        this.scheduleLastUpdated = scheduleLastUpdated;
        this.scheduleIsDeleted = scheduleIsDeleted;
        this.rateCodeId = rateCodeId;
    }

    public Integer getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(Integer scheduleId) {
        this.scheduleId = scheduleId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getScheduleOverride() {
        return scheduleOverride;
    }

    public void setScheduleOverride(int scheduleOverride) {
        this.scheduleOverride = scheduleOverride;
    }

    public Date getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(Date scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public int getScheduleStart() {
        return scheduleStart;
    }

    public void setScheduleStart(int scheduleStart) {
        this.scheduleStart = scheduleStart;
    }

    public int getScheduleEnd() {
        return scheduleEnd;
    }

    public void setScheduleEnd(int scheduleEnd) {
        this.scheduleEnd = scheduleEnd;
    }

    public int getScheduleDay() {
        return scheduleDay;
    }

    public void setScheduleDay(int scheduleDay) {
        this.scheduleDay = scheduleDay;
    }

    public int getScheduleWeek() {
        return scheduleWeek;
    }

    public void setScheduleWeek(int scheduleWeek) {
        this.scheduleWeek = scheduleWeek;
    }

    public int getScheduleType() {
        return scheduleType;
    }

    public void setScheduleType(int scheduleType) {
        this.scheduleType = scheduleType;
    }

    public int getScheduleMasterId() {
        return scheduleMasterId;
    }

    public void setScheduleMasterId(int scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
    }

    public long getScheduleGroup() {
        return scheduleGroup;
    }

    public void setScheduleGroup(long scheduleGroup) {
        this.scheduleGroup = scheduleGroup;
    }

    public Date getScheduleLastUpdated() {
        return scheduleLastUpdated;
    }

    public void setScheduleLastUpdated(Date scheduleLastUpdated) {
        this.scheduleLastUpdated = scheduleLastUpdated;
    }

    public short getScheduleIsDeleted() {
        return scheduleIsDeleted;
    }

    public void setScheduleIsDeleted(short scheduleIsDeleted) {
        this.scheduleIsDeleted = scheduleIsDeleted;
    }

    public String getSchedulePayOpt() {
        return schedulePayOpt;
    }

    public void setSchedulePayOpt(String schedulePayOpt) {
        this.schedulePayOpt = schedulePayOpt;
    }

    public String getScheduleBillOpt() {
        return scheduleBillOpt;
    }

    public void setScheduleBillOpt(String scheduleBillOpt) {
        this.scheduleBillOpt = scheduleBillOpt;
    }

    public int getRateCodeId() {
        return rateCodeId;
    }

    public void setRateCodeId(int rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    public Integer getLastUserChanged() {
        return lastUserChanged;
    }

    public void setLastUserChanged(Integer lastUserChanged) {
        this.lastUserChanged = lastUserChanged;
    }

    public Integer getHistoryLinkId() {
        return historyLinkId;
    }

    public void setHistoryLinkId(Integer historyLinkId) {
        this.historyLinkId = historyLinkId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleId != null ? scheduleId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Schedule)) {
            return false;
        }
        Schedule other = (Schedule) object;
        if ((this.scheduleId == null && other.scheduleId != null) || (this.scheduleId != null && !this.scheduleId.equals(other.scheduleId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.Schedule[scheduleId=" + scheduleId + "]";
    }

}
