/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.controller.UserControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class SalesEquipment implements Serializable {

    private Integer salesEquipmentId;
    private Integer clientEquipmentId;
    private Integer userId;
    private Date dateAssigned;
    private Boolean active;

    private transient User user;
    private transient ClientEquipment clientEquipment;

    public SalesEquipment() {

    }

    public SalesEquipment(Record_Set rst) {
        this.salesEquipmentId = rst.getInt("sales_equipment_id");
        this.clientEquipmentId = rst.getInt("client_equipment_id");
        this.userId = rst.getInt("user_id");
        this.dateAssigned = rst.getTimestamp("date_assigned");
        this.active = rst.getBoolean("active");
    }

    /**
     * @return the salesEquipmentId
     */
    public Integer getSalesEquipmentId() {
        return salesEquipmentId;
    }

    /**
     * @param salesEquipmentId the salesEquipmentId to set
     */
    public void setSalesEquipmentId(Integer salesEquipmentId) {
        this.salesEquipmentId = salesEquipmentId;
    }

    /**
     * @return the clientEquipmentId
     */
    public Integer getClientEquipmentId() {
        return clientEquipmentId;
    }

    /**
     * @param clientEquipmentId the clientEquipmentId to set
     */
    public void setClientEquipmentId(Integer clientEquipmentId) {
        this.clientEquipmentId = clientEquipmentId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the dateAssigned
     */
    public Date getDateAssigned() {
        return dateAssigned;
    }

    /**
     * @param dateAssigned the dateAssigned to set
     */
    public void setDateAssigned(Date dateAssigned) {
        this.dateAssigned = dateAssigned;
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

    public User getUser(String companyId) {
        if (this.user == null) {
            try {
                UserControllerInterface clientController = ControllerRegistryAbstract.getUserController(companyId);
                this.user = clientController.getUserById(this.userId);
            } catch (Exception exe) {}
        }
        return this.user;
    }
    
    public ClientEquipment getEquipment(String companyId) {
        if (this.clientEquipment == null) {
            try {
                EquipmentControllerInterface clientController = ControllerRegistryAbstract.getEquipmentController(companyId);
                this.clientEquipment = clientController.getEquipmentByPrimaryId(this.clientEquipmentId);
            } catch (Exception exe) {}
        }
        return this.clientEquipment;
    }
}
