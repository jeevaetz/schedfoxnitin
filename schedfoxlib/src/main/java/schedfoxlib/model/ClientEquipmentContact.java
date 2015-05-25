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
public class ClientEquipmentContact {
    private Integer clientEquipmentContactId;
    private Integer clientEquipmentId;
    private String version;
    private Date contactDate;
    private Date contactTime;
    private Boolean gpson;
    private Boolean inairplanemode;
    private Double signalPercentage;
    private Boolean hasGooglePlayServices;
    private Boolean networkLocationOn;
    private Double batteryPercentage;
    private Boolean powerOn;
    private Boolean powerOff;
    
    public ClientEquipmentContact() {
        
    }

    public ClientEquipmentContact(Record_Set rst) {
        clientEquipmentContactId = rst.getInt("client_equipment_contact_id");
        clientEquipmentId = rst.getInt("client_equipment_id");
        version = rst.getString("version");
        contactDate = rst.getDate("contact_date");
        contactTime = rst.getTimestamp("contact_time");
        gpson = rst.getBoolean("gpson");
        inairplanemode = rst.getBoolean("inairplanemode");
        try {
            signalPercentage = rst.getBigDecimal("signal_percentage").doubleValue();
        } catch (Exception exe) {
            signalPercentage = 0.0;
        }
        hasGooglePlayServices = rst.getBoolean("has_google_play_services");
        networkLocationOn = rst.getBoolean("network_location_on");
        try {
            batteryPercentage = rst.getBigDecimal("battery_percentage").doubleValue();
        } catch (Exception exe) {
            batteryPercentage = 0.0;
        }
        powerOn = rst.getBoolean("power_on");
        powerOff = rst.getBoolean("power_off");
    }
    
    /**
     * @return the clientEquipmentContactId
     */
    public Integer getClientEquipmentContactId() {
        return clientEquipmentContactId;
    }

    /**
     * @param clientEquipmentContactId the clientEquipmentContactId to set
     */
    public void setClientEquipmentContactId(Integer clientEquipmentContactId) {
        this.clientEquipmentContactId = clientEquipmentContactId;
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
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the contactDate
     */
    public Date getContactDate() {
        return contactDate;
    }

    /**
     * @param contactDate the contactDate to set
     */
    public void setContactDate(Date contactDate) {
        this.contactDate = contactDate;
    }

    /**
     * @return the gpson
     */
    public Boolean getGpson() {
        return gpson;
    }

    /**
     * @param gpson the gpson to set
     */
    public void setGpson(Boolean gpson) {
        this.gpson = gpson;
    }

    /**
     * @return the inairplanemode
     */
    public Boolean getInairplanemode() {
        return inairplanemode;
    }

    /**
     * @param inairplanemode the inairplanemode to set
     */
    public void setInairplanemode(Boolean inairplanemode) {
        this.inairplanemode = inairplanemode;
    }

    /**
     * @return the signalPercentage
     */
    public Double getSignalPercentage() {
        return signalPercentage;
    }

    /**
     * @param signalPercentage the signalPercentage to set
     */
    public void setSignalPercentage(Double signalPercentage) {
        this.signalPercentage = signalPercentage;
    }

    /**
     * @return the hasGooglePlayServices
     */
    public Boolean getHasGooglePlayServices() {
        return hasGooglePlayServices;
    }

    /**
     * @param hasGooglePlayServices the hasGooglePlayServices to set
     */
    public void setHasGooglePlayServices(Boolean hasGooglePlayServices) {
        this.hasGooglePlayServices = hasGooglePlayServices;
    }

    /**
     * @return the networkLocationOn
     */
    public Boolean getNetworkLocationOn() {
        return networkLocationOn;
    }

    /**
     * @param networkLocationOn the networkLocationOn to set
     */
    public void setNetworkLocationOn(Boolean networkLocationOn) {
        this.networkLocationOn = networkLocationOn;
    }

    /**
     * @return the contactTime
     */
    public Date getContactTime() {
        return contactTime;
    }

    /**
     * @param contactTime the contactTime to set
     */
    public void setContactTime(Date contactTime) {
        this.contactTime = contactTime;
    }

    /**
     * @return the batteryPercentage
     */
    public Double getBatteryPercentage() {
        return batteryPercentage;
    }

    /**
     * @param batteryPercentage the batteryPercentage to set
     */
    public void setBatteryPercentage(Double batteryPercentage) {
        this.batteryPercentage = batteryPercentage;
    }

    /**
     * @return the powerOn
     */
    public Boolean getPowerOn() {
        return powerOn;
    }

    /**
     * @param powerOn the powerOn to set
     */
    public void setPowerOn(Boolean powerOn) {
        this.powerOn = powerOn;
    }

    /**
     * @return the powerOff
     */
    public Boolean getPowerOff() {
        return powerOff;
    }

    /**
     * @param powerOff the powerOff to set
     */
    public void setPowerOff(Boolean powerOff) {
        this.powerOff = powerOff;
    }
    
    
}
