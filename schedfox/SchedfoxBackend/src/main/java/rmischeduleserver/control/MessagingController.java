/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.util.ByteArrayDataSource;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import rmischeduleserver.IPLocationFile;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.client.client_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_last_messaging_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_messaging_data_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.get_next_batch_sequence_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.save_messaging_batch_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.save_messaging_communication_query;
import rmischeduleserver.mysqlconnectivity.queries.messaging.update_messaging_sent_query;
import rmischeduleserver.mysqlconnectivity.queries.reports.assemble_schedule_for_employee_report_query;
import rmischeduleserver.util.MailAuthenticator;
import rmischeduleserver.util.xprint.xPrintData;
import schedfoxlib.controller.MessagingControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.MessagingCommunication;
import schedfoxlib.model.MessagingCommunicationBatch;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MessagingController implements MessagingControllerInterface {

    private String companyId;
    private Company comp;
    private HashMap<String, Branch> branchCache;

    public MessagingController() {}
    
    public MessagingController(String companyId) {
        this.companyId = companyId;
        branchCache = new HashMap<String, Branch>();
    }

    public static MessagingController getInstance(String companyId) {
        return new MessagingController(companyId);
    }

    public Integer saveCommunicationBatch(MessagingCommunicationBatch batch) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        try {
            boolean isUpdate = true;
            if (batch.getMessagingCommunicationBatchId() == null) {
                get_next_batch_sequence_query seqQuery = new get_next_batch_sequence_query();
                seqQuery.setPreparedStatement(new Object[]{});
                seqQuery.setCompany(companyId);
                Record_Set rst = conn.executeQuery(seqQuery, "");
                batch.setMessagingCommunicationBatchId(rst.getInt("myid"));
                isUpdate = false;
            }
            save_messaging_batch_query saveQuery = new save_messaging_batch_query();
            saveQuery.update(batch, isUpdate);
            saveQuery.setCompany(companyId);
            conn.executeUpdate(saveQuery, companyId);
            
            return batch.getMessagingCommunicationBatchId();
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public void updateMessageSent(MessagingCommunication messageComm) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        update_messaging_sent_query messagingSentQuery = new update_messaging_sent_query();
        try {
            messagingSentQuery.setPreparedStatement(new Object[]{messageComm.getMessagingCommunicationId()});
            messagingSentQuery.setCompany(companyId);
            conn.executeUpdate(messagingSentQuery, "");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public void saveMessagingCommunication(MessagingCommunication messageComm) throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        save_messaging_communication_query messagingQuery = new save_messaging_communication_query();
        try {
            messagingQuery.update(messageComm);
            messagingQuery.setCompany(companyId);
            conn.executeQuery(messagingQuery, "");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public ArrayList<MessagingCommunication> getUnsentCommunication() throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        ArrayList<MessagingCommunication> smsCommunications = new ArrayList<MessagingCommunication>();
        try {
            get_messaging_data_query messagingQuery = new get_messaging_data_query();
            messagingQuery.setPreparedStatement(new Object[]{});
            messagingQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(messagingQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                smsCommunications.add(new MessagingCommunication(rst));
                rst.moveNext();
            }
            return smsCommunications;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    public void sendEmail(MessagingCommunication comm, RMIScheduleServerImpl myConn, String branchId) throws Exception, AddressException, MessagingException {
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startWeek = null;
        String endWeek = null;
        try {
            startWeek = myFormat.format(comm.getSchedStart());
            endWeek = myFormat.format(comm.getSchedEnd());
        } catch (Exception exe) {
        }

        String message = comm.getBody();
        if (message.contains("\\[shifts\\]")) {
            message = message.replaceAll("\\[shifts\\]", this.getShifts(comm.getEmployeeId(), startWeek, endWeek, branchId, myConn));
        } else {
            message = message.replaceAll("\\[shifts\\]", "");
        }
        String[] correctedEmails = {comm.getSentTo()};
        byte[] dataToFile = null;
        if (comm.getAttachPdf()) {
            ReportingController reportingController = new ReportingController();
            dataToFile = reportingController.printEmployeeScheduleToFile(comm.getEmployeeId().toString(), startWeek, endWeek, branchId, myConn, companyId);
        }
        try {
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.setHtmlMsg(message);
            if (comm.getFromEmail() != null && comm.getFromEmail().length() > 0) {
                htmlEmail.setFrom(comm.getFromEmail());
            } else {
                htmlEmail.setFrom("notifications@schedfox.com");
            }
            htmlEmail.setSubject(comm.getSubject());
            htmlEmail.addTo(correctedEmails);
            htmlEmail.setBounceAddress("bounced@schedfox.com");
            htmlEmail.setAuthenticator(new MailAuthenticator(IPLocationFile.getEMAIL_USER(), IPLocationFile.getEMAIL_PASSWORD()));
            htmlEmail.setHostName(IPLocationFile.getEMAIL_HOST());
            htmlEmail.setSmtpPort(Integer.parseInt(IPLocationFile.getEMAIL_PORT()));
            if (dataToFile != null) {
                htmlEmail.attach(new ByteArrayDataSource(dataToFile, "application/pdf"), "Schedule.pdf", "Schedule", EmailAttachment.ATTACHMENT);
            }
            htmlEmail.send();
        } catch (Exception exe) {
            throw new MessagingException("Error sending email", exe);
        }
    }

    

    private String getShifts(Integer empId, String startWeek, String endWeek, String branchId, RMIScheduleServerImpl myConn) {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        assemble_schedule_for_employee_report_query myQuery = new assemble_schedule_for_employee_report_query();

        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", 0, true);

        myQuery.update("", empId + "", startWeek, endWeek, "", "", false);
        xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConn, companyId, branchId);
        return tableData.getDataString();
    }

    @Override
    public MessagingCommunication getMessageCommunication(String email) throws RetrieveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_last_messaging_query messagingQuery = new get_last_messaging_query();
        MessagingCommunication retVal = new MessagingCommunication();
        messagingQuery.setPreparedStatement(new Object[]{email});
        messagingQuery.setCompany(this.companyId);
        try {
            Record_Set rst = conn.executeQuery(messagingQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new MessagingCommunication(rst);
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
