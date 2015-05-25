/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.event;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_events_for_user_group_followup_query extends GeneralQueryFormat {
    
    private int size;
    
    public get_events_for_user_group_followup_query() {
        
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer userId, Date startDate, Date endDate, ArrayList<Integer> ints) {
        this.size = ints.size();
        
        ArrayList<Object> objs = new ArrayList<Object>();
        objs.add(userId);
        objs.add(userId);
        objs.add(startDate);
        objs.add(endDate);
        objs.addAll(ints);
        super.setPreparedStatement(objs.toArray());
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
        sql.append("1 = 1 ");
        sql.append("AND (");
        sql.append("    EXISTS (SELECT event_followup_id FROM event_followup WHERE followup_request_user = ? AND followup_processed_by IS NULL AND event_followup.event_id = event.event_id) ");
        sql.append("    OR ");
        sql.append("    EXISTS (SELECT event_followup_id FROM event_followup WHERE followup_request_group IN ");
        sql.append("    ( ");
        sql.append("        SELECT groups_id FROM ").append(super.getManagementSchema()).append(".user_groups WHERE user_id = ? ");
        sql.append("    ) ");
        sql.append("    AND followup_processed_by IS NULL AND event_followup.event_id = event.event_id) ");
        sql.append(") ");
        sql.append("AND DATE(entered_on) BETWEEN ? AND ? ");
        sql.append("AND event_type_id IN (");
        for (int s = 0; s < this.size; s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("ORDER BY entered_on DESC ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
