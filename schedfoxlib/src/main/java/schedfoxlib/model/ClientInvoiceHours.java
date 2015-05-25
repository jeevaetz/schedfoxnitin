/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;

/**
 *
 * @author user
 */
public class ClientInvoiceHours {
    private int client_invoice_hour_id;
    private int client_invoice_id;
    private int reg_num_hrs;
    private BigDecimal reg_hrs_bill_amt;
    private int overtime_num_hrs;
    private BigDecimal overtime_hrs_bill_amt;
    private int rate_code;

    /**
     * @return the client_invoice_hour_id
     */
    public int getClient_invoice_hour_id() {
        return client_invoice_hour_id;
    }

    /**
     * @param client_invoice_hour_id the client_invoice_hour_id to set
     */
    public void setClient_invoice_hour_id(int client_invoice_hour_id) {
        this.client_invoice_hour_id = client_invoice_hour_id;
    }

    /**
     * @return the client_invoice_id
     */
    public int getClient_invoice_id() {
        return client_invoice_id;
    }

    /**
     * @param client_invoice_id the client_invoice_id to set
     */
    public void setClient_invoice_id(int client_invoice_id) {
        this.client_invoice_id = client_invoice_id;
    }

    /**
     * @return the reg_num_hrs
     */
    public int getReg_num_hrs() {
        return reg_num_hrs;
    }

    /**
     * @param reg_num_hrs the reg_num_hrs to set
     */
    public void setReg_num_hrs(int reg_num_hrs) {
        this.reg_num_hrs = reg_num_hrs;
    }

    /**
     * @return the reg_hrs_bill_amt
     */
    public BigDecimal getReg_hrs_bill_amt() {
        return reg_hrs_bill_amt;
    }

    /**
     * @param reg_hrs_bill_amt the reg_hrs_bill_amt to set
     */
    public void setReg_hrs_bill_amt(BigDecimal reg_hrs_bill_amt) {
        this.reg_hrs_bill_amt = reg_hrs_bill_amt;
    }

    /**
     * @return the overtime_num_hrs
     */
    public int getOvertime_num_hrs() {
        return overtime_num_hrs;
    }

    /**
     * @param overtime_num_hrs the overtime_num_hrs to set
     */
    public void setOvertime_num_hrs(int overtime_num_hrs) {
        this.overtime_num_hrs = overtime_num_hrs;
    }

    /**
     * @return the overtime_hrs_bill_amt
     */
    public BigDecimal getOvertime_hrs_bill_amt() {
        return overtime_hrs_bill_amt;
    }

    /**
     * @param overtime_hrs_bill_amt the overtime_hrs_bill_amt to set
     */
    public void setOvertime_hrs_bill_amt(BigDecimal overtime_hrs_bill_amt) {
        this.overtime_hrs_bill_amt = overtime_hrs_bill_amt;
    }

    /**
     * @return the rate_code
     */
    public int getRate_code() {
        return rate_code;
    }

    /**
     * @param rate_code the rate_code to set
     */
    public void setRate_code(int rate_code) {
        this.rate_code = rate_code;
    }
}
