/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MobileFormFillout implements Serializable {
    private Integer mobileFormFilloutId;
    private Integer employeeId;
    private Date dateEntered;
    private Integer mobileFormId;
    private Integer clientId;
    private Boolean active;
    private Date notificationSent;
    
    public MobileFormFillout() {
        active = true;
    }
    
    public MobileFormFillout(Record_Set rst) {
        try {
            mobileFormFilloutId = rst.getInt("mobile_form_fillout_id");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            dateEntered = rst.getTimestamp("date_entered");
        } catch (Exception exe) {}
        try {
            mobileFormId = rst.getInt("mobile_form_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
        try {
            notificationSent = rst.getTimestamp("notification_sent");
        } catch (Exception exe) {}
    }

    /**
     * @return the mobileFormFilloutId
     */
    public Integer getMobileFormFilloutId() {
        return mobileFormFilloutId;
    }

    /**
     * @param mobileFormFilloutId the mobileFormFilloutId to set
     */
    public void setMobileFormFilloutId(Integer mobileFormFilloutId) {
        this.mobileFormFilloutId = mobileFormFilloutId;
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
     * @return the dateEntered
     */
    public Date getDateEntered() {
        return dateEntered;
    }

    /**
     * @param dateEntered the dateEntered to set
     */
    public void setDateEntered(Date dateEntered) {
        this.dateEntered = dateEntered;
    }

    /**
     * @return the mobileFormId
     */
    public Integer getMobileFormId() {
        return mobileFormId;
    }

    /**
     * @param mobileFormId the mobileFormId to set
     */
    public void setMobileFormId(Integer mobileFormId) {
        this.mobileFormId = mobileFormId;
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
     * @return the notificationSent
     */
    public Date getNotificationSent() {
        return notificationSent;
    }

    /**
     * @param notificationSent the notificationSent to set
     */
    public void setNotificationSent(Date notificationSent) {
        this.notificationSent = notificationSent;
    }
}
