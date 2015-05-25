/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientTerminationReason;

/**
 *
 * @author ira
 */
public class save_client_termination_reason_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(ClientTerminationReason reason, boolean isUpdate) {
        this.isUpdate = isUpdate;
        if (isUpdate) {
            super.setPreparedStatement(new Object[]{reason.getClientId(), reason.getTerminationReasonId(), reason.getNotes(), reason.getClientTerminationReasonId()});
        } else {
            super.setPreparedStatement(new Object[]{reason.getClientId(), reason.getTerminationReasonId(), reason.getNotes()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE ");
            sql.append("client_termination_reason ");
            sql.append("SET ");
            sql.append("client_id = ?, termination_reason_id = ?, notes = ?, date_of_note = NOW() ");
            sql.append("WHERE ");
            sql.append("client_termination_reason_id = ?; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append("client_termination_reason ");
            sql.append("(client_id, termination_reason_id, notes) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?); ");
        }
        return sql.toString();
    }

}
