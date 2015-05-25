/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.util;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

/**
 *
 * @author ira
 */
public class AutoEmail extends SendMail {

    public AutoEmail() {}

    public AutoEmail(String subject, String msg, String[] recipents, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, html);
    }

    public AutoEmail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, dataForFile, html);
    }

    public AutoEmail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html, String fileName) throws AddressException, MessagingException {
        super(subject, msg, recipents, dataForFile, html, fileName);
    }

    public AutoEmail(String subject, String msg, String[] recipents,String from) throws AddressException, MessagingException {
        super(subject, msg, recipents, from);
    }

    public AutoEmail(String subject, String msg, String[] recipents) throws AddressException, MessagingException {
        super(subject, msg, recipents);
    }

    public AutoEmail(String subject, String msg, String[] recipents, String[] filename, boolean html) throws AddressException, MessagingException {
        super(subject, msg, recipents, filename, html);
    }
    
    @Override
    public String getEmailAddressFrom() {
        return "auto@champ.net";
    }

    @Override
    protected void addToDB(String subject, String msg, String[] recipents) {
        
    }
    
}
