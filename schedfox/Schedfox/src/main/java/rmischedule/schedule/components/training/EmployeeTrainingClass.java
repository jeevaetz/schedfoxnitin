/*
 * EmployeeTrainingClass.java
 *
 * Created on May 17, 2005, 1:30 PM
 */

package rmischedule.schedule.components.training;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.schedule.components.*;
import java.util.*;
import rmischedule.components.*;
import java.util.Vector;
/**
 *
 * @author ira
 *
 * This class encapsulates all training info for training, unfortunately it's kinda
 * complicated because I felt the only way to do this right was to have two different
 * data sets, one the data before the schedule which indicates when the employee was
 * trained, then we also need to parse through the DShifts on the schedule....
 *
 */
public class EmployeeTrainingClass extends Hashtable {
    
    /** Creates a new instance of EmployeeTrainingClass */
    public EmployeeTrainingClass() {
        super();
    }
    
    /**
     * Called to add data directly from our database.... Upon Load of Schedule this
     * method should be used to load training info from recordset...
     */
    public void addTrainingShift(double timeWorked, SClient sc) {
        super.put(sc.getId(), new TrainingInfo(sc, timeWorked));
    }
    
    /**
     * Uses both data from before schedule starts and data loaded to determine if 
     * employee is trained for given employee...
     */
    public boolean isTrained(SClient cid, double timeToWork) {
        double hoursTrained = 0.0;
        TrainingInfo myInfo = (TrainingInfo)super.get(cid.getId());
        try {
            return myInfo.isTrained();
        } catch (Exception e) {
            if (cid.getTrainingTime() > 0.0) {
                return false;
            }
            return true;
        }
    }
    
    /**
     * Small class to allow you to store client/employee training info and increment 
     * when necessary...
     */
    public class TrainingInfo implements SourceOfConflict {
        private SClient client;
        private SShift shift;
        private double timeTrained;
        public TrainingInfo(SClient cli, double time) {
            client = cli;
            timeTrained = time;
        }
        
        public void incrementTime(double time) {
            timeTrained += time;
        }
        
        public double getTime() {
            return timeTrained;
        }
        
        public boolean isTrained() {
            if (timeTrained >= (client.getTrainingTime() / 60)) {
                return true;
            }
            return false;
        }
        
        public int getDayCode() {
            return 0;
        }
        
        public int getStartTime() {
            return 0;
        }
        
        public int getEndTime() {
            return 1440;
        }
        
        public boolean shouldCauseConflict() {
            return true;
        }
        
        public void setShiftConflictingWith(SourceOfConflict o) {
            
        }
        
        public String getDateString() {
            return "";
        }
        
        public String getFormattedStartTime() {
            return StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(0.0, timeTrained, Integer.parseInt(client.getMyParent().getConnection().myCompany)) + "";
        }
        
        public String getFormattedEndTime() {
            return StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(0.0, client.getTrainingTime(), Integer.parseInt(client.getMyParent().getConnection().myCompany)) + "";
        }
        
        public int getMyType() {
            return SourceOfConflict.NOTTRAINED;
        }
        
        public int getWeekNo() {
            return 0;
        }
        
        public boolean isDeleted() {
            return false;
        }
        
        public boolean equals(SourceOfConflict compareMe) {
            return false;
        }
        
        public String getShiftId() {
            return "";
        }
        
        public int getAvailType() {
            return 0;
        }

        public int getOffset() {
            return 0;
        }

        public SEmployee getEmployee() {
            return new SEmployee("0");
        }

        public String getGroupId() {
            return "0";
        }

        public SClient getClient() {
            return client;
        }

        public SShift getShift() {
            return shift;
        }

        public void setShift(SShift shift) {
            this.shift = shift;
        }

        public void setShift(SShift shift, boolean clearVal) {
            this.shift = shift;
        }

        public Calendar getStartCal() {
            return null;
        }

        public Calendar getEndCal() {
            return null;
        }
        
    }
        
    
    
}
