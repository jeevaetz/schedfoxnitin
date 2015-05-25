/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientWaypointScan {
    private Integer clientWaypointScanId;
    private Integer clientWaypointId;
    private Integer userId;
    private Date dateScanned;
    
    private Employee userObj;

    public ClientWaypointScan() {
        
    }
    
    public ClientWaypointScan(Record_Set rst) {
        try {
            clientWaypointScanId = rst.getInt("client_waypoint_scan_id");
        } catch (Exception exe) {}
        try {
            clientWaypointId = rst.getInt("client_waypoint_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            dateScanned = rst.getTimestamp("date_scanned");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the clientWaypointScanId
     */
    public Integer getClientWaypointScanId() {
        return clientWaypointScanId;
    }

    /**
     * @param clientWaypointScanId the clientWaypointScanId to set
     */
    public void setClientWaypointScanId(Integer clientWaypointScanId) {
        this.clientWaypointScanId = clientWaypointScanId;
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
     * @return the dateScanned
     */
    public Date getDateScanned() {
        return dateScanned;
    }

    /**
     * @param dateScanned the dateScanned to set
     */
    public void setDateScanned(Date dateScanned) {
        this.dateScanned = dateScanned;
    }

    /**
     * @return the userObj
     */
    public Employee getUserObj() {
        return userObj;
    }

    /**
     * @param userObj the userObj to set
     */
    public void setUserObj(Employee userObj) {
        this.userObj = userObj;
    }
    
    
}
