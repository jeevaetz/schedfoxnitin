/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import schedfoxlib.controller.TimeOffControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TimeOffSeries implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer timeOffSeriesId;
    private String timeOffSeries;
    private boolean active;
    private Integer employeeTypeId;
    private Integer timeOffTypeId;

    private ArrayList<TimeOffAccrual> timeoffAccrual;

    public TimeOffSeries() {
    }

    public TimeOffSeries(Record_Set rst) {
        try {
            this.timeOffSeriesId = rst.getInt("time_off_series_id");
        } catch (Exception e) {}
        try {
            this.timeOffSeries = rst.getString("time_off_series");
        } catch (Exception e) {}
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception e) {}
        try {
            this.employeeTypeId = rst.getInt("employee_type_id");
        } catch (Exception e) {}
        try {
            this.timeOffTypeId = rst.getInt("time_off_type_id");
        } catch (Exception e) {}
    }

    public Integer getTimeOffSeriesId() {
        return timeOffSeriesId;
    }

    public void setTimeOffSeriesId(Integer timeOffSeriesId) {
        this.timeOffSeriesId = timeOffSeriesId;
    }

    public String getTimeOffSeries() {
        return timeOffSeries;
    }

    public void setTimeOffSeries(String timeOffSeries) {
        this.timeOffSeries = timeOffSeries;
    }

    public boolean getActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void forceRefreshAccruals() {
        this.timeoffAccrual = null;
    }

    public ArrayList<TimeOffAccrual> getTimeOffAccruals(String companyId) {
        if (this.timeoffAccrual == null) {
            TimeOffControllerInterface timeOffInterface = ControllerRegistryAbstract.getTimeoffController(companyId);
            try {
                timeoffAccrual = timeOffInterface.getAccrualForSeries(this);
            } catch (Exception e) {
                timeoffAccrual = new ArrayList<TimeOffAccrual>();
            }
        }
        return this.timeoffAccrual;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (timeOffSeriesId != null ? timeOffSeriesId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TimeOffSeries)) {
            return false;
        }
        TimeOffSeries other = (TimeOffSeries) object;
        if ((this.timeOffSeriesId == null && other.timeOffSeriesId != null) || (this.timeOffSeriesId != null && !this.timeOffSeriesId.equals(other.timeOffSeriesId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.timeOffSeries;
    }

    /**
     * @return the employeeTypeId
     */
    public Integer getEmployeeTypeId() {
        return employeeTypeId;
    }

    /**
     * @param employeeTypeId the employeeTypeId to set
     */
    public void setEmployeeTypeId(Integer employeeTypeId) {
        this.employeeTypeId = employeeTypeId;
    }

    /**
     * @return the timeOffTypeId
     */
    public Integer getTimeOffTypeId() {
        return timeOffTypeId;
    }

    /**
     * @param timeOffTypeId the timeOffTypeId to set
     */
    public void setTimeOffTypeId(Integer timeOffTypeId) {
        this.timeOffTypeId = timeOffTypeId;
    }

}
