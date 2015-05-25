/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.messaging.email;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.swing.JOptionPane;
import org.apache.commons.validator.EmailValidator;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.messaging.email.InsEmailToDB;
import rmischeduleserver.util.SendMail;

/**
 *
 * @author user
 */
public class SchedfoxEmail extends SendMail {

    public SchedfoxEmail() {}

    public SchedfoxEmail(String subject, String msg, String[] recipents, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, html);
    }

    public SchedfoxEmail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, dataForFile, html);
    }

    public SchedfoxEmail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html, String fileName) throws AddressException, MessagingException {
        super(subject, msg, recipents, dataForFile, html, fileName);
    }

    public SchedfoxEmail(String subject, String msg, String[] recipents, String from) throws AddressException, MessagingException {
        super(subject, msg, recipents, from);
    }

    public SchedfoxEmail(String subject, String msg, String[] recipents) throws AddressException, MessagingException {
        super(subject, msg, recipents);
    }

    public SchedfoxEmail(String subject, String msg, String[] recipents, String[] filename, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, filename, html);
    }

    public String getEmailAddressFrom() {
        EmailValidator myValidator = EmailValidator.getInstance();
        String userEmailAddress = Main_Window.parentOfApplication.myUser.getEmail();
        if (userEmailAddress == null || !myValidator.isValid(userEmailAddress)) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                    "You appear to have an missing or invalid email associated with your account, you will "
                    + "still be able to send emails, however your employees will not be able to respond until you fix this.",
                    "Error", JOptionPane.ERROR_MESSAGE);
            userEmailAddress = "Scheduling@Schedfox.com";
        }
        return userEmailAddress;
    }

    protected void addToDB(String subject, String msg, String[] recipents) {
        try {
            int emp_id = Integer.parseInt(Main_Window.parentOfApplication.myUser.getUserId());
            RunQueriesEx queries = new RunQueriesEx();
            for (int i = 0; i < recipents.length; i++) {
                InsEmailToDB ins = new InsEmailToDB();
                ins.setPreparedStatement(new Object[]{recipents[i], subject, msg, emp_id});
                queries.add(ins);
            }
            Connection conn = new Connection();
            conn.executeQueryEx(queries);
        } catch (Exception exe) {}
    }
}
