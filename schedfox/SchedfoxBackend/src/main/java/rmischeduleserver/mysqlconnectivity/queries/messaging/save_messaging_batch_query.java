/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.messaging;

import java.sql.Timestamp;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MessagingCommunicationBatch;

/**
 *
 * @author ira
 */
public class save_messaging_batch_query extends GeneralQueryFormat {
    
    private boolean isUpdate;
    
    public void update(MessagingCommunicationBatch batch, boolean isUpdate) {
        this.isUpdate = isUpdate;
        Timestamp batchSent = null;
        if (batch.getTimeSent() != null) {
            batchSent = new java.sql.Timestamp(batch.getTimeSent().getTime());
        }
        super.setPreparedStatement(new Object[]{
            batch.getUserId(), batch.getSubject(), 
            batch.getIssms(), batch.getIsemail(), batch.getAttachPdf(), batch.getFromEmail(),
            batchSent, batch.getMessagingSouce(), batch.getMessagingCommunicationBatchId()
        });
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE messaging_communication_batch ");
            sql.append("SET ");
            sql.append("user_id = ?, subject = ?, issms = ?, isemail = ?, attach_pdf = ?, from_email = ?, time_sent = ?, messaging_souce = ? ");
            sql.append("WHERE ");
            sql.append("messaging_communication_batch_id = ?; ");
        } else {
            sql.append("INSERT INTO messaging_communication_batch ");
            sql.append("(user_id, subject, issms, isemail, attach_pdf, from_email, time_sent, messaging_souce, messaging_communication_batch_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
