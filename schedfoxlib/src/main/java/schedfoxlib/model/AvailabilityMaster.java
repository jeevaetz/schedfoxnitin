/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class AvailabilityMaster implements Serializable, AvailabilityInterface {
    private static final long serialVersionUID = 1L;

    private Integer availMId;
    private int employeeId;
    private Date availMDateStarted;
    private Date availMDateEnded;
    private boolean hasNote;
    private int availMTimeStarted;
    private int availMRow;
    private int availMTimeEnded;
    private int availMDayOfWeek;
    private Date availMLastUpdated;

    private int dow;

    //Cached object
    private Employee employeeObj;

    public AvailabilityMaster() {
    }

    public AvailabilityMaster(Integer availMId) {
        this.availMId = availMId;
    }

    public AvailabilityMaster(Record_Set rst) {
        try {
            this.availMId = rst.getInt("avail_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.availMDateStarted = rst.getDate("avail_m_date_started");
        } catch (Exception e) {}
        try {
            this.availMDateEnded = rst.getDate("avail_m_date_ended");
        } catch (Exception e) {}
        try {
            this.availMTimeStarted = rst.getInt("stime");
        } catch (Exception e) {}
        try {
            this.availMTimeEnded = rst.getInt("etime");
        } catch (Exception e) {}
        try {
            this.dow = rst.getInt("dow");
        } catch (Exception e) {}
        try {
            this.hasNote = rst.getBoolean("hasnote");
        } catch (Exception e) {}
    }

    public Integer getAvailMId() {
        return availMId;
    }

    public void setAvailMId(Integer availMId) {
        this.availMId = availMId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getAvailMDateStarted() {
        return availMDateStarted;
    }

    public void setAvailMDateStarted(Date availMDateStarted) {
        this.availMDateStarted = availMDateStarted;
    }

    public Date getAvailMDateEnded() {
        return availMDateEnded;
    }

    public void setAvailMDateEnded(Date availMDateEnded) {
        this.availMDateEnded = availMDateEnded;
    }

    public int getAvailMTimeStarted() {
        return availMTimeStarted;
    }

    public void setAvailMTimeStarted(int availMTimeStarted) {
        this.availMTimeStarted = availMTimeStarted;
    }

    public int getAvailMRow() {
        return availMRow;
    }

    public void setAvailMRow(int availMRow) {
        this.availMRow = availMRow;
    }

    public int getAvailMTimeEnded() {
        return availMTimeEnded;
    }

    public void setAvailMTimeEnded(int availMTimeEnded) {
        this.availMTimeEnded = availMTimeEnded;
    }

    public int getAvailMDayOfWeek() {
        return availMDayOfWeek;
    }

    public void setAvailMDayOfWeek(int availMDayOfWeek) {
        this.availMDayOfWeek = availMDayOfWeek;
    }

    public Date getAvailMLastUpdated() {
        return availMLastUpdated;
    }

    public void setAvailMLastUpdated(Date availMLastUpdated) {
        this.availMLastUpdated = availMLastUpdated;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (availMId != null ? availMId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AvailabilityMaster)) {
            return false;
        }
        AvailabilityMaster other = (AvailabilityMaster) object;
        if ((this.availMId == null && other.availMId != null) || (this.availMId != null && !this.availMId.equals(other.availMId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.AvailabilityMaster[availMId=" + availMId + "]";
    }

    @Override
    public int getAvailabilityId() {
        return this.getAvailMId();
    }

    @Override
    public int getStartTime() {
        return this.getAvailMTimeStarted();
    }

    @Override
    public int getEndTime() {
        return this.getAvailMTimeEnded();
    }

    @Override
    public boolean isIsDeleted() {
        return false;
    }

    @Override
    public int getAvailType() {
        return 1;
    }

    @Override
    public String getAvailIdStr() {
        return this.getAvailMId() + "";
    }

    @Override
    public Date getDateRequested() {
        return new Date();
    }

    @Override
    public User getUserCreatedBy(String companyId) {
        return null;
    }

    /**
     * @return the dow
     */
    public int getDow() {
        return dow;
    }

    /**
     * @param dow the dow to set
     */
    public void setDow(int dow) {
        this.dow = dow;
    }

    @Override
    public Employee getEmployeeObj(String companyId) {
        if (employeeObj == null) {
            EmployeeControllerInterface empController = ControllerRegistryAbstract.getEmployeeController(companyId);
            try {
                employeeObj = empController.getEmployeeById(employeeId);
            } catch (Exception e) {}
        }
        return employeeObj;
    }

    @Override
    public boolean isIsMaster() {
        return true;
    }

    /**
     * @param hasNote the hasNote to set
     */
    public void setHasNote(boolean hasNote) {
        this.hasNote = hasNote;
    }

    @Override
    public boolean hasNote() {
        return hasNote;
    }

    @Override
    public BigDecimal getHoursCompensated() {
        return new BigDecimal(0);
    }
}
