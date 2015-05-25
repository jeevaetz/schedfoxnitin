/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author user
 */
public interface GPSInterface {

    public Integer getIdentifier();
    
    public boolean isWaypointScan();
    
    /**
     * @return the accuracy
     */
    BigDecimal getAccuracy();

    /**
     * @return the employeeId
     */
    Integer getEmployeeId();

    /**
     * @return the latitude
     */
    BigDecimal getLatitude();

    /**
     * @return the longitude
     */
    BigDecimal getLongitude();

    /**
     * @return the recordedOn
     */
    Date getRecordedOn();

    /**
     * @param accuracy the accuracy to set
     */
    void setAccuracy(BigDecimal accuracy);

    /**
     * @param employeeId the employeeId to set
     */
    void setEmployeeId(Integer employeeId);

    /**
     * @param latitude the latitude to set
     */
    void setLatitude(BigDecimal latitude);

    /**
     * @param longitude the longitude to set
     */
    void setLongitude(BigDecimal longitude);

    /**
     * @param recordedOn the recordedOn to set
     */
    void setRecordedOn(Date recordedOn);
    
}
