/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.Client;

/**
 *
 * @author ira
 */
public class load_all_client_contact_query extends GeneralQueryFormat {

    private ArrayList<Integer> clientIds;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<Client> clients) {
        clientIds = new ArrayList<Integer>();
        for (int c = 0; c < clients.size(); c++) {
            clientIds.add(clients.get(c).getClientId());
        }
        super.setPreparedStatement(clientIds.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client_contact ");
        sql.append("WHERE ");
        sql.append("client_id IN (");
        for (int c = 0; c < clientIds.size(); c++) {
            sql.append("?");
            if (c < clientIds.size() - 1) {
                sql.append(",");
            }
        }
        sql.append(")");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
