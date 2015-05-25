/*
 * xPrintDays.java
 *
 * Created on February 2, 2005, 9:01 AM
 */
package rmischeduleserver.util.xprint;

import java.util.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.ShiftTypeClass;

/**
 *
 * @author ira
 */
public class xPrintDay implements JRDataSource {

    private xPrintIndividualData parent;
    public String databaseFormatDate;
    public String dayOfWeek;
    private ArrayList<String> shiftId;
    private ArrayList<String> stime;
    private ArrayList<Double> breaks;
    private ArrayList<String> etime;
    private ArrayList<ShiftTypeClass> type;
    private java.util.Date myDate;
    private int currPos;
    private int currentRow;
    private double breakTime;

    /** Creates a new instance of xPrintDays */
    public xPrintDay(Calendar day, String dayOfW, xPrintIndividualData parent) {
        databaseFormatDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(day);

        this.parent = parent;

        dayOfWeek = dayOfW;
        stime = new ArrayList();
        etime = new ArrayList();
        shiftId = new ArrayList();
        type = new ArrayList();
        breaks = new ArrayList();
        currPos = 0;
        myDate = day.getTime();
    }

    public boolean moveNext() {
        currPos++;
        if (currPos >= stime.size()) {
            return false;
        }
        return true;
    }

    public boolean hasData() {
        boolean retVal = false;
        for (int i = 0; i < this.etime.size(); i++) {
            if (etime.get(i).length() > 0) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    /**
     * Is their more data in this particular day?
     * @return boolean true if there is more data, false otherwise.
     */
    public boolean hasMore() {
        boolean retVal = false;
        if ((currPos + 1) < stime.size()) {
            retVal = true;
        }
        return retVal;
    }

    public void moveFirst() {
        currPos = 0;
    }

    public void moveLast() {
        currPos = stime.size();
    }

    public int size() {
        return stime.size();
    }

    public void insertTimes(String shiftId, String in, String out, String myType, String trainer, double breakTime) {
        if (in.length() > 0) {
            this.shiftId.add(shiftId);
            stime.add(in);
            etime.add(out);
            breaks.add(breakTime);
            
            ShiftTypeClass mySType = new ShiftTypeClass(myType);
            if (trainer == null) {
                trainer = "";
            }
            mySType.setTrainerInformation(0, trainer);
            type.add(mySType);
            //this.currPos++;
            sortList();
        }
    }

    private void sortList() {
        ArrayList<xSortObj> list = new ArrayList<xSortObj>();
        for (int i = 0; i < this.etime.size(); i++) {
            xSortObj xs = new xSortObj(
                    this.shiftId.get(i),
                    this.stime.get(i),
                    this.etime.get(i),
                    this.type.get(i),
                    this.breaks.get(i)
            );     
            list.add(xs);
        }     
        
        Collections.sort(list, 
            new Comparator<xPrintDay.xSortObj>() {
                public int compare(xPrintDay.xSortObj o1, xPrintDay.xSortObj o2) {
                    double a1 = Double.parseDouble(o1.zSTime);
                    double a2 = Double.parseDouble(o2.zSTime);
                    return (int)(a1 - a2);
                }                
            }
        );
                
        stime = new ArrayList();
        etime = new ArrayList();
        shiftId = new ArrayList();
        type = new ArrayList();

        for( int i = 0; i < list.size(); i++) {
            this.shiftId.add(list.get(i).zShiftId);
            this.stime.add(list.get(i).zSTime);
            this.etime.add(list.get(i).zETime);
            this.type.add(list.get(i).zType);
        }
    }
    
    private class xSortObj{
        public String zShiftId;
        public String zSTime;
        public String zETime;
        public double breakHours;
        public ShiftTypeClass zType;
        
        public xSortObj(String zShiftId, String zSTime, String zETime, ShiftTypeClass zType, double breakHours) {
            this.zShiftId = zShiftId;
            this.zSTime = zSTime;
            this.zETime = zETime;
            this.zType = zType;
            this.breakHours = breakHours;
        }   
    }
    
    public String toString() {
        return databaseFormatDate + ": " + stime + " - " + etime;
    }
    
    public String getDate() {
        return databaseFormatDate;
    }

    public String getStartTime() {
        try {
            return stime.get(currPos);
        } catch (Exception e) {
            return "";
        }
    }

    public String getShiftType() {
        String retVal = "";
        try {
            if (this.type.get(currPos).isShiftType(ShiftTypeClass.SHIFT_TRAINER_SHIFT)) {
                retVal = "Trainer\r\n";
            } else if (this.type.get(currPos).isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
                retVal = "Training\r\n";
            }
        } catch (Exception e) {}
        return retVal;
    }

    public String getFormattedStartTime() {
        if (getStartTime().equals("")) {
            return "";
        }
        return StaticDateTimeFunctions.stringToFormattedTime(getStartTime(), false);
    }

    public String getFormattedEndTime() {
        if (getEndTime().equals("")) {
            return "";
        }
        return StaticDateTimeFunctions.stringToFormattedTime(getEndTime(), false);
    }

    public Double getDayTotalHours() {
        try {
            int tempPos = this.currPos;
            double runningSum = 0;
                double endTime = Double.parseDouble(getEndTime());
                double startTime = Double.parseDouble(getStartTime());
                runningSum += StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(startTime, endTime, parent.getMyParent().getCompany(), getBreakTime());
                
            this.currPos = tempPos;
            return new Double(runningSum);
        } catch (Exception e) {
            return new Double(0);
        }
    }

    public Double getTotalHours() {
        try {
            int tempPos = this.currPos;
            double runningSum = 0;
            for (int i = 0; i < this.etime.size(); i++) {
                currPos = i;
                double endTime = Double.parseDouble(getEndTime());
                double startTime = Double.parseDouble(getStartTime());
                runningSum += StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(startTime, endTime, parent.getMyParent().getCompany(), getBreakTime());
            }
            this.currPos = tempPos;
            return new Double(runningSum);
        } catch (Exception e) {
            return new Double(0);
        }
    }

    public Double getHours() {
        try {
            double runningSum = 0;
            double endTime = Double.parseDouble(getEndTime());
            double startTime = Double.parseDouble(getStartTime());
            runningSum += StaticDateTimeFunctions.getDifferenceAndRoundByNumberOfMinutes(startTime, endTime, parent.getMyParent().getCompany(), getBreakTime());

            return new Double(runningSum);
        } catch (Exception e) {
            return new Double(0);
        }
    }
    public String getEndTime() {
        try {
            return etime.get(currPos);
        } catch (Exception e) {
            return "";
        }
    }
    
    public double getBreakTime() {
        try {
            return breaks.get(currPos);
        } catch (Exception e) {
            return 0;
        }
    }

    public ShiftTypeClass getType() {
        try {
            return type.get(currPos);
        } catch (Exception e) {
            return new ShiftTypeClass();
        }
    }
    
    public boolean overlapsToNewDate() {
        boolean retVal = false;
        try {
            int stime = Integer.parseInt(getStartTime());
            if (stime == 0 || stime == 1440) {
                return true;
            }
        } catch (Exception e) {}
        if(this.getFormattedStartTime().endsWith("AM") || this.getFormattedStartTime().endsWith("PM")) {
             return false;
        }
        try {
            int stime = Integer.parseInt(getStartTime());
            int etime = Integer.parseInt(getEndTime());
            if (stime > etime) {
                retVal = true;
            }
        } catch (Exception e) {}
        return retVal;
    }
    
    public java.util.Date getMyDate() {
        return myDate;
    }

    public void setMyDate(java.util.Date myDate) {
        this.myDate = myDate;
    }
    
    public xPrintSchedule getSchedule() {
        return this.parent.getParentSchedule();
    }
    
    /**
     * Does this day (which is comprised of multiple shifts) contain the following
     * shift? Provided by it's id.
     * @param shiftId
     * @return
     */
    public boolean contains(String shiftId) {
        boolean retVal = false;
        if (this.shiftId.contains(shiftId)) {
            retVal = true;
        }
        return retVal;
    }

    public Boolean hasMoreData() {
       return currPos < this.size() - 1;
    }
    
    public boolean next() {
        boolean retVal = false;
        if (hasMoreData()) {
            currPos++;
            retVal = true;
        }
        return retVal;
    }

    public Object getFieldValue(JRField arg0) throws JRException {
        Object retVal = null;
        if (arg0.getName().equals("date")) {
                retVal = this.myDate;
        } else if (arg0.getName().equals("endTime")) {
            retVal = this.getFormattedEndTime();
        } else if (arg0.getName().equals("startTime")) {
            retVal = this.getFormattedStartTime();
        } else if (arg0.getName().equals("clientName")) {
            retVal = this.parent.getClient().getName();
        } else if (arg0.getName().equals("totalTime")) {
            return this.getHours();
        }
        return retVal;
    }
}
