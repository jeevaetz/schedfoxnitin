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
public class get_expiring_client_contract_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        //I know it's weird but sometimes the contract has no idea, so putting them on either side
        sql.append("branch.branch_id, client.client_name, client_contract.*, client.client_start_date ");
        sql.append("FROM client_contract ");
        sql.append("INNER JOIN client ON client.client_id = client_contract.client_id ");
        sql.append("INNER JOIN control_db.branch ON branch.branch_id = client.branch_id ");
        sql.append("WHERE end_date BETWEEN DATE(NOW()) AND DATE(NOW() + interval '3 weeks') OR ");
        sql.append("(last_renewed + renewal_period) BETWEEN DATE(NOW()) AND DATE(NOW() + interval '3 weeks') AND client_is_deleted != 1 ");
        sql.append("ORDER BY last_renewed ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
