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
public class MessagingCommunication {
    private Integer messagingCommunicationId;
    private Integer userId;
    private Integer employeeId;
    private String shiftId;
    private Date datetimerequested;
    private Date datetimesent;
    private String sentTo;
    private String ccd;
    private String subject;
    private String body;
    private Boolean isSMS;
    private Boolean isEmail;
    private Date schedStart;
    private Date schedEnd;
    private Boolean attachPdf;
    private String fromEmail;
    private Date lastError;
    private Integer errorNum;
    private Integer messagingCommunicationBatchId;

    public MessagingCommunication() {
        
    }
    
    public MessagingCommunication(Record_Set rst) {
        try {
            messagingCommunicationId = rst.getInt("messaging_communication_id");
        } catch (Exception exe) {}
        try {
            messagingCommunicationBatchId = rst.getInt("messaging_communication_batch_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            shiftId = rst.getString("shift_id");
        } catch (Exception exe) {}
        try {
            datetimerequested = rst.getTimestamp("datetimerequested");
        } catch (Exception exe) {}
        try {
            datetimesent = rst.getTimestamp("datetimesent");
        } catch (Exception exe) {}
        try {
            sentTo = rst.getString("sent_to");
        } catch (Exception exe) {}
        try {
            ccd = rst.getString("ccd");
        } catch (Exception exe) {}
        try {
            subject = rst.getString("subject");
        } catch (Exception exe) {}
        try {
            body = rst.getString("body");
        } catch (Exception exe) {}
        try {
            subject = rst.getString("subject");
        } catch (Exception exe) {}
        try {
            isSMS = rst.getBoolean("issms");
        } catch (Exception exe) {}
        try {
            isEmail = rst.getBoolean("isemail");
        } catch (Exception exe) {}
        try {
            schedStart = rst.getDate("sched_start");
        } catch (Exception exe) {}
        try {
            schedEnd = rst.getDate("sched_end");
        } catch (Exception exe) {}
        try {
            attachPdf = rst.getBoolean("attach_pdf");
        } catch (Exception exe) {}
        try {
            fromEmail = rst.getString("from_email");
        } catch (Exception exe) {}
        try {
            lastError = rst.getTimestamp("last_error");
        } catch (Exception exe) {}
        try {
            errorNum = rst.getInt("error_num");
        } catch (Exception exe) {}
    }

    /**
     * @return the messagingCommunicationId
     */
    public Integer getMessagingCommunicationId() {
        return messagingCommunicationId;
    }

    /**
     * @param messagingCommunicationId the messagingCommunicationId to set
     */
    public void setMessagingCommunicationId(Integer messagingCommunicationId) {
        this.messagingCommunicationId = messagingCommunicationId;
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
     * @return the shiftId
     */
    public String getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(String shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the datetimerequested
     */
    public Date getDatetimerequested() {
        return datetimerequested;
    }

    /**
     * @param datetimerequested the datetimerequested to set
     */
    public void setDatetimerequested(Date datetimerequested) {
        this.datetimerequested = datetimerequested;
    }

    /**
     * @return the datetimesent
     */
    public Date getDatetimesent() {
        return datetimesent;
    }

    /**
     * @param datetimesent the datetimesent to set
     */
    public void setDatetimesent(Date datetimesent) {
        this.datetimesent = datetimesent;
    }

    /**
     * @return the sentTo
     */
    public String getSentTo() {
        return sentTo;
    }

    /**
     * @param sentTo the sentTo to set
     */
    public void setSentTo(String sentTo) {
        this.sentTo = sentTo;
    }

    /**
     * @return the ccd
     */
    public String getCcd() {
        return ccd;
    }

    /**
     * @param ccd the ccd to set
     */
    public void setCcd(String ccd) {
        this.ccd = ccd;
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
     * @return the body
     */
    public String getBody() {
        return body;
    }

    /**
     * @param body the body to set
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * @return the employeeId
     */
    public Integer getEmployeeId() {
        return employeeId;
    }

    /**
     * @param employeeId the employeeId to set
     */
    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    /**
     * @return the isSMS
     */
    public Boolean getIsSMS() {
        return isSMS;
    }

    /**
     * @param isSMS the isSMS to set
     */
    public void setIsSMS(Boolean isSMS) {
        this.isSMS = isSMS;
    }

    /**
     * @return the isEmail
     */
    public Boolean getIsEmail() {
        return isEmail;
    }

    /**
     * @param isEmail the isEmail to set
     */
    public void setIsEmail(Boolean isEmail) {
        this.isEmail = isEmail;
    }

    /**
     * @return the schedStart
     */
    public Date getSchedStart() {
        return schedStart;
    }

    /**
     * @param schedStart the schedStart to set
     */
    public void setSchedStart(Date schedStart) {
        this.schedStart = schedStart;
    }

    /**
     * @return the schedEnd
     */
    public Date getSchedEnd() {
        return schedEnd;
    }

    /**
     * @param schedEnd the schedEnd to set
     */
    public void setSchedEnd(Date schedEnd) {
        this.schedEnd = schedEnd;
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
     * @return the lastError
     */
    public Date getLastError() {
        return lastError;
    }

    /**
     * @param lastError the lastError to set
     */
    public void setLastError(Date lastError) {
        this.lastError = lastError;
    }

    /**
     * @return the errorNum
     */
    public Integer getErrorNum() {
        return errorNum;
    }

    /**
     * @param errorNum the errorNum to set
     */
    public void setErrorNum(Integer errorNum) {
        this.errorNum = errorNum;
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
}
