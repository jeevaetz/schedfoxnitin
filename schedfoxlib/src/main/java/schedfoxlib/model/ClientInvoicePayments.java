/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author user
 */
public class ClientInvoicePayments {
    private int client_invoice_payments_id;
    private int invoice_id;
    private Date paidon;
    private int payment_type;
    private BigDecimal paid_amount;
    private BigDecimal exempt_amount;
    private BigDecimal taxed_amount;

    /**
     * @return the client_invoice_payments_id
     */
    public int getClient_invoice_payments_id() {
        return client_invoice_payments_id;
    }

    /**
     * @param client_invoice_payments_id the client_invoice_payments_id to set
     */
    public void setClient_invoice_payments_id(int client_invoice_payments_id) {
        this.client_invoice_payments_id = client_invoice_payments_id;
    }

    /**
     * @return the invoice_id
     */
    public int getInvoice_id() {
        return invoice_id;
    }

    /**
     * @param invoice_id the invoice_id to set
     */
    public void setInvoice_id(int invoice_id) {
        this.invoice_id = invoice_id;
    }

    /**
     * @return the paidon
     */
    public Date getPaidon() {
        return paidon;
    }

    /**
     * @param paidon the paidon to set
     */
    public void setPaidon(Date paidon) {
        this.paidon = paidon;
    }

    /**
     * @return the payment_type
     */
    public int getPayment_type() {
        return payment_type;
    }

    /**
     * @param payment_type the payment_type to set
     */
    public void setPayment_type(int payment_type) {
        this.payment_type = payment_type;
    }

    /**
     * @return the paid_amount
     */
    public BigDecimal getPaid_amount() {
        return paid_amount;
    }

    /**
     * @param paid_amount the paid_amount to set
     */
    public void setPaid_amount(BigDecimal paid_amount) {
        this.paid_amount = paid_amount;
    }

    /**
     * @return the exempt_amount
     */
    public BigDecimal getExempt_amount() {
        return exempt_amount;
    }

    /**
     * @param exempt_amount the exempt_amount to set
     */
    public void setExempt_amount(BigDecimal exempt_amount) {
        this.exempt_amount = exempt_amount;
    }

    /**
     * @return the taxed_amount
     */
    public BigDecimal getTaxed_amount() {
        return taxed_amount;
    }

    /**
     * @param taxed_amount the taxed_amount to set
     */
    public void setTaxed_amount(BigDecimal taxed_amount) {
        this.taxed_amount = taxed_amount;
    }
}
