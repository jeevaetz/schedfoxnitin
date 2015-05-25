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
public class EventType implements Serializable, Comparable {
    
    private Integer eventTypeId;
    private Integer eventGroupId;
    private String event;
    
    public EventType() {
        
    }
    
    public EventType(Record_Set rst) {
        try {
            eventTypeId = rst.getInt("event_type_id");
        } catch (Exception e) {}
        try {
            eventGroupId = rst.getInt("event_group_id");
        } catch (Exception e) {}
        try {
            event = rst.getString("event");
        } catch (Exception e) {}
    }

    /**
     * @return the eventTypeId
     */
    public Integer getEventTypeId() {
        return eventTypeId;
    }

    /**
     * @param eventTypeId the eventTypeId to set
     */
    public void setEventTypeId(Integer eventTypeId) {
        this.eventTypeId = eventTypeId;
    }

    /**
     * @return the eventGroupId
     */
    public Integer getEventGroupId() {
        return eventGroupId;
    }

    /**
     * @param eventGroupId the eventGroupId to set
     */
    public void setEventGroupId(Integer eventGroupId) {
        this.eventGroupId = eventGroupId;
    }

    /**
     * @return the event
     */
    public String getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(String event) {
        this.event = event;
    }
    
    @Override
    public String toString() {
        return this.event;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof EventType) {
            try {
                EventType comp = (EventType)o;
                return this.event.compareTo(comp.getEvent());
            } catch (Exception e) {}
        }
        return -1;
    }
}
