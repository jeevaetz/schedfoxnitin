/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.EmployeeNotes;

/**
 *
 * @author user
 */
public class save_employee_note_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeNotes empNotes) {
        if (empNotes.getEmployeeNotesId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                empNotes.getUserId(), empNotes.getEmployeeId(), empNotes.getNoteTypeId(), 
                new java.sql.Timestamp(empNotes.getEmployeeNotesDateTime().getTime()), 
                empNotes.getEmployeeNotesNotes()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                empNotes.getUserId(), empNotes.getEmployeeId(), empNotes.getNoteTypeId(), 
                new java.sql.Timestamp(empNotes.getEmployeeNotesDateTime().getTime()), 
                empNotes.getEmployeeNotesNotes(), empNotes.getPrimaryKey()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE ");
            sql.append("employee_notes ");
            sql.append("SET ");
            sql.append("user_id = ?, employee_id = ?, note_type_id = ?, employee_notes_date_time = ?, employee_notes_notes = ? ");
            sql.append("WHERE ");
            sql.append("employee_notes_id = ?; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append("employee_notes ");
            sql.append("(user_id, employee_id, note_type_id, employee_notes_date_time, employee_notes_notes) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?); ");
        }
        return sql.toString();
    }

}
