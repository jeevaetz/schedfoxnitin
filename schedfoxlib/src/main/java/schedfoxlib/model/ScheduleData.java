/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ScheduleData implements Serializable {
    private String scheduleId;
    private Integer clientId;
    private String clientName;
    private String employeeName;
    private Integer scheduleMasterId;
    private Integer employeeId;
    private Integer dayOfWeek;
    private Integer startTime;
    private Integer endTime;
    private Date lastUpdated;
    private Date startDate;
    private Date endDate;
    private Long gp;
    private String payOpt;
    private String billOpt;
    private Integer rateCodeId;
    private Date date;
    private Integer trainerId;
    private Integer branchId;
    private Integer isDeleted;
    private Integer type;

    //Lazy loaded objects
    private transient Employee employee;
    private transient Client client;
    
    private boolean displayEmployee = false;
    
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
    
    
    public ScheduleData(){
        
    }
    public ScheduleData(Record_Set rst) {
        if (rst.hasColumn("sid")) {
            scheduleId = rst.getString("sid");
            clientId = rst.getInt("cid");
            clientName = rst.getString("cname");
            employeeName = rst.getString("ename");
            scheduleMasterId = rst.getInt("smid");
            employeeId = rst.getInt("eid");
            dayOfWeek = rst.getInt("dow");
            startTime = rst.getInt("start_time");
            endTime = rst.getInt("end_time");
            lastUpdated = rst.getDate("lu");
            startDate = rst.getDate("sdate");
            endDate = rst.getDate("edate");
            gp = (long)rst.getInt("gp");
            payOpt = rst.getString("pay_opt");
            billOpt = rst.getString("bill_opt");
            rateCodeId = rst.getInt("rate_code_id");
            date = rst.getDate("date");
            trainerId = rst.getInt("trainerid");
            branchId = rst.getInt("branch_id");
            isDeleted = rst.getInt("isdeleted");
            type = rst.getInt("type");
        } else if (rst.hasColumn("schedule_master_day")) {
            scheduleId = (rst.getInt("schedule_master_id") * -1) + "";
            clientId = rst.getInt("client_id");
            clientName = rst.getString("cname");
            employeeName = rst.getString("ename");
            scheduleMasterId = rst.getInt("schedule_master_id");
            employeeId = rst.getInt("employee_id");
            dayOfWeek = rst.getInt("schedule_master_day");
            startTime = rst.getInt("schedule_master_start");
            endTime = rst.getInt("schedule_master_end");
            lastUpdated = rst.getDate("schedule_master_last_updated");
            startDate = rst.getDate("schedule_master_date_started");
            endDate = rst.getDate("schedule_master_date_ended");
            gp = (long)rst.getInt("schedule_master_group");
            payOpt = rst.getString("schedule_master_pay_opt");
            billOpt = rst.getString("schedule_master_bill_opt");
            rateCodeId = rst.getInt("rate_code_id");
        } else if (rst.hasColumn("schedule_id")) {
            scheduleId = rst.getInt("schedule_id") + "";
            clientId = rst.getInt("client_id");
            clientName = rst.getString("cname");
            employeeName = rst.getString("ename");
            scheduleMasterId = rst.getInt("schedule_master_id");
            employeeId = rst.getInt("employee_id");
            dayOfWeek = rst.getInt("schedule_day");
            startTime = rst.getInt("schedule_start");
            endTime = rst.getInt("schedule_end");
            date = rst.getDate("schedule_date");
            lastUpdated = rst.getDate("schedule_last_updated");
            startDate = rst.getDate("schedule_date");
            endDate = rst.getDate("schedule_date");
            gp = (long)rst.getInt("schedule_group");
            payOpt = rst.getString("schedule_pay_opt");
            billOpt = rst.getString("schedule_bill_opt");
            rateCodeId = rst.getInt("rate_code_id");
        }
    }

    public boolean isMaster() {
        try {
            return Integer.parseInt(scheduleId) < 0;
        } catch (Exception exe) {
            return true;
        }
    }
    
    /**
     * @return the scheduleId
     */
    public String getScheduleId() {
        return scheduleId;
    }

    /**
     * @param scheduleId the scheduleId to set
     */
    public void setScheduleId(String scheduleId) {
        this.scheduleId = scheduleId;
    }

    /**
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the employeeName
     */
    public String getEmployeeName() {
        return employeeName;
    }

    /**
     * @param employeeName the employeeName to set
     */
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    /**
     * @return the scheduleMasterId
     */
    public Integer getScheduleMasterId() {
        return scheduleMasterId;
    }

    /**
     * @param scheduleMasterId the scheduleMasterId to set
     */
    public void setScheduleMasterId(Integer scheduleMasterId) {
        this.scheduleMasterId = scheduleMasterId;
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
     * @return the dayOfWeek
     */
    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    /**
     * @param dayOfWeek the dayOfWeek to set
     */
    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
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
    
    public void setStartTimeStr(String str) {
        try {
            this.startTime = parseTimes(str);
        } catch (Exception exe) {}
    }
    
    public void setEndTimeStr(String str) {
        try {
            this.endTime = parseTimes(str);
        } catch (Exception exe) {}
    }
    
    private Integer parseTimes(String str) {
        if (str.length() < 3) {
            return Integer.parseInt(str);
        } else if (str.length() < 4) {
            return (Integer.parseInt(str.substring(0, 1)) * 60) + Integer.parseInt(str.substring(1));
        } else {
            return (Integer.parseInt(str.substring(0, 2)) * 60) + Integer.parseInt(str.substring(2));
        }
    }
    
    public String getStartTimeStr() {
        try {
            String hours = (this.startTime / 60) + "";
            String minutes = (this.startTime % 60) + "";
            if (hours.length() == 1) {
                hours = "0" + hours;
            }
            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }
            return hours + minutes;
        } catch (Exception exe) {
            return "";
        }
    }
    
    public String getEndTimeStr() {
        try {
            String hours = (this.endTime / 60) + "";
            String minutes = (this.endTime % 60) + "";
            if (hours.length() == 1) {
                hours = "0" + hours;
            }
            if (minutes.length() == 1) {
                minutes = "0" + minutes;
            }
            return hours + minutes;
        } catch (Exception exe) {
            return "";
        }
    }

//    public double getTotalTimeWorked() {
//        
//    }
    
    /**
     * @return the lastUpdated
     */
    public Date getLastUpdated() {
        return lastUpdated;
    }

    /**
     * @param lastUpdated the lastUpdated to set
     */
    public void setLastUpdated(Date lastUpdated) {
        this.lastUpdated = lastUpdated;
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
     * @return the gp
     */
    public Long getGp() {
        return gp;
    }

    /**
     * @param gp the gp to set
     */
    public void setGp(Long gp) {
        this.gp = gp;
    }

    /**
     * @return the payOpt
     */
    public String getPayOpt() {
        return payOpt;
    }

    /**
     * @param payOpt the payOpt to set
     */
    public void setPayOpt(String payOpt) {
        this.payOpt = payOpt;
    }

    /**
     * @return the billOpt
     */
    public String getBillOpt() {
        return billOpt;
    }

    /**
     * @param billOpt the billOpt to set
     */
    public void setBillOpt(String billOpt) {
        this.billOpt = billOpt;
    }

    /**
     * @return the rateCodeId
     */
    public Integer getRateCodeId() {
        return rateCodeId;
    }

    /**
     * @param rateCodeId the rateCodeId to set
     */
    public void setRateCodeId(Integer rateCodeId) {
        this.rateCodeId = rateCodeId;
    }

    /**
     * @return the date
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param date the date to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the trainerId
     */
    public Integer getTrainerId() {
        return trainerId;
    }

    /**
     * @param trainerId the trainerId to set
     */
    public void setTrainerId(Integer trainerId) {
        this.trainerId = trainerId;
    }

    /**
     * @return the branchId
     */
    public Integer getBranchId() {
        return branchId;
    }

    /**
     * @param branchId the branchId to set
     */
    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    /**
     * @return the isDeleted
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    /**
     * @return the type
     */
    public Integer getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(Integer type) {
        this.type = type;
    }
    
    /**
     * Make sure you know what you are doing here as it will cause the lazy fetch
     * to not work...should really be invoked by controllers only.
     * @param employee 
     */
    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
    
    public Employee getEmployee() {
        return this.employee;
    }
    
    public boolean isShiftReconciled() {
        return isShiftType(ShiftTypeClass.SHIFT_RECONCILED);
    }
    
    public boolean isShiftUnreconciled() {
        return isShiftType(ShiftTypeClass.SHIFT_UNRECONCILED);
    }
    
    public boolean isShiftType(int shiftType) {
        ShiftTypeClass shiftTypeObj = new ShiftTypeClass(this.getType() + "");
        return shiftTypeObj.isShiftType(shiftType);
    }
    
    /**
     * Lazy loads our officer object - if it has not been loaded yet
     * @param companyId
     * @return 
     */
    public Employee getEmployee(String companyId) {
        if (this.employee == null) {
            try {
                EmployeeControllerInterface employeeController = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.employee = employeeController.getEmployeeById(this.employeeId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this.employee;
    }

    /**
     * @return the client
     */
    public Client getClient() {
        return client;
    }

    /**
     * @param client the client to set
     */
    public void setClient(Client client) {
        this.client = client;
    }
    
    @Override
    public String toString() {
        try {
            if (!displayEmployee) {
                if (getDate() == null) {
                    return this.client.getClientName() + " - " + getStartTimeStr() + " to " + getEndTimeStr() + " " + myFormat.format(this.getStartDate()) + " to " + myFormat.format(this.getEndDate());
                } else {
                    return this.client.getClientName() + " - " + getStartTimeStr() + " to " + getEndTimeStr() + " " + myFormat.format(getDate());
                }
            } else {
                return this.employee.getEmployeeFullName() + " - " + getStartTimeStr() + " to " + getEndTimeStr() + " " + myFormat.format(getDate());
            }
        } catch (Exception exe) {}
        return "";
    }

    /**
     * @return the displayEmployee
     */
    public boolean isDisplayEmployee() {
        return displayEmployee;
    }

    /**
     * @param displayEmployee the displayEmployee to set
     */
    public void setDisplayEmployee(boolean displayEmployee) {
        this.displayEmployee = displayEmployee;
    }
}
