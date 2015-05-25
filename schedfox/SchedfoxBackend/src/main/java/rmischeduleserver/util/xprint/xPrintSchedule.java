/*
 * This contains a schedule for a given person or client a schedule can be (for 
 * an employee) shifts at multiple or one client and vice versa
 */

package rmischeduleserver.util.xprint;

import java.util.Hashtable;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Ira
 */
public class xPrintSchedule implements JRDataSource {
    
    private Vector<xPrintIndividualData> schedules;
    private xPrintAllDays days;
    private int curPos;
    private xPrintData parent;
    private Hashtable headerValues;
    private boolean hasReset = false;
    
    public xPrintSchedule(xPrintData parent) {
        schedules = new Vector();
        days = new xPrintAllDays();
        curPos = -1;
        this.parent = parent;
        headerValues = new Hashtable();
    }
    
    public void addIndividualData(xPrintIndividualData data) {
        String position = "_" + this.schedules.size();
        this.schedules.add(data);
        if (parent.getSortType() == xPrintData.SORT_BY_CLIENT) {
            headerValues.put("schedule_header" + position, parent.getEmployeeName());
            headerValues.put("schedule_address" + position, parent.getEmployeeAddress());
            headerValues.put("schedule_city" + position, parent.getEmployeeCity());
            headerValues.put("schedule_state" + position, parent.getEmployeeState());
            headerValues.put("schedule_zip" + position, parent.getEmployeeZip());
            headerValues.put("schedule_phone" + position, parent.getEmployeePhone());
        } else {
            headerValues.put("schedule_header" + position, parent.getClientName());
            headerValues.put("schedule_address" + position, parent.getClientAddress());
            headerValues.put("schedule_city" + position, parent.getClientCity());
            headerValues.put("schedule_state" + position, parent.getClientState());
            headerValues.put("schedule_zip" + position, parent.getClientZip());
            headerValues.put("schedule_phone" + position, parent.getClientPhone());
        }
        for (int i = 0; i <= 6; i++) {
            xPrintDay day = data.getDataForDay(i);
            this.days.addToArray(day, i);
        }
    }
    
    /**
     * Is their more data in their particular schedule?
     * @return
     */
    public Boolean hasMoreData() {
        return curPos < schedules.size() - 1;
    }
    
    public boolean next() {
        boolean retVal = false;
        if (hasMoreData()) {
            curPos++;
            retVal = true;
        }
        return retVal;
    }
    
    public xPrintAllDays getDayData() {
        return this.days;
    }
    
    public Object getFieldValue(JRField jrField) {
        Object retVal = null;
        String hash = jrField.getName() + "_" + curPos;
        if (jrField.getName().equals("schedule")) {
            retVal = this.schedules.get(curPos);
        } else if (jrField.getName().startsWith("totalDays")) {
            retVal = this.days;
        } else if (jrField.getName().equals("showDetail")) {
            retVal = new Boolean(parent.getSortType() == xPrintData.SORT_BY_EMPLOYEE);
        } else if (this.headerValues.get(hash) != null) {
            retVal = this.headerValues.get(hash);
        } else {
            retVal = this.schedules.get(curPos).getFieldValue(jrField);
        }
        return retVal;
    }
    
}
