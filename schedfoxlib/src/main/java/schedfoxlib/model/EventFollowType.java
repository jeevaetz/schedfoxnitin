/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.model;

import java.io.Serializable;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class EventFollowType implements Serializable {
    private Integer eventFollowTypeId;
    private Integer eventFollowGroupId;
    private String eventFollow;
    
    public EventFollowType() {
        
    }

    public EventFollowType(Record_Set rst) {
        try {
            eventFollowTypeId = rst.getInt("event_follow_type_id");
        } catch (Exception exe) {}
        try {
            eventFollowGroupId = rst.getInt("event_follow_group_id");
        } catch (Exception exe) {}
        try {
            eventFollow = rst.getString("event_follow");
        } catch (Exception exe) {}
    }
    
    /**
     * @return the eventFollowTypeId
     */
    public Integer getEventFollowTypeId() {
        return eventFollowTypeId;
    }

    /**
     * @param eventFollowTypeId the eventFollowTypeId to set
     */
    public void setEventFollowTypeId(Integer eventFollowTypeId) {
        this.eventFollowTypeId = eventFollowTypeId;
    }

    /**
     * @return the eventFollowGroupId
     */
    public Integer getEventFollowGroupId() {
        return eventFollowGroupId;
    }

    /**
     * @param eventFollowGroupId the eventFollowGroupId to set
     */
    public void setEventFollowGroupId(Integer eventFollowGroupId) {
        this.eventFollowGroupId = eventFollowGroupId;
    }

    /**
     * @return the eventFollow
     */
    public String getEventFollow() {
        return eventFollow;
    }

    /**
     * @param eventFollow the eventFollow to set
     */
    public void setEventFollow(String eventFollow) {
        this.eventFollow = eventFollow;
    }
    
    @Override
    public String toString() {
        return this.eventFollow;
    }
}
