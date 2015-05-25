/*
 * DShift.java
 *
 * Created on April 27, 2004, 6:44 AM
 */
package rmischedule.schedule.components;

import schedfoxlib.model.ShiftOptionsClass;
import schedfoxlib.model.ShiftTypeClass;
import com.creamtec.ajaxswing.AjaxSwingManager;
import java.text.SimpleDateFormat;
import rmischedule.schedule.Schedule_View_Panel;
import java.util.*;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ScheduleController;

import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;

/**
 *
 * @author  jason.allen
 */
public class DShift implements Comparable, UnitToDisplay {

    private String shift_id;
    private String schedule_id;
    private SMainComponent myClient;
    private SEmployee myEmployee;
    private RunQueriesEx myQueryExecutor;
    public Schedule_View_Panel parent;
    private start_end_time myTimes;
    private int day_code;
    private String date;
    private int week_no;
    private String myDatabaseDate;
    private String masterEndDate;
    private String masterStartDate;
    public boolean isMaster;
    private Hashtable<String, SourceOfConflict> shiftIAmConflictingWith;

    public int myOveride;
    public DShift myMaster;
    public SShift mySShift;
    public boolean isBlank;
    public String masterShiftId;
    private boolean currentlyFilteringDontSave;
    public boolean isOverwrittingBuffer;
    public boolean justMove = false;
    public boolean isDeleted;
    public ShiftTypeClass myType;
    public ShiftOptionsClass myPayOptions;
    public ShiftOptionsClass myBillOptions;
    private boolean hasNote;
    private int rate_code;
    private int weekly_rotation;
    private double numberHoursCalculated;
    private String myStartTimeFormatted;
    private String myEndTimeFormatted;
    public String lu;

    private Calendar startCal;
    private Calendar endCal;
    private SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    /** Creates a new instance of DShift */
    public DShift(
            String shiftId, String shiftMasterId, SMainComponent sClient, SEmployee sEmployee,
            int startTime, int endTime, int dayCode, Schedule_View_Panel Parent, String scheduleId,
            boolean is_deleted, String mtype, String date, String endDate, String startDate,
            String pay_opt, String bill_opt, int rateCode, String trainer, String lastUpdated, String hasNotes,
            int weeklyRotation) {

        parent = Parent;
        masterShiftId = shiftMasterId;
        lu = lastUpdated;

        shift_id = shiftId;
        myClient = sClient;
        myEmployee = sEmployee;
        numberHoursCalculated = 0.0;
        day_code = dayCode;
        myTimes = new start_end_time();
        myStartTimeFormatted = new String();
        myEndTimeFormatted = new String();
        

        myDatabaseDate = date;
        masterEndDate = endDate;
        masterStartDate = startDate;
        rate_code = rateCode;
        week_no = this.getWeekIdNo();

        startCal = Calendar.getInstance();
        endCal = Calendar.getInstance();
        try {
            startCal.setTime(myFormat.parse(date));
            if (startTime == 0) {
                startCal.add(Calendar.MINUTE, 1440);
            } else {
                startCal.add(Calendar.MINUTE, startTime);
            }
        } catch (Exception e) {}
        try {
            endCal.setTime(myFormat.parse(date));
            endCal.add(Calendar.MINUTE, endTime);
            if (endTime < startTime || startTime == 0) {
                endCal.add(Calendar.MINUTE, 1440);
            }

        } catch (Exception e) {}

        myQueryExecutor = new RunQueriesEx();

        if (shift_id.charAt(0) == '-') {
            isMaster = true;
        } else {
            isMaster = false;
        }

        if (hasNotes.equals("t") || hasNotes.equals("true")) {
            this.hasNote = true;
        } else {
            this.hasNote = false;
        }

        schedule_id = scheduleId;
        isDeleted = is_deleted;
        myType = new ShiftTypeClass(mtype);
        if (trainer == null) {
            trainer = "0";
        }
        try {
            myType.setTrainerInformation(Integer.parseInt(trainer), "");
        } catch (Exception e) {
        }

        myPayOptions = new ShiftOptionsClass();
        myBillOptions = new ShiftOptionsClass();

        myPayOptions.parse(pay_opt);
        myBillOptions.parse(bill_opt);

        myTimes.setVal(startTime, endTime);
        
        isOverwrittingBuffer = false;
        trainer = "";
        this.weekly_rotation = weeklyRotation;

        currentlyFilteringDontSave = false;

        shiftIAmConflictingWith = new Hashtable<String, SourceOfConflict>();
    }

    public Calendar getStartCal() {
        return this.startCal;
    }

    public Calendar getEndCal() {
        return this.endCal;
    }

    @Override
    public DShift clone() {
        DShift newDShift = new DShift(
                shift_id, masterShiftId, myClient, myEmployee, myTimes.getStartVal(),
                myTimes.getEndVal(), day_code, parent,
                schedule_id, isDeleted, myType.toString(), getDatabaseDate(),
                getEndDate(), getStartDate(), myPayOptions.toString(), myBillOptions.toString(),
                rate_code, myType.getTrainerId() + "", lu, this.hasNote ? "t" : "f",
                this.weekly_rotation);
        newDShift.myType = myType.clone();
        return newDShift;
    }

    public String getDatabaseDate() {
        return myDatabaseDate;
    }

    public String getEndDate() {
        return masterEndDate;
    }

    public String getStartDate() {
        return masterStartDate;
    }

    public int getPayBreakLength() {
        return myPayOptions.getBreakLength();
    }

    public int getPayBreakStart() {
        return myPayOptions.getBreakStart();
    }

    public int getBillBreakLength() {
        return myBillOptions.getBreakLength();
    }

    public int getBillBreakStart() {
        return myBillOptions.getBreakStart();
    }

    public int getRateCode() {
        return rate_code;
    }

    public boolean hasNote() {
        return hasNote;
    }

    public boolean isOpenShift() {
        return this.myEmployee.getName().length() == 0;
    }

    /**
     * Returns the weekly rotation
     * @return int
     */
    public int getWeeklyRotation() {
        return this.weekly_rotation;
    }

    /**
     * Used to strip out Date information and just return shift id for master shifts or
     * just the shift id for temps...useful for retrieving history..
     */
    public String getRealShiftId() {
        if (isMaster) {
            try {
                StringTokenizer strtoken = new StringTokenizer(shift_id, "-/");
                return strtoken.nextToken();
            } catch (Exception e) {
                return "";
            }
        }
        return shift_id;
    }

    /**
     * Run both on DShifts and DAvailability when it is identifie as a source of conflict!
     */
    public void setShiftConflictingWith(SourceOfConflict o) {
        if (o != null) {
            try {
                getShiftIAmConflictingWith().put(o.getShiftId(), o);

                StringBuffer toolTipText = new StringBuffer();
                toolTipText.append("<html>");
                if (AjaxSwingManager.isAjaxSwingRunning()) {//Add Custom Style for AjaxSwing ToolTip
                    toolTipText.append("<div style='background-color:#B8CFE5;font-size:small;margin:-1px -5px;padding:1px 5px'>");
                }
                Iterator<String> keys = getShiftIAmConflictingWith().keySet().iterator();
                boolean isFirstLoop = true;
                while (keys.hasNext()) {
                    SourceOfConflict sc = getShiftIAmConflictingWith().get(keys.next());
                    if (!isFirstLoop) {
                        toolTipText.append(getNewLine() + getNewLine());
                    }
                    isFirstLoop = false;

                    if (sc.getMyType() == SourceOfConflict.DSHIFT) {
                        DShift myConfShift = (DShift) sc;
                        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        toolTipText.append(getFontColor("red") + "Conflict Detected With Shift!" + closeFontTag() + getFontColor("black")
                                + getNewLine() + "Day: " + sc.getDateString() + getNewLine() + "Client: " + myConfShift.getClient().getClientName()
                                + getNewLine() + "Time: " + sc.getFormattedStartTime() + " to " + sc.getFormattedEndTime()
                                + closeFontTag());
                    } else if (sc.getMyType() == SourceOfConflict.DAVAILABILITY) {
                        toolTipText.append(getFontColor("red") + "Not Available!" + closeFontTag() + getFontColor("black")
                                + getNewLine() + "Day: " + sc.getDateString()
                                + getNewLine() + "Time: " + sc.getFormattedStartTime() + " to " + sc.getFormattedEndTime() + closeFontTag());
                    } else if (sc.getMyType() == SourceOfConflict.BANNED) {
                        toolTipText.append(getFontColor("red") + "Employee Banned!" + closeFontTag() + getFontColor("black") + closeFontTag());
                    } else if (sc.getMyType() == SourceOfConflict.NOTTRAINED) {
                        toolTipText.append(getFontColor("red") + "Training Conflict Detected!" + closeFontTag()
                                + getFontColor("black") + getNewLine() + "Requires " + sc.getFormattedEndTime() + " hours of training." + closeFontTag());
                    }

                }

                if (AjaxSwingManager.isAjaxSwingRunning()) {
                    toolTipText.append("</div>");
                }

                mySShift.setToolTipText(toolTipText.toString());

                mySShift.setBG();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            getShiftIAmConflictingWith().clear();
            mySShift.setBG();
            mySShift.setToolTipText(null);
        }
    }

    private String getNewLine() {
        return "<br/>";
    }

    private String getFontColor(String color) {
        return "<font color = " + color + ">";
    }

    private String closeFontTag() {
        return AjaxSwingManager.isAjaxSwingRunning() ? "</font>" : "";
    }

    /**
     * Used to identify that this particular SourceOfConflict is a DSHIFt
     */
    public int getMyType() {
        return SourceOfConflict.DSHIFT;
    }

    /**
     * Returns myType which should be used to access it...
     */
    public ShiftTypeClass getType() {
        return myType;
    }

    /**
     * Method placed here to ensuer that setBG was called whenever our shift type is modified...
     */
    public void updateTypeVal(int newVal) {
        myType.updateVal(newVal);
        if (mySShift != null) {
            mySShift.setBG();
        }
    }

    /**
     * Method placed here to ensure that setBG was called whenever our shift type is modified...
     */
    public void updateTypeVal(ShiftTypeClass newVal) {
        myType = newVal.clone();
        if (mySShift != null) {
            mySShift.setBG();
        }
    }

    /**
     * Returns Date in format YYYY-MM-DD, 2005-05-02
     */
    public String getDatabaseFormatDate() {
        return myDatabaseDate;
    }

    /**
     * Mark shift as non conflict...
     */
    public void deConflictShift() {
        try {
            myType.updateVal(ShiftTypeClass.SHIFT_OVERRIDE_CONFLICT);
            mySShift.isSelected = false;
            QueryGenerateShift myQueryShift = new QueryGenerateShift(this);
            myQueryShift.editShift(false, false);
            mySShift.myRow.myWeek.mySched.myParent.getConnection().executeQueryEx(myQueryShift.getMyQuery());
        } catch (Exception e) {
        }
    }

    /**
     * Set shift to confirmed and save...yay!
     */
    public void confirmShift() {
        try {
            myType.updateVal(ShiftTypeClass.SHIFT_CONFIRMED);
            mySShift.isSelected = false;
            QueryGenerateShift myQueryShift = new QueryGenerateShift(this);
            myQueryShift.editShift(false);
            mySShift.myRow.myWeek.mySched.myParent.getConnection().executeQueryEx(myQueryShift.getMyQuery());
        } catch (Exception e) {
        }
    }

    public String getGroupId() {
        return schedule_id;
    }

    public void setScheduleId(String s) {
        schedule_id = s;
    }

    /*
     * Updates from the dshift
     * When this function is called most things should already be updated.
     */
    public void updateShift(DShift m) {
        shift_id = m.getShiftId();
        myClient = m.getClient();
        myEmployee = m.getEmployee();
        myTimes.setVal(m.getStartTime(), m.getEndTime());
        day_code = m.getDayCode();
        myDatabaseDate = m.getDatabaseDate();
        schedule_id = m.getGroupId();
        lu = m.lu;
        isDeleted = m.isDeleted;
        rate_code = m.getRateCode();
        myPayOptions = m.myPayOptions;
        myBillOptions = m.myBillOptions;
        updateTypeVal(m.myType);
        if (m.hasNote()) {
            this.hasNote = true;
        } else {
            this.hasNote = false;
        }
        if (mySShift != null) {
            mySShift.setBG();
            mySShift.repaint();
        } else {
            System.out.println("DShift with no SShift received");
        }
    }

    /** property returns */
    public int getStartTime() {
        return myTimes.getStartVal();
    }

    public int getEndTime() {
        return myTimes.getEndVal();
    }

    public int getDayCode() {
        return day_code;
    }

    public int getWeekNo() {
        return week_no;
    }

    public String getShiftId() {
        return shift_id;
    }

    public DShift getMaster() {
        return myMaster;
    }

    public SMainComponent getClient() {
        return myClient;
    }

    public SEmployee getEmployee() {
        return myEmployee;
    }

    public Calendar getCalDate() {
        return parent.getDate(week_no, day_code);
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public int getWeekIdNo() {
        return parent.getWeekNo(myDatabaseDate);
    }

    public void setClient(SClient sc) {
        myClient = sc;
    }

    public void setEmployee(SEmployee se) {
        myEmployee = se;
    }

    /**
     * Responsible for reconciling or unreconciling shifts...G...
     */
    public void reconcile(boolean reconcile) {
        if (reconcile) {
            updateTypeVal(ShiftTypeClass.SHIFT_RECONCILED);
        } else {
            updateTypeVal(ShiftTypeClass.SHIFT_UNRECONCILED);
        }
        try {
            ScheduleController scheduleController = new ScheduleController(this.parent.getConnection().myCompany);
            if (this.isMaster) {
                scheduleController.deletePermShiftForOneWeek(shift_id, this.myEmployee.getId() + "", this.myClient.getId(), Main_Window.parentOfApplication.getUser().getUserId());
                myType.updateVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
                scheduleController.createTempShift(this.myEmployee.getId() + "", this.myClient.getId(), Main_Window.parentOfApplication.getUser().getUserId(), 
                    myDatabaseDate, myTimes.startTime + "", myTimes.endTime + "", day_code, myType, schedule_id, masterShiftId, 
                    myPayOptions, myBillOptions, rate_code);
            } else {
                scheduleController.editTempShift(this.myEmployee.getId() + "", this.myClient.getId(), "0", myDatabaseDate, 
                        myTimes.startTime + "", myTimes.endTime + "", day_code + "", myType, schedule_id, masterShiftId, "0", this.shift_id, myPayOptions, myBillOptions, rate_code, Main_Window.parentOfApplication.getUser().getUserId());
            } 
        } catch (Exception exe) {}
    }

    public String getRealMasterId() {
        return masterShiftId;
    }

    /**
     * Parses shift Id to return the master id....
     */
    public String getMasterId() {
        if (shift_id.charAt(0) == '-') {
            StringTokenizer st = new StringTokenizer(shift_id, "-/");
            return st.nextToken();
        } else {
            return masterShiftId;
        }
    }

    /**
     * Allows you to control to reset old shift info or not....fixes problem with
     * new consolidate rows at day...
     */
    public void setShift(SShift s, boolean clearOldInfo) {
        if (clearOldInfo && mySShift != null) {
            mySShift.cleareInfo();
        }
        mySShift = s;
        schedule_id = mySShift.myRow.myWeek.mySched.getSortingScheduleId();
        if (s.getClient() instanceof SClient) {
            myClient = (SClient)s.getClient();
        }
        myEmployee = s.getEmployee();
        day_code = s.getDayCode();
        
        s.setBG();
        
    }

    public double getNoHoursDouble() {
        return numberHoursCalculated;
    }

    /**
     * Little format time function...just passes to StaticDateTimeFunctions....
     */
    public String convertTime(int t) {
        int format = StaticDateTimeFunctions.STANDARD_FORMAT;
        if (parent.isMilitaryTimeFormat()) {
            format = StaticDateTimeFunctions.MILITARY_FORMAT;
        }
        return StaticDateTimeFunctions.stringToFormattedTime(t + "", format);
    }

    /**
     * Returns if this Shift is conflicting with anything, returns false if
     * shiftIAmConflictingWith is null, else true
     */
    public boolean hasConflict() {
        if (getShiftIAmConflictingWith().size() == 0) {
            return false;
        } else {
            if (myType.isShiftType(ShiftTypeClass.SHIFT_OVERRIDE_CONFLICT)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /**
     * Uses our RunQueriesEx object to run all queries at the same damn time...wonderful...
     * 8/9/2005 Changed to executeQueryEx, if problem change back...
     */
    public void executeAllQueuedQueries() {
        try {
            this.parent.getConnection().prepQuery(myQueryExecutor);
            this.parent.getConnection().executeQueryEx(myQueryExecutor);
            myQueryExecutor.clear();
        } catch (Exception e) {
        }
    }

    public RunQueriesEx getQueryEx() {
        return myQueryExecutor;
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

    /**
     * Function to Compare two Dshifts to see if they have the same data...
     */
    public boolean equals(DShift o) {
        if (shift_id.compareTo(o.shift_id) == 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DShift) {
            DShift shiftComp = (DShift) o;
            if (shift_id.compareTo(shiftComp.shift_id) == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * This is the faster compare used on initial load only compares ints start and end time
     */
    private int fastCompareOnLoad(DShift o) {
        int mynewend, onewend;
        mynewend = getEndTime();
        onewend = o.getEndTime();
        if (mynewend < getStartTime()) {
            mynewend += 2400;
        }
        if (onewend < o.getStartTime()) {
            onewend += 2400;
        }
        if (mynewend > onewend) {
            return 1;
        } else if (mynewend < onewend) {
            return -1;
        } else {
            return 0;
        }
    }

    /**
     * Conflict Checking used by SEmployee, also used to flag shifts that are conflicted, used
     * by both Conflict checking and for availability....flag indicates if the shift should be 
     * graphically altered to reflect a conflict, false for availability...
     */
    public int checkThisShiftForConflicts(SourceOfConflict o, boolean flagShift, int buffer) {
        try {
            if (this.isDeleted || o.isDeleted()) {
                return 1;
            }
            Calendar startTime = o.getStartCal();
            Calendar endTime = o.getEndCal();

            if (startTime == null || endTime == null) {
                setShiftConflictingWith(o);
                o.setShiftConflictingWith(this);
                return 0;
            } else {
                Calendar bufferedStart = Calendar.getInstance();
                bufferedStart.setTime(startTime.getTime());
                bufferedStart.add(Calendar.MINUTE, -buffer);

                Calendar bufferedEnd = Calendar.getInstance();
                bufferedEnd.setTime(endTime.getTime());
                bufferedEnd.add(Calendar.MINUTE, buffer);

                Calendar myStartTime = this.getStartCal();
                Calendar myEndTime = this.getEndCal();

                myStartTime.getTime();
                myEndTime.getTime();
                
                if (myStartTime.before(bufferedStart) && (myEndTime.before(bufferedStart) || myEndTime.equals(bufferedStart))) {
                    return -1;
                } else if ((myStartTime.after(bufferedEnd) || myStartTime.equals(bufferedEnd)) && myEndTime.after(bufferedEnd)) {
                    return 1;
                } else {
                    if (o.shouldCauseConflict() && shouldCauseConflict() && flagShift && !o.isDeleted() && !isDeleted() && o.getAvailType() != Main_Window.AVAILABLE && getAvailType() != Main_Window.AVAILABLE) {
                        setShiftConflictingWith(o);
                        o.setShiftConflictingWith(this);
                    }
                    return 0;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int compareTo(Object o1) {
        DShift o = (DShift) o1;
        return fastCompareOnLoad(o);
    }

    public String getDateString() {
        return parent.getRegCalendarByDate(this.myDatabaseDate);
    }

    /**
     * Should be called from cleareShift in SShift to ensure that all totals are calculated properly do
     * not call from anywhere else since it really does not set the start and end time...
     */
    public void zeroTimes() {
        if (mySShift != null) {
            mySShift.myRow.myWeek.addTotal(-numberHoursCalculated);
            myClient.addTotal(this, -numberHoursCalculated);
        }
    }

    /**
     * Should be called from buildSShift to ensure that new week gets right total do
     * not call from anywhere else since it really does not set the start and end time...
     */
    public void recalTimes() {
        mySShift.myRow.myWeek.addTotal(numberHoursCalculated);
        myClient.addTotal(this, numberHoursCalculated);
    }

    /**
     * Small method that is run whenever a start or end time is changed, this is so they
     * dont' have to be calculated all the time, rather only when the values are changed...
     */
    private void calculateNoHours() {
        double breakMinutes = 0;
        try {
            if (this.getPayBreakLength() > 0) {
                breakMinutes = this.getPayBreakLength();
            }
        } catch (Exception exe) {}
        double newNumberOfHours =
                StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(myTimes.getStartVal(), myTimes.getEndVal(), Integer.parseInt(parent.getConnection().myCompany), breakMinutes);
        if (mySShift != null) {
            mySShift.myRow.myWeek.addTotal(newNumberOfHours - numberHoursCalculated);
            myClient.addTotal(this, newNumberOfHours - numberHoursCalculated);
            myEmployee.changeTimesForShift(this, numberHoursCalculated, newNumberOfHours);
        }
        int format = StaticDateTimeFunctions.STANDARD_FORMAT;
        if (parent.isMilitaryTimeFormat()) {
            format = StaticDateTimeFunctions.MILITARY_FORMAT;
        }
        String[] retS = StaticDateTimeFunctions.stringToScheduleTime(getStartTime(), getEndTime(), format, (day_code));
        myEndTimeFormatted = retS[1];
        myStartTimeFormatted = retS[0];
        
        numberHoursCalculated = newNumberOfHours;
    }

    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        try {
            sql.append("Shift ID: ").append(this.shift_id).append("\r\n");
            sql.append("Schedule ID: ").append(this.schedule_id).append("\r\n");
            sql.append("Date: ").append(this.date).append("\r\n");
            sql.append("Start Time: ").append(this.myStartTimeFormatted).append("\r\n");
            sql.append("End Time: ").append(this.myEndTimeFormatted).append("\r\n");
        } catch (Exception e) {
        }
        return sql.toString();
    }

    /**
     * Gets our start time yay, in military or whatever format
     */
    public String getFormattedStartTime() {
        return myStartTimeFormatted;
    }

    /**
     * Gets our end time yay, in military or whatever
     */
    public String getFormattedEndTime() {
        return myEndTimeFormatted;
    }

    /**
     * Does this dshift equal the source of conflict used to remove sources of conflict...
     */
    public boolean equals(SourceOfConflict compareMe) {
        if (compareMe.getMyType() == SourceOfConflict.DSHIFT) {
            if (compareMe.getShiftId().equals(getShiftId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * inhearited from SourceOfConflict so we can easily see if avail type = 0...AVAIL
     */
    public int getAvailType() {
        return 1;
    }

    public boolean shouldCauseConflict() {
        try {
            //if (myType.isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)
            //        || myType.isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
            //    return false;
            //}
        } catch (Exception e) {
        }
        return true;
    }

    /**
     * @return the shiftIAmConflictingWith
     */
    public Hashtable<String, SourceOfConflict> getShiftIAmConflictingWith() {
        return shiftIAmConflictingWith;
    }

    /**
     * @param shiftIAmConflictingWith the shiftIAmConflictingWith to set
     */
    public void setShiftIAmConflictingWith(Hashtable<String, SourceOfConflict> shiftIAmConflictingWith) {
        this.shiftIAmConflictingWith = shiftIAmConflictingWith;
    }

    public int getOffset() {
        return this.mySShift.getOffset();
    }

    public SShift getShift() {
        return mySShift;
    }

    public void setShift(SShift shift) {
        mySShift = shift;
    }

    public boolean isMaster() {
        return isMaster;
    }

    public Schedule_View_Panel getParent() {
        return parent;
    }

    public String getLastUpdateStr() {
        return lu;
    }

    public boolean isBlank() {
        return isBlank;
    }

    public boolean isOverwrittingBuffer() {
        return isOverwrittingBuffer;
    }

    /**
     * small class to hold start and end times, ensures that total hours is calculated
     * whenever these are changed.... small speed increase
     */
    private class start_end_time {

        int startTime;
        int endTime;

        public start_end_time() {
            startTime = 0;
            endTime = 0;
        }

        public void setVal(int stime, int etime) {
            if (stime == 0) {
                stime = 1440;
            }
            if (etime == 0) {
                etime = 1440;
            }
            startTime = stime;
            endTime = etime;
            calculateNoHours();
        }

        public int getStartVal() {
            return startTime;
        }

        public int getEndVal() {
            return endTime;
        }
    }
}
