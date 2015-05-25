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
public class get_events_with_no_followup_query extends GeneralQueryFormat {
    
    private Boolean showEventsWithoutFollowUp;
    private int size;
    private int branchSize;
    
    public get_events_with_no_followup_query() {
        
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<Integer> branches, Boolean showEventsWithoutFollowUp, Date startDate, Date endDate, ArrayList<Integer> ints) {
        this.showEventsWithoutFollowUp = showEventsWithoutFollowUp;
        this.size = ints.size();
        this.branchSize = branches.size();
        
        ArrayList<Object> objs = new ArrayList<Object>();
        objs.add(startDate);
        objs.add(endDate);
        objs.addAll(ints);
        objs.addAll(branches);
        super.setPreparedStatement(objs.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("event.*, ");
        sql.append("COALESCE(client.branch_id, employee.branch_id) as branch_id, ");
        sql.append("(SELECT COUNT(*) FROM event_followup WHERE event_followup.event_id = event.event_id AND followup_processed_by IS NOT NULL) as count ");
        sql.append("FROM ");
        sql.append("event ");
        sql.append("LEFT JOIN client ON client.client_id = event.client_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = event.employee_id ");
        sql.append("WHERE ");
        sql.append("1 = 1 ");
        if (showEventsWithoutFollowUp) {
            sql.append("AND NOT EXISTS (");
            sql.append("    SELECT event_followup_id FROM event_followup WHERE event_followup.event_id = event.event_id AND followup_processed_on IS NOT NULL ");
            sql.append(") ");
        }
        sql.append("AND DATE(entered_on) BETWEEN ? AND ? ");
        sql.append("AND event_type_id IN (");
        for (int s = 0; s < this.size; s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("AND COALESCE(client.branch_id, employee.branch_id) IN (");
        for (int s = 0; s < this.branchSize; s++) {
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
