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
public class GeoFencingPoints implements Serializable {
    private Integer geoFencingPointId;
    private Integer geoFencingId;
    private Double latitude;
    private Double longitude;
    private Integer pointCount;
    
    public GeoFencingPoints() {
        
    }

    public GeoFencingPoints(Record_Set rst) {
        try {
            geoFencingPointId = rst.getInt("geo_fencing_point_id");
        } catch (Exception exe) {}
        try {
            geoFencingId = rst.getInt("geo_fencing_id");
        } catch (Exception exe) {}
        try {
            latitude = rst.getBigDecimal("latitude").doubleValue();
        } catch (Exception exe) {}
        try {
            longitude = rst.getBigDecimal("longitude").doubleValue();
        } catch (Exception exe) {}
        try {
            pointCount = rst.getInt("point_count");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the geoFencingPointId
     */
    public Integer getGeoFencingPointId() {
        return geoFencingPointId;
    }

    /**
     * @param geoFencingPointId the geoFencingPointId to set
     */
    public void setGeoFencingPointId(Integer geoFencingPointId) {
        this.geoFencingPointId = geoFencingPointId;
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
     * @return the latitude
     */
    public Double getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Double getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the pointCount
     */
    public Integer getPointCount() {
        return pointCount;
    }

    /**
     * @param pointCount the pointCount to set
     */
    public void setPointCount(Integer pointCount) {
        this.pointCount = pointCount;
    }

}
