/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.event;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.Event;

/**
 *
 * @author ira
 */
public class save_event_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Event event) {
        if (event.getEventId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                event.getEventTypeId(), event.getShiftId(), event.getEventNotes(), event.getEnteredBy(), 
                event.getProblemSolverId(), event.getEmployeeId(), event.getClientId(), event.getOrigShiftStartTime(),
                event.getOrigShiftEndTime()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                event.getEventTypeId(), event.getShiftId(), event.getEventNotes(), event.getEnteredBy(), 
                event.getProblemSolverId(), event.getEmployeeId(), event.getClientId(), event.getOrigShiftStartTime(),
                event.getOrigShiftEndTime(), event.getEventId(), event.getEventId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("event ");
            sql.append("(event_type_id, shift_id, event_notes, entered_by, problem_solver_id, employee_id, client_id, orig_shift_start_time, orig_shift_end_time) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?); ");
            sql.append("SELECT currval('event_seq') as myid;");
        } else {
            sql.append("UPDATE ");
            sql.append("event ");
            sql.append("SET ");
            sql.append("event_type_id = ?, shift_id = ?, event_notes = ?, entered_by = ?, ");
            sql.append("problem_solver_id = ?, employee_id = ?, client_id = ?, ");
            sql.append("orig_shift_start_time = ?, orig_shift_end_time = ? ");
            sql.append("WHERE ");
            sql.append("event_id = ?; ");
            sql.append("SELECT ? as myid; ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
