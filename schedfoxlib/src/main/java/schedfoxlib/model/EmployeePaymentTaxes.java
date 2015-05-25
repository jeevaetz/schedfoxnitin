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
public class EmployeePaymentTaxes implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeePaymentTaxId;
    private int employeePaymentId;
    private String taxType;
    private String code;
    private BigDecimal taxableAmount;
    private BigDecimal exemptAmount;
    private BigDecimal liAmount;
    private BigDecimal deAmount;

    public EmployeePaymentTaxes() {
    }

    public EmployeePaymentTaxes(Record_Set rst) {
        try {
            employeePaymentTaxId = rst.getInt("employee_payment_tax_id");
        } catch (Exception e) {}
        try {
            employeePaymentId = rst.getInt("employee_payment_id");
        } catch (Exception e) {}
        try {
            taxType = rst.getString("tax_type");
        } catch (Exception e) {}
        try {
            code = rst.getString("code");
        } catch (Exception e) {}
        try {
            taxableAmount = rst.getBigDecimal("taxable_amount");
        } catch (Exception e) {}
        try {
            exemptAmount = rst.getBigDecimal("exempt_amount");
        } catch (Exception e) {}
        try {
            liAmount = rst.getBigDecimal("li_amount");
        } catch (Exception e) {}
        try {
            deAmount = rst.getBigDecimal("de_amount");
        } catch (Exception e) {}

    }

    public EmployeePaymentTaxes(Integer employeePaymentTaxId) {
        this.employeePaymentTaxId = employeePaymentTaxId;
    }

    public EmployeePaymentTaxes(Integer employeePaymentTaxId, int employeePaymentId) {
        this.employeePaymentTaxId = employeePaymentTaxId;
        this.employeePaymentId = employeePaymentId;
    }

    public Integer getEmployeePaymentTaxId() {
        return employeePaymentTaxId;
    }

    public void setEmployeePaymentTaxId(Integer employeePaymentTaxId) {
        this.employeePaymentTaxId = employeePaymentTaxId;
    }

    public int getEmployeePaymentId() {
        return employeePaymentId;
    }

    public void setEmployeePaymentId(int employeePaymentId) {
        this.employeePaymentId = employeePaymentId;
    }

    public String getTaxType() {
        return taxType;
    }

    public void setTaxType(String taxType) {
        this.taxType = taxType;
    }

    public String getPrintableTaxType() {
        if (taxType != null) {
            if (taxType.equalsIgnoreCase("MC")) {
                return "Medicare";
            } else if (taxType.equalsIgnoreCase("SS")) {
                return "Social Security";
            } else if (taxType.equalsIgnoreCase("FE")) {
                return "Federal";
            } else if (taxType.equalsIgnoreCase("ST")) {
                return "State";
            } else if (taxType.equalsIgnoreCase("FU")) {
                return "Federal Unemployment";
            } else if (taxType.equalsIgnoreCase("SU")) {
                return "State Unemployment";
            } else if (taxType.equalsIgnoreCase("WE")) {
                return "Workers Compensation";
            } else if (taxType.equalsIgnoreCase("SU")) {
                return "State Unemployment";
            } else if (taxType.equalsIgnoreCase("CT")) {
                return "City Tax";
            }
        }
        return taxType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(BigDecimal taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public BigDecimal getExemptAmount() {
        return exemptAmount;
    }

    public void setExemptAmount(BigDecimal exemptAmount) {
        this.exemptAmount = exemptAmount;
    }

    public BigDecimal getLiAmount() {
        return liAmount;
    }

    public void setLiAmount(BigDecimal liAmount) {
        this.liAmount = liAmount;
    }

    public BigDecimal getDeAmount() {
        return deAmount;
    }

    public void setDeAmount(BigDecimal deAmount) {
        this.deAmount = deAmount;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeePaymentTaxId != null ? employeePaymentTaxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeePaymentTaxes)) {
            return false;
        }
        EmployeePaymentTaxes other = (EmployeePaymentTaxes) object;
        if ((this.employeePaymentTaxId == null && other.employeePaymentTaxId != null) || (this.employeePaymentTaxId != null && !this.employeePaymentTaxId.equals(other.employeePaymentTaxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeePaymentTaxes[employeePaymentTaxId=" + employeePaymentTaxId + "]";
    }

}
