/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_client_invoices_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ON (issued_on, amount_due) client_invoice.*, ");
        sql.append("(SELECT SUM(reg_num_hrs) FROM client_invoice_hours WHERE client_invoice_hours.client_invoice_id = client_invoice.client_invoice_id ) as tot_reg_num_hrs, "); 
        sql.append("(SELECT SUM(overtime_num_hrs) FROM client_invoice_hours WHERE client_invoice_hours.client_invoice_id = client_invoice.client_invoice_id ) as tot_overtime_num_hrs, ");
        sql.append("(SELECT SUM(reg_hrs_bill_amt) FROM client_invoice_hours WHERE client_invoice_hours.client_invoice_id = client_invoice.client_invoice_id ) as tot_reg_hrs_bill_amt,  ");
        sql.append("(SELECT SUM(overtime_hrs_bill_amt) FROM client_invoice_hours WHERE client_invoice_hours.client_invoice_id = client_invoice.client_invoice_id ) as tot_overtime_hrs_bill_amt,  ");
        sql.append("(SELECT SUM(taxed_amount) FROM client_invoice_taxes WHERE client_invoice_taxes.invoice_id = client_invoice.client_invoice_id  ) as tot_taxed_amount ");
        sql.append("FROM ");
        sql.append("client_invoice ");
        sql.append("LEFT JOIN client_invoice_hours ON client_invoice_hours.client_invoice_id = client_invoice.client_invoice_id ");
        sql.append("LEFT JOIN client_invoice_taxes ON client_invoice_taxes.invoice_id = client_invoice.client_invoice_id  ");
        sql.append("WHERE ");
        sql.append("client_id = ? ");
        sql.append("ORDER BY ");
        sql.append("issued_on DESC ");
        return sql.toString();
    }

}
