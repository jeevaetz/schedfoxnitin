/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.xprint.data;

import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

/**
 *
 * @author Ira
 */
public class xGenericSubReportData implements JRDataSource {

    private Vector<CrossTabRow> innerData;
    private xGenericReportData parent;
    private int currPosition;

    private boolean inData = false;

    public xGenericSubReportData(xGenericReportData parent) {
        currPosition = 0;
        innerData = new Vector();
        this.parent = parent;
    }

    public void add(CrossTabRow row) {
        innerData.add(row);
    }

    public int size() {
        return innerData.size();
    }

    public CrossTabRow getCurrentRow() {
        return innerData.get(currPosition);
    }

    public boolean next() throws JRException {
        boolean ret_val = false;
        if (innerData.size() - 1 > currPosition && inData) {
            ret_val = true;
            currPosition++;
        }
        if (!inData) {
            ret_val = true;
        }
        return ret_val;
    }

    public Object getFieldValue(JRField arg0) {
        Object retVal = new String();
        String fieldName = arg0.getName();
        if (fieldName.equals("columnHeaders")) {
            retVal = getCurrentRow().getColumnHeader();
        } else if (fieldName.equals("rowHeader")) {
            retVal = getCurrentRow().getRowHeader();
        } else if (fieldName.equals("cellData")) {
            inData = true;
            if (parent.getFormatters().get(getCurrentRow().getColumnHeader()) != null) {
                retVal = parent.getFormatters().get(getCurrentRow().getColumnHeader()).formatInputString(getCurrentRow().getRowData());
            } else {
                retVal = getCurrentRow().getRowData();
            }
        }
        return retVal;
    //throw new UnsupportedOperationException("Not supported yet.");
    }
}
