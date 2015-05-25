/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import schedfoxlib.model.ClientNotes;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_client_note_query extends GeneralQueryFormat {

    private boolean isUpdate = false;
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientNotes clientNotes) {
        if (clientNotes.getClientNotesId() == null) {
            super.setPreparedStatement(new Object[]{
                clientNotes.getUserId(), clientNotes.getClientId(),
                clientNotes.getNoteTypeId(), new java.sql.Timestamp(clientNotes.getClientNotesDateTime().getTime()),
                clientNotes.getClientNotesNotes(), clientNotes.getReadOnCheckin()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                clientNotes.getUserId(), clientNotes.getClientId(),
                clientNotes.getNoteTypeId(), new java.sql.Timestamp(clientNotes.getClientNotesDateTime().getTime()),
                clientNotes.getClientNotesNotes(), clientNotes.getReadOnCheckin(), clientNotes.getClientNotesId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("client_notes ");
            sql.append("(user_id, client_id, note_type_id, client_notes_date_time, client_notes_notes, read_on_checkin) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("client_notes ");
            sql.append("SET ");
            sql.append("user_id = ?, client_id = ?, note_type_id = ?, client_notes_date_time = ?, client_notes_notes = ?, read_on_checkin = ? ");
            sql.append("WHERE ");
            sql.append("client_notes_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
