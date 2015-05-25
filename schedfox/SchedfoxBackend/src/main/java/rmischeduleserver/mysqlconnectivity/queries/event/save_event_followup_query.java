/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.event;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.EventFollowup;

/**
 *
 * @author ira
 */
public class save_event_followup_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(EventFollowup follow) {
        if (follow.getEventFollowupId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                follow.getEventId(), follow.getFollowupRequestedBy(), follow.getFollowupProcessedBy(), follow.getFollowupType(), 
                follow.getFollowupNote(), follow.getProblemSolverId(), follow.getFollowupRequestGroup(), follow.getFollowupRequestUser()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                follow.getEventId(), follow.getFollowupRequestedBy(), follow.getFollowupProcessedBy(), follow.getFollowupType(), 
                follow.getFollowupNote(), follow.getProblemSolverId(), follow.getEventFollowupId(), follow.getFollowupRequestGroup(), 
                follow.getFollowupRequestUser(), follow.getEventFollowupId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("event_followup ");
            sql.append("(event_id, followup_requested_by, followup_processed_by, followup_type, followup_note, problem_solver_id, followup_request_group, followup_request_user) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?);");
            sql.append("SELECT currval('event_followup_seq') as myid;");
        } else {
            sql.append("UPDATE ");
            sql.append("event_followup ");
            sql.append("SET ");
            sql.append("event_id = ?, followup_requested_by = ?, followup_processed_by = ?, followup_type = ?, followup_note = ?, problem_solver_id = ?, followup_request_group = ?, followup_request_user = ? ");
            sql.append("WHERE ");
            sql.append("event_followup_id = ?;");
            sql.append("SELECT ? as myid;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
