/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientRoute;

/**
 *
 * @author user
 */
public class save_client_route_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ClientRoute route, boolean isInsert) {
        this.isInsert = isInsert;
        if (isInsert) {
            super.setPreparedStatement(new Object[]{route.getRouteName(), route.getClientRouteId()});
        } else {
            super.setPreparedStatement(new Object[]{route.getRouteName(), route.getClientRouteId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO client_route ");
            sql.append("(route_name, client_route_id)");
            sql.append("VALUES ");
            sql.append("(?, ?)");
        } else {
            sql.append("UPDATE client_route ");
            sql.append("SET ");
            sql.append("route_name = ? ");
            sql.append("WHERE ");
            sql.append("client_route_id = ?;");
        }
        return sql.toString();
    }

}
