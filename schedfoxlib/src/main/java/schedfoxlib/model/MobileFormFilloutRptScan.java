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
public class MobileFormFilloutRptScan implements Serializable {
    private Integer clientWaypointId;
    private String waypointName;
    private String waypointData;
    private Date maxDate;
    
    public MobileFormFilloutRptScan() {
        
    }
    
    public MobileFormFilloutRptScan(Record_Set rst) {
        try {
            clientWaypointId = rst.getInt("client_waypoint_id");
        } catch (Exception exe) {}
        try {
            waypointName = rst.getString("waypoint_name");
        } catch (Exception exe) {}
        try {
            waypointData = rst.getString("waypoint_data");
        } catch (Exception exe) {}
        try {
            maxDate = rst.getTimestamp("max_date");
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
     * @return the maxDate
     */
    public Date getMaxDate() {
        return maxDate;
    }

    /**
     * @param maxDate the maxDate to set
     */
    public void setMaxDate(Date maxDate) {
        this.maxDate = maxDate;
    }
    
    
}
