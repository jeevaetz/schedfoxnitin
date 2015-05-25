/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class ClientInvoice {

    private int client_invoice_id;
    private int client_id;
    private Date issued_on;
    private Date due_on;
    private Date pif_on;
    private BigDecimal amount_due;
    private BigDecimal balance_due;

    //Lazy loaded objects
    private ArrayList<ClientInvoiceHours> hours = new ArrayList<ClientInvoiceHours>();
    private ArrayList<ClientInvoicePayments> payments = new ArrayList<ClientInvoicePayments>();
    private ArrayList<ClientInvoiceTaxes> taxes = new ArrayList<ClientInvoiceTaxes>();

    //Calculated / Greedy Loaded Fields
    private BigDecimal totalTaxes;
    private BigDecimal totalRegHrs;
    private BigDecimal totalOverHrs;
    private BigDecimal totalRegAmt;
    private BigDecimal totalOverAmt;

    public ClientInvoice() {
        
    }
    
    public ClientInvoice(Record_Set rst) {
        client_invoice_id = rst.getInt("client_invoice_id");
        client_id = rst.getInt("client_id");
        try {
            issued_on = rst.getDate("issued_on");
        } catch (Exception e) {}
        try {
            due_on = rst.getDate("due_on");
        } catch (Exception e) {}
        try {
            pif_on = rst.getDate("pif_on");
        } catch (Exception e) {}
        amount_due = rst.getBigDecimal("amount_due");
        balance_due = rst.getBigDecimal("balance_due");

        try {
            totalTaxes = rst.getBigDecimal("tot_taxed_amount");
        } catch (Exception e) {}
        try {
            totalRegHrs = rst.getBigDecimal("tot_reg_num_hrs");
        } catch (Exception e) {}
        try {
            totalOverHrs = rst.getBigDecimal("tot_overtime_num_hrs");
        } catch (Exception e) {}
        try {
            totalRegAmt = rst.getBigDecimal("tot_reg_hrs_bill_amt");
        } catch (Exception e) {}
        try {
            totalOverAmt = rst.getBigDecimal("tot_overtime_hrs_bill_amt");
        } catch (Exception e) {}
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
     * @return the client_id
     */
    public int getClient_id() {
        return client_id;
    }

    /**
     * @param client_id the client_id to set
     */
    public void setClient_id(int client_id) {
        this.client_id = client_id;
    }

    /**
     * @return the issued_on
     */
    public Date getIssued_on() {
        return issued_on;
    }

    /**
     * @param issued_on the issued_on to set
     */
    public void setIssued_on(Date issued_on) {
        this.issued_on = issued_on;
    }

    /**
     * @return the due_on
     */
    public Date getDue_on() {
        return due_on;
    }

    /**
     * @param due_on the due_on to set
     */
    public void setDue_on(Date due_on) {
        this.due_on = due_on;
    }

    /**
     * @return the pif_on
     */
    public Date getPif_on() {
        return pif_on;
    }

    /**
     * @param pif_on the pif_on to set
     */
    public void setPif_on(Date pif_on) {
        this.pif_on = pif_on;
    }

    /**
     * @return the amount_due
     */
    public BigDecimal getAmount_due() {
        return amount_due;
    }

    /**
     * @param amount_due the amount_due to set
     */
    public void setAmount_due(BigDecimal amount_due) {
        this.amount_due = amount_due;
    }

    /**
     * @return the balance_due
     */
    public BigDecimal getBalance_due() {
        return balance_due;
    }

    /**
     * @param balance_due the balance_due to set
     */
    public void setBalance_due(BigDecimal balance_due) {
        this.balance_due = balance_due;
    }

    /**
     * @return the hours
     */
    public ArrayList<ClientInvoiceHours> getHours() {
        return hours;
    }

    /**
     * @param hours the hours to set
     */
    public void setHours(ArrayList<ClientInvoiceHours> hours) {
        this.setHours(hours);
    }

    /**
     * @return the payments
     */
    public ArrayList<ClientInvoicePayments> getPayments() {
        return payments;
    }

    /**
     * @param payments the payments to set
     */
    public void setPayments(ArrayList<ClientInvoicePayments> payments) {
        this.payments = payments;
    }

    /**
     * @return the taxes
     */
    public ArrayList<ClientInvoiceTaxes> getTaxes() {
        return taxes;
    }

    /**
     * @param taxes the taxes to set
     */
    public void setTaxes(ArrayList<ClientInvoiceTaxes> taxes) {
        this.taxes = taxes;
    }

    /**
     * @return the totalTaxes
     */
    public BigDecimal getTotalTaxes() {
        return totalTaxes;
    }

    /**
     * @param totalTaxes the totalTaxes to set
     */
    public void setTotalTaxes(BigDecimal totalTaxes) {
        this.totalTaxes = totalTaxes;
    }

    /**
     * @return the totalRegHrs
     */
    public BigDecimal getTotalRegHrs() {
        return totalRegHrs;
    }

    /**
     * @param totalRegHrs the totalRegHrs to set
     */
    public void setTotalRegHrs(BigDecimal totalRegHrs) {
        this.totalRegHrs = totalRegHrs;
    }

    /**
     * @return the totalOverHrs
     */
    public BigDecimal getTotalOverHrs() {
        return totalOverHrs;
    }

    /**
     * @param totalOverHrs the totalOverHrs to set
     */
    public void setTotalOverHrs(BigDecimal totalOverHrs) {
        this.totalOverHrs = totalOverHrs;
    }

    /**
     * @return the totalRegAmt
     */
    public BigDecimal getTotalRegAmt() {
        return totalRegAmt;
    }

    /**
     * @param totalRegAmt the totalRegAmt to set
     */
    public void setTotalRegAmt(BigDecimal totalRegAmt) {
        this.totalRegAmt = totalRegAmt;
    }

    /**
     * @return the totalOverAmt
     */
    public BigDecimal getTotalOverAmt() {
        return totalOverAmt;
    }

    /**
     * @param totalOverAmt the totalOverAmt to set
     */
    public void setTotalOverAmt(BigDecimal totalOverAmt) {
        this.totalOverAmt = totalOverAmt;
    }

}
