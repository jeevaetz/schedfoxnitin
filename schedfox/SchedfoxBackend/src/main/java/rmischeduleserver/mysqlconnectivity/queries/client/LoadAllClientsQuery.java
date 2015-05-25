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
public class LoadAllClientsQuery extends GeneralQueryFormat {

    public LoadAllClientsQuery() {

    }

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
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM client ");
        sql.append("LEFT JOIN usked_client ON usked_client.client_id = client.client_id ");
        sql.append("WHERE branch_id = ?;");
        return sql.toString();
    }
}
