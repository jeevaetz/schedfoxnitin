/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class GPSCoordinate implements GPSInterface {
    private Integer gpsCoordinateId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private Date recordedOn;
    private Integer employeeId;
    private Integer clientId;
    private Integer equipmentId;
    private BigDecimal accuracy;
    private BigDecimal speed;

    public GPSCoordinate() {
        
    }
    
    public GPSCoordinate(Record_Set rst) {
        try {
            gpsCoordinateId = rst.getInt("gps_coordinate_id");
        } catch (Exception e) {}
        try {
            latitude = rst.getBigDecimal("latitude");
        } catch (Exception e) {}
        try {
            longitude = rst.getBigDecimal("longitude");
        } catch (Exception e) {}
        try {
            recordedOn = rst.getTimestamp("recorded_on");
        } catch (Exception e) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception e) {}
        try {
            equipmentId = rst.getInt("equipment_id");
        } catch (Exception e) {}
        try {
            accuracy = rst.getBigDecimal("accuracy");
        } catch (Exception e) {}
        try {
            speed = rst.getBigDecimal("speed");
        } catch (Exception e) {}
    }
    
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

    /**
     * @return the latitude
     */
    @Override
    public BigDecimal getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    @Override
    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    @Override
    public BigDecimal getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    @Override
    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the recordedOn
     */
    @Override
    public Date getRecordedOn() {
        return recordedOn;
    }

    /**
     * @param recordedOn the recordedOn to set
     */
    @Override
    public void setRecordedOn(Date recordedOn) {
        this.recordedOn = recordedOn;
    }

    /**
     * @return the employeeId
     */
    @Override
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    @Override
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
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
     * @return the accuracy
     */
    @Override
    public BigDecimal getAccuracy() {
        return accuracy;
    }

    /**
     * @param accuracy the accuracy to set
     */
    @Override
    public void setAccuracy(BigDecimal accuracy) {
        this.accuracy = accuracy;
    }

    @Override
    public Integer getIdentifier() {
        return gpsCoordinateId;
    }

    @Override
    public boolean isWaypointScan() {
        return false;
    }

    /**
     * @return the speed
     */
    public BigDecimal getSpeed() {
        return speed;
    }

    /**
     * @param speed the speed to set
     */
    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
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
}
