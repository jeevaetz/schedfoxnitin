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
public class ClientEquipmentWithStats implements Serializable {
    private Boolean hasGPS;
    private Boolean hasNetwork;
    private Boolean hasGoogleService;
    private Boolean isAirplaneOn;
    private Boolean powerOn;
    private Boolean powerOff;
    
    private ClientEquipment clientEquipment;
    private Client client;
    
    private String version;
    
    private Date lastContactDate; 
    
    private Double signalPercentage;
    private Double batteryPercentage;
    
    
    
    public ClientEquipmentWithStats() {
        
    }
    
    public ClientEquipmentWithStats(Record_Set rst) {
        clientEquipment = new ClientEquipment(rst);
        try {
            this.hasGPS = rst.getBoolean("gpson");
        } catch (Exception exe) {}
        try {
            this.hasNetwork = rst.getBoolean("network_location_on");
        } catch (Exception exe) {}
        try {
            this.hasGoogleService = rst.getBoolean("has_google_play_services");
        } catch (Exception exe) {}
        try {
            this.isAirplaneOn = rst.getBoolean("inairplanemode");
        } catch (Exception exe) {}
        try {
            this.powerOn = rst.getBoolean("power_on");
        } catch (Exception exe) {}
        try {
            this.powerOff = rst.getBoolean("power_off");
        } catch (Exception exe) {}
        
        try {
            this.version = rst.getString("version");
        } catch (Exception exe) {}
        
        try {
            this.lastContactDate = rst.getTimestamp("contact_time");
        } catch (Exception exe) {}
        
        try {
            this.signalPercentage = rst.getBigDecimal("signal_percentage").doubleValue();
        } catch (Exception exe) {}
        try {
            this.batteryPercentage = rst.getBigDecimal("battery_percentage").doubleValue();
        } catch (Exception exe) {}
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

    /**
     * @return the hasNetwork
     */
    public Boolean getHasNetwork() {
        return hasNetwork;
    }

    /**
     * @param hasNetwork the hasNetwork to set
     */
    public void setHasNetwork(Boolean hasNetwork) {
        this.hasNetwork = hasNetwork;
    }

    /**
     * @return the hasGoogleService
     */
    public Boolean getHasGoogleService() {
        return hasGoogleService;
    }

    /**
     * @param hasGoogleService the hasGoogleService to set
     */
    public void setHasGoogleService(Boolean hasGoogleService) {
        this.hasGoogleService = hasGoogleService;
    }

    /**
     * @return the isAirplaneOn
     */
    public Boolean getIsAirplaneOn() {
        return isAirplaneOn;
    }

    /**
     * @param isAirplaneOn the isAirplaneOn to set
     */
    public void setIsAirplaneOn(Boolean isAirplaneOn) {
        this.isAirplaneOn = isAirplaneOn;
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

    /**
     * @return the clientEquipment
     */
    public ClientEquipment getClientEquipment() {
        return clientEquipment;
    }

    /**
     * @param clientEquipment the clientEquipment to set
     */
    public void setClientEquipment(ClientEquipment clientEquipment) {
        this.clientEquipment = clientEquipment;
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
}
