/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.messaging.email;

import rmischeduleserver.util.SendMail;
import rmischeduleserver.mysqlconnectivity.queries.messaging.email.ScheduleEmailData;
import rmischeduleserver.mysqlconnectivity.queries.messaging.email.GetNextValMessaging;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import rmischedule.data_connection.Connection;
import rmischedule.messaging.datacomponents.Employee_Messaging_List_Data;

import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author vnguyen
 */
public class ScheduleEmailRequest {

    public ScheduleEmailRequest(Vector<Employee_Messaging_List_Data> emld) {
        //loop to handle each text and insertion
        SendMail[] email = new SendMail[emld.size()];

        for (int i = 0; i < emld.size(); i++) {
            //Create Connection and Query object for batch querying
            Connection myConn = new Connection();
            //add an insert for each sms txt
            try {
                //Grab the next place for a message id to be inserted
                Record_Set rs = myConn.executeQuery(GetNextValMessaging.getInstance());
                String outbound_id = rs.getString("ID");
                //send the email belonging to each object in vector
                String[] address = {emld.get(i).getEmailPrimary()};
                //String company_id,String txt,int msgType,String phoneNumber,String employee_id,int userSentId,int messaging_mod_id
                Employee_Messaging_List_Data t = emld.get(i);
                ScheduleEmailData query = new ScheduleEmailData(t.getCompanyId(), t.getMessage(), t.getMessageType(), t.getEmployeeCell(), t.getEmployeeId(),
                        t.getUserSentId(), t.getMessagingModId());
                String msgAndlink = emld.get(i).getMessage() + getConfirmationEmail(query.getUniqueId(), outbound_id);
                email[i] = new SchedfoxEmail("Schedule Request", msgAndlink, address, false);
                //add an insert for each email
                myConn.executeQuery(query);
            } catch (AddressException ex) {
                Logger.getLogger(ScheduleEmailRequest.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MessagingException ex) {
                Logger.getLogger(ScheduleEmailRequest.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


    }

    private String getConfirmationEmail(String uniqueId, String outbound_id) {
        return "";
    }
}
