/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;

import schedfoxlib.model.AvailabilityNotes;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_availability_note_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(AvailabilityNotes availNote) {
        isInsert = false;
        if (availNote.getAvailabilityNoteId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{
                availNote.getAvailabilityId(),
                availNote.getEnteredBy(), availNote.getNote()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                availNote.getAvailabilityId(), availNote.getNote(),
                availNote.getAvailabilityNoteId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isInsert) {
            sql.append("UPDATE availability_notes ");
            sql.append("SET ");
            sql.append("availability_id = ?, note = ? ");
            sql.append("WHERE ");
            sql.append("availability_note_id = ?");
        } else {
            sql.append("INSERT INTO ");
            sql.append("availability_notes ");
            sql.append("(availability_id,entered_by, note) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?);");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
