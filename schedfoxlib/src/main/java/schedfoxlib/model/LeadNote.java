/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.sql.ResultSet;
import java.util.Date;

/**
 *
 * @author ira
 */
public class LeadNote {
    private Integer id;
    private Integer leadId;
    private String title;
    private String note;
    private Date dateCreated;
    private Integer userId;
    private Boolean isDeleted;
    
    public LeadNote() {
        
    }

    public LeadNote(ResultSet rst) {
        try {
            id = rst.getInt("id");
        } catch (Exception exe) {}
        try {
            leadId = rst.getInt("LeadID");
        } catch (Exception exe) {}
        try {
            title = rst.getString("Title");
        } catch (Exception exe) {}
        try {
            note = rst.getString("Note1");
        } catch (Exception exe) {}
        try {
            dateCreated = rst.getTimestamp("DateCreated");
        } catch (Exception exe) {}
        try {
            userId = rst.getInt("userid");
        } catch (Exception exe) {}
        try {
            isDeleted = rst.getBoolean("isdeleted");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return the leadId
     */
    public Integer getLeadId() {
        return leadId;
    }

    /**
     * @param leadId the leadId to set
     */
    public void setLeadId(Integer leadId) {
        this.leadId = leadId;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the dateCreated
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated the dateCreated to set
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
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
     * @return the isDeleted
     */
    public Boolean getIsDeleted() {
        return isDeleted;
    }

    /**
     * @param isDeleted the isDeleted to set
     */
    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
