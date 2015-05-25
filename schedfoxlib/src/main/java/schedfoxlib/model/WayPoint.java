/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class WayPoint implements Serializable {
    private Integer clientWaypointId;
    private Integer clientId;
    private String waypointName;
    private String waypointData;
    private Date dateAdded;
    private Boolean active;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Integer waypointType;
    private transient String iconURL;
    
    public static final int BARCODE = 1;
    public static final int WAYPOINT = 2;
    
    public WayPoint() {
        waypointName = new String();
        waypointData = new String();
        active = true;
        latitude = new BigDecimal(0);
        longitude = new BigDecimal(0);
        waypointType = BARCODE;
    }

    public WayPoint(Record_Set rst) {
        try {
            clientWaypointId = rst.getInt("client_waypoint_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            waypointName = rst.getString("waypoint_name");
        } catch (Exception exe) {}
        try {
            waypointData = rst.getString("waypoint_data");
        } catch (Exception exe) {}
        try {
            dateAdded = rst.getTimestamp("date_added");
        } catch (Exception exe) {}
        try {
            latitude = rst.getBigDecimal("latitude");
        } catch (Exception exe) {}
        try {
            longitude = rst.getBigDecimal("longitude");
        } catch (Exception exe) {}
        try {
            active = rst.getBoolean("active");
        } catch (Exception exe) {}
        try {
            waypointType = rst.getInt("waypoint_type");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the clientWaypointId
     */
    public Integer getClientWaypointId() {
        return clientWaypointId;
    }

    /**
     * @param clientWaypointId the clientWaypointId to set
     */
    public void setClientWaypointId(Integer clientWaypointId) {
        this.clientWaypointId = clientWaypointId;
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
     * @return the waypointName
     */
    public String getWaypointName() {
        return waypointName;
    }

    /**
     * @param waypointName the waypointName to set
     */
    public void setWaypointName(String waypointName) {
        this.waypointName = waypointName;
    }

    /**
     * @return the waypointData
     */
    public String getWaypointData() {
        return waypointData;
    }

    /**
     * @param waypointData the waypointData to set
     */
    public void setWaypointData(String waypointData) {
        this.waypointData = waypointData;
    }

    /**
     * @return the dateAdded
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * @param dateAdded the dateAdded to set
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
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
     * @return the latitude
     */
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the waypointType
     */
    public Integer getWaypointType() {
        return waypointType;
    }

    /**
     * @param waypointType the waypointType to set
     */
    public void setWaypointType(Integer waypointType) {
        this.waypointType = waypointType;
    }
    
    public String getIconURL() {
        return iconURL;
    }
    
    public void setIconURL(String iconURL) {
        this.iconURL = iconURL;
    }
}
