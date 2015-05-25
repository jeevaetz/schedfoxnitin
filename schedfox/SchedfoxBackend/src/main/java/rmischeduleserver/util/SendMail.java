package rmischeduleserver.util;

import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;

public abstract class SendMail extends Mail {

    //create Authenticator object
    private Authenticator auth;
    //creating the session
    private Properties props = new Properties();
    // fill props with any information
    private Session session;
    private Address[] toAddress;
    private MimeMessage message;
    //sends mail without attachment

    public SendMail() {
    }

    public SendMail(String subject, String msg, String[] recipents, String[] filename, boolean html) throws AddressException, MessagingException {

        this.setup(recipents, subject);

        Multipart multipart = new MimeMultipart();
        BodyPart txt = new MimeBodyPart();
        if (html) {
            txt.setContent(msg, "text/html; charset=utf-8");
        } else {
            txt.setContent(msg, "text/plain; charset=utf-8");
        }
        multipart.addBodyPart(txt);
        for (int i = 0; i < filename.length; i++) {
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText("");
            multipart.addBodyPart(messageBodyPart);
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(filename[i]);
            messageBodyPart.setDataHandler(new DataHandler(source));
            String[] name = filename[i].split("\\\\");
            messageBodyPart.setFileName(name[name.length - 1]);
            multipart.addBodyPart(messageBodyPart);
        }
        message.setContent(multipart);
        //Sends the message as a email
        this.send(subject, msg, recipents);
    }

    public SendMail(String subject, String msg, String[] recipents, boolean html) throws AddressException, MessagingException {
        this.setup(recipents, subject);
        try {
            if (html) {
                message.setContent(msg, "text/html; charset=utf-8");
            } else {
                message.setText(msg);
            }
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Sends the message as a email
        message.saveChanges();//implicit with send()
        this.send(subject, msg, recipents);
    }

    public SendMail(String subject, String msg, String[] recipents) throws AddressException, MessagingException {

        this.setupNoReply(recipents, subject);
        //Creating the message
        try {
            message.setContent(msg, "text/html; charset=utf-8");
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Sends the message as a email
        message.saveChanges();
        this.send(subject, msg, recipents);
    }

    public SendMail(String subject, String msg, String[] recipents, String from) throws AddressException, MessagingException {
        this.setupNoReply(recipents, subject);
        props.put("mail.smtp.from", from);
        try {
            message.setContent(msg, "text/html; charset=utf-8");
        } catch (MessagingException ex) {
            Logger.getLogger(SendMail.class.getName()).log(Level.SEVERE, null, ex);
        }
        message.saveChanges();
        try {
            Address[] fromAdd = new Address[1];
            fromAdd[0] = new InternetAddress(from);
            message.setReplyTo(fromAdd);
            message.setSender(new InternetAddress(from));
        } catch (Exception e) {
        }
        this.send(subject, msg, recipents);
    }

    public SendMail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html) throws AddressException, MessagingException {
        this(subject, msg, recipents, dataForFile, html, "Schedule");
    }

    public SendMail(String subject, String msg, String[] recipents, byte[] dataForFile, boolean html, String fileName) throws AddressException, MessagingException {
        this.setup(recipents, subject);

        Multipart multipart = new MimeMultipart();
        BodyPart txt = new MimeBodyPart();
        if (html) {
            txt.setContent(msg, "text/html; charset=utf-8");
        } else {
            txt.setContent(msg, "text/plain; charset=utf-8");
        }
        multipart.addBodyPart(txt);
        BodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart = new MimeBodyPart();

        DataSource source = new ByteArrayDataSource(dataForFile, "application/pdf");
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName + ".pdf");
        multipart.addBodyPart(messageBodyPart);
        message.setContent(multipart);
        //Sends the message as a email
        this.send(subject, msg, recipents);
    }

    private void addProps() {
        props.setProperty("mail.transport.protocol", "smtp");
        //props.put("mail.smtp.ehlo","true");
        props.put("mail.smtp.auth", "true");

        props.put("mail.smtp.user", getUser());
        props.put("mail.smtp.password", getPassword());

        props.put("mail.smtp.host", Mail.getHost());

        //props.put("mail.host", Mail.getHost());

        props.put("mail.smtp.auth", "PLAIN");

        //  props.put("mail.debug", "true");
        props.put("mail.smtp.port", Mail.getPort());

    }

    private void send(String subject, String msg, String[] recipents) throws MessagingException {
        try {
            message.saveChanges();//implicit with send()
            Transport transport = session.getTransport("smtp");
            transport.connect(getUser(), getPassword());

            transport.sendMessage(message, toAddress);

            transport.close();
            this.addToDB(subject, msg, recipents);
        } catch (Exception e) {
            e.printStackTrace();
            throw new MessagingException();
        }
    }

    public abstract String getEmailAddressFrom();

    protected abstract void addToDB(String subject, String msg, String[] recipents);

    private void setupNoReply(String[] recipents, String subject) throws AddressException, MessagingException {
        this.addProps();
        //addSSL adds proper properties if a ssl connection
        //   addSSL(props);
        // fill props with any information
        if (session == null) {
            session = Session.getDefaultInstance(props, auth);
        }
        toAddress = new InternetAddress[recipents.length];

        for (int i = 0; i < recipents.length; i++) {
            try {
                toAddress[i] = new InternetAddress(recipents[i]);
            } catch (Exception e) {
            }
        }


        Address localFromAddress = new InternetAddress("sched.scheduler@gmail.com");
        //Creating the message
        message = new MimeMessage(session);
        //setMessage from and recipents
        message.setReplyTo(new Address[]{localFromAddress});

        message.setSender(localFromAddress);
        props.put("mail.smtp.from", "sched.scheduler@gmail.com");

        message.addRecipients(Message.RecipientType.TO, toAddress);
        message.setSentDate(new Date());//set the date to the heater
        message.setSubject(subject);
    }

    private void setup(String[] recipents, String subject) throws AddressException, MessagingException {
        this.addProps();

        this.auth = new MailAuthenticator(getUser(), getPassword());

        //addSSL adds proper properties if a ssl connection
        //addSSL(props);
        // fill props with any information
        session = Session.getDefaultInstance(props, auth);
        session.setDebug(false);

        toAddress = new InternetAddress[recipents.length];
        for (int i = 0; i < recipents.length; i++) {
            toAddress[i] = new InternetAddress(recipents[i]);
        }


        Address localFromAddress = new InternetAddress(getEmailAddressFrom());

        //Creating the message
        message = new MimeMessage(session);
        //setMessage from and recipents
        message.setReplyTo(new Address[]{localFromAddress});
        message.setSender(localFromAddress);
        message.setFrom(localFromAddress);
        props.put("mail.smtp.from", getEmailAddressFrom());
        message.addRecipients(Message.RecipientType.TO, toAddress);
        message.setSentDate(new Date());//set the date to the heater
        message.setSubject(subject);
    }
}
