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
public class ClientTerminationReason {
    private Integer clientTerminationReasonId;
    private Integer clientId;
    private Integer terminationReasonId;
    private String notes;
    private Date dateOfNote;
    
    public ClientTerminationReason() {
        
    }
    
    public ClientTerminationReason(Record_Set rst) {
        try {
            clientTerminationReasonId = rst.getInt("client_termination_reason_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            terminationReasonId = rst.getInt("termination_reason_id");
        } catch (Exception exe) {}
        try {
            notes = rst.getString("notes");
        } catch (Exception exe) {}
        try {
            dateOfNote = rst.getTimestamp("date_of_note");
        } catch (Exception exe) {}
    }

    /**
     * @return the clientTerminationReasonId
     */
    public Integer getClientTerminationReasonId() {
        return clientTerminationReasonId;
    }

    /**
     * @param clientTerminationReasonId the clientTerminationReasonId to set
     */
    public void setClientTerminationReasonId(Integer clientTerminationReasonId) {
        this.clientTerminationReasonId = clientTerminationReasonId;
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
     * @return the terminationReasonId
     */
    public Integer getTerminationReasonId() {
        return terminationReasonId;
    }

    /**
     * @param terminationReasonId the terminationReasonId to set
     */
    public void setTerminationReasonId(Integer terminationReasonId) {
        this.terminationReasonId = terminationReasonId;
    }

    /**
     * @return the notes
     */
    public String getNotes() {
        return notes;
    }

    /**
     * @param notes the notes to set
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * @return the dateOfNote
     */
    public Date getDateOfNote() {
        return dateOfNote;
    }

    /**
     * @param dateOfNote the dateOfNote to set
     */
    public void setDateOfNote(Date dateOfNote) {
        this.dateOfNote = dateOfNote;
    }
}
