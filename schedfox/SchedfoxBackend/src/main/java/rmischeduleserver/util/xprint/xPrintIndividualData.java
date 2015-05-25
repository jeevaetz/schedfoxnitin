/*
 * xPrintIndividualData.java
 *
 * Created on February 1, 2005, 11:03 AM
 */

package rmischeduleserver.util.xprint;
import schedfoxlib.model.util.Record_Set;
import java.util.*;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;
import rmischeduleserver.util.StaticDateTimeFunctions;
import schedfoxlib.model.ShiftOptionsClass;

/**
 *
 * @author ira
 */
public class xPrintIndividualData implements Comparable, JRDataSource {

    public Vector<xPrintDay> days;
    public String eid;
    public String cid;
    public String shiftid;
    private String doy;
    private String dow;
    private String stime;
    private String isDeleted;
    private String etime;
    private xPrintData Parent;
    private xPrintSchedule parentSchedule;
    private String sdate;
    private String edate;
    private String grpId;
    private String type;
    private String trainer;
    private String clientName;
    
    private PEmployee employee;
    private PClient client;
    private int maxSize;
    public boolean hasData;
    private boolean firstRow;
    
    public ShiftOptionsClass myPayOptions;
    
    /** Creates a new instance of xPrintIndividualData */
    public xPrintIndividualData(Record_Set rs, Calendar startDate, Calendar endDate, xPrintData parent, PClient client, PEmployee employee) {
        int diffDays = 7;
        int dayOfWeekOffset = startDate.get(Calendar.DAY_OF_WEEK) - 1;
        this.client = client;
        this.employee = employee;
        hasData = false;
        maxSize = 0;
        Parent = parent;
        firstRow = true;
        Calendar myStartCal = StaticDateTimeFunctions.setCalendarTo(startDate);
        if (dayOfWeekOffset == -1) {
            dayOfWeekOffset = 6;
        } 
        days = new Vector(diffDays);
        for (int i = 0; i < diffDays; i++) {
            days.add(new xPrintDay(myStartCal, dayOfWeekOffset++ + "", this));
            myStartCal.add(Calendar.DATE, 1);
            if (dayOfWeekOffset > 7) {
                dayOfWeekOffset = 0;
            }
        }
        do {
            cid = rs.getString("cid");
            shiftid = rs.getString("sid");
            eid = rs.getString("eid");
            doy = rs.getString("date");
            dow = rs.getString("dow");
            isDeleted = rs.getString("isdeleted");
            stime = rs.getString("start_time");
            etime = rs.getString("end_time");
            sdate = rs.getString("sdate");
            edate = rs.getString("edate");
            grpId = rs.getString("gp");
            type = rs.getString("type");
            trainer = rs.getString("trainer");
            clientName = rs.getString("cname");
            
            myPayOptions = new ShiftOptionsClass();
            myPayOptions.parse(rs.getString("pay_opt"));
            
            processData();
        } while (rs.moveNext() &&
                 cid.equals(rs.getString("cid")) &&
                 eid.equals(rs.getString("eid")));
    }

    public xPrintData getMyParent() {
        return this.Parent;
    }

    public int getSize() {
        int greatestSoFar = -1;
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).size() > greatestSoFar) {
                greatestSoFar = days.get(i).size();
            }
        }
        return greatestSoFar;
    }
    
    public PEmployee getEmployee() {
        return this.employee;
    }
    
    public PClient getClient() {
        return this.client;
    }
    
    public xPrintSchedule getParentSchedule() {
        return this.parentSchedule;
    }
    
    public void setParentSchedule(xPrintSchedule parentSchedule) {
        this.parentSchedule = parentSchedule;
    }
    
    /**
     * Is the provided shift anywhere in the Vector of days that this Individual
     * Data object contains?
     * @param shiftId
     * @return
     */
    public boolean contains(String shiftId) {
        boolean retVal = false;
        for (int i = 0; i < this.days.size(); i++) {
            retVal = retVal || days.get(i).contains(shiftId);
        }
        return retVal;
    }
    
    public boolean nextRow() {
        boolean canMove = false;
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).moveNext()) {
                canMove = true;
            }
        }
        return canMove;
    }
    
    public void movePrevious() {
       for (int i = 0; i < days.size(); i++) {
           days.get(i).moveFirst();
       }
    }
    
    public void processData() {
        if (isDeleted.equals("1")) {
            return;
        }
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).databaseFormatDate.equals((doy))) {
                days.get(i).insertTimes(shiftid, stime, etime, type, trainer, myPayOptions.getBreakLength());
                hasData = true;
            }
        }
    }
    
    @Override
    public String toString() {
        StringBuffer myReturnString = new StringBuffer();
        myReturnString.append("EId - " + eid + " CId - " + cid + " " + " Shift Id: " + shiftid);
        for (int i = 0; i < days.size(); i++) {
            myReturnString.append(days.get(i).toString() + " ");
        }
        return myReturnString.toString();
    }
    
    public int compareTo(Object o) {
        xPrintIndividualData comp = (xPrintIndividualData)o;
        if (Parent.sortType == xPrintData.SORT_BY_CLIENT) {
            try {
                int clientCompare = clientName.compareToIgnoreCase(comp.clientName);
                if (clientCompare != 0) {
                    return clientCompare;
                } else {
                    return Parent.employeesArray[Parent.employeeHash.get(this.eid)].getName().compareToIgnoreCase(Parent.employeesArray[Parent.employeeHash.get(comp.eid)].getName());
                }
            } catch (Exception e) {
                return 0;
            }
        } else {  //(Parent.sortType == Parent.SORT_BY_EMPLOYEE)
            try {
                int employeeCompare = Parent.employeesArray[Parent.employeeHash.get(this.eid)].getName().compareToIgnoreCase(Parent.employeesArray[Parent.employeeHash.get(comp.eid)].getName());
                int idCompare = this.eid.compareToIgnoreCase(comp.eid);
                if (employeeCompare != 0) {
                    return employeeCompare;
                } else if (idCompare != 0) {
                    return idCompare;
                } else {
                    return clientName.compareToIgnoreCase(comp.clientName);
                }
            } catch (Exception e) {
                return 0;
            }
        }
    }
    
    /**
     * Below are methods for Jasper Reports
     */
    
    /**
     * Is their more data in their particular schedule?
     * @return
     */
    public Boolean hasMoreData() {
        boolean retVal = false;
        for (int i = 0; i < days.size(); i++) {
            retVal = retVal || days.get(i).hasMore();
        }
        return retVal;
    }
    
    public boolean next() {
        boolean retVal = true;
        if (!firstRow) {
            retVal = this.nextRow();
        }
        firstRow = false;
        return retVal;
    }
    
    public Object getFieldValue(JRField jrField) {
        Object retVal = null;
        if (jrField.getName().equals("day1") || jrField.getName().equals("totalDay0")) {
            retVal = this.getDataForDay(0);
        } else if (jrField.getName().equals("day2") || jrField.getName().equals("totalDay1")) {
            retVal = this.getDataForDay(1);
        } else if (jrField.getName().equals("day3") || jrField.getName().equals("totalDay2")) {
            retVal = this.getDataForDay(2);
        } else if (jrField.getName().equals("day4") || jrField.getName().equals("totalDay3")) {
            retVal = this.getDataForDay(3);
        } else if (jrField.getName().equals("day5") || jrField.getName().equals("totalDay4")) {
            retVal = this.getDataForDay(4);
        } else if (jrField.getName().equals("day6") || jrField.getName().equals("totalDay5")) {
            retVal = this.getDataForDay(5);
        } else if (jrField.getName().equals("day7") || jrField.getName().equals("totalDay6")) {
            retVal = this.getDataForDay(6);
        } else if (jrField.getName().equals("employee_name")) {
            retVal = this.Parent.getEmployeeName();
        }
        return retVal;
    }
    
    public xPrintDay getDataForDay(int dayOfWeek) {
        if (days.get(dayOfWeek) != null) {
            return days.get(dayOfWeek);
        } else {
            return null;
        }
    }
}
