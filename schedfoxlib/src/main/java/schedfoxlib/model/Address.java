/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 * Non Normalized class used to encapsulate our addresses in our client and
 * employee object
 * @author user
 */
public class Address implements Serializable {
    private String address1;
    private String address2;
    private String city;
    private String state;
    private String zip;

    private Integer addressGeocodeId;
    private Float latitude;
    private Float longitude;

    public Address() {
        address1 = "";
        address2 = "";
        city = "";
        state = "";
        zip = "";
    }

    public Address(Record_Set rst) {
        try {
            address1 = rst.getString("address1");
        } catch (Exception exe) {}
        try {
            address2 = rst.getString("address2");
        } catch (Exception exe) {}
        try {
            city = rst.getString("city");
        } catch (Exception exe) {}
        try {
            state = rst.getString("state");
        } catch (Exception exe) {}
        try {
            zip = rst.getString("zip");
        } catch (Exception exe) {}
        try {
            addressGeocodeId = rst.getInt("address_geocode_id"); 
        } catch (Exception exe) {}
        try {
            latitude = rst.getFloat("latitude");
        } catch (Exception exe) {}
        try {
            longitude = rst.getFloat("longitude");
        } catch (Exception exe) {}
    }

    /**
     * @return the address1
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * @param address1 the address1 to set
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * @return the address2
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * @param address2 the address2 to set
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * @return the city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city the city to set
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the zip
     */
    public String getZip() {
        return zip;
    }

    /**
     * @param zip the zip to set
     */
    public void setZip(String zip) {
        this.zip = zip;
    }

    /**
     * @return the latitude
     */
    public Float getLatitude() {
        return latitude;
    }

    /**
     * @param latitude the latitude to set
     */
    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    /**
     * @return the longitude
     */
    public Float getLongitude() {
        return longitude;
    }

    /**
     * @param longitude the longitude to set
     */
    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    /**
     * @return the addressGeocodeId
     */
    public Integer getAddressGeocodeId() {
        return addressGeocodeId;
    }

    /**
     * @param addressGeocodeId the addressGeocodeId to set
     */
    public void setAddressGeocodeId(Integer addressGeocodeId) {
        this.addressGeocodeId = addressGeocodeId;
    }
}
