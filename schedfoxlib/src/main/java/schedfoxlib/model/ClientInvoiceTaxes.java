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
public class ClientInvoiceTaxes {
    private int client_invoice_tax_id;
    private int invoice_id;
    private String tax_type;
    private String code;
    private BigDecimal total_taxable_amount;
    private BigDecimal exempt_amount;
    private BigDecimal taxed_amount;

    /**
     * @return the client_invoice_tax_id
     */
    public int getClient_invoice_tax_id() {
        return client_invoice_tax_id;
    }

    /**
     * @param client_invoice_tax_id the client_invoice_tax_id to set
     */
    public void setClient_invoice_tax_id(int client_invoice_tax_id) {
        this.client_invoice_tax_id = client_invoice_tax_id;
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
     * @return the tax_type
     */
    public String getTax_type() {
        return tax_type;
    }

    /**
     * @param tax_type the tax_type to set
     */
    public void setTax_type(String tax_type) {
        this.tax_type = tax_type;
    }

    /**
     * @return the code
     */
    public String getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * @return the total_taxable_amount
     */
    public BigDecimal getTotal_taxable_amount() {
        return total_taxable_amount;
    }

    /**
     * @param total_taxable_amount the total_taxable_amount to set
     */
    public void setTotal_taxable_amount(BigDecimal total_taxable_amount) {
        this.total_taxable_amount = total_taxable_amount;
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
