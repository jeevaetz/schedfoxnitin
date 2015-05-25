/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.ClientRateCode;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_rate_code_query extends GeneralQueryFormat {

    private ClientRateCode clientRateCode;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientRateCode clientRateCode) {
        this.clientRateCode = clientRateCode;
        if (clientRateCode.getClientRateCodeId() == null) {
            super.setPreparedStatement(new Object[]{clientRateCode.getRateCodeId(),
                clientRateCode.getPayAmount(), clientRateCode.getOvertimeAmount(),
                clientRateCode.getBillAmount(), clientRateCode.getOvertimeBill(),
                clientRateCode.getHourType(), clientRateCode.getDescription(),
                clientRateCode.getClientId()});
        } else {
            super.setPreparedStatement(new Object[]{clientRateCode.getRateCodeId(),
                clientRateCode.getPayAmount(), clientRateCode.getOvertimeAmount(),
                clientRateCode.getBillAmount(), clientRateCode.getOvertimeBill(),
                clientRateCode.getHourType(), clientRateCode.getDescription(),
                clientRateCode.getClientId(), clientRateCode.getClientRateCodeId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (clientRateCode.getClientRateCodeId() == null) {
            sql.append("INSERT INTO client_rate_code ");
            sql.append("(rate_code_id, pay_amount, overtime_amount, bill_amount, ");
            sql.append(" overtime_bill, hour_type, description, client_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE client_rate_code ");
            sql.append("SET ");
            sql.append("rate_code_id = ?, pay_amount = ?, overtime_amount = ?, ");
            sql.append("bill_amount = ?, overtime_bill = ?, hour_type = ?, ");
            sql.append("description = ?, client_id = ? ");
            sql.append("WHERE ");
            sql.append("client_rate_code_id = ? ");
        }

        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
