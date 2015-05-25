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
public class Event implements Serializable {
    private Integer eventId;
    private Integer eventTypeId;
    private String shiftId;
    private String eventNotes;
    private Integer enteredBy;
    private Date enteredOn;
    private Integer problemSolverId;
    private Integer employeeId;
    private Integer clientId;
    private Integer origShiftStartTime;
    private Integer origShiftEndTime;
    
    //Not always filled
    private Integer numberFollowUps;
    private Integer userRequested;
    private Integer groupRequested;
    
    public Event() {
        
    }
    
    public Event(Record_Set rst) {
        try {
            eventId = rst.getInt("event_id");
        } catch (Exception exe) {}
        try {
            eventTypeId = rst.getInt("event_type_id");
        } catch (Exception exe) {}
        try {
            shiftId = rst.getString("shift_id");
        } catch (Exception exe) {}
        try {
            eventNotes = rst.getString("event_notes");
        } catch (Exception exe) {}
        try {
            enteredBy = rst.getInt("entered_by");
        } catch (Exception exe) {}
        try {
            enteredOn = rst.getTimestamp("entered_on");
        } catch (Exception exe) {}
        try {
            problemSolverId = rst.getInt("problem_solver_id");
        } catch (Exception exe) {}
        try {
            employeeId = rst.getInt("employee_id");
        } catch (Exception exe) {}
        try {
            clientId = rst.getInt("client_id");
        } catch (Exception exe) {}
        try {
            numberFollowUps = rst.getInt("count");
        } catch (Exception exe) {}
        try {
            if (rst.getString("orig_shift_start_time").length() > 0) {
                origShiftStartTime = rst.getInt("orig_shift_start_time");
            }
        } catch (Exception exe) {}
        try {
            if (rst.getString("orig_shift_end_time").length() > 0) {
                origShiftEndTime = rst.getInt("orig_shift_end_time");
            }
        } catch (Exception exe) {}
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
     * @return the eventNotes
     */
    public String getEventNotes() {
        return eventNotes;
    }

    /**
     * @param eventNotes the eventNotes to set
     */
    public void setEventNotes(String eventNotes) {
        this.eventNotes = eventNotes;
    }

    /**
     * @return the enteredBy
     */
    public Integer getEnteredBy() {
        return enteredBy;
    }

    /**
     * @param enteredBy the enteredBy to set
     */
    public void setEnteredBy(Integer enteredBy) {
        this.enteredBy = enteredBy;
    }

    /**
     * @return the enteredOn
     */
    public Date getEnteredOn() {
        return enteredOn;
    }

    /**
     * @param enteredOn the enteredOn to set
     */
    public void setEnteredOn(Date enteredOn) {
        this.enteredOn = enteredOn;
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
     * @return the numberFollowUps
     */
    public Integer getNumberFollowUps() {
        return numberFollowUps;
    }

    /**
     * @param numberFollowUps the numberFollowUps to set
     */
    public void setNumberFollowUps(Integer numberFollowUps) {
        this.numberFollowUps = numberFollowUps;
    }

    /**
     * @return the userRequested
     */
    public Integer getUserRequested() {
        return userRequested;
    }

    /**
     * @param userRequested the userRequested to set
     */
    public void setUserRequested(Integer userRequested) {
        this.userRequested = userRequested;
    }

    /**
     * @return the groupRequested
     */
    public Integer getGroupRequested() {
        return groupRequested;
    }

    /**
     * @param groupRequested the groupRequested to set
     */
    public void setGroupRequested(Integer groupRequested) {
        this.groupRequested = groupRequested;
    }

    /**
     * @return the origShiftStartTime
     */
    public Integer getOrigShiftStartTime() {
        return origShiftStartTime;
    }

    /**
     * @param origShiftStartTime the origShiftStartTime to set
     */
    public void setOrigShiftStartTime(Integer origShiftStartTime) {
        this.origShiftStartTime = origShiftStartTime;
    }

    /**
     * @return the origShiftEndTime
     */
    public Integer getOrigShiftEndTime() {
        return origShiftEndTime;
    }

    /**
     * @param origShiftEndTime the origShiftEndTime to set
     */
    public void setOrigShiftEndTime(Integer origShiftEndTime) {
        this.origShiftEndTime = origShiftEndTime;
    }

    
}
