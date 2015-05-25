/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.event;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_event_by_id_query extends GeneralQueryFormat {

    public get_event_by_id_query() {

    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("*, ");
        sql.append("(SELECT COUNT(*) FROM event_followup WHERE event_followup.event_id = event.event_id AND followup_processed_by IS NOT NULL) as count ");
        sql.append("FROM ");
        sql.append("event ");
        sql.append("WHERE ");
        sql.append("event_id = ?");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
