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
public class get_clients_by_id_query extends GeneralQueryFormat {

    private Integer size;
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Integer size) {
        this.size = size;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT * FROM ");
        sql.append("client ");
        sql.append("WHERE ");
        sql.append("client_id IN ");
        sql.append("(");
        for (int s = 0; s < size; s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }

}
