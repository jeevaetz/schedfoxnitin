/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class BranchInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer branchId;

    private String phone;

    //Lazy addres object
    private Address addressObj;
    
    private String contactName;
    private String contactPhone;
    private String contactEmail;

    public BranchInfo() {
        addressObj = new Address();
    }

    public BranchInfo(Record_Set rst) {
        addressObj = new Address();

        branchId = rst.getInt("branch_id");
        try {
            addressObj.setAddress1(rst.getString("address"));
        } catch (Exception exe) {}
        try {
            addressObj.setAddress2(rst.getString("address2"));
        } catch (Exception exe) {}
        try {
            addressObj.setCity(rst.getString("city"));
        } catch (Exception exe) {}
        try {
            addressObj.setState(rst.getString("state"));
        } catch (Exception exe) {}
        try {
            addressObj.setZip(rst.getString("zip"));
        } catch (Exception exe) {}
        try {
            contactName = rst.getString("contact_name");
        } catch (Exception exe) {}
        try {
            contactPhone = rst.getString("contact_phone");
        } catch (Exception exe) {}
        try {
            contactEmail = rst.getString("contact_email");
        } catch (Exception exe) {}
        phone = rst.getString("phone");
    }

    public BranchInfo(Integer branchId) {
        addressObj = new Address();

        this.branchId = branchId;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getAddress() {
        return addressObj.getAddress1();
    }

    public void setAddress(String address) {
        this.addressObj.setAddress1(address);
    }

    public String getAddress2() {
        return addressObj.getAddress2();
    }

    public void setAddress2(String address2) {
        this.addressObj.setAddress2(address2);
    }

    public String getCity() {
        return addressObj.getCity();
    }

    public void setCity(String city) {
        this.addressObj.setCity(city);
    }

    public String getState() {
        return addressObj.getState();
    }

    public void setState(String state) {
        this.addressObj.setState(state);
    }

    public String getZip() {
        return addressObj.getZip();
    }

    public void setZip(String zip) {
        this.addressObj.setZip(zip);
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (branchId != null ? branchId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof BranchInfo)) {
            return false;
        }
        BranchInfo other = (BranchInfo) object;
        if ((this.branchId == null && other.branchId != null) || (this.branchId != null && !this.branchId.equals(other.branchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.BranchInfo[branchId=" + branchId + "]";
    }

    public Address getAddressObj() {
        return this.addressObj;
    }

    /**
     * @return the contactName
     */
    public String getContactName() {
        return contactName;
    }

    /**
     * @param contactName the contactName to set
     */
    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    /**
     * @return the contactPhone
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * @param contactPhone the contactPhone to set
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * @return the contactEmail
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * @param contactEmail the contactEmail to set
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

}
