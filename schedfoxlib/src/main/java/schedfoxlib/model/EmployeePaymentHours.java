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
public class EmployeePaymentHours implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer employeePaymentHourId;

    private int employeePaymentId;
    private String hourType;
    private BigDecimal regularHrs;
    private BigDecimal overtimeHrs;
    private BigDecimal regularPay;
    private BigDecimal overtimePay;

    public EmployeePaymentHours() {
    }

    public EmployeePaymentHours(Integer employeePaymentHourId) {
        this.employeePaymentHourId = employeePaymentHourId;
    }

    public EmployeePaymentHours(Integer employeePaymentHourId, int employeePaymentId, String hourType) {
        this.employeePaymentHourId = employeePaymentHourId;
        this.employeePaymentId = employeePaymentId;
        this.hourType = hourType;
    }

    public EmployeePaymentHours(Record_Set rst) {
        try {
             this.employeePaymentHourId = rst.getInt("employee_payment_hour_id");
        } catch (Exception e) {}
        try {
             this.employeePaymentId = rst.getInt("employee_payment_id");
        } catch (Exception e) {}
        try {
             this.hourType = rst.getString("hour_type");
        } catch (Exception e) {}
        try {
             this.overtimeHrs = rst.getBigDecimal("overtime_hrs");
        } catch (Exception e) {}
        try {
             this.overtimePay = rst.getBigDecimal("overtime_pay");
        } catch (Exception e) {}
        try {
             this.regularHrs = rst.getBigDecimal("regular_hrs");
        } catch (Exception e) {}
        try {
             this.regularPay = rst.getBigDecimal("regular_pay");
        } catch (Exception e) {}

    }

    public Integer getEmployeePaymentHourId() {
        return employeePaymentHourId;
    }

    public void setEmployeePaymentHourId(Integer employeePaymentHourId) {
        this.employeePaymentHourId = employeePaymentHourId;
    }

    public int getEmployeePaymentId() {
        return employeePaymentId;
    }

    public void setEmployeePaymentId(int employeePaymentId) {
        this.employeePaymentId = employeePaymentId;
    }

    public String getHourType() {
        return hourType;
    }

    public void setHourType(String hourType) {
        this.hourType = hourType;
    }

    public BigDecimal getRegularHrs() {
        return regularHrs;
    }

    public void setRegularHrs(BigDecimal regularHrs) {
        this.regularHrs = regularHrs;
    }

    public BigDecimal getOvertimeHrs() {
        return overtimeHrs;
    }

    public void setOvertimeHrs(BigDecimal overtimeHrs) {
        this.overtimeHrs = overtimeHrs;
    }

    public BigDecimal getRegularPay() {
        return regularPay;
    }

    public void setRegularPay(BigDecimal regularPay) {
        this.regularPay = regularPay;
    }

    public BigDecimal getOvertimePay() {
        return overtimePay;
    }

    public void setOvertimePay(BigDecimal overtimePay) {
        this.overtimePay = overtimePay;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (employeePaymentHourId != null ? employeePaymentHourId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof EmployeePaymentHours)) {
            return false;
        }
        EmployeePaymentHours other = (EmployeePaymentHours) object;
        if ((this.employeePaymentHourId == null && other.employeePaymentHourId != null) || (this.employeePaymentHourId != null && !this.employeePaymentHourId.equals(other.employeePaymentHourId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "schedfoxlib.model.EmployeePaymentHours[employeePaymentHourId=" + employeePaymentHourId + "]";
    }

}
