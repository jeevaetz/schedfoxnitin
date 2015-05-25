/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import java.util.Date;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EventFollowup implements Serializable {
    
    private Integer eventFollowupId;
    private Integer eventId;
    private Integer followupRequestedBy;
    private Date followupRequestCreatedon;
    private Integer followupProcessedBy;
    private Date followupProcessedOn;
    private Integer followupType;
    private String followupNote;
    private Integer problemSolverId;
    private Integer followupRequestGroup;
    private Integer followupRequestUser;
    
    public EventFollowup() {
        
    }
    
    public EventFollowup(Record_Set rst) {
        try {
            eventFollowupId = rst.getInt("event_followup_id");
        } catch (Exception exe) {}
        try {
            eventId = rst.getInt("event_id");
        } catch (Exception exe) {}
        try {
            followupRequestedBy = rst.getInt("followup_requested_by");
        } catch (Exception exe) {}
        try {
            followupRequestCreatedon = rst.getTimestamp("followup_request_createdon");
        } catch (Exception exe) {}
        try {
            followupProcessedBy = rst.getInt("followup_processed_by");
        } catch (Exception exe) {}
        try {
            followupProcessedOn = rst.getTimestamp("followup_processed_on");
        } catch (Exception exe) {}
        try {
            followupType = rst.getInt("followup_type");
        } catch (Exception exe) {}
        try {
            followupNote = rst.getString("followup_note");
        } catch (Exception exe) {}
        try {
            problemSolverId = rst.getInt("problem_solver_id");
        } catch (Exception exe) {}
        try {
            followupRequestGroup = rst.getInt("followup_request_group");
        } catch (Exception exe) {}
        try {
            followupRequestUser = rst.getInt("followup_request_user");
        } catch (Exception exe) {}
    }

    /**
     * @return the eventFollowupId
     */
    public Integer getEventFollowupId() {
        return eventFollowupId;
    }

    /**
     * @param eventFollowupId the eventFollowupId to set
     */
    public void setEventFollowupId(Integer eventFollowupId) {
        this.eventFollowupId = eventFollowupId;
    }

    /**
     * @return the eventId
     */
    public Integer getEventId() {
        return eventId;
    }

    /**
     * @param eventId the eventId to set
     */
    public void setEventId(Integer eventId) {
        this.eventId = eventId;
    }

    /**
     * @return the followupRequestedBy
     */
    public Integer getFollowupRequestedBy() {
        return followupRequestedBy;
    }

    /**
     * @param followupRequestedBy the followupRequestedBy to set
     */
    public void setFollowupRequestedBy(Integer followupRequestedBy) {
        this.followupRequestedBy = followupRequestedBy;
    }

    /**
     * @return the followupRequestCreatedon
     */
    public Date getFollowupRequestCreatedon() {
        return followupRequestCreatedon;
    }

    /**
     * @param followupRequestCreatedon the followupRequestCreatedon to set
     */
    public void setFollowupRequestCreatedon(Date followupRequestCreatedon) {
        this.followupRequestCreatedon = followupRequestCreatedon;
    }

    /**
     * @return the followupProcessedBy
     */
    public Integer getFollowupProcessedBy() {
        return followupProcessedBy;
    }

    /**
     * @param followupProcessedBy the followupProcessedBy to set
     */
    public void setFollowupProcessedBy(Integer followupProcessedBy) {
        this.followupProcessedBy = followupProcessedBy;
    }

    /**
     * @return the followupPorocessedOn
     */
    public Date getFollowupProcessedOn() {
        return followupProcessedOn;
    }

    /**
     * @param followupPorocessedOn the followupPorocessedOn to set
     */
    public void setFollowupProcessedOn(Date followupPorocessedOn) {
        this.followupProcessedOn = followupPorocessedOn;
    }

    /**
     * @return the followupType
     */
    public Integer getFollowupType() {
        return followupType;
    }

    /**
     * @param followupType the followupType to set
     */
    public void setFollowupType(Integer followupType) {
        this.followupType = followupType;
    }

    /**
     * @return the followupNote
     */
    public String getFollowupNote() {
        return followupNote;
    }

    /**
     * @param followupNote the followupNote to set
     */
    public void setFollowupNote(String followupNote) {
        this.followupNote = followupNote;
    }

    /**
     * @return the problemSolverId
     */
    public Integer getProblemSolverId() {
        return problemSolverId;
    }

    /**
     * @param problemSolverId the problemSolverId to set
     */
    public void setProblemSolverId(Integer problemSolverId) {
        this.problemSolverId = problemSolverId;
    }
    
    @Override
    public String toString() {
        return this.followupNote;
    }

    /**
     * @return the followupRequestGroup
     */
    public Integer getFollowupRequestGroup() {
        return followupRequestGroup;
    }

    /**
     * @param followup_request_group the followupRequestGroup to set
     */
    public void setFollowupRequestGroup(Integer followup_request_group) {
        this.followupRequestGroup = followup_request_group;
    }

    /**
     * @return the followupRequestUser
     */
    public Integer getFollowupRequestUser() {
        return followupRequestUser;
    }

    /**
     * @param followup_request_user the followupRequestUser to set
     */
    public void setFollowupRequestUser(Integer followup_request_user) {
        this.followupRequestUser = followup_request_user;
    }
    
}
