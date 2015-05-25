/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class CompanyObj {
    private Integer companyId;
    private String companyName;
    private String companyDb;
    private Integer companyManagementId;
    private String companyStatus;
    private String statusDescription;
    private Date statusModifieddate;
    private String employeeLoginPrefix;
    private String companyUrl;
    private Date dateOfCreation;
    private Integer hasSchedule;
    private Boolean patrolProClient;
    private Boolean alertLateCheckin;
    private Boolean showAllEmployeesOnTracking;
    
    public CompanyObj() {
        
    }

    public CompanyObj(Record_Set rst) {
        try {
            companyId = rst.getInt("company_id");
        } catch (Exception exe) {}
        try {
            companyName = rst.getString("company_name");
        } catch (Exception exe) {}
        try {
            companyDb = rst.getString("company_db");
        } catch (Exception exe) {}
        try {
            companyManagementId = rst.getInt("company_management_id");
        } catch (Exception exe) {}
        try {
            companyStatus = rst.getString("company_status");
        } catch (Exception exe) {}
        try {
            statusDescription = rst.getString("status_description");
        } catch (Exception exe) {}
        try {
            statusModifieddate = rst.getDate("status_modifieddate");
        } catch (Exception exe) {}
        try {
            employeeLoginPrefix = rst.getString("employee_login_prefix");
        } catch (Exception exe) {}
        try {
            companyUrl = rst.getString("company_url");
        } catch (Exception exe) {}
        try {
            dateOfCreation = rst.getDate("date_of_creation");
        } catch (Exception exe) {}
        try {
            hasSchedule = rst.getInt("hasSchedule");
        } catch (Exception exe) {}
        try {
            patrolProClient = rst.getBoolean("patrol_pro_client");
        } catch (Exception exe) {}
        try {
            alertLateCheckin = rst.getBoolean("alert_late_checkin");
        } catch (Exception exe) {}
        try {
            showAllEmployeesOnTracking = rst.getBoolean("show_all_employees_on_tracking");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the companyId
     */
    public Integer getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the companyName
     */
    public String getCompanyName() {
        return companyName;
    }

    /**
     * @param companyName the companyName to set
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * @return the companyDb
     */
    public String getCompanyDb() {
        return companyDb;
    }

    /**
     * @param companyDb the companyDb to set
     */
    public void setCompanyDb(String companyDb) {
        this.companyDb = companyDb;
    }

    /**
     * @return the companyManagementId
     */
    public Integer getCompanyManagementId() {
        return companyManagementId;
    }

    /**
     * @param companyManagementId the companyManagementId to set
     */
    public void setCompanyManagementId(Integer companyManagementId) {
        this.companyManagementId = companyManagementId;
    }

    /**
     * @return the companyStatus
     */
    public String getCompanyStatus() {
        return companyStatus;
    }

    /**
     * @param companyStatus the companyStatus to set
     */
    public void setCompanyStatus(String companyStatus) {
        this.companyStatus = companyStatus;
    }

    /**
     * @return the statusDescription
     */
    public String getStatusDescription() {
        return statusDescription;
    }

    /**
     * @param statusDescription the statusDescription to set
     */
    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    /**
     * @return the statusModifieddate
     */
    public Date getStatusModifieddate() {
        return statusModifieddate;
    }

    /**
     * @param statusModifieddate the statusModifieddate to set
     */
    public void setStatusModifieddate(Date statusModifieddate) {
        this.statusModifieddate = statusModifieddate;
    }

    /**
     * @return the employeeLoginPrefix
     */
    public String getEmployeeLoginPrefix() {
        return employeeLoginPrefix;
    }

    /**
     * @param employeeLoginPrefix the employeeLoginPrefix to set
     */
    public void setEmployeeLoginPrefix(String employeeLoginPrefix) {
        this.employeeLoginPrefix = employeeLoginPrefix;
    }

    /**
     * @return the companyUrl
     */
    public String getCompanyUrl() {
        return companyUrl;
    }

    /**
     * @param companyUrl the companyUrl to set
     */
    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    /**
     * @return the dateOfCreation
     */
    public Date getDateOfCreation() {
        return dateOfCreation;
    }

    /**
     * @param dateOfCreation the dateOfCreation to set
     */
    public void setDateOfCreation(Date dateOfCreation) {
        this.dateOfCreation = dateOfCreation;
    }

    /**
     * @return the hasSchedule
     */
    public Integer getHasSchedule() {
        return hasSchedule;
    }

    /**
     * @param hasSchedule the hasSchedule to set
     */
    public void setHasSchedule(Integer hasSchedule) {
        this.hasSchedule = hasSchedule;
    }

    /**
     * @return the patrolProClient
     */
    public Boolean getPatrolProClient() {
        return patrolProClient;
    }

    /**
     * @param patrolProClient the patrolProClient to set
     */
    public void setPatrolProClient(Boolean patrolProClient) {
        this.patrolProClient = patrolProClient;
    }

    /**
     * @return the alertLateCheckin
     */
    public Boolean getAlertLateCheckin() {
        return alertLateCheckin;
    }

    /**
     * @param alertLateCheckin the alertLateCheckin to set
     */
    public void setAlertLateCheckin(Boolean alertLateCheckin) {
        this.alertLateCheckin = alertLateCheckin;
    }

    /**
     * @return the showAllEmployeesOnTracking
     */
    public Boolean getShowAllEmployeesOnTracking() {
        return showAllEmployeesOnTracking;
    }

    /**
     * @param showAllEmployeesOnTracking the showAllEmployeesOnTracking to set
     */
    public void setShowAllEmployeesOnTracking(Boolean showAllEmployeesOnTracking) {
        this.showAllEmployeesOnTracking = showAllEmployeesOnTracking;
    }
}
