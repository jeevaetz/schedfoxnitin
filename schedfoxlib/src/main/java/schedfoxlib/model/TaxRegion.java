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
public class TaxRegion implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer taxRegionId;
    private String region;

    public TaxRegion() {
    }

    public TaxRegion(Record_Set rst) {
        try {
            this.taxRegionId = rst.getInt("tax_region_id");
        } catch (Exception e) {}
        try {
            this.region = rst.getString("region");
        } catch (Exception e) {}
    }

    public TaxRegion(Integer taxRegionId) {
        this.taxRegionId = taxRegionId;
    }

    public TaxRegion(Integer taxRegionId, String region) {
        this.taxRegionId = taxRegionId;
        this.region = region;
    }

    public Integer getTaxRegionId() {
        return taxRegionId;
    }

    public void setTaxRegionId(Integer taxRegionId) {
        this.taxRegionId = taxRegionId;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxRegionId != null ? taxRegionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxRegion)) {
            return false;
        }
        TaxRegion other = (TaxRegion) object;
        if ((this.taxRegionId == null && other.taxRegionId != null) || (this.taxRegionId != null && !this.taxRegionId.equals(other.taxRegionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.TaxRegion[taxRegionId=" + taxRegionId + "]";
    }

}
