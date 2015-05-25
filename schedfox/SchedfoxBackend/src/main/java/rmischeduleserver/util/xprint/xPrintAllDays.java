/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.util.xprint;

import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Ira
 */
public class xPrintAllDays implements JRDataSource {
    private Vector<xPrintTotalDay> days;
    private int pos = -1;
    
    public xPrintAllDays() {
        days = new Vector();
    }

    public Boolean hasMoreData() {
        return pos < days.size() - 1;
    }
    
     public boolean next() {
        boolean retVal = false;
        if (hasMoreData()) {
            pos++;
            retVal = true;
        }
        return retVal;
    }
    
    public void addToArray(xPrintDay day, int pos) {
        if (days.size() <= pos || days.get(pos) == null) {
            days.add(pos, new xPrintTotalDay());
        }
        days.get(pos).addDay(day);
    }
    
    public Object getFieldValue(JRField arg0) throws JRException {
        String name = arg0.getName();
        Object retVal = null;
        if (name.startsWith("totalDay")) {
            int pos = Integer.parseInt(name.replaceAll("totalDay", ""));
            retVal = this.days.get(pos);
        }
        return retVal;
    }
    
    
    
}
