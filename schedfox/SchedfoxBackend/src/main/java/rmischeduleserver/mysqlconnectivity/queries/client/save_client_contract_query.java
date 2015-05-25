/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ClientContract;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_contract_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientContract clientContract) {
        if (clientContract.getClientContractId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                clientContract.getClientId(), clientContract.getStartDate(),
                clientContract.getEndDate(), clientContract.getProjectedIncrease(),
                clientContract.getClientContractTypeId(), clientContract.getRenewalPeriod(),
                clientContract.getLastRenewed(), clientContract.getAutoRenew(),
                clientContract.getSalesPerson(), clientContract.getSalesCommission(),
                clientContract.getSalesManager(), clientContract.getTempAccount()
            });
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{
                clientContract.getClientContractId(),
                clientContract.getClientId(), clientContract.getStartDate(),
                clientContract.getEndDate(), clientContract.getProjectedIncrease(),
                clientContract.getClientContractTypeId(), clientContract.getRenewalPeriod(),
                clientContract.getLastRenewed(), clientContract.getAutoRenew(),
                clientContract.getSalesPerson(), clientContract.getSalesCommission(),
                clientContract.getSalesManager(), clientContract.getTempAccount(),
                clientContract.getClientContractId()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("SELECT nextval('client_contract_seq');");
            sql.append("INSERT INTO client_contract ");
            sql.append("(client_contract_id, client_id, start_date, end_date, ");
            sql.append(" projected_increase, client_contract_type_id, ");
            sql.append(" renewal_period, last_renewed, auto_renew, salesperson, salescommission, salesmanager, temp_account ");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("(currval('client_contract_seq'), ?, ?, ?, ?, ?, ?::interval, ?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("SELECT ?;");
            sql.append("UPDATE client_contract ");
            sql.append("SET ");
            sql.append("client_id = ?, start_date = ?, end_date = ?, projected_increase = ?, ");
            sql.append("client_contract_type_id = ?, renewal_period = ?::interval, last_renewed = ?, ");
            sql.append("auto_renew = ?, salesperson = ?, salescommission = ?, salesmanager = ?, ");
            sql.append("temp_account = ? ");
            sql.append("WHERE ");
            sql.append("client_contract_id = ?;");
        }
        return sql.toString();
    }
}
