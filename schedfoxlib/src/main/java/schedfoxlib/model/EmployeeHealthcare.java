/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.controller.HealthCareControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EmployeeHealthcare {
    private Integer employeeHealthcareId;
    private Integer employeeId;
    private Integer healthcareOptionId;
    private Boolean active;
    private Date setOn;
    private Date inactiveOn;
    
    private HealthCareOption option;
    
    public EmployeeHealthcare() {
        
    }
    
    public EmployeeHealthcare(Record_Set rst) {
        this.employeeHealthcareId = rst.getInt("employee_healthcare_id");
        this.employeeId = rst.getInt("employee_id");
        this.healthcareOptionId = rst.getInt("healthcare_option_id");
        this.active = rst.getBoolean("active");
        try {
            this.setOn = rst.getTimestamp("set_on");
        } catch (Exception exe) {
            this.setOn = null;
        }
        try {
            this.inactiveOn = rst.getTimestamp("inactive_on");
        } catch (Exception exe) {
            this.inactiveOn = null;
        }
    }

    /**
     * @return the employeeHealthcareId
     */
    public Integer getEmployeeHealthcareId() {
        return employeeHealthcareId;
    }

    /**
     * @param employeeHealthcareId the employeeHealthcareId to set
     */
    public void setEmployeeHealthcareId(Integer employeeHealthcareId) {
        this.employeeHealthcareId = employeeHealthcareId;
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
     * @return the healthcareOptionId
     */
    public Integer getHealthcareOptionId() {
        return healthcareOptionId;
    }

    /**
     * @param healthcareOptionId the healthcareOptionId to set
     */
    public void setHealthcareOptionId(Integer healthcareOptionId) {
        this.healthcareOptionId = healthcareOptionId;
    }

    /**
     * @return the active
     */
    public Boolean getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(Boolean active) {
        this.active = active;
    }

    /**
     * @return the setOn
     */
    public Date getSetOn() {
        return setOn;
    }

    /**
     * @param setOn the setOn to set
     */
    public void setSetOn(Date setOn) {
        this.setOn = setOn;
    }

    /**
     * @return the inactiveOn
     */
    public Date getInactiveOn() {
        return inactiveOn;
    }

    /**
     * @param inactiveOn the inactiveOn to set
     */
    public void setInactiveOn(Date inactiveOn) {
        this.inactiveOn = inactiveOn;
    }

    /**
     * @return the option
     */
    public HealthCareOption getOption(String companyId) {
        if (option == null) {
            try {
                HealthCareControllerInterface healthController = ControllerRegistryAbstract.getHealthController(companyId);
                this.option = healthController.getOptionById(this.healthcareOptionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return option;
    }

    /**
     * @param option the option to set
     */
    public void setOption(HealthCareOption option) {
        this.option = option;
    }
}
