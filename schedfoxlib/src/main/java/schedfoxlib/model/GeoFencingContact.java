/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class GeoFencingContact implements Serializable {
    private Integer geoFencingContactId;
    private Integer geoFenceId;
    private String contactName;
    private String contactValue;
    private Integer contactType;
    private Boolean active;
    
    public GeoFencingContact() {
        
    }
    
    public GeoFencingContact(Record_Set rst) {
        try {
            this.geoFencingContactId = rst.getInt("geo_fencing_contact_id");
        } catch (Exception exe) {}
        try {
            this.geoFenceId = rst.getInt("geo_fence_id");
        } catch (Exception exe) {}
        try {
            this.contactName = rst.getString("contact_name");
        } catch (Exception exe) {}
        try {
            this.contactValue = rst.getString("contact_value");
        } catch (Exception exe) {}
        try {
            this.contactType = rst.getInt("contact_type");
        } catch (Exception exe) {}
        try {
            this.active = rst.getBoolean("active");
        } catch (Exception exe) {}
    }

    /**
     * @return the geoFencingContactId
     */
    public Integer getGeoFencingContactId() {
        return geoFencingContactId;
    }

    /**
     * @param geoFencingContactId the geoFencingContactId to set
     */
    public void setGeoFencingContactId(Integer geoFencingContactId) {
        this.geoFencingContactId = geoFencingContactId;
    }

    /**
     * @return the geoFenceId
     */
    public Integer getGeoFenceId() {
        return geoFenceId;
    }

    /**
     * @param geoFenceId the geoFenceId to set
     */
    public void setGeoFenceId(Integer geoFenceId) {
        this.geoFenceId = geoFenceId;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the contactValue
     */
    public String getContactValue() {
        return contactValue;
    }

    /**
     * @param contactValue the contactValue to set
     */
    public void setContactValue(String contactValue) {
        this.contactValue = contactValue;
    }

    /**
     * @return the contactType
     */
    public Integer getContactType() {
        return contactType;
    }

    /**
     * @param contactType the contactType to set
     */
    public void setContactType(Integer contactType) {
        this.contactType = contactType;
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
}
