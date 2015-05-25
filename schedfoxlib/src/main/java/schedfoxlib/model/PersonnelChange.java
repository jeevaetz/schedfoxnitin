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
public class PersonnelChange {
    private Integer personnelChangeId;
    private Date dateOfChange;
    private Integer employeeId;
    private Integer clientId;
    private Integer reasonId;
    private String reasonText;
    private Integer userId;
    private String avail_notes_notes;
    private Date dateSent;
    private String templateText;

    public PersonnelChange() {
        
    }
    
    public PersonnelChange(Record_Set rst) {
        try {
            personnelChangeId = rst.getInt("personnel_change_id");
        } catch (Exception exe) {}
        try {
            dateOfChange = rst.getTimestamp("date_of_change");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            reasonId = rst.getInt("reason_id");
        } catch (Exception exe) {}
        try {
            reasonText = rst.getString("reason_text");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("user_id");
        } catch (Exception exe) {}
        try {
            dateSent = rst.getTimestamp("date_sent");
        } catch (Exception exe) {}
        try {
            templateText = rst.getString("template_text");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the personnelChangeId
     */
    public Integer getPersonnelChangeId() {
        return personnelChangeId;
    }

    /**
     * @param personnelChangeId the personnelChangeId to set
     */
    public void setPersonnelChangeId(Integer personnelChangeId) {
        this.personnelChangeId = personnelChangeId;
    }

    /**
     * @return the dateOfChange
     */
    public Date getDateOfChange() {
        return dateOfChange;
    }

    /**
     * @param dateOfChange the dateOfChange to set
     */
    public void setDateOfChange(Date dateOfChange) {
        this.dateOfChange = dateOfChange;
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
     * @return the clientId
     */
    public Integer getClientId() {
        return clientId;
    }

    /**
     * @param clientId the clientId to set
     */
    public void setClientId(Integer clientId) {
        this.clientId = clientId;
    }

    /**
     * @return the reasonId
     */
    public Integer getReasonId() {
        return reasonId;
    }

    /**
     * @param reasonId the reasonId to set
     */
    public void setReasonId(Integer reasonId) {
        this.reasonId = reasonId;
    }

    /**
     * @return the reasonText
     */
    public String getReasonText() {
        return reasonText;
    }

    /**
     * @param reasonText the reasonText to set
     */
    public void setReasonText(String reasonText) {
        this.reasonText = reasonText;
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
     * @return the avail_notes_notes
     */
    public String getAvail_notes_notes() {
        return avail_notes_notes;
    }

    /**
     * @param avail_notes_notes the avail_notes_notes to set
     */
    public void setAvail_notes_notes(String avail_notes_notes) {
        this.avail_notes_notes = avail_notes_notes;
    }

    /**
     * @return the dateSent
     */
    public Date getDateSent() {
        return dateSent;
    }

    /**
     * @param dateSent the dateSent to set
     */
    public void setDateSent(Date dateSent) {
        this.dateSent = dateSent;
    }

    /**
     * @return the templateText
     */
    public String getTemplateText() {
        return templateText;
    }

    /**
     * @param templateText the templateText to set
     */
    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }
}
