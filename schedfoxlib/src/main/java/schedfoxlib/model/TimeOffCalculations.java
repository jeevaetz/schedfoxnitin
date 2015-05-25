/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.controller.GenericControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TimeOffCalculations {

    private Integer employeeId;
    private Date employeeHireDate;
    private String timeWorked;
    private String startInterval;
    private String endInterval;
    private Date startDate;
    private Date endDate;
    private boolean qualifies;
    private String timeOffInterval;
    private Integer days;
    private String qualifiedTime;
    private Integer daysOff;
    private boolean resetAccrual;

    public TimeOffCalculations(Record_Set rst) {
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {
        }
        try {
            this.employeeHireDate = rst.getDate("employee_hire_date");
        } catch (Exception e) {
        }
        try {
            this.timeWorked = rst.getString("time_worked");
        } catch (Exception e) {
        }
        try {
            this.startInterval = rst.getString("start_interval");
        } catch (Exception e) {
        }
        try {
            this.endInterval = rst.getString("end_interval");
        } catch (Exception e) {
        }
        try {
            this.startDate = rst.getDate("start_date");
        } catch (Exception e) {
        }
        try {
            this.endDate = rst.getDate("end_date");
        } catch (Exception e) {
        }
        try {
            this.qualifies = rst.getBoolean("qualifies");
        } catch (Exception e) {
        }
        try {
            this.timeOffInterval = rst.getString("time_off_interval");
        } catch (Exception e) {
        }
        try {
            this.days = rst.getInt("days");
        } catch (Exception e) {
        }
        try {
            this.qualifiedTime = rst.getString("qualified_time");
        } catch (Exception e) {
        }
        try {
            this.daysOff = rst.getInt("days_off");
        } catch (Exception e) {
        }
        try {
            this.resetAccrual = rst.getBoolean("reset_accrual");
        } catch (Exception e) {
        }

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
     * @return the employeeHireDate
     */
    public Date getEmployeeHireDate() {
        return employeeHireDate;
    }

    /**
     * @param employeeHireDate the employeeHireDate to set
     */
    public void setEmployeeHireDate(Date employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

    /**
     * @return the timeWorked
     */
    public String getTimeWorked() {
        return timeWorked;
    }

    /**
     * @param timeWorked the timeWorked to set
     */
    public void setTimeWorked(String timeWorked) {
        this.timeWorked = timeWorked;
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

    /**
     * @return the qualifies
     */
    public boolean isQualifies() {
        return qualifies;
    }

    /**
     * @param qualifies the qualifies to set
     */
    public void setQualifies(boolean qualifies) {
        this.qualifies = qualifies;
    }

    /**
     * @return the timeOffInterval
     */
    public String getTimeOffInterval() {
        return timeOffInterval;
    }

    /**
     * @param timeOffInterval the timeOffInterval to set
     */
    public void setTimeOffInterval(String timeOffInterval) {
        this.timeOffInterval = timeOffInterval;
    }

    /**
     * @return the days
     */
    public Integer getDays() {
        return days;
    }

    /**
     * @param days the days to set
     */
    public void setDays(Integer days) {
        this.days = days;
    }

    /**
     * @return the qualifiedTime
     */
    public String getQualifiedTime() {
        return qualifiedTime;
    }

    /**
     * @param qualifiedTime the qualifiedTime to set
     */
    public void setQualifiedTime(String qualifiedTime) {
        this.qualifiedTime = qualifiedTime;
    }

    /**
     * @return the daysOff
     */
    public Integer getDaysOff() {
        return daysOff;
    }

    /**
     * @param daysOff the daysOff to set
     */
    public void setDaysOff(Integer daysOff) {
        this.daysOff = daysOff;
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

    /**
     * @return the resetAccrual
     */
    public boolean isResetAccrual() {
        return resetAccrual;
    }

    /**
     * @param resetAccrual the resetAccrual to set
     */
    public void setResetAccrual(boolean resetAccrual) {
        this.resetAccrual = resetAccrual;
    }

    public boolean isExpired() {
        boolean expired = false;
        GenericControllerInterface genericInterface = ControllerRegistryAbstract.getGenericController("");
        Date currDate = new Date(genericInterface.getCurrentTimeMillis());
        if (this.isResetAccrual() && this.getEndDate().compareTo(currDate) < 0) {
            expired = true;
        }
        return expired;
    }
}
