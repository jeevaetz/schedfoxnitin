/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.util.Date;

/**
 *
 * @author ira
 */
public class CapturedEmail implements java.io.Serializable {
    private Integer capturedEmailId;
    private String capturedEmail;
    private Integer source;
    private Date dateAdded;
    
    public CapturedEmail() {
        
    }

    /**
     * @return the capturedEmailId
     */
    public Integer getCapturedEmailId() {
        return capturedEmailId;
    }

    /**
     * @param capturedEmailId the capturedEmailId to set
     */
    public void setCapturedEmailId(Integer capturedEmailId) {
        this.capturedEmailId = capturedEmailId;
    }

    /**
     * @return the capturedEmail
     */
    public String getCapturedEmail() {
        return capturedEmail;
    }

    /**
     * @param capturedEmail the capturedEmail to set
     */
    public void setCapturedEmail(String capturedEmail) {
        this.capturedEmail = capturedEmail;
    }

    /**
     * @return the source
     */
    public Integer getSource() {
        return source;
    }

    /**
     * @param source the source to set
     */
    public void setSource(Integer source) {
        this.source = source;
    }

    /**
     * @return the dateAdded
     */
    public Date getDateAdded() {
        return dateAdded;
    }

    /**
     * @param dateAdded the dateAdded to set
     */
    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }
}
