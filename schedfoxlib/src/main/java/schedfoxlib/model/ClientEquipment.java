/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.ClientControllerInterface;
import schedfoxlib.controller.EquipmentControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientEquipment implements EntityEquipment, Serializable {
    private Integer clientEquipmentId;
    private Integer clientId;
    private Integer routeId;
    private Integer equipmentId;
    private Date dateIssued;
    private Integer issuedBy;
    private Date dateReturned;
    private Integer receivedBy;
    private Integer returnedCondition;
    private String uniqueId;
    private String nickname;
    private String mdn;
    private Date dateOfContractRenewal;
    private BigDecimal cost;
    private String serialNumber;
    private String phoneNumber;
    private Integer vendorId;
    private Boolean active;
    
    //Lazy loaded objects
    private transient Client client;
    private transient Equipment equipment;
    private transient ArrayList<ClientEquipmentContact> contacts;
    
    //Not always around
    private transient Date lastContactDate;
    private transient String workingVersion;
    
    private transient Boolean hasGPS;
    //private transient 
    
    public ClientEquipment() {
        
    }
    
    public ClientEquipment(Record_Set rst) {
        try {
            clientEquipmentId = rst.getInt("client_equipment_id");
        } catch (Exception e) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception e) {}
        try {
            routeId = rst.getInt("route_id");
        } catch (Exception e) {}
        try {
            equipmentId = rst.getInt("equipment_id");
        } catch (Exception e) {}
        try {
            dateIssued = rst.getDate("date_issued");
        } catch (Exception e) {}
        try {
            issuedBy = rst.getInt("issued_by");
        } catch (Exception e) {}
        try {
            dateReturned = rst.getDate("date_returned");
        } catch (Exception e) {}
        try {
            receivedBy = rst.getInt("received_by");
        } catch (Exception e) {}
        try {
            returnedCondition = rst.getInt("returned_condition");
        } catch (Exception e) {}
        try {
            uniqueId = rst.getString("unique_id");
        } catch (Exception e) {}
        try {
            nickname = rst.getString("nickname");
        } catch (Exception e) {}
        try {
            mdn = rst.getString("mdn");
        } catch (Exception exe) {}
        try {
            lastContactDate = rst.getTimestamp("last_contact_date");
        } catch (Exception exe) {}
        try {
            workingVersion = rst.getString("working_version");
        } catch (Exception exe) {}
        try {
            dateOfContractRenewal = rst.getDate("date_of_contract_renewal");
        } catch (Exception exe) {}
        try {
            cost = rst.getBigDecimal("cost");
        } catch (Exception exe) {}
        try {
            serialNumber = rst.getString("serial_number");
        } catch (Exception exe) {}
        try {
            phoneNumber = rst.getString("phone_number");
        } catch (Exception exe) {}
        try {
            vendorId = rst.getInt("vendor_id");
        } catch (Exception exe) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
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
     * @return the equipmentId
     */
    public Integer getEquipmentId() {
        return equipmentId;
    }

    /**
     * @param equipmentId the equipmentId to set
     */
    public void setEquipmentId(Integer equipmentId) {
        this.equipmentId = equipmentId;
    }

    /**
     * @return the dateIssued
     */
    public Date getDateIssued() {
        return dateIssued;
    }

    /**
     * @param dateIssued the dateIssued to set
     */
    public void setDateIssued(Date dateIssued) {
        this.dateIssued = dateIssued;
    }

    /**
     * @return the issuedBy
     */
    public Integer getIssuedBy() {
        return issuedBy;
    }

    /**
     * @param issuedBy the issuedBy to set
     */
    public void setIssuedBy(Integer issuedBy) {
        this.issuedBy = issuedBy;
    }

    /**
     * @return the dateReturned
     */
    public Date getDateReturned() {
        return dateReturned;
    }

    /**
     * @param dateReturned the dateReturned to set
     */
    public void setDateReturned(Date dateReturned) {
        this.dateReturned = dateReturned;
    }

    /**
     * @return the receivedBy
     */
    public Integer getReceivedBy() {
        return receivedBy;
    }

    /**
     * @param receivedBy the receivedBy to set
     */
    public void setReceivedBy(Integer receivedBy) {
        this.receivedBy = receivedBy;
    }

    /**
     * @return the returnedCondition
     */
    public Integer getReturnedCondition() {
        return returnedCondition;
    }

    /**
     * @param returnedCondition the returnedCondition to set
     */
    public void setReturnedCondition(Integer returnedCondition) {
        this.returnedCondition = returnedCondition;
    }

    /**
     * @return the uniqueId
     */
    public String getUniqueId() {
        return uniqueId;
    }

    /**
     * @param uniqueId the uniqueId to set
     */
    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    @Override
    public Integer getId() {
        return this.getClientEquipmentId();
    }

    @Override
    public void setId(Integer id) {
        this.setClientEquipmentId(id);
    }

    @Override
    public Integer getEntityId() {
        return this.getClientId();
    }

    @Override
    public void setEntityId(Integer entityId) {
        this.setClientId(entityId);
    }

    public void setEntity(Client entity) {
        this.client = entity;
    }
    
    public Client getEntity() {
        return this.client;
    }
    
    @Override
    public Entity getEntity(String companyId) {
        if (client == null) {
            ClientControllerInterface cliController = ControllerRegistryAbstract.getClientController(companyId);
            try {
                client = cliController.getClientById(clientId);
            } catch (Exception e) {}
        }
        return client;
    }

    @Override
    public Equipment getEquipment(String companyId) {
        if (equipment == null) {
            EquipmentControllerInterface equipController = ControllerRegistryAbstract.getEquipmentController(companyId);
            try {
                equipment = equipController.getEquipmentById(equipmentId);
            } catch (Exception e) {}
        }
        return equipment;
    }
    
    public ArrayList<ClientEquipmentContact> getContacts(String companyId) {
        if (contacts == null && this.clientEquipmentId != null) {
            EquipmentControllerInterface equipController = ControllerRegistryAbstract.getEquipmentController(companyId);
            try {
                contacts = equipController.getClientContact(this.clientEquipmentId, 10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return contacts;
    }
    
    public void setContacts(ArrayList<ClientEquipmentContact> contacts) {
        this.contacts = contacts;
    }
    
    public Equipment getEquipment() {
        return this.equipment;
    }
    
    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
    
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if (this.equipment != null) {
            builder.append("Device: ").append(this.equipment.getEquipmentName()).append(" ");
        }
        builder.append("ID: ").append(uniqueId);
        return builder.toString();
    }

    /**
     * @return the nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @param nickname the nickname to set
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    /**
     * @return the mdn
     */
    public String getMdn() {
        return mdn;
    }

    /**
     * @param mdn the mdn to set
     */
    public void setMdn(String mdn) {
        this.mdn = mdn;
    }

    /**
     * @return the lastContactDate
     */
    public Date getLastContactDate() {
        return lastContactDate;
    }

    /**
     * @param lastContactDate the lastContactDate to set
     */
    public void setLastContactDate(Date lastContactDate) {
        this.lastContactDate = lastContactDate;
    }

    /**
     * @return the workingVersion
     */
    public String getWorkingVersion() {
        return workingVersion;
    }

    /**
     * @param workingVersion the workingVersion to set
     */
    public void setWorkingVersion(String workingVersion) {
        this.workingVersion = workingVersion;
    }

    /**
     * @return the dateOfContractRenewal
     */
    public Date getDateOfContractRenewal() {
        return dateOfContractRenewal;
    }

    /**
     * @param dateOfContractRenewal the dateOfContractRenewal to set
     */
    public void setDateOfContractRenewal(Date dateOfContractRenewal) {
        this.dateOfContractRenewal = dateOfContractRenewal;
    }

    /**
     * @return the serialNumber
     */
    public String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber the serialNumber to set
     */
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    /**
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the cost
     */
    public BigDecimal getCost() {
        return cost;
    }

    /**
     * @param cost the cost to set
     */
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    /**
     * @return the vendor_id
     */
    public Integer getVendorId() {
        return vendorId;
    }

    /**
     * @param vendor_id the vendor_id to set
     */
    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
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
     * @return the routeId
     */
    public Integer getRouteId() {
        return routeId;
    }

    /**
     * @param routeId the routeId to set
     */
    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    /**
     * @return the hasGPS
     */
    public Boolean getHasGPS() {
        return hasGPS;
    }

    /**
     * @param hasGPS the hasGPS to set
     */
    public void setHasGPS(Boolean hasGPS) {
        this.hasGPS = hasGPS;
    }

}
