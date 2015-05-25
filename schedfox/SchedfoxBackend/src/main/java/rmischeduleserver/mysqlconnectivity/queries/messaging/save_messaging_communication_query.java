/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.messaging;

import java.sql.Timestamp;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.MessagingCommunication;

/**
 *
 * @author ira
 */
public class save_messaging_communication_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(MessagingCommunication comm) {
        Timestamp myDate = null;
        if (comm.getDatetimesent() != null) {
            myDate = new Timestamp(comm.getDatetimesent().getTime());
        }
        if (comm.getMessagingCommunicationId() == null) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{
                comm.getUserId(), comm.getEmployeeId(), comm.getShiftId(),
                comm.getSentTo(), comm.getCcd(), comm.getSubject(), comm.getBody(),
                comm.getSchedStart(), comm.getSchedEnd(), comm.getIsSMS(),
                comm.getIsEmail(), comm.getAttachPdf(), comm.getFromEmail(),
                comm.getLastError(), comm.getErrorNum(), myDate,
                comm.getMessagingCommunicationBatchId()
            });
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{
                comm.getUserId(), comm.getEmployeeId(), comm.getShiftId(),
                comm.getSentTo(), comm.getCcd(), comm.getSubject(), comm.getBody(),
                comm.getSchedStart(), comm.getSchedEnd(), comm.getIsSMS(),
                comm.getIsEmail(), comm.getAttachPdf(), comm.getFromEmail(),
                comm.getLastError(), comm.getErrorNum(), myDate,
                comm.getMessagingCommunicationBatchId(),
                comm.getMessagingCommunicationId()
            });
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE ");
            sql.append("messaging_communication ");
            sql.append("SET ");
            sql.append(" user_id = ?, employee_id = ?, shift_id = ?, ");
            sql.append(" sent_to = ?, ccd = ?, subject = ?, body = ?, sched_start = ?, ");
            sql.append(" sched_end = ?, issms = ?, isemail = ?, attach_pdf = ?, ");
            sql.append(" from_email = ?, last_error = ?, error_num = ?, datetimesent = ?, ");
            sql.append(" messaging_communication_batch_id = ? ");
            sql.append("WHERE ");
            sql.append("messaging_communication_id = ?");
        } else {
            sql.append("INSERT INTO ");
            sql.append("messaging_communication ");
            sql.append("(");
            sql.append(" user_id, employee_id, shift_id, ");
            sql.append(" sent_to, ccd, subject, body, sched_start, ");
            sql.append(" sched_end, issms, isemail, attach_pdf, ");
            sql.append(" from_email, last_error, error_num, datetimesent, ");
            sql.append(" messaging_communication_batch_id ");
            sql.append(")");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?) ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
