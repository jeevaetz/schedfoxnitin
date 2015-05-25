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
public class get_non_closed_event_followup_for_user_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("event_followup ");
        sql.append("WHERE ");
        sql.append("event_id = ? AND ");
        sql.append("    (");
        sql.append("        event_followup.followup_request_user = ? OR ");
        sql.append("        event_followup.followup_request_group IN ");
        sql.append("        (");
        sql.append("            SELECT groups_id FROM ").append(super.getManagementSchema()).append(".user_groups WHERE user_id = ? ");
        sql.append("        )");
        sql.append("    )");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
