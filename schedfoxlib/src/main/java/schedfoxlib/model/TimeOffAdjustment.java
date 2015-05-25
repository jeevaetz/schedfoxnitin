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
 * @author user
 */
public class TimeOffAdjustment implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer timeOffAdjustmentId;
    private int employeeId;
    private Date dateAdded;
    private int adjustedBy;
    private String adjustmentNotes;
    private int adjustment;
    private int timeOffTypeId;

    public TimeOffAdjustment() {
    }

    public TimeOffAdjustment(Record_Set rst) {
        try {
            this.timeOffAdjustmentId = rst.getInt("time_off_adjustment_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.dateAdded = rst.getDate("date_added");
        } catch (Exception e) {}
        try {
            this.adjustedBy = rst.getInt("adjusted_by");
        } catch (Exception e) {}
        try {
            this.adjustmentNotes = rst.getString("adjustment_notes");
        } catch (Exception e) {}
        try {
            this.adjustment = rst.getInt("adjustment");
        } catch (Exception e) {}
        try {
            this.timeOffTypeId = rst.getInt("time_off_type_id");
        } catch (Exception e) {}
    }

    public Integer getTimeOffAdjustmentId() {
        return timeOffAdjustmentId;
    }

    public void setTimeOffAdjustmentId(Integer timeOffAdjustmentId) {
        this.timeOffAdjustmentId = timeOffAdjustmentId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public int getAdjustedBy() {
        return adjustedBy;
    }

    public void setAdjustedBy(int adjustedBy) {
        this.adjustedBy = adjustedBy;
    }

    public String getAdjustmentNotes() {
        return adjustmentNotes;
    }

    public void setAdjustmentNotes(String adjustmentNotes) {
        this.adjustmentNotes = adjustmentNotes;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeOffAdjustmentId != null ? timeOffAdjustmentId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeOffAdjustment)) {
            return false;
        }
        TimeOffAdjustment other = (TimeOffAdjustment) object;
        if ((this.timeOffAdjustmentId == null && other.timeOffAdjustmentId != null) || (this.timeOffAdjustmentId != null && !this.timeOffAdjustmentId.equals(other.timeOffAdjustmentId))) {
            return false;
        }
        return true;
    }

    /**
     * @return the adjustment
     */
    public int getAdjustment() {
        return adjustment;
    }

    /**
     * @param adjustment the adjustment to set
     */
    public void setAdjustment(int adjustment) {
        this.adjustment = adjustment;
    }

    /**
     * @return the timeOffTypeId
     */
    public int getTimeOffTypeId() {
        return timeOffTypeId;
    }

    /**
     * @param timeOffTypeId the timeOffTypeId to set
     */
    public void setTimeOffTypeId(int timeOffTypeId) {
        this.timeOffTypeId = timeOffTypeId;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.TimeOffAdjustment[timeOffAdjustmentId=" + timeOffAdjustmentId + "]";
    }
}
