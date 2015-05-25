/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

/**
 *
 * @author ira
 */
public class AddressGeocodeDistance {
    private Integer addressGeocodeDistance;
    private Integer addressGeocode1;
    private Integer addressGeocode2;
    private Integer travelDuration;
    private Integer travelDistance;
    
    public AddressGeocodeDistance() {
        
    }

    /**
     * @return the addressGeocodeDistance
     */
    public Integer getAddressGeocodeDistance() {
        return addressGeocodeDistance;
    }

    /**
     * @param addressGeocodeDistance the addressGeocodeDistance to set
     */
    public void setAddressGeocodeDistance(Integer addressGeocodeDistance) {
        this.addressGeocodeDistance = addressGeocodeDistance;
    }

    /**
     * @return the addressGeocode1
     */
    public Integer getAddressGeocode1() {
        return addressGeocode1;
    }

    /**
     * @param addressGeocode1 the addressGeocode1 to set
     */
    public void setAddressGeocode1(Integer addressGeocode1) {
        this.addressGeocode1 = addressGeocode1;
    }

    /**
     * @return the addressGeocode2
     */
    public Integer getAddressGeocode2() {
        return addressGeocode2;
    }

    /**
     * @param addressGeocode2 the addressGeocode2 to set
     */
    public void setAddressGeocode2(Integer addressGeocode2) {
        this.addressGeocode2 = addressGeocode2;
    }

    /**
     * @return the travelDuration
     */
    public Integer getTravelDuration() {
        return travelDuration;
    }

    /**
     * @param travelDuration the travelDuration to set
     */
    public void setTravelDuration(Integer travelDuration) {
        this.travelDuration = travelDuration;
    }

    /**
     * @return the travelDistance
     */
    public Integer getTravelDistance() {
        return travelDistance;
    }

    /**
     * @param travelDistance the travelDistance to set
     */
    public void setTravelDistance(Integer travelDistance) {
        this.travelDistance = travelDistance;
    }
}
