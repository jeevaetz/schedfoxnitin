/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TimeOffAccrual implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer timeOffAccrualId;
    private Integer timeOffSeriesId;
    private String timeOffInterval;
    private String startInterval;
    private String endInterval;
    private Integer days;
    private boolean active;
    private boolean timeOffAccrual;

    public TimeOffAccrual() {
    }

    public TimeOffAccrual(Integer timeOffAccrualId) {
        this.timeOffAccrualId = timeOffAccrualId;
    }

    public TimeOffAccrual(Integer timeOffAccrualId, boolean active) {
        this.timeOffAccrualId = timeOffAccrualId;
        this.active = active;
    }

    public TimeOffAccrual(Record_Set rst) {
        try {
            this.timeOffAccrualId = rst.getInt("time_off_accrual_id");
        } catch (Exception e) {}
        try {
            this.timeOffSeriesId = rst.getInt("time_off_series_id");
        } catch (Exception e) {}
        try {
            this.timeOffInterval = rst.getString("time_off_interval");
        } catch (Exception e) {}
        try {
            this.startInterval = rst.getString("start_interval");
        } catch (Exception e) {}
        try {
            this.endInterval = rst.getString("end_interval");
        } catch (Exception e) {}
        try {
            this.days = rst.getInt("days");
        } catch (Exception e) {}
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception e) {}
        try {
            this.timeOffAccrual = rst.getBoolean("reset_accrual");
        } catch (Exception e) {}
    }

    public Integer getTimeOffAccrualId() {
        return timeOffAccrualId;
    }

    public void setTimeOffAccrualId(Integer timeOffAccrualId) {
        this.timeOffAccrualId = timeOffAccrualId;
    }

    /**
     * @return the timeOffSeriesId
     */
    public Integer getTimeOffSeriesId() {
        return timeOffSeriesId;
    }

    /**
     * @param timeOffSeriesId the timeOffSeriesId to set
     */
    public void setTimeOffSeriesId(Integer timeOffSeriesId) {
        this.timeOffSeriesId = timeOffSeriesId;
    }
    
    public String getTimeOffInterval() {
        return timeOffInterval;
    }

    public void setTimeOffInterval(String timeOffInterval) {
        this.timeOffInterval = timeOffInterval;
    }

    /**
     * @return the startInterval
     */
    public String getStartInterval() {
        return startInterval;
    }

    /**
     * @param startInterval the startInterval to set
     */
    public void setStartInterval(String startInterval) {
        this.startInterval = startInterval;
    }

    /**
     * @return the endInterval
     */
    public String getEndInterval() {
        return endInterval;
    }

    /**
     * @param endInterval the endInterval to set
     */
    public void setEndInterval(String endInterval) {
        this.endInterval = endInterval;
    }

    public Integer getDays() {
        return days;
    }

    public void setDays(Integer days) {
        this.days = days;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeOffAccrualId != null ? timeOffAccrualId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeOffAccrual)) {
            return false;
        }
        TimeOffAccrual other = (TimeOffAccrual) object;
        if ((this.timeOffAccrualId == null && other.timeOffAccrualId != null) || (this.timeOffAccrualId != null && !this.timeOffAccrualId.equals(other.timeOffAccrualId))) {
            return false;
        }
        return true;
    }

    /**
     * @return the timeOffAccrual
     */
    public boolean isTimeOffAccrual() {
        return timeOffAccrual;
    }

    /**
     * @param timeOffAccrual the timeOffAccrual to set
     */
    public void setTimeOffAccrual(boolean timeOffAccrual) {
        this.timeOffAccrual = timeOffAccrual;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.TimeOffAccrual[timeOffAccrualId=" + timeOffAccrualId + "]";
    }


}
