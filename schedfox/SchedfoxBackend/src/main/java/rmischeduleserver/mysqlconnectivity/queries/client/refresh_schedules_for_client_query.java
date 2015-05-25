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
public class refresh_schedules_for_client_query extends GeneralQueryFormat {

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
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE schedule_master SET ");
        sql.append("schedule_master_last_updated = NOW() + interval '5 seconds' ");
        sql.append("WHERE ");
        sql.append("client_id = ?;");
        sql.append("UPDATE schedule SET ");
        sql.append("schedule_last_updated = NOW() + interval '5 seconds' ");
        sql.append("WHERE ");
        sql.append("client_id = ?;");
        return sql.toString();
    }

}
