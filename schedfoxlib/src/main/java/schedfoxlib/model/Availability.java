/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class Availability implements Serializable, AvailabilityInterface {
    private static final long serialVersionUID = 1L;

    private int availShift;
    private Integer availId;
    private int employeeId;
    private Date availDayOfYear;
    private int availStartTime;
    private int availEndTime;
    private int availMasterRow;
    private int availType;
    private Date availLastUpdated;
    private boolean isMaster;
    private boolean hasNote;
    private AvailStatus availStatus = AvailStatus.APPROVED;
    private boolean isDeleted;
    private BigDecimal hoursCompensated;

    private Date createdOn;
    private Date approvedOn;
    private int createdBy;
    private int approvedBy;

    //Cached object
    private Employee employeeObj;
    private User createdUser;

    /**
     * @return the isMaster
     */
    public boolean isIsMaster() {
        return isMaster;
    }

    /**
     * @param isMaster the isMaster to set
     */
    public void setIsMaster(boolean isMaster) {
        this.isMaster = isMaster;
    }

    /**
     * @param hasNote the hasNote to set
     */
    public void setHasNote(boolean hasNote) {
        this.hasNote = hasNote;
    }

    public boolean hasNote() {
        return this.hasNote;
    }

    /**
     * @return the hoursCompensated
     */
    @Override
    public BigDecimal getHoursCompensated() {
        BigDecimal rounded = hoursCompensated.setScale(2, BigDecimal.ROUND_HALF_UP); 
        return rounded;
    }

    /**
     * @param hoursCompensated the hoursCompensated to set
     */
    public void setHoursCompensated(BigDecimal hoursCompensated) {
        this.hoursCompensated = hoursCompensated;
    }


    public enum AvailStatus {
        REQUESTED(1), APPROVED(2), DENIED(3);

        private int value;

        AvailStatus(int val) {
            this.value = val;
        }

        public Integer getValue() {
            return this.value;
        }
    }
    
    public Availability() {
    }

    public Availability(Record_Set rst) {
        try {
            this.availShift = rst.getInt("avail_shift");
        } catch (Exception e) {}
        try {
            this.availId = rst.getInt("avail_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("avail_day_of_year")) {
                this.availDayOfYear = rst.getDate("avail_day_of_year");
            } else {
                this.availDayOfYear = rst.getDate("doy");
            }
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("stime")) {
                this.availStartTime = rst.getInt("stime");
            } else {
                this.availStartTime = rst.getInt("avail_start_time");
            }
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("etime")) {
                this.availStartTime = rst.getInt("etime");
            } else {
                this.availEndTime = rst.getInt("avail_end_time");
            }
        } catch (Exception e) {}
        try {
            this.availMasterRow = rst.getInt("avail_master_row");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("type")) {
                this.availType = rst.getInt("type");
            } else {
                this.availType = rst.getInt("avail_type");
            }
        } catch (Exception e) {}
        try {
            this.availLastUpdated = rst.getDate("avail_last_updated");
        } catch (Exception e) {}
        try {
            this.approvedOn = rst.getDate("approvedon");
        } catch (Exception e) {}
        try {
            this.approvedBy = rst.getInt("approvedby");
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("requestedon")) {
                this.createdOn = rst.getDate("requestedon");
            } else {
                this.createdOn = rst.getDate("createdon");
            }
        } catch (Exception e) {}
        try {
            if (rst.hasColumn("requestedby")) {
                this.createdBy = rst.getInt("requestedby");
            } else {
                this.createdBy = rst.getInt("createdby");
            }
        } catch (Exception e) {}
        try {
            this.isDeleted = rst.getBoolean("isdeleted");
        } catch (Exception e) {}
        int stats = rst.getInt("avail_status");
        if (stats == 1) {
            this.availStatus = AvailStatus.REQUESTED;
        } else if (stats == 2) {
            this.availStatus = AvailStatus.APPROVED;;
        } else {
            this.availStatus = AvailStatus.DENIED;
        }
        try {
            this.hasNote = rst.getBoolean("hasnote");
        } catch (Exception e) {}
        try {
            this.hoursCompensated = rst.getBigDecimal("hours_compensated");
        } catch (Exception e) {}
    }

    /**
     * @return the createdOn
     */
    public Date getCreatedOn() {
        return createdOn;
    }

    /**
     * @param createdOn the createdOn to set
     */
    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    /**
     * @return the approvedOn
     */
    public Date getApprovedOn() {
        return approvedOn;
    }

    /**
     * @param approvedOn the approvedOn to set
     */
    public void setApprovedOn(Date approvedOn) {
        this.approvedOn = approvedOn;
    }

    /**
     * @return the createdBy
     */
    public int getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(int createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the approvedBy
     */
    public int getApprovedBy() {
        return approvedBy;
    }

    /**
     * @param approvedBy the approvedBy to set
     */
    public void setApprovedBy(int approvedBy) {
        this.approvedBy = approvedBy;
    }

    /**
     * @return the isDeleted
     */
    public boolean isIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public int getAvailShift() {
        return availShift;
    }

    public void setAvailShift(int availShift) {
        this.availShift = availShift;
    }

    public Integer getAvailId() {
        return availId;
    }

    public void setAvailId(Integer availId) {
        this.availId = availId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getAvailDayOfYear() {
        return availDayOfYear;
    }

    public void setAvailDayOfYear(Date availDayOfYear) {
        this.availDayOfYear = availDayOfYear;
    }

    public int getAvailStartTime() {
        return availStartTime;
    }

    public void setAvailStartTime(int availStartTime) {
        this.availStartTime = availStartTime;
    }

    public int getAvailEndTime() {
        return availEndTime;
    }

    public void setAvailEndTime(int availEndTime) {
        this.availEndTime = availEndTime;
    }

    public int getAvailMasterRow() {
        return availMasterRow;
    }

    public void setAvailMasterRow(int availMasterRow) {
        this.availMasterRow = availMasterRow;
    }

    public int getAvailType() {
        return availType;
    }

    public void setAvailType(int availType) {
        this.availType = availType;
    }

    public Date getAvailLastUpdated() {
        return availLastUpdated;
    }

    public void setAvailLastUpdated(Date availLastUpdated) {
        this.availLastUpdated = availLastUpdated;
    }

    /**
     * @return the availStatus
     */
    public AvailStatus getAvailStatus() {
        return availStatus;
    }

    /**
     * @param availStatus the availStatus to set
     */
    public void setAvailStatus(AvailStatus availStatus) {
        this.availStatus = availStatus;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (availId != null ? availId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Availability)) {
            return false;
        }
        Availability other = (Availability) object;
        if ((this.availId == null && other.availId != null) || (this.availId != null && !this.availId.equals(other.availId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.Availability[availId=" + availId + "]";
    }

    public int getAvailabilityId() {
        return this.getAvailId();
    }

    public int getStartTime() {
        return this.getAvailStartTime();
    }

    public int getEndTime() {
        return this.getAvailEndTime();
    }

    public String getAvailIdStr() {
        return this.getAvailId() + "";
    }

    public Date getDateRequested() {
        return this.getCreatedOn();
    }
    
    public User getUserCreatedBy(String companyId) {
        UserControllerInterface userController = ControllerRegistryAbstract.getUserController(companyId);
        if (createdUser == null) {
            try {
                createdUser = userController.getUserById(this.createdBy);
            } catch (Exception e) {}
        }
        return createdUser;
    }

    public Employee getEmployeeObj(String companyId) {
        if (employeeObj == null) {
            EmployeeControllerInterface empController = ControllerRegistryAbstract.getEmployeeController(companyId);
            try {
                employeeObj = empController.getEmployeeById(employeeId);
            } catch (Exception e) {}
        }
        return employeeObj;
    }
}
