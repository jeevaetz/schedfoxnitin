/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class MessagingCommunicationBatch {
    private Integer messagingCommunicationBatchId;
    private Integer userId;
    private String subject;
    private Boolean issms;
    private Boolean isemail;
    private Boolean attachPdf;
    private String fromEmail;
    private Date timeSent;
    private Integer messagingSouce;

    public static Integer AUTO_CURRENT_SCHEDULE_EMAIL = 4;
    public static Integer AUTO_CONFIRM_SCHEDULE_EMAIL = 5;
    public static Integer AUTO_CURRENT_SCHEDULE_SMS = 6;
    public static Integer AUTO_CONFIRM_SCHEDULE_SMS = 7;
    public static Integer templateEmail = 9;
    public static Integer templateSMS = 10;
    
    public MessagingCommunicationBatch() {
        
    }
    
    public MessagingCommunicationBatch(Record_Set rst) {
        try {
            messagingCommunicationBatchId = rst.getInt("messaging_communication_batch_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            subject = rst.getString("subject");
        } catch (Exception exe) {}
        try {
            issms = rst.getBoolean("issms");
        } catch (Exception exe) {}
        try {
            isemail = rst.getBoolean("isemail");
        } catch (Exception exe) {}
        try {
            attachPdf = rst.getBoolean("attach_pdf");
        } catch (Exception exe) {}
        try {
            fromEmail = rst.getString("from_email");
        } catch (Exception exe) {}
        try {
            timeSent = rst.getTimestamp("time_sent");
        } catch (Exception exe) {}
        try {
            messagingSouce = rst.getInt("messaging_souce");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the messagingCommunicationBatchId
     */
    public Integer getMessagingCommunicationBatchId() {
        return messagingCommunicationBatchId;
    }

    /**
     * @param messagingCommunicationBatchId the messagingCommunicationBatchId to set
     */
    public void setMessagingCommunicationBatchId(Integer messagingCommunicationBatchId) {
        this.messagingCommunicationBatchId = messagingCommunicationBatchId;
    }

    /**
     * @return the userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return the subject
     */
    public String getSubject() {
        return subject;
    }

    /**
     * @param subject the subject to set
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * @return the issms
     */
    public Boolean getIssms() {
        return issms;
    }

    /**
     * @param issms the issms to set
     */
    public void setIssms(Boolean issms) {
        this.issms = issms;
    }

    /**
     * @return the isemail
     */
    public Boolean getIsemail() {
        return isemail;
    }

    /**
     * @param isemail the isemail to set
     */
    public void setIsemail(Boolean isemail) {
        this.isemail = isemail;
    }

    /**
     * @return the attachPdf
     */
    public Boolean getAttachPdf() {
        return attachPdf;
    }

    /**
     * @param attachPdf the attachPdf to set
     */
    public void setAttachPdf(Boolean attachPdf) {
        this.attachPdf = attachPdf;
    }

    /**
     * @return the fromEmail
     */
    public String getFromEmail() {
        return fromEmail;
    }

    /**
     * @param fromEmail the fromEmail to set
     */
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    /**
     * @return the timeSent
     */
    public Date getTimeSent() {
        return timeSent;
    }

    /**
     * @param timeSent the timeSent to set
     */
    public void setTimeSent(Date timeSent) {
        this.timeSent = timeSent;
    }

    /**
     * @return the messagingSouce
     */
    public Integer getMessagingSouce() {
        return messagingSouce;
    }

    /**
     * @param messagingSouce the messagingSouce to set
     */
    public void setMessagingSouce(Integer messagingSouce) {
        this.messagingSouce = messagingSouce;
    }
    
    
}
