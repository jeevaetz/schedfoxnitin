/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messaging.sms;

import schedfoxlib.sms.SmsSender;
import schedfoxlib.sms.SmsException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import rmischedule.data_connection.Connection;
import rmischedule.schedule.components.SEmployee;
import rmischedule.security.User;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmailController;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.control.MessagingController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Company;
import schedfoxlib.model.MessagingCommunication;
import schedfoxlib.model.MessagingCommunicationBatch;

/**
 *
 * @author vnguyen
 */
public class ScheduleSmSRequest {

    public ScheduleSmSRequest(HashMap<String, SEmployee> myListComponents, User user, String company, 
            String globalMessage, Calendar startDate, Calendar endDate, Connection myConn,
            MessagingCommunicationBatch batch) throws SmsException {
        try {
            CompanyController companyController = new CompanyController();
            MessagingController messagingController = new MessagingController(myConn.myCompany);
            Company compObj = companyController.getCompanyById(Integer.parseInt(company));

            Iterator<String> keys = myListComponents.keySet().iterator();
            int i = 0;
            while (keys.hasNext()) {
                try {
                    String phoneNumber = keys.next();
                    String message = globalMessage;
                    SEmployee tempData = myListComponents.get(phoneNumber);

                    if (startDate != null && endDate != null) {
                        EmailController emailController = new EmailController(compObj, new Branch());
                        message = emailController.getViewOnlyHtml(message, startDate.getTime(), endDate.getTime(), tempData.getEmployee(), true);
                        message = emailController.getConfirmHtml(message, startDate.getTime(), endDate.getTime(), tempData.getEmployee(), true);
                    }

                    MessagingCommunication comm = new MessagingCommunication();
                    comm.setIsEmail(false);
                    comm.setIsSMS(true);
                    comm.setAttachPdf(false);
                    comm.setSentTo(phoneNumber);
                    comm.setBody(message);
                    comm.setUserId(tempData.getId());
                    comm.setMessagingCommunicationBatchId(batch.getMessagingCommunicationBatchId());
                    try {
                        comm.setSchedEnd(endDate.getTime());
                        comm.setSchedStart(startDate.getTime());
                    } catch (Exception exe) {}
                    comm.setEmployeeId(tempData.getEmployee().getEmployeeId());
                    comm.setSubject(tempData.getSubject());
                    messagingController.saveMessagingCommunication(comm);

                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

}
