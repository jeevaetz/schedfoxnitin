/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.math.BigDecimal;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EmployeePaymentWage implements Serializable {
    private Integer employeePaymentWageId;
    private Integer employeePaymentId;
    private String wageType;
    private String uskedId;
    private BigDecimal wageAmount;
    
    public EmployeePaymentWage() {
        
    }
    
    public EmployeePaymentWage(Record_Set rst) {
        try {
            employeePaymentWageId = rst.getInt("employee_payment_wage_id");
        } catch (Exception exe) {}
        try {
            employeePaymentId = rst.getInt("employee_payment_id");
        } catch (Exception exe) {}
        try {
            wageType = rst.getString("wage_type");
        } catch (Exception exe) {}
        try {
            uskedId = rst.getString("usked_id");
        } catch (Exception exe) {}
        try {
            wageAmount = rst.getBigDecimal("wage_amount");
        } catch (Exception exe) {}
    }

    /**
     * @return the employeePaymentWageId
     */
    public Integer getEmployeePaymentWageId() {
        return employeePaymentWageId;
    }

    /**
     * @param employeePaymentWageId the employeePaymentWageId to set
     */
    public void setEmployeePaymentWageId(Integer employeePaymentWageId) {
        this.employeePaymentWageId = employeePaymentWageId;
    }

    /**
     * @return the employeePaymentId
     */
    public Integer getEmployeePaymentId() {
        return employeePaymentId;
    }

    /**
     * @param employeePaymentId the employeePaymentId to set
     */
    public void setEmployeePaymentId(Integer employeePaymentId) {
        this.employeePaymentId = employeePaymentId;
    }

    /**
     * @return the wageType
     */
    public String getWageType() {
        return wageType;
    }

    /**
     * @param wageType the wageType to set
     */
    public void setWageType(String wageType) {
        this.wageType = wageType;
    }

    /**
     * @return the uskedId
     */
    public String getUskedId() {
        return uskedId;
    }

    /**
     * @param uskedId the uskedId to set
     */
    public void setUskedId(String uskedId) {
        this.uskedId = uskedId;
    }

    /**
     * @return the wageAmount
     */
    public BigDecimal getWageAmount() {
        return wageAmount;
    }

    /**
     * @param wageAmount the wageAmount to set
     */
    public void setWageAmount(BigDecimal wageAmount) {
        this.wageAmount = wageAmount;
    }
    
    public String getPrintableType() {
        try {
            if (this.wageType.equals("M1")) {
                return "Vacation Pay";
            } else if (this.wageType.equals("M2")) {
                return "Miscellaneous";
            } else if (this.wageType.equals("M4")) {
                return "Health Insurance Reimbursement 2014";
            } else if (this.wageType.equals("M5")) {
                return "Health Insurance Reimbursement";
            }
        } catch (Exception exe) {
        }
        return this.wageType;
    }
}
