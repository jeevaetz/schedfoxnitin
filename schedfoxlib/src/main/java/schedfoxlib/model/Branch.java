/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *Atlanta, GA
 * @author user
 */
public class Branch implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer branchId;
    private String branchName;
    private Short branchManagementId;
    private String timezone;
    private Boolean usBranch;  
    private Boolean allowInfoExchange;
    private String defaultLanguage;
    private Boolean defaultBranch;
    //we

    private BranchInfo branchInfo;

    public Branch() {
    }

    public Branch(Record_Set rst) {
        branchId = rst.getInt("branch_id");
        branchName = rst.getString("branch_name");
        branchManagementId = (short)rst.getInt("branch_management_id");
        timezone = rst.getString("timezone");
        try {
            usBranch = rst.getBoolean("us_branch");
        } catch (Exception exe) {}
        
        try {
            this.branchInfo = new BranchInfo(rst);
        } catch (Exception e) {}
        try {
            this.allowInfoExchange = rst.getBoolean("allow_info_exchange");
        } catch (Exception e) {}
        try {
            this.defaultLanguage = rst.getString("default_language");
        } catch (Exception e) {}
        try {
            this.defaultBranch = rst.getBoolean("default_branch");
        } catch (Exception e) {}
    }

    public Branch(Integer branchId) {
        this.branchId = branchId;
    }

    public Branch(Integer branchId, String branchName) {
        this.branchId = branchId;
        this.branchName = branchName;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public Short getBranchManagementId() {
        return branchManagementId;
    }

    public void setBranchManagementId(Short branchManagementId) {
        this.branchManagementId = branchManagementId;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
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
        if (!(object instanceof Branch)) {
            return false;
        }
        Branch other = (Branch) object;
        if ((this.branchId == null && other.branchId != null) || (this.branchId != null && !this.branchId.equals(other.branchId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return branchName;
    }

    public Address getAddressObj() {
        return this.branchInfo.getAddressObj();
    }

    /**
     * @return the branchInfo
     */
    public BranchInfo getBranchInfo() {
        return branchInfo;
    }

    /**
     * @param branchInfo the branchInfo to set
     */
    public void setBranchInfo(BranchInfo branchInfo) {
        this.branchInfo = branchInfo;
    }

    /**
     * @return the usBranch
     */
    public Boolean getUsBranch() {
        return usBranch;
    }

    /**
     * @param usBranch the usBranch to set
     */
    public void setUsBranch(Boolean usBranch) {
        this.usBranch = usBranch;
    }

    /**
     * @return the allowInfoExchange
     */
    public Boolean getAllowInfoExchange() {
        return allowInfoExchange;
    }

    /**
     * @param allowInfoExchange the allowInfoExchange to set
     */
    public void setAllowInfoExchange(Boolean allowInfoExchange) {
        this.allowInfoExchange = allowInfoExchange;
    }

    /**
     * @return the defaultLanguage
     */
    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    /**
     * @param defaultLanguage the defaultLanguage to set
     */
    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    /**
     * @return the defaultBranch
     */
    public Boolean getDefaultBranch() {
        return defaultBranch;
    }

    /**
     * @param defaultBranch the defaultBranch to set
     */
    public void setDefaultBranch(Boolean defaultBranch) {
        this.defaultBranch = defaultBranch;
    }

}
