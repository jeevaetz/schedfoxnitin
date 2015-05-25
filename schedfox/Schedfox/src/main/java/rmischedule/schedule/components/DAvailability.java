/*
 * DAvailability.java
 *
 * Created on August 24, 2005, 3:54 PM
 *
 * Copyright: SchedFox 2005
 */
package rmischedule.schedule.components;

import schedfoxlib.model.ShiftTypeClass;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import rmischedule.main.Main_Window;
import rmischedule.schedule.Schedule_View_Panel;
import rmischeduleserver.util.StaticDateTimeFunctions;

import javax.swing.ImageIcon;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author Ira Juneau
 */
public class DAvailability implements UnitToDisplay {

    private String dbasedate;
    private int type;
    private int starttime;
    private int endtime;
    private int daycode;
    private int week_no;
    private Schedule_View_Panel parent;
    private String formattedStart;
    private String formattedEnd;
    private String readableDate;
    private boolean isDeleted;
    private String myId;
    private Integer availId;
    private boolean isMaster;
    private boolean hasNote;
    private boolean currentlyFilteringDontSave;

    private SEmployee employee;
    private SMainComponent myCli;
    private SShift shift;

    private Calendar startCal;
    private Calendar endCal;

    private SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

    /** Creates a new instance of DAvailability */
    public DAvailability(SEmployee myEmp, Record_Set rst,
            Schedule_View_Panel myParent) {
        this.employee = myEmp;

        dbasedate = rst.getString("doy");
        availId = rst.getInt("avail_id");
        isMaster = rst.getBoolean("ismaster");
        parent = myParent;
        type = rst.getInt("type");
        String myType = type + "";
        if (type == Main_Window.AVAILABLE) {
            //If available we don't care about type since we want to clear all shifts for date.
            myType = "";
        }
        myId = "A" + dbasedate + "-" + myEmp.getId() + "-" + myType;
        starttime = rst.getInt("stime");
        endtime = rst.getInt("etime");

        if (starttime == 0 && endtime == 0) {
            endtime = 1440;
        }
                
        try {
            startCal = Calendar.getInstance();
            startCal.setTime(dbFormat.parse(dbasedate));
            startCal.add(Calendar.MINUTE, starttime);

            endCal = Calendar.getInstance();
            endCal.setTime(dbFormat.parse(dbasedate));
            endCal.add(Calendar.MINUTE, endtime);
        } catch (Exception e) {}
        int format = StaticDateTimeFunctions.STANDARD_FORMAT;
        if (parent.isMilitaryTimeFormat()) {
            format = StaticDateTimeFunctions.MILITARY_FORMAT;
        }
        isDeleted = rst.getBoolean("isdeleted");
        formattedStart = StaticDateTimeFunctions.stringToFormattedTime(rst.getString("stime"), format);
        formattedEnd = StaticDateTimeFunctions.stringToFormattedTime(rst.getString("etime"), format);
        readableDate = StaticDateTimeFunctions.convertDatabaseDateToReadable(dbasedate);

        hasNote = rst.getBoolean("hasnote");
        week_no = myParent.getWeekNo(dbasedate);
        daycode = myParent.getDayNumber(dbasedate);
    }

    public void setClient(SMainComponent myCli) {
        this.myCli = myCli;
    }

    public int getAvailType() {
        return type;
    }

    /**
     * Is this avail marked deleted?
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * Gets date in that good ol database format 2005-10-02
     */
    public String getDatabaseDate() {
        return dbasedate;
    }

    /**
     * Gets readable database 10/02/2005
     */
    public String getDateString() {
        return readableDate;
    }

    /**
     * Blank method just needed since defined by SourceOfConflict...
     */
    public void setShiftConflictingWith(SourceOfConflict o) {
    }

    /**
     * Tells using class that we are a DAvailability...
     */
    public int getMyType() {
        return SourceOfConflict.DAVAILABILITY;
    }

    /**
     * Gets end time in either Military or Standard Format...fun
     */
    public String getFormattedEndTime() {
        return formattedEnd;
    }

    /**
     * Gets start time in either Military or Standard Format...greatness
     */
    public String getFormattedStartTime() {
        return formattedStart;
    }

    /**
     * Gets Week Number
     */
    public int getWeekNo() {
        return week_no;
    }

    /**
     * Gets day code
     */
    public int getDayCode() {
        return daycode;
    }

    /**
     * Returns the start time for this Availability
     */
    public int getStartTime() {
        return starttime;
    }

    /**
     * Returns the end time for this Availability
     */
    public int getEndTime() {
        return endtime;
    }

    /**
     * Gets the appropriate Icon Depending on the Type of the Unavailability
     */
    public ImageIcon getAvailIcon() {
        if (type == Main_Window.NON_AVAILABLE) {
            return Main_Window.Generic_NA_Icon_Faded;
        } else if (type == Main_Window.NON_AVAILABLE_PERSONAL) {
            return Main_Window.Personal_Icon_Faded;
        } else if (type == Main_Window.NON_AVAILABLE_SICK) {
            return Main_Window.Sic_Icon_Faded;
        } else if (type == Main_Window.NON_AVAILABLE_VACATION) {
            return Main_Window.Vac_Icon_Faded;
        } else if (type == Main_Window.NON_AVAILABLE_HALF_VACATION) {
            return Main_Window.Vac_Half_Icon_Faded;
        } else if (type == Main_Window.NON_AVAILABLE_HALF_PERSONAL) {
            return Main_Window.Personal_Half_Icon_Faded;
        }
        return null;
    }

    public boolean equals(SourceOfConflict compareMe) {
        if (compareMe.getMyType() == SourceOfConflict.DAVAILABILITY) {
            if (compareMe.getShiftId().equals(getShiftId())) {
                return true;
            }
        }
        return false;
    }

    public String getShiftId() {
        return myId;
    }

    public boolean shouldCauseConflict() {
        return true;
    }

    public int getOffset() {
        //Now lines up w/ Calendar int for DOW
        int day = (this.getDayCode() % 7) + 1;
        int value = (day - parent.getBegOfWeek()) % 7;
        if (value < 0) {
            value = 7 + value;
        }
        return value;
    }

    /**
     * @return the employee
     */
    public SEmployee getEmployee() {
        return employee;
    }

    public SMainComponent getClient() {
        return myCli;
    }
    
    public String getGroupId() {
        return "0";
    }

    public SShift getShift() {
        return shift;
    }

    public void setShift(SShift shift) {
        this.shift = shift;
    }

    public void setShift(SShift shift, boolean clearVal) {
        if (clearVal && this.shift != null) {
            this.shift.cleareInfo();
        }
        this.shift = shift;
        myCli = this.shift.getClient();
        employee = this.shift.getEmployee();
        daycode = this.shift.getDayCode();
        this.shift.setBG();
    }

    public boolean hasNote() {
        return hasNote;
    }

    public double getNoHoursDouble() {
        return StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(starttime, endtime, Integer.parseInt(parent.getConnection().myCompany));

    }

    public boolean hasConflict() {
        return false;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public UnitToDisplay getMaster() {
        //TODO: master logic
        return null;
    }

    public ShiftTypeClass getType() {
        //TODO: return actual value
        return new ShiftTypeClass();
    }

    public void recalTimes() {
        shift.myRow.myWeek.addTotal(getNoHoursDouble());
        myCli.addTotal(this, getNoHoursDouble());
    }

    public Schedule_View_Panel getParent() {
        return parent;
    }

    public String getLastUpdateStr() {
        //TODO: fille this in properly
        return "";
    }

    public void zeroTimes() {
        if (shift != null) {
            shift.myRow.myWeek.addTotal(-getNoHoursDouble());
            myCli.addTotal(this, -getNoHoursDouble());
        }
    }

    public boolean isBlank() {
        if (this.getAvailType() == Main_Window.AVAILABLE) {
            return true;
        }
        return false;
    }

    public boolean isOverwrittingBuffer() {
        return false;
    }

    /**
     * Called by filterMaster in SClient when we are filtering Master so that it wont save the
     * info as a temp sched when it builds the shift....
     */
    public void setAsFilteringMasterDontSave(boolean filtering) {
        currentlyFilteringDontSave = filtering;
    }

    public boolean isCurrentlyFilteringDontSave() {
        return currentlyFilteringDontSave;
    }

    public String getRealMasterId() {
        return "0";
    }

    /**
     * @return the availId
     */
    public Integer getAvailId() {
        return availId;
    }

    /**
     * @param availId the availId to set
     */
    public void setAvailId(Integer availId) {
        this.availId = availId;
    }

    public Calendar getStartCal() {
        return startCal;
    }

    public Calendar getEndCal() {
        return endCal;
    }
}
