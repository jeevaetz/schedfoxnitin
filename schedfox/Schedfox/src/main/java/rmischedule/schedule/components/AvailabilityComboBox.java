/*
 * AvailabilityComboBox.java
 * Author Ira Juneau
 * Created on November 11, 2004, 12:13 PM
 */
package rmischedule.schedule.components;

import javax.swing.*;
import java.awt.event.*;
import java.util.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.util.Record_Set;
import rmischedule.data_connection.Connection;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.schedule.Schedule_Employee_Availability;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.schedule.components.availability.AvailabilityFilteringOptions;

/**
 *
 * @author ira
 */
public class AvailabilityComboBox extends JComboBox {

    private Main_Window parent;
    private String[] optionChoices = new String[3];
    public Schedule_Employee_Availability EmployeeListWindow;
    private Connection myConn;
    public Schedule_View_Panel sv;
    private mySShiftVector currentlySelectedShifts;
    private boolean ThreadLock;
    private DisplayAvailabilityThread dat;
    private int LastSortType;
    private int CurrentWeek;
    public boolean killThread;
    private SEmployee[] employees;
    private AvailabilityFilteringOptions availFilterOptions;

    /**
     * Creates a new instance of AvailabilityComboBox
     */
    public AvailabilityComboBox(Schedule_Employee_Availability sea, AvailabilityFilteringOptions availFilterOptions) {
        //parent = sea.parent().parent;
        EmployeeListWindow = sea;
        this.availFilterOptions = availFilterOptions;

        currentlySelectedShifts = new mySShiftVector();
        killThread = false;
        ThreadLock = false;
        dat = new DisplayAvailabilityThread(this);
        dat.start();
        buildOptionBox();
        addItemListener(new ItemSelectedEvent(this));
        probeForScheduleView();
        LastSortType = (SEmployee.SORT_BY_NAME);
        CurrentWeek = -1;
    }

    public void killThread() {
        killThread = true;
    }

    public int getSortType() {
        return LastSortType;
    }

    public boolean showAvailableEmloyeesOnly() {
        return this.availFilterOptions.isShowAvailableOnly();
    }

    public boolean showTrainedEmployeesOnly() {
        return this.availFilterOptions.isShowTrainedOnly();
    }

    public Vector<EmployeeType> getUnSelectedEmployeeTypes() {
        return this.availFilterOptions.getUnSelectedEmployeeTypes();
    }

    public Vector<CertificationClass> getUnSelectedCertificationClass() {
        return this.availFilterOptions.getUnSelectedCertifications();
    }

    public void setSelectedCertifications(Vector<CertificationClass> certs) {
        this.availFilterOptions.setSelectedCertifications(certs);
    }

    public void setShowTrainedEmployeesOnly(boolean showTrainedOnly) {
        this.availFilterOptions.setShowTrained(showTrainedOnly);
    }

    /**
     * Just used to check if ScheduleView exists yet, if so will set sv to it...
     */
    private void probeForScheduleView() {
        if (sv == null) {
            sv = EmployeeListWindow.parent();
        }
    }

    /**
     * Sets the value of our Thread Lock boolean used by the class
     * DisplayAvailabilityThread
     */
    public void setThreadLock(boolean value) {
        ThreadLock = value;
    }

    /**
     * Returns value of our Thread Lock boolean used by the class
     * DisplayAvailabilityThread
     */
    public boolean getThreadLock() {
        return ThreadLock;
    }

    /**
     * Kinda crappy way of building our ComboBox should probably implement a
     * Vector later and allow it to sort all options but for right now it's cool
     * I guess
     */
    public void buildOptionBox() {
        optionChoices[0] = "Sort By Last Name";
        optionChoices[1] = "Sort by Num Hours Worked";
        optionChoices[2] = "Sort by Distance";
        this.addItem(optionChoices[0]);
        this.addItem(optionChoices[1]);
        this.addItem(optionChoices[2]);
    }

    /**
     * Changes employee list based only on data from one shift...
     */
    public void ChangeByAvailability(UnitToDisplay myShift) {

        dat.RunAvailability();
        checkIfShouldSortByOvertimeAndWeek();

        if (LastSortType == SEmployee.SORT_BY_DISTANCE) {
            genericSortFunction(false);
        }
    }

    /**
     * Helper function to see if new shift has a time conflict with existing
     * shifts used by AddShiftToCurrentAvailability.
     *
     * NOTE: Still need to handle circumstance where end date after end date but
     * start date before start date...both ways....
     */
    private boolean CheckNewShiftForConflict(SShift currentShift) {
        for (int i = 0; i < currentlySelectedShifts.size(); i++) {
            //If two calendars are equal we may have a problem...
            SShift arrayShft = ((SShift) (currentlySelectedShifts.get(i)));
            if (StaticDateTimeFunctions.areCalendarsEqual(currentShift.getMyDate(), arrayShft.getMyDate())) {
                int startTime = arrayShft.myShift.getStartTime();
                int endTime = arrayShft.myShift.getEndTime();
                int cStartTime = currentShift.myShift.getStartTime();
                int cEndTime = currentShift.myShift.getEndTime();
                if (arrayShft.myShift.getStartTime() > arrayShft.myShift.getEndTime()) {
                    endTime = endTime + 1440;
                }
                if (currentShift.myShift.getStartTime() > currentShift.myShift.getEndTime()) {
                    cEndTime = cEndTime + 1440;
                }
                if (startTime < cStartTime
                        && endTime <= cStartTime) {
                    //No Conflict
                } else if (startTime >= cEndTime
                        && endTime > cEndTime) {
                    //No Conflict
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public void AddShiftToCurrentAvailability(UnitToDisplay myShift, SShift currentShift, boolean shouldSetAvail) {
        if (shouldSetAvail) {
            AddShiftToCurrentAvailability(myShift, currentShift);
        } else {
            currentlySelectedShifts.add(currentShift);
        }

    }

    /**
     * Allows you to list employees available to work several different shifts
     * can keep adding shift indefinitely.
     */
    public synchronized void AddShiftToCurrentAvailability(UnitToDisplay myShift, SShift currentShift) {
        currentlySelectedShifts.add(currentShift);
        adjustCertificationsForSelectedShifts();
        adjustTrainingForSelectedShifts();
        dat.RunAvailability();
        checkIfShouldSortByOvertimeAndWeek();
        if (currentlySelectedShifts.size() > 1) {
            EmployeeListWindow.setShiftsForDisplayingAvailabilityForm(currentlySelectedShifts.size());
        } else {
            EmployeeListWindow.setShiftDisplayingAvailabilityFor(myShift);
        }

    }

    /**
     * Method used to return a vector of SShifts which represents all selected
     * shifts called by SShift which is in turn passed to AEmployee to determine
     * what shifts we are adding employee to...
     */
    public mySShiftVector getSelectedShifts() {
        return currentlySelectedShifts;
    }

    /**
     * This will check to see if the selected availability is compatible With
     * us.
     */
    public boolean selectedShiftCanLand() {
        Vector av = currentlySelectedShifts;
        if (av.size() == 1) {
            SShift s = (SShift) av.get(0);
            SEmployee currEmp = s.getEmployee();

        }
        return false;
    }

    private void adjustCertificationsForSelectedShifts() {
        Vector<CertificationClass> neededCerts = new Vector<CertificationClass>();
        for (int x = 0; x < currentlySelectedShifts.size(); x++) {
            if (currentlySelectedShifts.get(x).getClient() instanceof SClient) {
                SClient currClient = (SClient) currentlySelectedShifts.get(x).getClient();
                neededCerts.addAll(currClient.getCertificationsNeeded());
            }
        }
        setSelectedCertifications(neededCerts);
    }

    private void adjustTrainingForSelectedShifts() {
        boolean hasShiftWithTraining = false;
        for (int x = 0; x < currentlySelectedShifts.size(); x++) {
//            hasShiftWithTraining = hasShiftWithTraining ||
//                    currentlySelectedShifts.get(x).getClient().getTrainingTime() > 2;
        }
        this.setShowTrainedEmployeesOnly(hasShiftWithTraining);
    }

    /**
     * Allows you to remove one of the shifts you just added to to shift list
     */
    public synchronized void RemoveShiftFromCurrentAvailability(UnitToDisplay myShift, SShift currentShift) {
        currentlySelectedShifts.remove(currentShift);
        adjustCertificationsForSelectedShifts();
        adjustTrainingForSelectedShifts();
        dat.RunAvailability();
        checkIfShouldSortByOvertimeAndWeek();
    }

    public void clearCurrentlySelectedShifts() {
        currentlySelectedShifts.clear();
        adjustCertificationsForSelectedShifts();
        adjustTrainingForSelectedShifts();
    }

    public void ClearAllShiftSelections() {
        clearCurrentlySelectedShifts();
        this.probeForScheduleView();
        checkIfShouldSortByOvertimeAndWeek();
    }

    /**
     * Gets this list...
     */
    public Vector<SEmployee> getEmployeesFromScheduleView() {
        return EmployeeListWindow.getEmployeesList();
    }

    /**
     * Processes our combo box changing will be implemented more later on to
     * allow you to screen by pay rate etc...
     */
    public void processComboChange(int selection) {
        if (selection == 0) {
            sortByLastNameAndDisplay(false);
        } else if (selection == 1) {
            sortByOverTimeAndDisplay(false);
        } else if (selection == 2) {
            sortByDistanceAndDisplay(false);
        } else if (selection == 3) {
            displayAll();
        }
        if (selection != 3) {
            dat.RunAvailability();
        }
    }

    public void refreshEmpList() {
        dat.RunAvailability();
    }

    private void sortByTrainingAndDisplay() {
        LastSortType = SEmployee.SORT_BY_TRAINING;
        genericSortFunction(true);
    }

    private void sortByAvailabilityAndDisplay(boolean shouldGetNewData) {
        LastSortType = SEmployee.SORT_BY_AVAILABILITY;
        genericSortFunction(shouldGetNewData);
    }

    /**
     * Sorts our list by Last Name...
     */
    private void sortByLastNameAndDisplay(boolean shouldGetNewData) {
        LastSortType = SEmployee.SORT_BY_NAME;
        genericSortFunction(shouldGetNewData);
    }

    private void sortByDistanceAndDisplay(boolean shouldGetNewData) {
        LastSortType = SEmployee.SORT_BY_DISTANCE;
        genericSortFunction(shouldGetNewData);
    }

    /**
     * Sorts our list by Overtime...
     */
    private void sortByOverTimeAndDisplay(boolean shouldGetNewData) {
        LastSortType = SEmployee.SORT_BY_OVERTIME;
        genericSortFunction(shouldGetNewData);
    }

    /**
     * Displays All Employeess
     */
    private void displayAll() {
        LastSortType = SEmployee.DISPLAY_ALL;
        genericSortFunction(true);
        showAvailabilityForNoShift();
    }

    /**
     * Sorts our list by Overtime for specific week
     */
    private void sortByOverTimeForWeekAndDisplay(int week) {
        LastSortType = SEmployee.SORT_BY_OVERTIME_ON_WEEK + week;
        genericSortFunction(true);
    }

    /**
     * Functions checks if all of shifts in Vector are for same week if so it
     * sets sort to just sort by overtime for that week, if for multiple weeks
     * it will sort by all weeks overtime
     */
    public void checkIfShouldSortByOvertimeAndWeek() {
        int thisWeekNum = 0;
        int firstWeekNum = -999;
        boolean value = true;
        if (currentlySelectedShifts.size() <= 0) {
            value = false;
        }

        for (int i = 0; i < currentlySelectedShifts.size(); i++) {
            try {
                thisWeekNum = ((SShift) currentlySelectedShifts.get(i)).myShift.getWeekNo();
                if (firstWeekNum == -999) {
                    firstWeekNum = thisWeekNum;
                }
                if (firstWeekNum != thisWeekNum) {
                    value = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (value) {
            CurrentWeek = firstWeekNum;
        } else {
            CurrentWeek = -1;
        }
        if (LastSortType == SEmployee.SORT_BY_OVERTIME
                || LastSortType >= SEmployee.SORT_BY_OVERTIME_ON_WEEK) {
            if (value) {
                LastSortType = SEmployee.SORT_BY_OVERTIME_ON_WEEK + EmployeeListWindow.parent.getCurrentSelectedWeek();
            } else {
                LastSortType = SEmployee.SORT_BY_OVERTIME;
            }
            genericSortFunction(false);
        }
    }

    private void genericSortFunction(boolean grabNewData) {
        this.probeForScheduleView();
        Vector<SEmployee> myEmployees = EmployeeListWindow.getEmployeesList();
        SEmployee.setComparatorValue(LastSortType);
        java.util.Collections.sort(myEmployees);

        EmployeeListWindow.redrawEmployeesList(myEmployees, LastSortType);
    }

    /**
     * Used by AddShiftToCurrentAvailability to compare what employees are in
     * the passed in Record Set and what employees are in our list box and only
     * display employees that are in both...
     */
    public void compareAndListResultsToEmployeeList(Record_Set rs) {
        if (rs.length() > 0) {
            compareAndListResultsFromRecordSetToEmployeeList(rs);
        }
    }

    /**
     * Used by RemoveShiftToCurrentAvailability to compare what employees are
     * returned by record_Set and those from Employee_list then it only adds
     * those on that are in the record_set but not the Employee_List.
     */
    public void compareAndListResultsFromRecordSetToEmployeeList(Record_Set rs) {
        if (LastSortType == SEmployee.DISPLAY_ALL) {
            return;
        }
        if (rs.length() > 0) {
            do {
                try {
                    EmployeeListWindow.setGivenEmployeeToVisible(Integer.parseInt(rs.getString("id")));
                } catch (Exception e) {
                    System.out.println("Cannot find emp number " + rs.getString("id") + " information in refresh Employees List in Availability Combo Box");
                }
            } while (rs.moveNext());
            EmployeeListWindow.repaint();
        }
    }

    /**
     * Same as refreshEmployeeList but will sort if should be ordered
     * differently than they are...
     */
    public void refreshEmployeeListAndSort(Record_Set rs) {
        refreshEmployeeList(rs);
    }

    /**
     * Used by ChangeByAvailability to completely load in new employee info
     * based off of which employees can work given shift...
     */
    public void refreshEmployeeList(Record_Set rs) {
        while (rs.movePrev()) {
        }
        if (rs.length() > 0) {
            if (LastSortType != SEmployee.DISPLAY_ALL) {
                EmployeeListWindow.setListToNonVisible();
            } else {
                EmployeeListWindow.setListToVisible();
            }
        }
        do {
            try {
                EmployeeListWindow.setGivenEmployeeToVisible(Integer.parseInt(rs.getString("id")));
            } catch (Exception e) {
            }
        } while (rs.moveNext());
        EmployeeListWindow.repaint();
    }

    /**
     * Shows the availability for a non selected shift.
     */
    public void showAvailabilityForNoShift() {
        EmployeeListWindow.setShiftDisplayingAvailabilityFor(null);
        EmployeeListWindow.setListToVisible();
    }

    /**
     * ActionListener for our Combo Box to invoke processComboChange.
     */
    private class ItemSelectedEvent implements ItemListener {

        AvailabilityComboBox parentCombo;

        public ItemSelectedEvent(AvailabilityComboBox acb) {
            parentCombo = acb;
        }

        public void itemStateChanged(ItemEvent e) {
            String currentSelection = (String) (parentCombo.getSelectedItem());
            if (optionChoices[0].compareTo(currentSelection) == 0) {
                processComboChange(0);
            } else if (optionChoices[1].compareTo(currentSelection) == 0) {
                processComboChange(1);
            } else if (optionChoices[2].compareTo(currentSelection) == 0) {
                processComboChange(2);
            }
        }
    }

    public boolean hasEmployee(SEmployee se) {
        int i, c;

        if (employees == null) {
            return false;
        }

        c = employees.length;

        for (i = 0; i < c; i++) {
            if (se.getId() == employees[i].getId()) {
                return true;
            }
        }

        return false;
    }

    public class mySShiftVector extends Vector {

        public mySShiftVector() {
        }

        public void add(SShift newShift) {
            super.add(newShift);
            try {
                newShift.selectGraphicQualities(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void clear() {
            for (int i = 0; i < size(); i++) {
                try {
                    ((SShift) super.get(i)).selectGraphicQualities(false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            super.clear();
        }

        public SShift get(int i) {
            return (SShift) super.get(i);
        }

        public Object remove(int i) {
            try {
                ((SShift) super.get(i)).selectGraphicQualities(false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return super.remove(i);
        }
    }
}
