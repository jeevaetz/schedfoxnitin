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
public class EmployeePaymentDeduction implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeePaymentDeductionId;
    private int employeePaymentId;
    private BigDecimal amount;
    private String type;

    public EmployeePaymentDeduction() {
    }

    public EmployeePaymentDeduction(Record_Set rst) {
        try {
            this.employeePaymentDeductionId = rst.getInt("employee_payment_deduction_id");
        } catch (Exception e) {}
        try {
            this.employeePaymentId = rst.getInt("employee_payment_id");
        } catch (Exception e) {}
        try {
            this.amount = rst.getBigDecimal("amount");
        } catch (Exception e) {}
        try {
            this.type = rst.getString("type");
        } catch (Exception e) {}
    }

    public EmployeePaymentDeduction(Integer employeePaymentDeductionId) {
        this.employeePaymentDeductionId = employeePaymentDeductionId;
    }

    public EmployeePaymentDeduction(Integer employeePaymentDeductionId, int employeePaymentId) {
        this.employeePaymentDeductionId = employeePaymentDeductionId;
        this.employeePaymentId = employeePaymentId;
    }

    public Integer getEmployeePaymentDeductionId() {
        return employeePaymentDeductionId;
    }

    public void setEmployeePaymentDeductionId(Integer employeePaymentDeductionId) {
        this.employeePaymentDeductionId = employeePaymentDeductionId;
    }

    public int getEmployeePaymentId() {
        return employeePaymentId;
    }

    public void setEmployeePaymentId(int employeePaymentId) {
        this.employeePaymentId = employeePaymentId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getPrintableType() {
        if (type != null) {
            if (type.equalsIgnoreCase("UN")) {
                return "Uniform";
            } else if (type.equalsIgnoreCase("RG")) {
                return "Registration";
            } else if (type.equalsIgnoreCase("CS")) {
                return "Child Support";
            } else if (type.equalsIgnoreCase("LI")) {
                return "Life Insurance";
            } else if (type.equalsIgnoreCase("HI")) {
                return "Health Insurance";
            } else if (type.equalsIgnoreCase("DR")) {
                return "Draw";
            } else if (type.equalsIgnoreCase("IR")) {
                return "IRS Loan Bank";
            }
        }
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeePaymentDeductionId != null ? employeePaymentDeductionId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeePaymentDeduction)) {
            return false;
        }
        EmployeePaymentDeduction other = (EmployeePaymentDeduction) object;
        if ((this.employeePaymentDeductionId == null && other.employeePaymentDeductionId != null) || (this.employeePaymentDeductionId != null && !this.employeePaymentDeductionId.equals(other.employeePaymentDeductionId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeePaymentDeduction[employeePaymentDeductionId=" + employeePaymentDeductionId + "]";
    }

}
