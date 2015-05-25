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
public class CompanyView implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer companyViewId;
    private int companyViewOptionTypeId;
    private String companyViewKey;
    private String companyViewDefaultValue;
    private Integer viewType;

    public CompanyView() {
    }

    public CompanyView(Integer companyViewId) {
        this.companyViewId = companyViewId;
    }

    public CompanyView(Integer companyViewId, int companyViewOptionTypeId, String companyViewKey, String companyViewDefaultValue) {
        this.companyViewId = companyViewId;
        this.companyViewOptionTypeId = companyViewOptionTypeId;
        this.companyViewKey = companyViewKey;
        this.companyViewDefaultValue = companyViewDefaultValue;
    }

    public CompanyView(Record_Set rst) {
        try {
            this.companyViewDefaultValue = rst.getString("company_view_default_value");
        } catch (Exception e) {}
        try {
            this.companyViewId = rst.getInt("company_view_id");
        } catch (Exception e) {}
        try {
            this.companyViewKey = rst.getString("company_view_key");
        } catch (Exception e) {}
        try {
            this.companyViewOptionTypeId = rst.getInt("company_view_option_type_id");
        } catch (Exception e) {}
        try {
            this.viewType = rst.getInt("view_type");
        } catch (Exception e) {}
    }

    public Integer getCompanyViewId() {
        return companyViewId;
    }

    public void setCompanyViewId(Integer companyViewId) {
        this.companyViewId = companyViewId;
    }

    public int getCompanyViewOptionTypeId() {
        return companyViewOptionTypeId;
    }

    public void setCompanyViewOptionTypeId(int companyViewOptionTypeId) {
        this.companyViewOptionTypeId = companyViewOptionTypeId;
    }

    public String getCompanyViewKey() {
        return companyViewKey;
    }

    public void setCompanyViewKey(String companyViewKey) {
        this.companyViewKey = companyViewKey;
    }

    public String getCompanyViewDefaultValue() {
        return companyViewDefaultValue;
    }

    public void setCompanyViewDefaultValue(String companyViewDefaultValue) {
        this.companyViewDefaultValue = companyViewDefaultValue;
    }

    public Integer getViewType() {
        return viewType;
    }

    public void setViewType(Integer viewType) {
        this.viewType = viewType;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (companyViewId != null ? companyViewId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CompanyView)) {
            return false;
        }
        CompanyView other = (CompanyView) object;
        if ((this.companyViewId == null && other.companyViewId != null) || (this.companyViewId != null && !this.companyViewId.equals(other.companyViewId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.CompanyView[companyViewId=" + companyViewId + "]";
    }

}
