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
public class delete_client_note_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(ClientNotes clientNotes) {
        super.setPreparedStatement(new Object[]{
            clientNotes.getClientNotesId()
        });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append("client_notes ");
        sql.append("WHERE ");
        sql.append("client_notes_id = ?; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
