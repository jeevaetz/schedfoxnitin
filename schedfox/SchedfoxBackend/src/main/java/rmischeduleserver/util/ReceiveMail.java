package rmischeduleserver.util;


import rmischeduleserver.util.Mail;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author vnguyen
 */
public class ReceiveMail {

    private Session session = null;
    private Store store = null;
    private Folder folder;

    /**
     * connects to the mail server
     * @throws Exception
     */
    public void pop3_connect() throws Exception {

        //Create the authentication object
        MailAuthenticator auth = new MailAuthenticator(Mail.getUser(), Mail.getPassword());

        //create the pop3 method
        Properties pop3Props = new Properties();

        pop3Props.setProperty("mail.pop3.socketFactory.class", Mail.SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port", Mail.recPort);
        pop3Props.setProperty("mail.pop3.socketFactory.port", Mail.recPort);
        pop3Props.put("mail.debug", "true");



        //Create a session object
        session = Session.getDefaultInstance(pop3Props, auth);

        this.store = session.getStore(Mail.recProtocol);
        store.connect(Mail.recHost, Mail.user, Mail.password);
        this.getInbox();
    }

    /**
     * grabs the first 20 messages in your inbox
     * @throws IOException
     */
    public void getInbox() throws IOException {
        try {
            this.folder = this.store.getFolder("INBOX");
            this.folder.open(Folder.READ_ONLY);
            Message message[] = folder.getMessages();
            for(int i = 0; i < 20; i++){
                printMessage(message[i]);
            }
        } catch (MessagingException ex) {
            //Logger.getLogger(ReceiveMail.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * prints to console the message object passed
     * @param message object from teh java mail class
     */
    public static void printMessage(Message message) {
        try {
            // Get the header information
            String from = ((InternetAddress) message.getFrom()[0]).getPersonal();
            if (from == null) {
                from = ((InternetAddress) message.getFrom()[0]).getAddress();
            }
            System.out.println("FROM: " + from);

            String subject = message.getSubject();
            System.out.println("SUBJECT: " + subject);

            //String dateTime = message.getSentDate().toString();
            System.out.println("DATE: " + message.getSentDate());

            // -- Get the message part (i.e. the message itself) --
            Part messagePart = message;
            Object content = messagePart.getContent();

            // -- or its first body part if it is a multipart message --
            if (content instanceof Multipart) {
                messagePart = ((Multipart) content).getBodyPart(0);
                System.out.println("[ Multipart Message ]");
            }

            // -- Get the content type --
            String contentType = messagePart.getContentType();

            // -- If the content is plain text, we can print it --
            System.out.println("CONTENT:" + contentType);

            if (contentType.startsWith("text/plain") || contentType.startsWith("text/html")) {
                InputStream is = messagePart.getInputStream();

                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String thisLine = reader.readLine();

                while (thisLine != null) {
                    System.out.println(thisLine);
                    thisLine = reader.readLine();
                }
            }

            System.out.println("-----------------------------");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
