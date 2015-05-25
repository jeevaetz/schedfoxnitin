/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_clients_with_report_times_query extends GeneralQueryFormat {
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 AND ");
        sql.append("(NOW()::time - COALESCE(report_time, '08:00:00'::time)::time) < '00:45:00'::time AND ");
        sql.append("(NOW()::time - COALESCE(report_time, '08:00:00'::time)::time) > '00:00:00'::time ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }
}
