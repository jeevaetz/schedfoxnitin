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
public class ScheduleMaster implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer scheduleMasterId;
    private int clientId;
    private int employeeId;
    private int scheduleMasterDay;
    private int scheduleMasterStart;
    private int scheduleMasterEnd;
    private Date scheduleMasterLastUpdated;
    private Date scheduleMasterDateStarted;
    private Date scheduleMasterDateEnded;
    private long scheduleMasterGroup;
    private int scheduleMasterShift;
    private String scheduleMasterPayOpt;
    private String scheduleMasterBillOpt;
    private int rateCodeId;
    private Integer lastUserChanged;
    private Integer historyLinkId;
    private Integer scheduleMasterType;
    private Integer weeklyNumRotation;

    public ScheduleMaster() {
    }

    public ScheduleMaster(Integer scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
    }

    public ScheduleMaster(Integer scheduleMasterId, int clientId, int employeeId, int scheduleMasterDay, int scheduleMasterStart, int scheduleMasterEnd, Date scheduleMasterLastUpdated, Date scheduleMasterDateStarted, Date scheduleMasterDateEnded, long scheduleMasterGroup, int scheduleMasterShift, int rateCodeId) {
        this.scheduleMasterId = scheduleMasterId;
        this.clientId = clientId;
        this.employeeId = employeeId;
        this.scheduleMasterDay = scheduleMasterDay;
        this.scheduleMasterStart = scheduleMasterStart;
        this.scheduleMasterEnd = scheduleMasterEnd;
        this.scheduleMasterLastUpdated = scheduleMasterLastUpdated;
        this.scheduleMasterDateStarted = scheduleMasterDateStarted;
        this.scheduleMasterDateEnded = scheduleMasterDateEnded;
        this.scheduleMasterGroup = scheduleMasterGroup;
        this.scheduleMasterShift = scheduleMasterShift;
        this.rateCodeId = rateCodeId;
    }

    public Integer getScheduleMasterId() {
        return scheduleMasterId;
    }

    public void setScheduleMasterId(Integer scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
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

    public int getScheduleMasterDay() {
        return scheduleMasterDay;
    }

    public void setScheduleMasterDay(int scheduleMasterDay) {
        this.scheduleMasterDay = scheduleMasterDay;
    }

    public int getScheduleMasterStart() {
        return scheduleMasterStart;
    }

    public void setScheduleMasterStart(int scheduleMasterStart) {
        this.scheduleMasterStart = scheduleMasterStart;
    }

    public int getScheduleMasterEnd() {
        return scheduleMasterEnd;
    }

    public void setScheduleMasterEnd(int scheduleMasterEnd) {
        this.scheduleMasterEnd = scheduleMasterEnd;
    }

    public Date getScheduleMasterLastUpdated() {
        return scheduleMasterLastUpdated;
    }

    public void setScheduleMasterLastUpdated(Date scheduleMasterLastUpdated) {
        this.scheduleMasterLastUpdated = scheduleMasterLastUpdated;
    }

    public Date getScheduleMasterDateStarted() {
        return scheduleMasterDateStarted;
    }

    public void setScheduleMasterDateStarted(Date scheduleMasterDateStarted) {
        this.scheduleMasterDateStarted = scheduleMasterDateStarted;
    }

    public Date getScheduleMasterDateEnded() {
        return scheduleMasterDateEnded;
    }

    public void setScheduleMasterDateEnded(Date scheduleMasterDateEnded) {
        this.scheduleMasterDateEnded = scheduleMasterDateEnded;
    }

    public long getScheduleMasterGroup() {
        return scheduleMasterGroup;
    }

    public void setScheduleMasterGroup(long scheduleMasterGroup) {
        this.scheduleMasterGroup = scheduleMasterGroup;
    }

    public int getScheduleMasterShift() {
        return scheduleMasterShift;
    }

    public void setScheduleMasterShift(int scheduleMasterShift) {
        this.scheduleMasterShift = scheduleMasterShift;
    }

    public String getScheduleMasterPayOpt() {
        return scheduleMasterPayOpt;
    }

    public void setScheduleMasterPayOpt(String scheduleMasterPayOpt) {
        this.scheduleMasterPayOpt = scheduleMasterPayOpt;
    }

    public String getScheduleMasterBillOpt() {
        return scheduleMasterBillOpt;
    }

    public void setScheduleMasterBillOpt(String scheduleMasterBillOpt) {
        this.scheduleMasterBillOpt = scheduleMasterBillOpt;
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

    public Integer getScheduleMasterType() {
        return scheduleMasterType;
    }

    public void setScheduleMasterType(Integer scheduleMasterType) {
        this.scheduleMasterType = scheduleMasterType;
    }

    public Integer getWeeklyNumRotation() {
        return weeklyNumRotation;
    }

    public void setWeeklyNumRotation(Integer weeklyNumRotation) {
        this.weeklyNumRotation = weeklyNumRotation;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (scheduleMasterId != null ? scheduleMasterId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ScheduleMaster)) {
            return false;
        }
        ScheduleMaster other = (ScheduleMaster) object;
        if ((this.scheduleMasterId == null && other.scheduleMasterId != null) || (this.scheduleMasterId != null && !this.scheduleMasterId.equals(other.scheduleMasterId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.ScheduleMaster[scheduleMasterId=" + scheduleMasterId + "]";
    }

}
