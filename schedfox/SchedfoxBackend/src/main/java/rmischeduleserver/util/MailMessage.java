package rmischeduleserver.util;

import java.util.ArrayList;

/**
 * Class to package data for sending to the server and mailing contents from the
 * server.
 * @author Ira
 */
public class MailMessage implements java.io.Serializable {
    private String mailServer;
    private String mailSubject;
    
    private String fromAddress;
    private String messageContents;
    private ArrayList<String> mailTo;
    private ArrayList<String> mailCc;
    private ArrayList<String> mailBcc;

    public MailMessage() {
        mailServer = new String();
        mailSubject = new String();
        
        fromAddress = new String();
        messageContents = new String();
        mailTo = new ArrayList();
        mailCc = new ArrayList();
        mailBcc = new ArrayList();
    }
    
    public String getMailServer() {
        return mailServer;
    }

    public void setMailServer(String mailServer) {
        this.mailServer = mailServer;
    }

    public String getMailSubject() {
        return mailSubject;
    }

    public void setMailSubject(String mailSubject) {
        this.mailSubject = mailSubject;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getMessageContents() {
        return messageContents;
    }

    public void setMessageContents(String messageContents) {
        this.messageContents = messageContents;
    }

    public ArrayList<String> getMailTo() {
        return mailTo;
    }

    public void setMailTo(ArrayList<String> mailTo) {
        this.mailTo = mailTo;
    }

    public ArrayList<String> getMailCc() {
        return mailCc;
    }

    public void setMailCc(ArrayList<String> mailCc) {
        this.mailCc = mailCc;
    }

    public ArrayList<String> getMailBcc() {
        return mailBcc;
    }

    public void setMailBcc(ArrayList<String> mailBcc) {
        this.mailBcc = mailBcc;
    }
    
    
}
