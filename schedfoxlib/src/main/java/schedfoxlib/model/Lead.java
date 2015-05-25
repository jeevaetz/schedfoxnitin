/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author ira
 */
public class Lead implements Serializable {
    private Integer id;
    private String name;
    private String cfname;
    private String clname;
    private String ctitle;
    private String address;
    private String city;
    private String state;
    private String zip;
    private String competitor;
    private String phone;
    private String email;
    private String leadType;
    private Date dateUpdated;
    
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    public Lead() {
        
    }

    public Lead(ResultSet rst) throws SQLException {
        id = rst.getInt("ID");
        name = rst.getString("Name");
        cfname = rst.getString("CFname");
        clname = rst.getString("CLname");
        ctitle = rst.getString("CTitle");
        address = rst.getString("address");
        city = rst.getString("city");
        state = rst.getString("state");
        zip = rst.getString("zip");
        competitor = rst.getString("competiter");
        phone = rst.getString("Phone");
        email = rst.getString("Email");
        leadType = rst.getString("LeadType");
        dateUpdated = rst.getDate("DateUpdated");
        
        if (city == null || city.replaceAll("\"", "").trim().length() == 2) {
            ctitle = rst.getString("CLname");
            cfname = rst.getString("Name");
            name = rst.getString("CFname");
            address = rst.getString("zip");
            city = rst.getString("address");
            state = rst.getString("city");
            zip = rst.getString("state");
            phone = rst.getString("CTitle");
            email = rst.getString("Phone");
        }
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the cfname
     */
    public String getCfname() {
        return cfname;
    }

    /**
     * @param cfname the cfname to set
     */
    public void setCfname(String cfname) {
        this.cfname = cfname;
    }

    /**
     * @return the clname
     */
    public String getClname() {
        return clname;
    }

    /**
     * @param clname the clname to set
     */
    public void setClname(String clname) {
        this.clname = clname;
    }

    /**
     * @return the ctitle
     */
    public String getCtitle() {
        return ctitle;
    }

    /**
     * @param ctitle the ctitle to set
     */
    public void setCtitle(String ctitle) {
        this.ctitle = ctitle;
    }

    /**
     * @return the address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
        this.address = address;
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
     * @return the competitor
     */
    public String getCompetitor() {
        return competitor;
    }

    /**
     * @param competitor the competitor to set
     */
    public void setCompetitor(String competitor) {
        this.competitor = competitor;
    }

    /**
     * @return the phone
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone to set
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the leadType
     */
    public String getLeadType() {
        return leadType;
    }

    /**
     * @param leadType the leadType to set
     */
    public void setLeadType(String leadType) {
        this.leadType = leadType;
    }

    /**
     * @return the dateUpdated
     */
    public Date getDateUpdated() {
        return dateUpdated;
    }

    /**
     * @param dateUpdated the dateUpdated to set
     */
    public void setDateUpdated(Date dateUpdated) {
        this.dateUpdated = dateUpdated;
    }
    
    /**
    / Method used for Geo-Tagging, converts data to our Address object for easy 
    /retrieval / persistence to the database
     * @return Address 
    **/
    public Address getAddressObj() {
        Address retVal = new Address();
        retVal.setAddress1(this.address);
        retVal.setAddress2("");
        retVal.setCity(this.city);
        retVal.setState(this.state);
        retVal.setZip(this.zip);
        return retVal;
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
}
