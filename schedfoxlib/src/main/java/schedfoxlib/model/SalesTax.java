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
public class SalesTax implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer salesTaxId;
    private String salesTax;
    private BigDecimal salesTaxRate;
    private String exportId;

    public SalesTax() {
    }

    public SalesTax(Integer salesTaxId) {
        this.salesTaxId = salesTaxId;
    }

    public SalesTax(Record_Set rst) {
        this.salesTaxId = rst.getInt("sales_tax_id");
        this.salesTaxRate = rst.getBigDecimal("sales_tax_rate");
        this.exportId = rst.getString("export_id");
        this.salesTax = rst.getString("sales_tax");
    }

    public SalesTax(Integer salesTaxId, String salesTax, BigDecimal salesTaxRate) {
        this.salesTaxId = salesTaxId;
        this.salesTax = salesTax;
        this.salesTaxRate = salesTaxRate;
    }

    public Integer getSalesTaxId() {
        return salesTaxId;
    }

    public void setSalesTaxId(Integer salesTaxId) {
        this.salesTaxId = salesTaxId;
    }

    public String getSalesTax() {
        return salesTax;
    }

    public void setSalesTax(String salesTax) {
        this.salesTax = salesTax;
    }

    public BigDecimal getSalesTaxRate() {
        return salesTaxRate;
    }

    public void setSalesTaxRate(BigDecimal salesTaxRate) {
        this.salesTaxRate = salesTaxRate;
    }

    public String getExportId() {
        return exportId;
    }

    public void setExportId(String exportId) {
        this.exportId = exportId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (salesTaxId != null ? salesTaxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof SalesTax)) {
            return false;
        }
        SalesTax other = (SalesTax) object;
        if ((this.salesTaxId == null && other.salesTaxId != null) || (this.salesTaxId != null && !this.salesTaxId.equals(other.salesTaxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.salesTax;
    }

}
