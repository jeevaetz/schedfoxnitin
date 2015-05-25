/*
 * DisplayAvailabilityThread.java
 *
 * Created on December 8, 2004, 10:35 AM
 */
package rmischedule.schedule.components;

import java.util.*;
import rmischedule.schedule.components.availability.ScheduleDisplayAvailabilityForShift;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author  ira
 * Purpose: The primary purpose of this class is used to update the employee panel
 *          on the right side of the schedule when employees are clicked, this was
 *          implemented to reduce lag and increase responsiveness of the program
 *          most of the methods called are in the availabilitycombobox class. Commands
 *          are queued as Strings of start, and end times, dates and appropriate
 *          command to run on the RecordSet returned by specified data....pretty bad
 *          ass stuff...
 */
public class DisplayAvailabilityThread extends rmischedule.schedule.ScheduleThread {

    private AvailabilityComboBox parent;
    private ScheduleDisplayAvailabilityForShift scheduleDisplayShifts;
    private boolean killMe;
    private boolean runMe;

    /**
     * Creates a new instance of DisplayAvailabilityThread 
     * acb     - parent AvailabilityComboBox
     */
    public DisplayAvailabilityThread(AvailabilityComboBox acb) {
        parent = acb;
        killMe = false;
        runMe = false;
        scheduleDisplayShifts = new ScheduleDisplayAvailabilityForShift(parent);
        registerMe(acb.EmployeeListWindow.parent);
    }

    public void killMe() {
        killMe = true;
        interrupt();
    }

    /**
     * Adds a Command for the Thread to do when it has completed it's current task list
     * Start - int Start Time
     * End   - int End Time
     * Date  - Date returning values from
     * Command - as Defined by final ints in this file
     */
    public void RunAvailability() {
        runMe = true;
    }

    /**
     * Runs appropriate methods so long as AvailabilityComboBox is not ThreadLocked
     * ie: Method already running...
     * Will keep running so long as parent object != null...sleeps for .1 second if 
     * no info in queue, very small overhead if no info...only checking size of queue
     */
    public void run() {
        boolean rp = false;
        //While parent object is not destroyed run
        Record_Set returnedRecordSet = new Record_Set();
        while (!killMe) {
            while (runMe && !killMe) {
                rp = true;
                runMe = false;
                if (!parent.getThreadLock()) {
                    this.setPriority(super.MAX_PRIORITY);
                    parent.setThreadLock(true);

                    scheduleDisplayShifts.refreshListOfEmployees();

                    parent.EmployeeListWindow.refreshEmployeeListByVisibility();
                    parent.setThreadLock(false);
                }
                this.setPriority(super.MIN_PRIORITY);
            }
            try {
                sleep(300);
            } catch (Exception e) {}
        }
    }

    /**
     * Private class to hold date for and individual command...
     */
    private class individualCommand {

        public DShift myShift;
        public int Command;

        public individualCommand(DShift myDShift, int command) {
            myShift = myDShift;
            Command = command;
        }
    }
}
