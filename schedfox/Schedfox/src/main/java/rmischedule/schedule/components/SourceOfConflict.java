/*
 * SourceOfConflict.java
 *
 * Created on August 25, 2005, 8:55 AM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package rmischedule.schedule.components;

import java.util.Calendar;

/**
 *
 * @author Ira Juneau
 *
 * This is an interface for any classes that can cause a conflict with our Shifts, 
 * used by DShift and DAvailability...since both can cause a conflict...g...
 */
public interface SourceOfConflict {
    
    public final static int DSHIFT = 1;
    public final static int DAVAILABILITY = 2;
    public final static int NOTTRAINED = 3;
    public final static int BANNED = 4;
    
    public Calendar getStartCal();
    public Calendar getEndCal();
    public int getDayCode();
    public int getStartTime();
    public int getEndTime();
    public void setShiftConflictingWith(SourceOfConflict o);
    public String getDateString();
    public String getFormattedStartTime();
    public String getFormattedEndTime();
    public int getMyType();
    public int getWeekNo();
    public int getOffset();
    public boolean isDeleted();
    
    public boolean equals(SourceOfConflict compareMe);
    public String getShiftId();
    public int getAvailType();
    public boolean shouldCauseConflict();

}
