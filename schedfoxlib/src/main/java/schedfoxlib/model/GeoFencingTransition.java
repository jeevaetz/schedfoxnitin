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
public class GeoFencingTransition implements Serializable {
    private Integer geoFencingTransitionId;
    private Integer geoFenceId;
    private Integer transitionType;
    private Date transitionDate;
    private Date alertSentOn;
    private Integer clientEquipmentId;
    private Integer gpsCoordinateId;

    /**
     * @return the gpsCoordinateId
     */
    public Integer getGpsCoordinateId() {
        return gpsCoordinateId;
    }

    /**
     * @param gpsCoordinateId the gpsCoordinateId to set
     */
    public void setGpsCoordinateId(Integer gpsCoordinateId) {
        this.gpsCoordinateId = gpsCoordinateId;
    }
    
    public enum TransitionType {
        INSIDE(1), OUTSIDE(2);
        
        private final Integer myType;
        
        TransitionType(Integer trans) {
            myType = trans;
        }
        
        public Integer getValue() {
            return myType;
        }

        public Boolean equals(Integer val) {
            try {
                return myType.equals(val);
            } catch (Exception exe) {
                return false;
            }
        }
    }
    
    public GeoFencingTransition() {
        
    }
    
    public GeoFencingTransition(Record_Set rst) {
        try {
            this.geoFencingTransitionId = rst.getInt("geo_fencing_transition_id");
        } catch (Exception exe) {}
        try {
            this.geoFenceId = rst.getInt("geo_fence_id");
        } catch (Exception exe) {}
        try {
            this.transitionType = rst.getInt("transition_type");
        } catch (Exception exe) {}
        try {
            this.transitionDate = rst.getTimestamp("transition_date");
        } catch (Exception exe) {}
        try {
            this.alertSentOn = rst.getTimestamp("alert_sent_on");
        } catch (Exception exe) {}
        try {
            this.clientEquipmentId = rst.getInt("client_equipment_id");
        } catch (Exception exe) {}
        try {
            this.gpsCoordinateId = rst.getInt("gps_coordinate_id");
        } catch (Exception exe) {}
    }

    /**
     * @return the geoFencingTransitionId
     */
    public Integer getGeoFencingTransitionId() {
        return geoFencingTransitionId;
    }

    /**
     * @param geoFencingTransitionId the geoFencingTransitionId to set
     */
    public void setGeoFencingTransitionId(Integer geoFencingTransitionId) {
        this.geoFencingTransitionId = geoFencingTransitionId;
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
     * @return the transitionType
     */
    public Integer getTransitionType() {
        return transitionType;
    }

    /**
     * @param transitionType the transitionType to set
     */
    public void setTransitionType(Integer transitionType) {
        this.transitionType = transitionType;
    }

    /**
     * @return the transitionDate
     */
    public Date getTransitionDate() {
        return transitionDate;
    }

    /**
     * @param transitionDate the transitionDate to set
     */
    public void setTransitionDate(Date transitionDate) {
        this.transitionDate = transitionDate;
    }

    /**
     * @return the alertSentOn
     */
    public Date getAlertSentOn() {
        return alertSentOn;
    }

    /**
     * @param alertSentOn the alertSentOn to set
     */
    public void setAlertSentOn(Date alertSentOn) {
        this.alertSentOn = alertSentOn;
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
}
