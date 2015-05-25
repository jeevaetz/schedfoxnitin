/*
 * QueryGenerateShift.java
 *
 * Created on November 11, 2005, 10:07 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.schedule.components;
import schedfoxlib.model.ShiftOptionsClass;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.ShiftTypeClass;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.training.*;
/**
 *
 * @author Ira Juneau
 * This class is used to generate all queries pertaining to the generation, modification, 
 * moving or editing of a shift. IT SHOULD NEVER TIE INTO ANYTHING GRAPHICAL! this allows us
 * to decouple query generation from the visual aspects of the schedule. Therefore all shift
 * adding, editing, modification and deletion passes through the one entry point of our heartbeat
 * rather than both local and remote sources...
 */
public class QueryGenerateShift {
    
    public String   clientId;
    public String   employeeId;
    public String   scheduleId;
    public String   shiftId;
    public String   masterShiftId;
    public int      dayOfWeek;
    public String   dayOfYear;
    public String   masterEndDate;
    public String   masterStartDate;
    public boolean  isDeleted;
    public String   lu;
    public String   trainer;
    public String   trainerId;
    public String   startT;
    public String   endT;
    public int weeklyRotation;
    
    public int                  rate_code;
    public ShiftTypeClass       myType;
    public ShiftOptionsClass    myPayOptions;
    public ShiftOptionsClass    myBillOptions;
    
    private RunQueriesEx myCompleteQueryForShift;
    
    /** 
     * Creates a new instance of QueryGenerateShift
     * This constructor is used to grab shift characteristics as a start point so 
     * we wont have to set each one individually. Could also use other controls 
     * later possible the checkin needs a constructor...
     */
    public QueryGenerateShift(DShift shiftToCopy) {
        String cid = shiftToCopy.getClient().getId();
        String eid = shiftToCopy.getEmployee().getId() + "";
        String schedId = "0";
        weeklyRotation = shiftToCopy.getWeeklyRotation();
        try {
            schedId = shiftToCopy.mySShift.myRow.myWeek.mySched.getInsertScheduleId();
        } catch (Exception e) {}
        String sid = shiftToCopy.getShiftId();
        String smid = shiftToCopy.getMasterId();
        int dow = shiftToCopy.getDayCode();
        String doy = shiftToCopy.getDatabaseDate();
        String endD = shiftToCopy.getEndDate();
        String startD = shiftToCopy.getStartDate();
        boolean del = shiftToCopy.isDeleted();
        String LU = shiftToCopy.lu;
        String train = "";
        //String trainId = shiftToCopy
        int rate = 0;
        ShiftTypeClass tempType = null;
        ShiftOptionsClass myPay = null;
        ShiftOptionsClass myBill = null;
        String start = shiftToCopy.getStartTime() + "";
        String end = shiftToCopy.getEndTime() + "";
        //this.weeklyRotation = shiftToCopy.
        try {
            rate = shiftToCopy.getRateCode();
            tempType = shiftToCopy.getType();
            myPay = shiftToCopy.myPayOptions;
            myBill = shiftToCopy.myBillOptions;
        } catch (Exception e) {}
        instantiateAllFields(eid, cid, schedId, sid, smid, dow, doy, endD, startD, del, LU, train, "", rate, tempType, myPay, myBill, start, end);
    }
    
    public QueryGenerateShift(String...fields) {
        
    }
    
    /**
     * Should be called by all constructors to set appropriate fields....
     */
    private void instantiateAllFields(String eid, String cid, String schedid, String sid, String smid, int dow, String doy, String endDate, 
            String startDate, boolean deleted, String lastU, String trainerN, String trainerID, int rate, ShiftTypeClass newType, 
            ShiftOptionsClass myPay, ShiftOptionsClass myBill, String start, String end) {
        
        myCompleteQueryForShift = new RunQueriesEx();
        clientId = cid;
        employeeId = eid;
        scheduleId = sid;
        shiftId = sid;
        masterShiftId = smid;
        dayOfWeek = dow;
        dayOfYear = doy;
        masterEndDate = endDate;
        masterStartDate = startDate;
        isDeleted = deleted;
        lu = lastU;
        trainer = trainerN;
        trainerId = trainerID;
        rate_code = rate;
        myType = newType;
        myPayOptions = myPay;
        myBillOptions = myBill;
        startT = start;
        endT = end;
    }
    
    /**
     * Small method used to ensure that whenever a shift is modified it will no longer
     * be flagged to not display a conflict, user must set shift to not display conflict
     * every time they modify shift in any way!
     */
    private void setShiftTypeToNonOverrideConflict() {
        try {
            myType.updateVal(ShiftTypeClass.SHIFT_NOTOVERRIDDEN_CONFLICT);
        } catch (Exception e) {}
    }
    
    /**
     * Used to generate query to move a Shift, give id of new Client, id of new Employee, id of new Group, and 
     * whether or not this is a permanent change.
     */
    public void moveShift(String newClient, String newEmployee, String newScheduleId, boolean permanentChange) {
        setShiftTypeToNonOverrideConflict();
        if (permanentChange) {
            if (amIMasterShift()) {
                this.
                move_Master_Shift(newEmployee, newClient, newScheduleId);
            } else {
                move_Temp_Shift(newEmployee, newClient, newScheduleId);
            }
        } else {
            if (amIMasterShift()) {
                deleteMasterShiftForOneWeek();
                employeeId = newEmployee;
                clientId = newClient;
                scheduleId = newScheduleId;
                myType.updateVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
                createTempShift();
            } else {
                move_Temp_Shift(newEmployee, newClient, newScheduleId);
            }
        }
    }
    
    /**
     * Public call to create new shift with info provided....
     */
    public void createShift(boolean permanent) {
        if (permanent) {
            createMasterShift();
        } else {
            myType.updateVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
            createTempShift();
        }
    }
    
    /**
     * Is this a permanent delete or not?
     */
    public void deleteShift(boolean permanentChange) {
        if (permanentChange) {
            if (amIMasterShift()) {
                deleteMasterPerm();
            } else {
                deleteTempShift();
            }
        } else {
            if (amIMasterShift()) {
                deleteMasterShiftForOneWeek();
            } else {
                deleteTempShift();
            }
        }
    }
    
    /**
     * This method only updates the last_updated field for a given shift...very useful for things such
     * as notes...
     */
    public void updateShiftTimeUpdated() {
        ScheduleController schedController = new ScheduleController("");
        if (amIMasterShift()) {
            myCompleteQueryForShift.add(schedController.updateMasterShiftTime(shiftId));
        } else {
            myCompleteQueryForShift.add(schedController.getTempShiftTimeUpdateQuery(shiftId));
        }
    }
    
    /**
     * Helper method to flag whether or not we need to reset the Conflict override
     * on the shift...
     */
    public void editShift(boolean permanentChange, boolean shouldResetConflict) {
        if (shouldResetConflict) {
            setShiftTypeToNonOverrideConflict();
        }
        if (permanentChange) {
            if (amIMasterShift()) {
                edit_Master_Shift();
            } else {
                this.deleteTempShift();
                this.createMasterShift();
                //edit_Temp_Shift();
            }
        } else {
            if (amIMasterShift()) {
                deleteMasterShiftForOneWeek();
                myType.updateVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
                createTempShift();
            } else {
                edit_Temp_Shift();
            }
        }
    }
    
    public void  saveTrainingInformation() {
        try {
            if (!trainerId.equals("0") && trainerId.trim().length() > 0) {
                save_training_information myTrainSave = new save_training_information();
                myTrainSave.update(employeeId, clientId, shiftId, trainerId);
                myCompleteQueryForShift.add(myTrainSave);
            }
        } catch (Exception e) {}
    }
    
    /**
     * Ok yeah no parameters here I know, its because its actually easier if you set them your self rather than making you
     * pass in whatever parameters like a Jackass....
     */
    public void editShift(boolean permanentChange) {
        editShift(permanentChange, true);
    }
    
    /**
     * Method used to delete this Master Shift for only one week. Just overwrites 
     * it with a blank shift...
     */
    private void deleteMasterShiftForOneWeek() {
        ScheduleController scheduleController = new ScheduleController("");
        myCompleteQueryForShift.add(scheduleController.getDeleteMasterShiftForOneWeek(shiftId, employeeId, clientId, ""));
    }
    
    /**
     * Method used to delete a Master Shift From this day on...fucka
     */
    private void deleteMasterPerm() {
        schedule_master_delete_query myMasterDeleteQuery = new schedule_master_delete_query();
        myMasterDeleteQuery.update(shiftId);
        myCompleteQueryForShift.add(myMasterDeleteQuery);
    }
    
    /**
     * Method used to delete a temporary shift
     */
    private void deleteTempShift() {
        delete_temporary_schedule_query myTempDeleteQuery = new delete_temporary_schedule_query();
        myTempDeleteQuery.update(shiftId);
        myCompleteQueryForShift.add(myTempDeleteQuery);
    }
    
    /**
     * Create a temp shift from the info given...
     */
    private void createTempShift() {
        ScheduleController scheduleController = new ScheduleController("");
        myCompleteQueryForShift.add(scheduleController.getTempShiftQuery(employeeId, clientId, "", dayOfYear, startT, endT, dayOfWeek, myType, scheduleId, masterShiftId, myPayOptions, myBillOptions, rate_code));
    }
    
    /**
     * Create a master shift from the info given...
     */
    private void createMasterShift() {
        create_master_shift_query newShiftQuery = new create_master_shift_query();
        myType.updateVal(ShiftTypeClass.SHIFT_CONFIRMED);
        newShiftQuery.update(clientId, employeeId, dayOfWeek + "", startT, endT, scheduleId,
                "0", dayOfYear, myPayOptions.toString(), myBillOptions.toString(), rate_code, myType.toString(), this.weeklyRotation);
        myCompleteQueryForShift.add(newShiftQuery);
    }
    
    /**
     * Create a shift from the info given...
     */
    private void createTemporaryShift() {
        create_temporary_shift_query myCreateTempQuery = new create_temporary_shift_query();
        myCreateTempQuery.update(employeeId, clientId, "0", dayOfYear, startT, endT ,dayOfWeek + "",myType.toString(), 
                scheduleId, masterShiftId, "0", masterShiftId, myPayOptions.toString(), myBillOptions.toString(), rate_code);
        myCompleteQueryForShift.add(myCreateTempQuery);
    }
    
    /**
     * Kinda obvious but it moves a master shift...Does this by running a query that
     * terminates the old master, then moves it onto a new employee...groovy...need to pass
     * it the old SShift so it can ascertain the id of the master shift to term...
     */
    private void move_Master_Shift(String newEmployee, String newClient, String newScheduleId) {
        schedule_master_move_query myMoveQuery = new schedule_master_move_query();
        myMoveQuery.update(shiftId, employeeId, newEmployee, clientId, newClient, scheduleId, newScheduleId);
        myCompleteQueryForShift.add(myMoveQuery);
    }
    
    /**
     * Moves a temporary shift to given client, employee and group fun fun..
     * it is necessary for right now, kinda double work but shouldnt take
     * long
     */
    private void move_Temp_Shift(String newEmployee, String newClient, String newScheduleId) {
        schedule_move_query myMoveQuery = new schedule_move_query();
        if (myType.isShiftType(ShiftTypeClass.SHIFT_VIEWED_BY_EMPLOYEE)) {
            myType.setVal(ShiftTypeClass.SHIFT_UNCONFIRMED);
        }
        myMoveQuery.update(shiftId, newEmployee, newClient, newScheduleId, myType.toString());
        myCompleteQueryForShift.add(myMoveQuery);
        
    }
    
    /**
     * Edits a Master Shift from shift day on...
     */
    private void edit_Master_Shift() {
        edit_master_shift_query myEditQuery = new edit_master_shift_query();
        myEditQuery.update(shiftId, clientId, employeeId, dayOfWeek + "", startT, endT, scheduleId, masterShiftId,
                dayOfYear, myPayOptions.toString(), myBillOptions.toString(), myType.toString(), rate_code,
                weeklyRotation);
        myCompleteQueryForShift.add(myEditQuery);
    }
    
    /**
     * Edits an existing temp shift....
     */
    private void edit_Temp_Shift() {
        ScheduleController scheduleController = new ScheduleController("");
        myCompleteQueryForShift.add(scheduleController.getEditTempShiftQuery(employeeId, clientId, "0", dayOfYear,
                startT, endT, dayOfWeek + "", myType, scheduleId, masterShiftId, "0", shiftId, 
                myPayOptions, myBillOptions, rate_code));
        saveTrainingInformation();
    }
    
    /**
     * Returns the query for this shift!
     */
    public RunQueriesEx getMyQuery() {
        return myCompleteQueryForShift;
    }
    
    /**
     * Removes any queries in our query list for this shift...
     */
    public void clearQueuedQueries() {
        myCompleteQueryForShift = new RunQueriesEx();
    }
    
    /**
     * Reads shift id to indicate whether or not this is a master shift....
     * Negative first integer indicates a master shift...
     */
    private boolean amIMasterShift() {
        try {
            if (this.shiftId.charAt(0) == '-') {
                return true;
            }
        } catch (Exception e) {
        
        }
        return false;
    }
    
}
