/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ManagementClientInvoice {
    private Integer management_client_invoice_id;
    private Integer management_id;
    private BigDecimal amount_invoiced;
    private BigDecimal weekly_charge;
    private Date invoiced_on;
    private Integer employee_count;
    private BigDecimal employee_charge;
    private BigDecimal total_employees;
    private BigDecimal total_invoiced;

    public ManagementClientInvoice(Record_Set rst) {
        try {
            management_client_invoice_id = rst.getInt("management_client_invoice_id");
        } catch (Exception exe) {}
        try {
            management_id = rst.getInt("management_id");
        } catch (Exception exe) {}
        try {
            amount_invoiced = rst.getBigDecimal("amount_invoiced");
        } catch (Exception exe) {}
        try {
            weekly_charge = rst.getBigDecimal("weekly_charge");
        } catch (Exception exe) {}
        try {
            invoiced_on = rst.getDate("invoiced_on");
        } catch (Exception exe) {}

        try {
            employee_count = rst.getInt("total_employees");
        } catch (Exception exe) {}
        try {
            employee_charge = rst.getBigDecimal("amount_per_employee");
        } catch (Exception exe) {}
        try {
            total_invoiced = rst.getBigDecimal("total_invoiced");
        } catch (Exception exe) {}
        try {
            total_employees = rst.getBigDecimal("total_employees");
        } catch (Exception exe) {}
    }

    public BigDecimal calcTotalEmpCharge() {
        BigDecimal retVal = new BigDecimal(0);
        try {
            retVal = new BigDecimal((this.getEmployee_count() - 25) * this.getEmployee_charge().floatValue());
            if (retVal.doubleValue() < 0) {
                retVal = new BigDecimal(0);
            }
        } catch (Exception e) {}
        return retVal;
    }

    /**
     * @return the management_client_invoice_id
     */
    public Integer getManagement_client_invoice_id() {
        return management_client_invoice_id;
    }

    /**
     * @param management_client_invoice_id the management_client_invoice_id to set
     */
    public void setManagement_client_invoice_id(Integer management_client_invoice_id) {
        this.management_client_invoice_id = management_client_invoice_id;
    }

    /**
     * @return the management_id
     */
    public Integer getManagement_id() {
        return management_id;
    }

    /**
     * @param management_id the management_id to set
     */
    public void setManagement_id(Integer management_id) {
        this.management_id = management_id;
    }

    /**
     * @return the amount_invoiced
     */
    public BigDecimal getAmount_invoiced() {
        return amount_invoiced;
    }

    /**
     * @param amount_invoiced the amount_invoiced to set
     */
    public void setAmount_invoiced(BigDecimal amount_invoiced) {
        this.amount_invoiced = amount_invoiced;
    }

    /**
     * @return the invoiced_on
     */
    public Date getInvoiced_on() {
        return invoiced_on;
    }

    /**
     * @param invoiced_on the invoiced_on to set
     */
    public void setInvoiced_on(Date invoiced_on) {
        this.invoiced_on = invoiced_on;
    }

    /**
     * @return the employee_count
     */
    public Integer getEmployee_count() {
        return employee_count;
    }

    /**
     * @param employee_count the employee_count to set
     */
    public void setEmployee_count(Integer employee_count) {
        this.employee_count = employee_count;
    }

    /**
     * @return the employee_charge
     */
    public BigDecimal getEmployee_charge() {
        return employee_charge;
    }

    /**
     * @param employee_charge the employee_charge to set
     */
    public void setEmployee_charge(BigDecimal employee_charge) {
        this.employee_charge = employee_charge;
    }

    /**
     * @return the total_invoiced
     */
    public BigDecimal getTotal_invoiced() {
        return total_invoiced;
    }

    /**
     * @param total_invoiced the total_invoiced to set
     */
    public void setTotal_invoiced(BigDecimal total_invoiced) {
        this.total_invoiced = total_invoiced;
    }

    /**
     * @return the total_employees
     */
    public BigDecimal getTotal_employees() {
        return total_employees;
    }

    /**
     * @param total_employees the total_employees to set
     */
    public void setTotal_employees(BigDecimal total_employees) {
        this.total_employees = total_employees;
    }

    /**
     * @return the weekly_charge
     */
    public BigDecimal getWeekly_charge() {
        return weekly_charge;
    }

    /**
     * @param weekly_charge the weekly_charge to set
     */
    public void setWeekly_charge(BigDecimal weekly_charge) {
        this.weekly_charge = weekly_charge;
    }
}
