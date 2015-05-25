/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class assign_clients_to_client_route_query extends GeneralQueryFormat {

    private Integer routeId;
    private ArrayList<Integer> clientIds;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer routeId, ArrayList<Integer> clientIds) {
        this.routeId = routeId;
        this.clientIds = clientIds;
        
        ArrayList<Integer> myIds = new ArrayList<Integer>();
        try {
            myIds.add(routeId);
            for (int c = 0; c < clientIds.size(); c++) {
                myIds.add(clientIds.get(c));
                myIds.add(routeId);
            }
        } catch (Exception exe) {}
        super.setPreparedStatement(myIds.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM client_route_to_client ");
        sql.append("WHERE ");
        sql.append("route_id = ?; ");
        for (int c = 0; c < clientIds.size(); c++) {
            sql.append("INSERT INTO ");
            sql.append("client_route_to_client ");
            sql.append("(client_id, route_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?); ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
