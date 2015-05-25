/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ira
 */
public class WayPointCoordinate implements GPSInterface {
    private WayPoint wayPoint;
    private ClientWaypointScan wayPointScan;
    
    public WayPointCoordinate(WayPoint wayPoint, ClientWaypointScan scan) {
        this.wayPoint = wayPoint;
        this.wayPointScan = scan;
    }

    @Override
    public Integer getIdentifier() {
        return this.wayPoint.getClientWaypointId();
    }

    @Override
    public boolean isWaypointScan() {
        return true;
    }

    @Override
    public BigDecimal getAccuracy() {
        return new BigDecimal(0);
    }

    @Override
    public Integer getEmployeeId() {
        return wayPointScan.getUserId();
    }

    @Override
    public BigDecimal getLatitude() {
        return wayPoint.getLatitude();
    }

    @Override
    public BigDecimal getLongitude() {
        return wayPoint.getLongitude();
    }

    @Override
    public Date getRecordedOn() {
        return wayPointScan.getDateScanned();
    }

    @Override
    public void setAccuracy(BigDecimal accuracy) {
        
    }

    @Override
    public void setEmployeeId(Integer employeeId) {
        
    }

    @Override
    public void setLatitude(BigDecimal latitude) {
        
    }

    @Override
    public void setLongitude(BigDecimal longitude) {
        
    }

    @Override
    public void setRecordedOn(Date recordedOn) {
        
    }

    /**
     * @return the wayPoint
     */
    public WayPoint getWayPoint() {
        return wayPoint;
    }

    /**
     * @param wayPoint the wayPoint to set
     */
    public void setWayPoint(WayPoint wayPoint) {
        this.wayPoint = wayPoint;
    }

    /**
     * @return the wayPointScan
     */
    public ClientWaypointScan getWayPointScan() {
        return wayPointScan;
    }

    /**
     * @param wayPointScan the wayPointScan to set
     */
    public void setWayPointScan(ClientWaypointScan wayPointScan) {
        this.wayPointScan = wayPointScan;
    }
    
}
