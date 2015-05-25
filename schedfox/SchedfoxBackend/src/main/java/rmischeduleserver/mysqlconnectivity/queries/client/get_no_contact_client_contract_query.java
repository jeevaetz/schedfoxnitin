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
public class get_no_contact_client_contract_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        //I know it's weird but sometimes the contract has no idea, so putting them on either side
        sql.append("branch.branch_id, client.client_name, client.client_id, client_contract.*, client.client_id, client.client_start_date ");
        sql.append("FROM client ");
        sql.append("LEFT JOIN client_contract ON client.client_id = client_contract.client_id ");
        sql.append("INNER JOIN control_db.branch ON branch.branch_id = client.branch_id ");
        sql.append("WHERE ");
        sql.append("client.contact_for_renewal = false AND client_is_deleted != 1 ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
