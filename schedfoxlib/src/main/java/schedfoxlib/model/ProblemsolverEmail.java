/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class ProblemsolverEmail {
    private Integer problemsolverEmailId;
    private Integer psId;
    private Integer userId;
    private Date datetimerequested;
    private Date datetimesent;
    private String sentTo;
    private String ccd;
    private String emailSubject;
    private String emailBody;
    private Boolean attachPdf;
    
    public ProblemsolverEmail() {
        
    }

    public ProblemsolverEmail(Record_Set rst) {
        try {
            problemsolverEmailId = rst.getInt("problemsolver_email_id");
        } catch (Exception exe) {}
        try {
            psId = rst.getInt("ps_id");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
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
            emailSubject = rst.getString("email_subject");
        } catch (Exception exe) {}
        try {
            emailBody = rst.getString("email_body");
        } catch (Exception exe) {}
        try {
            attachPdf = rst.getBoolean("attach_pdf");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the problemsolverEmailId
     */
    public Integer getProblemsolverEmailId() {
        return problemsolverEmailId;
    }

    /**
     * @param problemsolverEmailId the problemsolverEmailId to set
     */
    public void setProblemsolverEmailId(Integer problemsolverEmailId) {
        this.problemsolverEmailId = problemsolverEmailId;
    }

    /**
     * @return the psId
     */
    public Integer getPsId() {
        return psId;
    }

    /**
     * @param psId the psId to set
     */
    public void setPsId(Integer psId) {
        this.psId = psId;
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
     * @return the emailSubject
     */
    public String getEmailSubject() {
        return emailSubject;
    }

    /**
     * @param emailSubject the emailSubject to set
     */
    public void setEmailSubject(String emailSubject) {
        this.emailSubject = emailSubject;
    }

    /**
     * @return the emailBody
     */
    public String getEmailBody() {
        return emailBody;
    }

    /**
     * @param emailBody the emailBody to set
     */
    public void setEmailBody(String emailBody) {
        this.emailBody = emailBody;
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
}
