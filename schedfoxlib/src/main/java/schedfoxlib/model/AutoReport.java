/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.util.Date;

/**
 *
 * @author ira
 */
public class AutoReport {
    private Integer autoReportId;
    private Integer userId;
    private Integer shiftId;
    private Date dateTimeRequested;
    private Date dateTimeSent;
    private String sentTo;
    private String ccd;
    private String subject;
    private String body;
    private String reportData;
    private String reportName;
    private String fromEmail;
    private Date lastError;
    private Integer errorNum;
    
    public AutoReport() {
        
    }

    /**
     * @return the autoReportId
     */
    public Integer getAutoReportId() {
        return autoReportId;
    }

    /**
     * @param autoReportId the autoReportId to set
     */
    public void setAutoReportId(Integer autoReportId) {
        this.autoReportId = autoReportId;
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
    public Integer getShiftId() {
        return shiftId;
    }

    /**
     * @param shiftId the shiftId to set
     */
    public void setShiftId(Integer shiftId) {
        this.shiftId = shiftId;
    }

    /**
     * @return the dateTimeRequested
     */
    public Date getDateTimeRequested() {
        return dateTimeRequested;
    }

    /**
     * @param dateTimeRequested the dateTimeRequested to set
     */
    public void setDateTimeRequested(Date dateTimeRequested) {
        this.dateTimeRequested = dateTimeRequested;
    }

    /**
     * @return the dateTimeSent
     */
    public Date getDateTimeSent() {
        return dateTimeSent;
    }

    /**
     * @param dateTimeSent the dateTimeSent to set
     */
    public void setDateTimeSent(Date dateTimeSent) {
        this.dateTimeSent = dateTimeSent;
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
     * @return the reportData
     */
    public String getReportData() {
        return reportData;
    }

    /**
     * @param reportData the reportData to set
     */
    public void setReportData(String reportData) {
        this.reportData = reportData;
    }

    /**
     * @return the reportName
     */
    public String getReportName() {
        return reportName;
    }

    /**
     * @param reportName the reportName to set
     */
    public void setReportName(String reportName) {
        this.reportName = reportName;
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
}
