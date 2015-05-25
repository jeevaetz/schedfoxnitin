/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeAccounts;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_account_query extends GeneralQueryFormat {

    private EmployeeAccounts account;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeAccounts account) {
        this.account = account;
        super.setPreparedStatement(new Object[]{
            account.getEmployeeId(), account.getAllowDirectDeposit(),
            account.getRoutingNumber(), account.getAccountNumber(),
            account.getAccountType(), account.getEmployeeId()
        });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM employee_accounts WHERE employee_id = ?;");
        sql.append("INSERT INTO employee_accounts ");
        sql.append("(allow_direct_deposit, routing_number, account_number, account_type, employee_id)");
        sql.append(" VALUES ");
        sql.append("(?, ?, ?, ?, ?);");
        return sql.toString();
    }

    @Override
    public String toString() {
        String allowDeposit = (account.getAllowDirectDeposit() == true ? "true" : "false");
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM employee_accounts WHERE employee_id = " + account.getEmployeeId() + ";");
        sql.append("INSERT INTO employee_accounts ");
        sql.append("(allow_direct_deposit, routing_number, account_number, account_type, employee_id)");
        sql.append(" VALUES ");
        sql.append("(" + allowDeposit + ", '" + account.getRoutingNumber() + "', ");
        sql.append("'" + account.getAccountNumber() + "', " + account.getAccountType() + ", ");
        sql.append("" + account.getEmployeeId() + ");");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
