/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client.billing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_client_billing_query extends GeneralQueryFormat {

    private int clientId;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(int clientId) {
        this.clientId = clientId;
    }
    
    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client_billing ");
        sql.append("WHERE ");
        sql.append("client_id = " + this.clientId + ";");
        return sql.toString();
    }

}
