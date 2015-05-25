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
public class EmployeeTax implements Serializable {
    private static final long serialVersionUID = 1L;


    private Integer employeeTaxId;
    private int taxType;
    private String marriageStatus;
    private int exemptions;
    private BigDecimal additionalWithholding;
    private int employeeId;

    //Lazy loaded objects
    private TaxTypes taxTypeObj;

    public EmployeeTax() {
    }

    public EmployeeTax(Record_Set rst) {
        try {
            this.employeeTaxId = rst.getInt("employee_tax_id");
        } catch (Exception e) {}
        try {
            this.taxType = rst.getInt("tax_type");
        } catch (Exception e) {}
        try {
            this.marriageStatus = rst.getString("marriage_status");
        } catch (Exception e) {}
        try {
            this.exemptions = rst.getInt("exemptions");
        } catch (Exception e) {}
        try {
            this.additionalWithholding = rst.getBigDecimal("additional_withholding");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}

        this.taxTypeObj = new TaxTypes(rst);
    }

    public EmployeeTax(Integer employeeTaxId) {
        this.employeeTaxId = employeeTaxId;
    }

    public EmployeeTax(Integer employeeTaxId, int taxType, String marriageStatus, int exemptions, BigDecimal additionalWithholding, int employeeId) {
        this.employeeTaxId = employeeTaxId;
        this.taxType = taxType;
        this.marriageStatus = marriageStatus;
        this.exemptions = exemptions;
        this.additionalWithholding = additionalWithholding;
        this.employeeId = employeeId;
    }

    public Integer getEmployeeTaxId() {
        return employeeTaxId;
    }

    public void setEmployeeTaxId(Integer employeeTaxId) {
        this.employeeTaxId = employeeTaxId;
    }

    public int getTaxType() {
        return taxType;
    }

    public void setTaxType(int taxType) {
        this.taxType = taxType;
    }

    public String getMarriageStatus() {
        return marriageStatus;
    }

    public void setMarriageStatus(String marriageStatus) {
        this.marriageStatus = marriageStatus;
    }

    public int getExemptions() {
        return exemptions;
    }

    public void setExemptions(int exemptions) {
        this.exemptions = exemptions;
    }

    public BigDecimal getAdditionalWithholding() {
        return additionalWithholding;
    }

    public void setAdditionalWithholding(BigDecimal additionalWithholding) {
        this.additionalWithholding = additionalWithholding;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeeTaxId != null ? employeeTaxId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeeTax)) {
            return false;
        }
        EmployeeTax other = (EmployeeTax) object;
        if ((this.employeeTaxId == null && other.employeeTaxId != null) || (this.employeeTaxId != null && !this.employeeTaxId.equals(other.employeeTaxId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeeTax[employeeTaxId=" + employeeTaxId + "]";
    }

    /**
     * @return the taxTypeObj
     */
    public TaxTypes getTaxTypeObj() {
        return taxTypeObj;
    }

    /**
     * @param taxTypeObj the taxTypeObj to set
     */
    public void setTaxTypeObj(TaxTypes taxTypeObj) {
        this.taxTypeObj = taxTypeObj;
    }

}
