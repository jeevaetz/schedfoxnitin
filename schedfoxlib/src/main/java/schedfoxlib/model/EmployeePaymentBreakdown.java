/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EmployeePaymentBreakdown implements Serializable {

    private Integer employeePaymentBreakdownId;
    private Integer employee_payment_id;
    private Integer client_id;
    private Double reg_pay_hours;
    private Double reg_pay_rate;
    private Double reg_pay_amount;
    private Double over_pay_hours;
    private Double over_pay_rate;
    private Double over_pay_amount;
    private Double dbl_pay_hours;
    private Double dbl_pay_rate;
    private Double dbl_pay_amount;
    private Double reg_bill_hours;
    private Double reg_bill_rate;
    private Double reg_bill_amount;
    private Double over_bill_hours;
    private Double over_bill_rate;
    private Double over_bill_amount;
    private Double dbl_bill_hours;
    private Double dbl_bill_rate;
    private Double dbl_bill_amount;
    private Date work_date;
    
    //Lazy loaded objects
    private EmployeePayments payment;

    public EmployeePaymentBreakdown() {

    }

    public EmployeePaymentBreakdown(Record_Set rst) {
        try {
            employeePaymentBreakdownId = rst.getInt("employee_payment_breakdown_id");
        } catch (Exception exe) {
        }
        try {
            employee_payment_id = rst.getInt("employee_payment_id");
        } catch (Exception exe) {
        }
        try {
            client_id = rst.getInt("client_id");
        } catch (Exception exe) {
        }
        try {
            reg_pay_hours = rst.getBigDecimal("reg_pay_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            reg_pay_rate = rst.getBigDecimal("reg_pay_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            reg_pay_amount = rst.getBigDecimal("reg_pay_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_pay_hours = rst.getBigDecimal("over_pay_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_pay_rate = rst.getBigDecimal("over_pay_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_pay_amount = rst.getBigDecimal("over_pay_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_pay_hours = rst.getBigDecimal("dbl_pay_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_pay_rate = rst.getBigDecimal("dbl_pay_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_pay_amount = rst.getBigDecimal("dbl_pay_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            reg_bill_hours = rst.getBigDecimal("reg_bill_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            reg_bill_rate = rst.getBigDecimal("reg_bill_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            reg_bill_amount = rst.getBigDecimal("reg_bill_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_bill_hours = rst.getBigDecimal("over_bill_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_bill_rate = rst.getBigDecimal("over_bill_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            over_bill_amount = rst.getBigDecimal("over_bill_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_bill_hours = rst.getBigDecimal("dbl_bill_hours").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_bill_rate = rst.getBigDecimal("dbl_bill_rate").doubleValue();
        } catch (Exception exe) {
        }
        try {
            dbl_bill_amount = rst.getBigDecimal("dbl_bill_amount").doubleValue();
        } catch (Exception exe) {
        }
        try {
            work_date = rst.getDate("work_date");
        } catch (Exception exe) {}
    }

    /**
     * @return the employeePaymentBreakdownId
     */
    public Integer getEmployeePaymentBreakdownId() {
        return employeePaymentBreakdownId;
    }

    /**
     * @param employeePaymentBreakdownId the employeePaymentBreakdownId to set
     */
    public void setEmployeePaymentBreakdownId(Integer employeePaymentBreakdownId) {
        this.employeePaymentBreakdownId = employeePaymentBreakdownId;
    }

    /**
     * @return the employee_payment_id
     */
    public Integer getEmployee_payment_id() {
        return employee_payment_id;
    }

    /**
     * @param employee_payment_id the employee_payment_id to set
     */
    public void setEmployee_payment_id(Integer employee_payment_id) {
        this.employee_payment_id = employee_payment_id;
    }

    /**
     * @return the client_id
     */
    public Integer getClient_id() {
        return client_id;
    }

    /**
     * @param client_id the client_id to set
     */
    public void setClient_id(Integer client_id) {
        this.client_id = client_id;
    }

    /**
     * @return the reg_pay_hours
     */
    public Double getReg_pay_hours() {
        return reg_pay_hours;
    }

    /**
     * @param reg_pay_hours the reg_pay_hours to set
     */
    public void setReg_pay_hours(Double reg_pay_hours) {
        this.reg_pay_hours = reg_pay_hours;
    }

    /**
     * @return the reg_pay_rate
     */
    public Double getReg_pay_rate() {
        return reg_pay_rate;
    }

    /**
     * @param reg_pay_rate the reg_pay_rate to set
     */
    public void setReg_pay_rate(Double reg_pay_rate) {
        this.reg_pay_rate = reg_pay_rate;
    }

    /**
     * @return the reg_pay_amount
     */
    public Double getReg_pay_amount() {
        return reg_pay_amount;
    }

    /**
     * @param reg_pay_amount the reg_pay_amount to set
     */
    public void setReg_pay_amount(Double reg_pay_amount) {
        this.reg_pay_amount = reg_pay_amount;
    }

    /**
     * @return the over_pay_hours
     */
    public Double getOver_pay_hours() {
        return over_pay_hours;
    }

    /**
     * @param over_pay_hours the over_pay_hours to set
     */
    public void setOver_pay_hours(Double over_pay_hours) {
        this.over_pay_hours = over_pay_hours;
    }

    /**
     * @return the over_pay_rate
     */
    public Double getOver_pay_rate() {
        return over_pay_rate;
    }

    /**
     * @param over_pay_rate the over_pay_rate to set
     */
    public void setOver_pay_rate(Double over_pay_rate) {
        this.over_pay_rate = over_pay_rate;
    }

    /**
     * @return the over_pay_amount
     */
    public Double getOver_pay_amount() {
        return over_pay_amount;
    }

    /**
     * @param over_pay_amount the over_pay_amount to set
     */
    public void setOver_pay_amount(Double over_pay_amount) {
        this.over_pay_amount = over_pay_amount;
    }

    /**
     * @return the dbl_pay_hours
     */
    public Double getDbl_pay_hours() {
        return dbl_pay_hours;
    }

    /**
     * @param dbl_pay_hours the dbl_pay_hours to set
     */
    public void setDbl_pay_hours(Double dbl_pay_hours) {
        this.dbl_pay_hours = dbl_pay_hours;
    }

    /**
     * @return the dbl_pay_rate
     */
    public Double getDbl_pay_rate() {
        return dbl_pay_rate;
    }

    /**
     * @param dbl_pay_rate the dbl_pay_rate to set
     */
    public void setDbl_pay_rate(Double dbl_pay_rate) {
        this.dbl_pay_rate = dbl_pay_rate;
    }

    /**
     * @return the dbl_pay_amount
     */
    public Double getDbl_pay_amount() {
        return dbl_pay_amount;
    }

    /**
     * @param dbl_pay_amount the dbl_pay_amount to set
     */
    public void setDbl_pay_amount(Double dbl_pay_amount) {
        this.dbl_pay_amount = dbl_pay_amount;
    }

    /**
     * @return the reg_bill_hours
     */
    public Double getReg_bill_hours() {
        return reg_bill_hours;
    }

    /**
     * @param reg_bill_hours the reg_bill_hours to set
     */
    public void setReg_bill_hours(Double reg_bill_hours) {
        this.reg_bill_hours = reg_bill_hours;
    }

    /**
     * @return the reg_bill_rate
     */
    public Double getReg_bill_rate() {
        return reg_bill_rate;
    }

    /**
     * @param reg_bill_rate the reg_bill_rate to set
     */
    public void setReg_bill_rate(Double reg_bill_rate) {
        this.reg_bill_rate = reg_bill_rate;
    }

    /**
     * @return the reg_bill_amount
     */
    public Double getReg_bill_amount() {
        return reg_bill_amount;
    }

    /**
     * @param reg_bill_amount the reg_bill_amount to set
     */
    public void setReg_bill_amount(Double reg_bill_amount) {
        this.reg_bill_amount = reg_bill_amount;
    }

    /**
     * @return the over_bill_hours
     */
    public Double getOver_bill_hours() {
        return over_bill_hours;
    }

    /**
     * @param over_bill_hours the over_bill_hours to set
     */
    public void setOver_bill_hours(Double over_bill_hours) {
        this.over_bill_hours = over_bill_hours;
    }

    /**
     * @return the over_bill_rate
     */
    public Double getOver_bill_rate() {
        return over_bill_rate;
    }

    /**
     * @param over_bill_rate the over_bill_rate to set
     */
    public void setOver_bill_rate(Double over_bill_rate) {
        this.over_bill_rate = over_bill_rate;
    }

    /**
     * @return the over_bill_amount
     */
    public Double getOver_bill_amount() {
        return over_bill_amount;
    }

    /**
     * @param over_bill_amount the over_bill_amount to set
     */
    public void setOver_bill_amount(Double over_bill_amount) {
        this.over_bill_amount = over_bill_amount;
    }

    /**
     * @return the dbl_bill_hours
     */
    public Double getDbl_bill_hours() {
        return dbl_bill_hours;
    }

    /**
     * @param dbl_bill_hours the dbl_bill_hours to set
     */
    public void setDbl_bill_hours(Double dbl_bill_hours) {
        this.dbl_bill_hours = dbl_bill_hours;
    }

    /**
     * @return the dbl_bill_rate
     */
    public Double getDbl_bill_rate() {
        return dbl_bill_rate;
    }

    /**
     * @param dbl_bill_rate the dbl_bill_rate to set
     */
    public void setDbl_bill_rate(Double dbl_bill_rate) {
        this.dbl_bill_rate = dbl_bill_rate;
    }

    /**
     * @return the dbl_bill_amount
     */
    public Double getDbl_bill_amount() {
        return dbl_bill_amount;
    }

    /**
     * @param dbl_bill_amount the dbl_bill_amount to set
     */
    public void setDbl_bill_amount(Double dbl_bill_amount) {
        this.dbl_bill_amount = dbl_bill_amount;
    }

    /**
     * @return the payment
     */
    public EmployeePayments getPayment() {
        return payment;
    }

    /**
     * @param payment the payment to set
     */
    public void setPayment(EmployeePayments payment) {
        this.payment = payment;
    }

    /**
     * @return the work_date
     */
    public Date getWork_date() {
        return work_date;
    }

    /**
     * @param work_date the work_date to set
     */
    public void setWork_date(Date work_date) {
        this.work_date = work_date;
    }
}
