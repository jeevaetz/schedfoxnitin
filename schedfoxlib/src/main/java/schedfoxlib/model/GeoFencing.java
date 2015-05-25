/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class GeoFencing implements Serializable {
    private Integer geoFencingId;
    private String geoFenceName;
    private Date geoFenceAdded;
    private Integer geoFenceAddedBy;
    private Integer clientId;
    private Integer geoFenceType;
    private Boolean active;
    
    private ArrayList<GeoFencingContact> contacts;
    private ArrayList<GeoFencingPoints> points;
    
    public GeoFencing() {
        
    }

    public GeoFencing(Record_Set rst) {
        try {
            geoFencingId = rst.getInt("geo_fencing_id");
        } catch (Exception exe) {}
        try {
            geoFenceName = rst.getString("geo_fence_name");
        } catch (Exception exe) {}
        try {
            geoFenceAdded = rst.getTimestamp("geo_fence_added");
        } catch (Exception exe) {}
        try {
            geoFenceAddedBy = rst.getInt("geo_fence_added_by");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            geoFenceType = rst.getInt("geo_fence_type");
        } catch (Exception exe) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the geoFencingId
     */
    public Integer getGeoFencingId() {
        return geoFencingId;
    }

    /**
     * @param geoFencingId the geoFencingId to set
     */
    public void setGeoFencingId(Integer geoFencingId) {
        this.geoFencingId = geoFencingId;
    }

    /**
     * @return the geoFenceName
     */
    public String getGeoFenceName() {
        return geoFenceName;
    }

    /**
     * @param geoFenceName the geoFenceName to set
     */
    public void setGeoFenceName(String geoFenceName) {
        this.geoFenceName = geoFenceName;
    }

    /**
     * @return the geoFenceAdded
     */
    public Date getGeoFenceAdded() {
        return geoFenceAdded;
    }

    /**
     * @param geoFenceAdded the geoFenceAdded to set
     */
    public void setGeoFenceAdded(Date geoFenceAdded) {
        this.geoFenceAdded = geoFenceAdded;
    }

    /**
     * @return the geoFenceAddedBy
     */
    public Integer getGeoFenceAddedBy() {
        return geoFenceAddedBy;
    }

    /**
     * @param geoFenceAddedBy the geoFenceAddedBy to set
     */
    public void setGeoFenceAddedBy(Integer geoFenceAddedBy) {
        this.geoFenceAddedBy = geoFenceAddedBy;
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
     * @return the points
     */
    public ArrayList<GeoFencingPoints> getPoints() {
        return points;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(ArrayList<GeoFencingPoints> points) {
        this.points = points;
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
     * @return the geoFenceType
     */
    public Integer getGeoFenceType() {
        return geoFenceType;
    }

    /**
     * @param geoFenceType the geoFenceType to set
     */
    public void setGeoFenceType(Integer geoFenceType) {
        this.geoFenceType = geoFenceType;
    }

    /**
     * @return the contacts
     */
    public ArrayList<GeoFencingContact> getContacts() {
        return contacts;
    }

    /**
     * @param contacts the contacts to set
     */
    public void setContacts(ArrayList<GeoFencingContact> contacts) {
        this.contacts = contacts;
    }
}
