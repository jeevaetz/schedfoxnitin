/*
 * SEmployee.java
 *
 * Created on April 13, 2004, 2:51 PM
 */
/**
 *
 */
package rmischedule.schedule.components;

import schedfoxlib.model.ShiftTypeClass;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.training.*;
import java.util.*;
import javax.swing.SwingUtilities;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.main.Main_Window;
import schedfoxlib.model.CalcedLocationDistance;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;

/**
 *
 * @author jason.allen
 */
public class SEmployee implements Comparable {

    /**
     * Below are static ints used to declare what we want to sort our list by
     * passed into the function setComparatorValue().
     */
    public static final int SORT_BY_NAME = 1;
    public static final int SORT_BY_OVERTIME = 2;
    public static final int SORT_BY_AVAILABILITY = 3;
    public static final int SORT_BY_TRAINING = 5;
    public static final int DISPLAY_ALL = 6;
    public static final int SORT_BY_DISTANCE = 7;
    public static final int SORT_BY_OVERTIME_ON_WEEK = 100;
    public static int ComparatorValue;
    public static int WeekToCompareOverTimes = -1;
    private Employee employee;
    private EmployeeTrainingClass myTimeVector;
    /**
     * Data pieces added for messaging
     */
    private String message;
    private String subject;
    /**
     * additions by Jeffrey Davis on 07/26/2010 complete
     */
    private String types;
    private Hashtable<String, CertificationClass> employeeCerts;
    private int lastShift;
    private int lastClient;
    //Added by Ira Juneau for total Hours worked by Employee
    private Vector<DShift> ShiftsWorkedByEmployee;
    private Vector<DAvailability> AvailabilitiesForEmployee;
    private Hashtable<String, DAvailability> HashOfAvailabilities;
    private int SizeOfShiftsTotalsByWeek = 52;
    private double[] ShiftsTotalsByWeek;
    private ArrayList<String> clients;
    private boolean allow_sms_messaging = false;
    private boolean isEmpArmed;
    public boolean hasOverTime;
    public boolean hasConflict;
    public boolean hasBufferOverwrite;
    private AEmployee myAEmp;
    public SEmployee employeeJustDroppedOn;
    private boolean hasNote;
    /*
     * This is for when we insert, should be inserted afterward.
     */
    public SEmployee afterEmp;
    private Schedule_View_Panel myParent;
    /*
     *  removed lastScheduleId and placed it with the client, will be
     *  better there, less buggy with exsisting code.
     */
    /**
     * Show we show this employee, just due to availability
     */
    private boolean showBecauseOfAvailability;
    private boolean showBecauseOfBanning;
    private boolean showBecauseOfClientTraining;
    private boolean showBecauseOfClientList;
    private boolean showBecauseOfEmployeeType;
    private boolean showBecauseOfCertifications;
    private boolean showBecauseOfDaysWorked;
    private boolean showBecauseOfPartTimeHours;
    
    private ArrayList<CalcedLocationDistance> locationDistance;

    /* This is used for blank retention (linking) */
    public SEmployee(
            Employee employee, ArrayList<CalcedLocationDistance> locationDistance,
            Schedule_View_Panel parent, String hasNote, String types, ArrayList<String> clients) {

        this.employee = employee;

        this.locationDistance = locationDistance;

        showBecauseOfAvailability = true;
        showBecauseOfBanning = true;
        showBecauseOfClientTraining = true;
        showBecauseOfEmployeeType = true;
        showBecauseOfCertifications = true;
        showBecauseOfDaysWorked = true;
        showBecauseOfClientList = true;
        showBecauseOfPartTimeHours = true;

        this.types = types;
        this.clients = clients;

        if (hasNote.equals("f") || hasNote.equals("false")) {
            this.hasNote = false;
        } else {
            this.hasNote = true;
        }

        employeeCerts = new Hashtable(30);
        ShiftsWorkedByEmployee = new Vector(60);
        AvailabilitiesForEmployee = new Vector(60);
        HashOfAvailabilities = new Hashtable(60);
        myTimeVector = new EmployeeTrainingClass();
        ShiftsTotalsByWeek = new double[SizeOfShiftsTotalsByWeek];
        ComparatorValue = SORT_BY_NAME;
        hasOverTime = false;
        hasConflict = false;
        hasBufferOverwrite = false;
        employeeJustDroppedOn = this;
        myParent = parent;
    }

    /*
     *  Additions by Jeffrey Davis for copy constructor complete
     */
    public SEmployee(Schedule_View_Panel Parent) {
        this(new Employee(new Date()), null, Parent, "f", "", null);
        employeeJustDroppedOn = this;
    }

    /**
     * Very important constructor! Used to return schedules for client...
     */
    public SEmployee(String id) {
        showBecauseOfAvailability = true;
        showBecauseOfBanning = true;
        showBecauseOfClientTraining = true;

        this.employee = new Employee(new Date());
        this.employee.setEmployeeId(Integer.parseInt(id));
    }

    public boolean isInvisible() {
        try {
            return this.employee.getMarkInvisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * These dispose methods are so very important... We have a massive memory
     * leak since it appears java's GC cannot handle all of our bidirection
     * references... Therefore it is very very important as you add classes that
     * you properly dispose all new private class, and remove all objects from
     * sub panels. Please verify from time to time in HEAP stack that this is
     * still working.
     */
    public void dispose() {
        myTimeVector = null;

        ShiftsWorkedByEmployee = null;
        AvailabilitiesForEmployee = null;
        HashOfAvailabilities = null;
        clients = null;
        myAEmp = null;
        employeeJustDroppedOn = null;
        afterEmp = null;
        myParent = null;
    }

    public boolean hasNote() {
        return hasNote;
    }

    public AEmployee getAEmp() {
        return myAEmp;
    }

    public String getEmpTypes() {
        return this.types;
    }

    public void setVisible(boolean val) {
        try {
            myAEmp.setVisible(val);
        } catch (Exception e) {
        }
    }

    public boolean isVisible() {
        try {
            return myAEmp.isVisible();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Method used for sorting, what this does is set the Employee that was just
     * dropped on the shift so that we know what to sort by...look at compareTo
     * of SSchedule for further explanation of how this is used....
     */
    public void setEmpDroppedOn(SEmployee se) {
        employeeJustDroppedOn = se;
    }

    public String getName() {
        //This if statement is odd, however it's used to get subsets of employees.
        if (employee.getEmployeeId() == null || employee.getEmployeeId() == -1) {
            return "             ";
        } else if (employee.getEmployeeId() != null && employee.getEmployeeId() == -2) {
            return "?????????????";
        }
        return employee.getEmployeeFullNameReversed();
    }

    public int getId() {
        try {
            return employee.getEmployeeId();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public String getPhone() {
        return employee.getEmployeePhone();
    }

    public String getPhone2() {
        return employee.getEmployeePhone2();
    }

    public String getCell() {
        return employee.getEmployeeCell();
    }

    public String getPager() {
        return employee.getEmployeePager();
    }

    public String getAddress() {
        return employee.getEmployeeAddress();
    }

    public String getAddress2() {
        return employee.getEmployeeAddress2();
    }

    public String getCity() {
        return employee.getEmployeeCity();
    }

    public String getState() {
        return employee.getEmployeeState();
    }

    public String getZip() {
        return employee.getEmployeeZip();
    }

    public boolean isDeleted() {
        return (employee.getEmployeeIsDeleted() == 1);
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return employee.getEmployeeEmail();
    }

    public int getLastShift() {
        return lastShift;
    }

    public int getLastClient() {
        return lastClient;
    }

    public String getReadableTerm() {
        return employee.getEmployeeTermDateStr();
    }

    public String getReadableHire() {
        return employee.getEmployeeHireDateStr();
    }

    public Date getTerm() {
        return employee.getEmployeeTermDate();
    }

    public Date getHire() {
        return employee.getEmployeeHireDate();
    }

    public void clearCertifications() {
        employeeCerts.clear();
    }

    /**
     * Add a certification for our employee....yippy...
     */
    public void addCertification(CertificationClass cert) {
        employeeCerts.put(cert.getId(), cert);
    }

    /**
     * Returns Hashtable of certifications...
     */
    public Hashtable getCertifications() {
        return employeeCerts;
    }

    public Vector getShifts() {
        return this.ShiftsWorkedByEmployee;
    }

    public boolean hasOvertimeForCurrentWeek() {
        try {
            if (getHoursWorkedForWeek(myParent.getCurrentSelectedWeek()) > 40.0) {
                return true;
            }
        } catch (Exception e) {
            return hasOverTime;
        }
        return false;
    }

    /**
     * This returns the Hours Worked by the current employee, for all weeks used
     * by availability to sort employees...
     */
    public double getHoursWorked() {
        double returnVal = 0.0;
        for (int i = 0; i < SizeOfShiftsTotalsByWeek; i++) {
            returnVal = returnVal + ShiftsTotalsByWeek[i];
        }
        return returnVal;
    }

    /**
     * Returns hours worked for particular week for current employee
     */
    public double getHoursWorkedForWeek(int week) {
        return ShiftsTotalsByWeek[week];
    }

    /**
     * This Function is used to remove a DShift needs to be called whenever a
     * shift is taken from an employee...
     */
    public void removeShift(UnitToDisplay sp) {
        ShiftsWorkedByEmployee.remove(sp);
        ShiftsTotalsByWeek[sp.getWeekNo()] = ShiftsTotalsByWeek[sp.getWeekNo()] - sp.getNoHoursDouble();
    }

    /**
     * This Function should be called when the hours have changed, via the
     * DShift method that runs when hours are changed for shift, it is very
     * fast... since it must be called fairly often
     */
    public void changeTimesForShift(DShift sp, double oldTotal, double newTotal) {
        ShiftsTotalsByWeek[sp.getWeekIdNo()] = ShiftsTotalsByWeek[sp.getWeekIdNo()] + (newTotal - oldTotal);
    }

    /**
     * Is Employee Trained for given Client...
     */
    public boolean isTrained(SMainComponent sc) {
        if (employee.getEmployeeId() == null) {
            return true;
        }
        if (sc instanceof SClient) {
            return myTimeVector.isTrained((SClient) sc, sc.getTrainingTime());
        } else {
            return false;
        }
    }

    /**
     * Checks is employee is trained by the selected date...
     */
    public boolean isTrainedForShift(SMainComponent sc, String date) {
        if (sc.getTrainingTime() == 0.0 || isTrained(sc)) {
            return true;
        }

        Calendar shiftDate = StaticDateTimeFunctions.setCalendarToString(date);
        double timeSoFar;
        try {
            timeSoFar = ((EmployeeTrainingClass.TrainingInfo) (myTimeVector.get(sc.getId()))).getTime();
        } catch (Exception e) {
            timeSoFar = 0.0;
        }

        for (DShift shift : ShiftsWorkedByEmployee) {
            Calendar trainingDate = StaticDateTimeFunctions.setCalendarToString(shift.getDatabaseDate());
            //Kind of an ugly if statement... if it's the correct client, and if it's a training shift before
            //the shift to check, or if it's a shift in the past, add up the hours
            boolean sameClient = shift.getClient().getId().equals(sc.getId());
            boolean isTrainingShift = shift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT);
            boolean trainingIsBeforeShift = trainingDate.compareTo(shiftDate) <= 0;
            boolean isPastShift = shift.mySShift.isPast();
            if (sameClient && ((isTrainingShift && trainingIsBeforeShift) || isPastShift)) {
                timeSoFar += shift.getNoHoursDouble();
            }
        }

        if (timeSoFar < (sc.getTrainingTime() / 60)) {
            return false;
        }
        return true;
    }

    /**
     * Checks this employee for Conflicts using our marvelous shift merge sort
     * and the compareTo function in the DShift
     */
    public void checkConflicts() {
        if (this.getId() == 0) {
            return;
        }

        //Do not change this to use iteratores, they are not thread safe!
        for (int s = 0; s < ShiftsWorkedByEmployee.size(); s++) {
            ShiftsWorkedByEmployee.get(s).setShiftConflictingWith(null);
        }

        int hoursForConflict = Main_Window.parentOfApplication.getNumberOfHoursBetweenShifts();
        for (int s = 0; s < ShiftsWorkedByEmployee.size(); s++) {
            DShift shift = ShiftsWorkedByEmployee.get(s);

            for (int is = 0; is < ShiftsWorkedByEmployee.size(); is++) {
                DShift conflict = ShiftsWorkedByEmployee.get(is);
                if (conflict != shift) {
                    shift.checkThisShiftForConflicts(conflict, true, hoursForConflict);
                }
            }

            for (DAvailability avail : AvailabilitiesForEmployee) {
                shift.checkThisShiftForConflicts(avail, true, 0);
            }

            if (!shift.mySShift.isPast()
                    && !shift.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)
                    && !isTrainedForShift(shift.getClient(), shift.getDatabaseDate())) {

                if (myTimeVector.get(shift.getClient().getId()) == null) {
                    if (shift.getClient() instanceof SClient) {
                        myTimeVector.addTrainingShift(0.0, (SClient) shift.getClient());
                    }
                }
                shift.setShiftConflictingWith(((EmployeeTrainingClass.TrainingInfo) (myTimeVector.get(shift.getClient().getId()))));
            }

            if (shift.getClient() instanceof SClient) {
                if (this.myParent.isClientEmpBanned((SClient) shift.getClient(), this) && !shift.mySShift.isPast()) {
                    shift.setShiftConflictingWith(new BannedConflict());
                }
            }
        }
    }

    /**
     * Check if this employee is available for the given Shift...
     */
    public boolean isAvailable(DShift shiftToCheck) {
        int hoursToBuffer = Main_Window.parentOfApplication.getNumberOfHoursBetweenShifts();
        for (int i = 0; i < ShiftsWorkedByEmployee.size(); i++) {
            if (ShiftsWorkedByEmployee.get(i).checkThisShiftForConflicts(shiftToCheck, false, hoursToBuffer) == 0) {
                return false;
            }
        }
        for (int i = 0; i < AvailabilitiesForEmployee.size(); i++) {
            if (!AvailabilitiesForEmployee.get(i).isDeleted()) {
                try {
                    if (shiftToCheck.checkThisShiftForConflicts(AvailabilitiesForEmployee.get(i), false, 0) == 0) {
                        return false;
                    }
                } catch (Exception e) {
                }
            }
        }
        return true;
    }

    public Vector<DShift> getShiftsWorkedByEmployee() {
        if (this.ShiftsWorkedByEmployee == null) {
            this.ShiftsWorkedByEmployee = new Vector<DShift>();
        }
        return this.ShiftsWorkedByEmployee;
    }

    public double[] getShiftTotalsByWeek() {
        if (this.ShiftsTotalsByWeek == null) {
            this.ShiftsTotalsByWeek = new double[SizeOfShiftsTotalsByWeek];
        }
        return this.ShiftsTotalsByWeek;
    }

    /**
     * This function is called whenever a DShift is created upon the employee
     * owning the DShift it adds data to our Vector which is later used by
     * getHoursWorked();
     */
    public void addShift(UnitToDisplay sp) {
        Vector<DShift> empShifts = getShiftsWorkedByEmployee();
        if (sp instanceof DShift) {
            empShifts.add((DShift) sp);
        }
        try {
            getShiftTotalsByWeek()[sp.getWeekNo()] = getShiftTotalsByWeek()[sp.getWeekNo()] + sp.getNoHoursDouble();
            if ((getShiftTotalsByWeek()[sp.getWeekNo()]) > 40.0) {
                this.hasOverTime = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates this employees information with the new information, also all
     * schedules this employee has...
     */
    public void updateEmployee(final Employee newEmp) {
        boolean termHireChanged = false;

        if (!this.getReadableHire().equals(newEmp.getEmployeeHireDateStr())
                || !this.getReadableTerm().equals(newEmp.getEmployeeTermDateStr())) {
            termHireChanged = true;
        }

        final boolean fTimerHireChanged = termHireChanged;

        //Employee has become either deleted or undeleted.
        if (employee.getEmployeeIsDeleted() != newEmp.getEmployeeIsDeleted()) {
            this.myParent.getAvail().refreshEmployeesList(myParent.getEmployeeList());
            termHireChanged = true;
        }
        final Vector<SSchedule> mySchedules = myParent.mySchedules.getEmployeeSchedules(this);
        this.employee = newEmp;
        final SEmployee thisObj = this;

        Runnable updateEmployeeRunnable = new Runnable() {
            public void run() {
                if (newEmp.getEmployeeIsDeleted() == 1 || newEmp.getMarkInvisible()) {
                    if (myAEmp != null) {
                        myAEmp.setVisible(false);
                    }
                } else {
                    if (myAEmp != null && !newEmp.getMarkInvisible()) {
                        myAEmp.setVisible(true);
                    }
                }

                if (mySchedules != null) {
                    for (int s = 0; s < mySchedules.size(); s++) {
                        mySchedules.get(s).adjustHeader();
                    }
                }

                if (fTimerHireChanged) {
                    setBGForAllShifts();

                    Vector<SSchedule> schedules = myParent.getEmployeeSchedules(thisObj);
                    if (schedules != null) {
                        for (int s = 0; s < schedules.size(); s++) {
                            for (int w = 0; w < schedules.get(s).getWeeks().length; w++) {
                                SWeek currentWeek = schedules.get(s).getWeeks()[w];
                                for (int r = 0; r < currentWeek.getRowSize(); r++) {
                                    SRow currentRow = currentWeek.getRow(r);
                                    for (int d = 0; d < 7; d++) {
                                        currentRow.getSShift(d).setBG();
                                        currentRow.getSShift(d).repaint();
                                    }
                                }
                            }
                            schedules.get(s).revalidate();
                        }
                    }
                }
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            updateEmployeeRunnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(updateEmployeeRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Sets BG for all shifts under this employee, useful when changing term or
     * hire date so that little circles will update immediately as they
     * should...
     */
    public void setBGForAllShifts() {
        for (int day = 0; day < ShiftsWorkedByEmployee.size(); day++) {
            try {
                ShiftsWorkedByEmployee.get(day).mySShift.setBG();
                ShiftsWorkedByEmployee.get(day).mySShift.repaint();
            } catch (Exception exe) {
            }
        }
    }

    /**
     * Places availability in this Employee....fun fun
     */
    public void placeAvailability(DAvailability availToPlace) {
        if (availToPlace.getShiftId().startsWith("-") && availToPlace.isDeleted()) {
            deletePermAvail(availToPlace);
        } else {
            placeAvail(availToPlace);
        }
    }

    private void deletePermAvail(DAvailability availToDelete) {
        Calendar deletedOn = StaticDateTimeFunctions.setCalendarToString(availToDelete.getDateString());
        Vector<DAvailability> removed = new Vector();
        int i = 0;
        while (i < AvailabilitiesForEmployee.size()) {
            DAvailability avail = AvailabilitiesForEmployee.get(i);
            Calendar availDate = StaticDateTimeFunctions.setCalendarToString(avail.getDateString());
            if (availToDelete.getShiftId().equals(avail.getShiftId()) && availDate.compareTo(deletedOn) >= 0) {
                AvailabilitiesForEmployee.remove(i);
                HashOfAvailabilities.remove(avail.getDatabaseDate());
                removed.add(avail);
            } else {
                i++;
            }
        }

        DAvailability[] update = new DAvailability[removed.size()];
        update = removed.toArray(update);
        updateScheduleGraphics(update);
    }

    private void placeAvail(DAvailability availToPlace) {
        if (employee.getEmployeeId() != null) {
            for (int i = 0; i < AvailabilitiesForEmployee.size(); i++) {
                DAvailability avail = AvailabilitiesForEmployee.get(i);
                if (avail.getDateString().equals(availToPlace.getDateString())) {
                    HashOfAvailabilities.remove(avail.getDatabaseDate());
                    AvailabilitiesForEmployee.remove(i);
                    break;
                }
            }

            AvailabilitiesForEmployee.add(availToPlace);
            HashOfAvailabilities.put(availToPlace.getDatabaseDate(), availToPlace);
            updateScheduleGraphics(availToPlace);
        }
    }

    private void updateScheduleGraphics(DAvailability... avail) {
        Vector<SSchedule> mySchedules = myParent.mySchedules.getEmployeeSchedules(this);
        try {
            for (int s = 0; s < mySchedules.size(); s++) {
                SSchedule sched = mySchedules.get(s);
                for (DAvailability av : avail) {
                    SWeek CurrWeek = sched.getWeek(av.getWeekNo());
                    for (int x = 0; x < CurrWeek.getRowSize(); x++) {
                        CurrWeek.getRow(x).getShift(av.getDayCode()).setBG();
                        CurrWeek.getRow(x).getShift(av.getDayCode()).repaint();
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    /**
     * Gets avail or null for given shift...
     */
    public DAvailability getShiftsAvailability(SShift myShift) {
        DAvailability myAvail = HashOfAvailabilities.get(myParent.getDateByWeekDay(myShift.getWeekNo(), myShift.getOffset()));
        if (myShift.getEmployee() == null || myShift.getEmployee().getId() == 0) {
            return null;
        }
        if (myAvail != null) {
            if (myAvail.isDeleted() || myAvail.getAvailType() == Main_Window.AVAILABLE) {
                return null;
            }
        }
        return myAvail;
    }

    public void addTrainingInformation(Double timeTrained, SClient sc) {
        myTimeVector.addTrainingShift(timeTrained, sc);
    }

    public void removeTrainingInformation(String timeTrained, SClient sc) {
    }

    /**
     * Sets ComparatorValue, one execption to this being easy is passing in
     * sortByOverTimeOnWeek that expects a number greater than 100, ie: 108 will
     * tell it to sort by overtime on week 8 103 will tell it to sort by
     * overtime on week 3; Also of interest is how SORT_BY_OVERTIME works, it
     * will sort employees by hours total hours worked however it will place
     * anyone with any overtime for any week on the bottom of the list
     * reguardless of number of hours worked on other weeks...dunno if this is
     * best logic or not.
     */
    public static void setComparatorValue(int valueToCompare) {
        if (valueToCompare >= SORT_BY_OVERTIME_ON_WEEK) {
            ComparatorValue = SORT_BY_OVERTIME_ON_WEEK;
            WeekToCompareOverTimes = valueToCompare - SORT_BY_OVERTIME_ON_WEEK;
        } else {
            //WeekToCompareOverTimes = -1;
            ComparatorValue = valueToCompare;
        }
    }

    public int getComparatorValue() {
        return ComparatorValue;
    }

    public void setAEmployee(AEmployee emp) {
        this.myAEmp = emp;
    }

    public int getTravelDistance() {
        try {
            if (myParent.getSelectedShifts().size() == 1) {
                Client myClient = myParent.getSelectedShifts().get(0).getClient().getClientData();
                ArrayList<CalcedLocationDistance> clientDist = this.getLocationDistance();
                for (int d = 0; d < clientDist.size(); d++) {
                    if (myClient.getClientId().intValue() == clientDist.get(d).getClientId()) {
                        return clientDist.get(d).getTravelDistance();
                    }
                }
            }
        } catch (Exception exe) {
        }
        return -1;
    }

    /*
     * Compare function used because we implement Comparator so we can easily sort
     * our array later by last name, screw writing my own sort function!
     */
    public int compareTo(Object o) {
        if (ComparatorValue == SORT_BY_NAME || ComparatorValue == DISPLAY_ALL || ComparatorValue == SORT_BY_TRAINING) {
            return (getName().compareToIgnoreCase(((SEmployee) o).getName()));
        } else if (ComparatorValue == SORT_BY_OVERTIME) {
            double myVal = new Double(this.getHoursWorkedForWeek(myParent.getCurrentSelectedWeek()) - ((SEmployee) o).getHoursWorkedForWeek(myParent.getCurrentSelectedWeek()));
            if (myVal < 0) {
                return -1;
            } else if (myVal > 0) {
                return 1;
            } else {
                return 0;
            }
        } else if (ComparatorValue == SORT_BY_OVERTIME_ON_WEEK) {
            return (new Double(this.getHoursWorkedForWeek(WeekToCompareOverTimes))).compareTo(new Double(((SEmployee) o).getHoursWorkedForWeek(WeekToCompareOverTimes)));
        } else if (ComparatorValue == SORT_BY_DISTANCE) {
            SEmployee emp = (SEmployee) o;
            int myDistance = 0;
            int theirDistance = 0;
            try {
                if (myParent.getSelectedShifts().size() == 1) {
                    myDistance = getTravelDistance();
                    theirDistance = emp.getTravelDistance();
                }
            } catch (Exception exe) {
            }
            if (myDistance == -1 || myDistance == 0) {
                return 1;
            }
            if (theirDistance == -1 || theirDistance == 0) {
                return -1;
            }
            if (myDistance > theirDistance) {
                return 1;
            } else if (myDistance < theirDistance) {
                return -1;
            } else {
                return 0;
            }
        } else if (ComparatorValue == SORT_BY_AVAILABILITY) {
            if (this.hasBufferOverwrite && !((SEmployee) o).hasBufferOverwrite) {
                return 1;
            } else if (!this.hasBufferOverwrite && ((SEmployee) o).hasBufferOverwrite) {
                return -1;
            }
            return (this.getName().compareToIgnoreCase(((SEmployee) o).getName()));
        }

        return 0;
    }

    @Override
    public String toString() {
        return getName();
    }

    /**
     * @return the showBecauseOfAvailability
     */
    public boolean isShowBecauseOfAvailability() {
        return showBecauseOfAvailability;
    }

    public void checkIfShowByClientList(SShift shift) {
        boolean retVal = true;
        if (this.clients != null) {
            retVal = false;
            for (int c = 0; c < this.clients.size(); c++) {
                if (shift.myShift.getClient().getId().equals(clients.get(c))) {
                    retVal = true;
                }
            }
        }
        this.showBecauseOfClientList = retVal;
    }

    public void setShowByClientList(boolean val) {
        this.showBecauseOfClientList = val;
    }

    public boolean isShowByClientList() {
        return this.showBecauseOfClientList;
    }

    /**
     * @param showBecauseOfAvailability the showBecauseOfAvailability to set
     */
    public void setShowBecauseOfAvailability(boolean showBecauseOfAvailability) {
        this.showBecauseOfAvailability = showBecauseOfAvailability;
    }

    public void setShowBecauseOfEmployeeType(boolean showBecauseOfEmployeeType) {
        this.showBecauseOfEmployeeType = showBecauseOfEmployeeType;
    }

    public void setShowBecauseOfEmployeeCertifications(boolean showBecauseOfEmployeeCert) {
        this.showBecauseOfCertifications = showBecauseOfEmployeeCert;
    }

    public boolean isShowBecauseOfEmployeeCertifications() {
        return this.showBecauseOfCertifications;
    }

    public void setShowBecauseOfDaysWorked(boolean showBecauseOfDaysWorked) {
        this.showBecauseOfDaysWorked = showBecauseOfDaysWorked;
    }

    public boolean isShowBecauseOfDaysWorked() {
        return this.showBecauseOfDaysWorked;
    }

    public void setShowBecauseOfPartTimeHours(boolean showBecauseOfPartTimeHours) {
        this.showBecauseOfPartTimeHours = showBecauseOfPartTimeHours;
    }
    
    public boolean isShowBecauseOfPartTimeHours() {
        return this.showBecauseOfPartTimeHours;
    }
    
    public boolean checkIfEmployeeHiredWithinLastXDays(int numberDaysWorked) {
        Calendar currentDay = Calendar.getInstance();
        currentDay.add(Calendar.DAY_OF_MONTH, -numberDaysWorked);
        return this.getHire().compareTo(currentDay.getTime()) >= 0;
    }

    public boolean checkIfShouldShowBecauseOfCertifications(Vector<CertificationClass> certsToHide) {
        boolean retVal = true;
        try {
            Hashtable certs = this.getCertifications();

            if (certs != null && certs.size() > 0) {
                for (int s = 0; s < certsToHide.size(); s++) {
                    CertificationClass certClass = certsToHide.get(s);
                    if (certs.get(certClass.getId()) != null) {
                        retVal = false;
                    }
                }
            } else {
                //We have something checked, and user has no certs.
                if (certsToHide.size() != 0) {
                    retVal = false;
                }
            }
        } catch (Exception e) {
        }
        return retVal;
    }

    public boolean checkIfShouldShowBecauseOfPartTimeHours() {
        if (this.getEmployee() != null && this.getEmployee().getFullTime() != null && !this.getEmployee().getFullTime()) {
            double hoursWorked = getHoursWorkedForWeek(myParent.getCurrentSelectedWeek());
            return hoursWorked < 24.0;
        } else {
            return true;
        }
    }
    
    public boolean checkIfShouldShowBecauseOfEmployeeType(Vector<EmployeeType> typesToShow) {
        boolean retVal = true;
        try {
            String type = this.getEmpTypes();

            if (type != null && type.trim().length() > 0) {
                for (int s = 0; s < typesToShow.size(); s++) {
                    if (type.indexOf(typesToShow.get(s).getEmployeeTypeId() + "") > -1) {
                        retVal = false;
                    } else if (type.trim().length() == 0 && (typesToShow.get(s).getEmployeeTypeId() == -1)) {
                        retVal = true;
                    }
                }
            } else {
                //We have something checked, and user has no certs.
                if (typesToShow.size() != 0) {
                    retVal = false;
                }
            }
        } catch (Exception e) {
        }
        return retVal;
    }

    /**
     * @return the showBecauseOfEmployeeType
     */
    public boolean isShowBecauseOfEmployeeType() {
        return showBecauseOfEmployeeType;
    }

    /**
     * @return the showBecauseOfBanning
     */
    public boolean isShowBecauseOfBanning() {
        return showBecauseOfBanning;
    }

    /**
     * @param showBecauseOfBanning the showBecauseOfBanning to set
     */
    public void setShowBecauseOfBanning(boolean showBecauseOfBanning) {
        this.showBecauseOfBanning = showBecauseOfBanning;
    }

    /**
     * @return the showBecauseOfClientTraining
     */
    public boolean isShowBecauseOfClientTraining() {
        return showBecauseOfClientTraining;
    }

    /**
     * @param showBecauseOfClientTraining the showBecauseOfClientTraining to set
     */
    public void setShowBecauseOfClientTraining(boolean showBecauseOfClientTraining) {
        this.showBecauseOfClientTraining = showBecauseOfClientTraining;
    }

    /**
     * @return the allow_sms_messaging
     */
    public boolean isAllow_sms_messaging() {
        return allow_sms_messaging;
    }

    /**
     * @param allow_sms_messaging the allow_sms_messaging to set
     */
    public void setAllow_sms_messaging(boolean allow_sms_messaging) {
        this.allow_sms_messaging = allow_sms_messaging;
    }

    public void toggleMyAEmp(boolean b) {
        myAEmp.setVisible(b);
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public boolean equals(SEmployee objToComp) {
        try {
            return (objToComp.getId() == employee.getEmployeeId());
        } catch (Exception e) {
            return (objToComp.getName().equals(employee.getEmployeeFullName()));
        }
    }

    //  getter/setter methods for messaging, added by Jeffrey Davis on 07/26/2010
    public String getMessage() {
        return this.message;
    }

    public String getSubject() {
        return this.subject;
    }

    public void setMessage(String tempMessage) {
        this.message = tempMessage;
    }

    public void setSubject(String tempSubject) {
        this.subject = tempSubject;
    }

    /**
     * @return the locationDistance
     */
    public ArrayList<CalcedLocationDistance> getLocationDistance() {
        return locationDistance;
    }

    /**
     * @param locationDistance the locationDistance to set
     */
    public void setLocationDistance(ArrayList<CalcedLocationDistance> locationDistance) {
        this.locationDistance = locationDistance;
    }
    /**
     * getter/setters for messaging by Jeffrey Davis on 07/26/2010 complete
     */
};
