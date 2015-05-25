/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class TaxTypes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer taxTypeId;
    private String taxName;
    private BigDecimal taxRate;
    private int taxRegionId;

    //Lazy loaded objects
    private TaxRegion taxRegion;

    public TaxTypes() {
    }

    public TaxTypes(Record_Set rst) {
        try {
            this.taxTypeId = rst.getInt("tax_type_id");
        } catch (Exception e) {}
        try {
            this.taxName = rst.getString("tax_name");
        } catch (Exception e) {}
        try {
            this.taxRate = rst.getBigDecimal("tax_rate");
        } catch (Exception e) {}
        try {
            this.taxRegionId = rst.getInt("tax_region_id");
        } catch (Exception e) {}

        this.taxRegion = new TaxRegion(rst);
    }

    public TaxTypes(Integer taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public TaxTypes(Integer taxTypeId, String taxName, BigDecimal taxRate, int taxRegionId) {
        this.taxTypeId = taxTypeId;
        this.taxName = taxName;
        this.taxRate = taxRate;
        this.taxRegionId = taxRegionId;
    }

    public Integer getTaxTypeId() {
        return taxTypeId;
    }

    public void setTaxTypeId(Integer taxTypeId) {
        this.taxTypeId = taxTypeId;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public BigDecimal getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(BigDecimal taxRate) {
        this.taxRate = taxRate;
    }

    public int getTaxRegionId() {
        return taxRegionId;
    }

    public void setTaxRegionId(int taxRegionId) {
        this.taxRegionId = taxRegionId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (taxTypeId != null ? taxTypeId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TaxTypes)) {
            return false;
        }
        TaxTypes other = (TaxTypes) object;
        if ((this.taxTypeId == null && other.taxTypeId != null) || (this.taxTypeId != null && !this.taxTypeId.equals(other.taxTypeId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.TaxTypes[taxTypeId=" + taxTypeId + "]";
    }

    /**
     * @return the taxRegion
     */
    public TaxRegion getTaxRegion() {
        return taxRegion;
    }

    /**
     * @param taxRegion the taxRegion to set
     */
    public void setTaxRegion(TaxRegion taxRegion) {
        this.taxRegion = taxRegion;
    }

}
