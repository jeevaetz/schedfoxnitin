/*
 * BannedConflict.java
 *
 * Created on August 18, 2006, 12:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischedule.schedule.components;

import java.util.Calendar;

/**
 *
 * @author shawn
 *
 * ...yeah.
 *
 * This is a phenominally stupid class so a shift assigned to a banned employee shows up
 * as a conflict
 */
public class BannedConflict implements SourceOfConflict {
    
    /** Creates a new instance of BannedConflict */
    public BannedConflict() { }

    public int getDayCode() { return 0; }
    public int getStartTime() { return 0; }
    public int getEndTime() { return 0; }
    
    public void setShiftConflictingWith(SourceOfConflict o) { }

    public String getDateString() { return ""; }
    public String getFormattedStartTime() { return ""; }
    public String getFormattedEndTime() { return ""; }
    public int getMyType() { return BANNED; }
    public int getWeekNo() { return 0;}

    public boolean isDeleted() { return false; }
    public boolean equals(SourceOfConflict compareMe) { return false; }
    public String getShiftId() { return ""; }
    public int getAvailType() { return 0; }
    public int getOffset() { return 0; }
    public Calendar getStartCal() {
        return null;
    }

    public Calendar getEndCal() {
        return null;
    }
    
    public boolean shouldCauseConflict() { return false; }

    public SEmployee getEmployee() {
        return new SEmployee("0");
    }

    public String getGroupId() {
        return "";
    }

    public SClient getClient() {
        return new SClient("0");
    }

    public SShift getShift() {
        return null;
    }

    public void setShift(SShift shift) {

    }

    public void setShift(SShift shift, boolean clearVal) {

    }
    
}
