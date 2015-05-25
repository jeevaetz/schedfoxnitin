/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class HealthCareOption {
    private Integer healthcareOptionsId;
    private String name;
    private Boolean active;
    private Integer orderNum;
    
    public HealthCareOption() {
        
    }

    public HealthCareOption(Record_Set rst) {
        this.healthcareOptionsId = rst.getInt("healthcare_options_id");
        this.name = rst.getString("name");
        this.active = rst.getBoolean("active");
        this.orderNum = rst.getInt("order_num");
    }
    
    /**
     * @return the healthcareOptionsId
     */
    public Integer getHealthcareOptionsId() {
        return healthcareOptionsId;
    }

    /**
     * @param healthcareOptionsId the healthcareOptionsId to set
     */
    public void setHealthcareOptionsId(Integer healthcareOptionsId) {
        this.healthcareOptionsId = healthcareOptionsId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
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
     * @return the orderNum
     */
    public Integer getOrderNum() {
        return orderNum;
    }

    /**
     * @param orderNum the orderNum to set
     */
    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }
}
