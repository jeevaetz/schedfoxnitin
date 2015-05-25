/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.controller.EmployeeControllerInterface;
import schedfoxlib.controller.registry.ControllerRegistryAbstract;

/**
 *
 * @author user
 */
public class EmployeePayments implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeePaymentsId;
    private int employeeId;
    private Date dateOfTrans;
    private int transType;
    private BigDecimal netPay;
    private BigDecimal grossPay;
    private String checkNum;

    //Non table fields for speed
    private BigDecimal sumDeductions;
    private BigDecimal sumTax;

    //Lazy loaded objects
    private ArrayList<EmployeePaymentHours> hours;
    private ArrayList<EmployeePaymentDeduction> deductions;
    private ArrayList<EmployeePaymentTaxes> taxes;
    private ArrayList<EmployeePaymentWage> wages;

    public EmployeePayments() {
    }

    public EmployeePayments(Integer employeePaymentsId) {
        this.employeePaymentsId = employeePaymentsId;
    }

    public EmployeePayments(Record_Set rst) {
        try {
            this.employeePaymentsId = rst.getInt("employee_payments_id");
        } catch (Exception e) {}
        try {
            this.employeeId = rst.getInt("employee_id");
        } catch (Exception e) {}
        try {
            this.dateOfTrans = rst.getDate("date_of_trans");
        } catch (Exception e) {}
        try {
            this.transType = rst.getInt("trans_type");
        } catch (Exception e) {}
        try {
            this.netPay = rst.getBigDecimal("net_pay");
        } catch (Exception e) {}
        try {
            this.grossPay = rst.getBigDecimal("gross_pay");
        } catch (Exception e) {}
        try {
            this.sumDeductions = rst.getBigDecimal("dedamt");
        } catch (Exception e) {}
        try {
            this.sumTax = rst.getBigDecimal("taxamt");
        } catch (Exception e) {}
        try {
            this.checkNum = rst.getString("check_num");
        } catch (Exception e) {}
    }

    public Integer getEmployeePaymentsId() {
        return employeePaymentsId;
    }

    public void setEmployeePaymentsId(Integer employeePaymentsId) {
        this.employeePaymentsId = employeePaymentsId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public Date getDateOfTrans() {
        return dateOfTrans;
    }

    public void setDateOfTrans(Date dateOfTrans) {
        this.dateOfTrans = dateOfTrans;
    }

    public int getTransType() {
        return transType;
    }

    public void setTransType(int transType) {
        this.transType = transType;
    }

    public BigDecimal getNetPay() {
        return netPay;
    }

    public void setNetPay(BigDecimal netPay) {
        this.netPay = netPay;
    }

    public BigDecimal getGrossPay() {
        return grossPay;
    }

    public void setGrossPay(BigDecimal grossPay) {
        this.grossPay = grossPay;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeePaymentsId != null ? employeePaymentsId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeePayments)) {
            return false;
        }
        EmployeePayments other = (EmployeePayments) object;
        /*if ((this.employeePaymentsId == null && other.employeePaymentsId != null) || (this.employeePaymentsId != null && !this.employeePaymentsId.equals(other.employeePaymentsId))) {
            return false;
        }
        return true;*/
        return this.employeeId == other.employeeId;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeePayments[employeePaymentsId=" + employeePaymentsId + "]";
    }

    public BigDecimal getSumWages() {
        try {
            double runningVal = 0;
            for (int w = 0; w < this.wages.size(); w++) {
                runningVal += this.wages.get(w).getWageAmount().doubleValue();
            }
            return new BigDecimal(runningVal);
        } catch (Exception exe) {}
        return new BigDecimal(0);
    }
    
    /**
     * @return the sumDeductions
     */
    public BigDecimal getSumDeductions() {
        return sumDeductions;
    }

    /**
     * @param sumDeductions the sumDeductions to set
     */
    public void setSumDeductions(BigDecimal sumDeductions) {
        this.sumDeductions = sumDeductions;
    }

    /**
     * @return the sumTax
     */
    public BigDecimal getSumTax() {
        return sumTax;
    }

    /**
     * @param sumTax the sumTax to set
     */
    public void setSumTax(BigDecimal sumTax) {
        this.sumTax = sumTax;
    }

    /**
     * Gets the hours.
     * @return
     */
    public ArrayList<EmployeePaymentHours> getHours(String companyId) {
        if (this.hours == null) {
            try {
                EmployeeControllerInterface employeeInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.hours = employeeInterface.getHours(this.getEmployeePaymentsId());
            } catch (Exception e) {
                this.hours = new ArrayList<EmployeePaymentHours>();
            }
        }
        return this.hours;
    }

    /**
     * Gets the deductions.
     * @return
     */
    public ArrayList<EmployeePaymentDeduction> getDeductions(String companyId) {
        if (this.deductions == null) {
            try {
                EmployeeControllerInterface employeeInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.deductions = employeeInterface.getDeductions(this.getEmployeePaymentsId());
            } catch (Exception e) {
                this.deductions = new ArrayList<EmployeePaymentDeduction>();
            }
        }
        return this.deductions;
    }
    
    /**
     * Gets the wages.
     * @return
     */
    public ArrayList<EmployeePaymentWage> getWages(String companyId) {
        if (this.wages == null) {
            try {
                EmployeeControllerInterface employeeInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.wages = employeeInterface.getWages(this.getEmployeePaymentsId());
            } catch (Exception e) {
                this.wages = new ArrayList<EmployeePaymentWage>();
            }
        }
        return this.wages;
    }

    /**
     * Gets the deductions.
     * @return
     */
    public ArrayList<EmployeePaymentTaxes> getTaxes(String companyId) {
        if (this.taxes == null) {
            try {
                EmployeeControllerInterface employeeInterface = ControllerRegistryAbstract.getEmployeeController(companyId);
                this.taxes = employeeInterface.getTaxes(this.getEmployeePaymentsId());
            } catch (Exception e) {
                this.taxes = new ArrayList<EmployeePaymentTaxes>();
            }
        }
        return this.taxes;
    }

    /**
     * @return the checkNum
     */
    public String getCheckNum() {
        return checkNum;
    }

    /**
     * @param checkNum the checkNum to set
     */
    public void setCheckNum(String checkNum) {
        this.checkNum = checkNum;
    }
}
