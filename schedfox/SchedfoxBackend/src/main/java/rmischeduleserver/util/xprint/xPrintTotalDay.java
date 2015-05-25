/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.util.xprint;

import rmischeduleserver.util.xprint.xPrintDay;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Ira
 */
public class xPrintTotalDay implements JRDataSource {

    private Vector<xPrintDay> days;
    private int currPos;
    private boolean firstRun;
    
    public xPrintTotalDay() {
        days = new Vector();
        currPos = 0;
        firstRun = true;
    }
    
    private void initialize() {
        for (int i = 0; i < days.size(); i++) {
            days.get(i).moveFirst();
        }
        firstRun = false;
    }
    
    public void addDay(xPrintDay day) {
        days.add(day);
    }
    
    public boolean hasData() {
        boolean retVal = false;
        for (int i = 0; i < days.size(); i++) {
            if (days.get(i).hasData()) {
                retVal = true;
            }
        }
        return retVal;
    }
    
    public Double getTotal() {
        Double retVal = new Double(0);
        for (int i = 0; i < days.size(); i++) {
            retVal = retVal.doubleValue() + days.get(i).getTotalHours().doubleValue();
        }
        return retVal;
    }
    
    public boolean next() throws JRException {
        boolean retVal = false;
        if (firstRun) {
            initialize();
        } else {
            if (days.size() - 1 >= currPos && days.get(currPos).hasMore()) {
                days.get(currPos).moveNext();
            } else {
                if (currPos <= days.size() - 1) {
                    currPos++;
                }
            }
        }
        if (currPos <= days.size() - 1) {
            retVal = true;
            if (days.get(currPos).getEndTime().length() == 0 && currPos <= days.size() - 1) {
                retVal = next();
            }    
        }
        return retVal;
    }

    public Object getFieldValue(JRField arg0) throws JRException {
        return days.get(currPos).getFieldValue(arg0);
    }

}
