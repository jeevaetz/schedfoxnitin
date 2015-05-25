/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.ClientBilling;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_billing_query extends GeneralQueryFormat {

    private ClientBilling clientBilling;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientBilling clientBilling) {
        this.clientBilling = clientBilling;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        String billable = clientBilling.getClientIsBillable() == true ? "true" : "false";
        String overtime = clientBilling.isBillOvertime() == true ? "true" : "false";
        sql.append("DELETE FROM client_billing WHERE client_id = " + clientBilling.getClientId() + ";");
        if (clientBilling.getClientBillingId() == null) {
            sql.append("INSERT INTO client_billing ");
            sql.append("(client_is_billable, client_bill_frequency_id, ");
            sql.append(" client_id, sales_tax_id, bill_overtime) ");
            sql.append("VALUES ");
            sql.append("(" + billable + ", " + clientBilling.getClientBillFrequencyId() + ", ");
            sql.append(" " + clientBilling.getClientId() + "," + clientBilling.getSalesTaxId());
            sql.append("," + overtime + ");");
        } else {
            sql.append("UPDATE client_billing ");
            sql.append("SET ");
            sql.append("client_is_billable = " + billable + ",");
            sql.append("client_bill_frequency_id = " + clientBilling.getClientBillFrequencyId() + ", ");
            sql.append("client_id = " + clientBilling.getClientId() + ", ");
            sql.append("sales_tax_id = " + clientBilling.getSalesTaxId() + ", ");
            sql.append("bill_overtime = " + overtime + " ");
            sql.append("WHERE ");
            sql.append("client_billing_id = " + clientBilling.getClientBillingId());
        }
        return sql.toString();
    }

}
