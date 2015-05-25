/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.xprint.data;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Vector;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;
import rmischedule.main.Main_Window;
import rmischedule.xprint.templates.genericreportcomponents.formatterClass;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author Ira
 */
public class xGenericReportData implements JRDataSource {

    private String[] columnHeaders;
    private String[] rowDefinitions;
    private String[] titles;
    private Hashtable<String, formatterClass> formatters;
    private Vector<xGenericSubReportData> outerData;

    private CrossTabRow currRow;
    
    private int outerPosition = 0;
    private int row = 0;

    public xGenericReportData(Record_Set data, String[] columnHeaders, String[] rowDefinitions, String[] titles, Hashtable<String, formatterClass> formatters) {
        outerData = new Vector(data.length());
        this.initialize(columnHeaders, rowDefinitions, titles, formatters);
        this.processRecordSet(data);

    }

    public xGenericReportData(ArrayList data, String[] columnHeaders, String[] rowDefinitions, String[] titles, Hashtable<String, formatterClass> formatters) {
        outerData = new Vector();
        this.initialize(columnHeaders, rowDefinitions, titles, formatters);
        for (int i = 0; i < data.size(); i++) {
            this.processRecordSet((Record_Set) data.get(i));
        }
    }

    private void initialize(String[] columnHeaders, String[] rowDefinitions, String[] titles, Hashtable<String, formatterClass> formatters) {
        this.columnHeaders = columnHeaders;
        this.rowDefinitions = rowDefinitions;
        this.formatters = formatters;
        this.titles = titles;

        Main_Window.lastColumnsForGenericReport = this.columnHeaders;
    }
    
    public java.util.Hashtable<String, formatterClass> getFormatters() {
        return this.formatters;
    }

    private void processRecordSet(Record_Set data) {
        if (data.length() > 0) {
            xGenericSubReportData subData = new xGenericSubReportData(this);
            do {
                for (int i = 0; i < columnHeaders.length; i++) {
                    subData.add(new CrossTabRow(row, columnHeaders[i], data.getString(rowDefinitions[i])));
                }
                row++;
            } while (data.moveNext());
            outerData.add(subData);
        }
    }

    public String[] getColumnHeaders() {
        return columnHeaders;
    }

    public boolean next() throws JRException {
        boolean retVal = false;
        //Check the outer index.
        if (outerPosition <= outerData.size() - 1) {
            xGenericSubReportData innerData = outerData.get(outerPosition);
            retVal = retVal || innerData.next();
            currRow = innerData.getCurrentRow();
            if (!retVal) {
                do {
                    outerPosition++;
                } while (outerPosition < outerData.size() - 1 && outerData.get(outerPosition).size() == 0);
            }
            if (outerPosition < outerData.size() - 1) {
                retVal = true;
            }
        }
        return retVal;
    }

    public Object getFieldValue(JRField fieldRequested) throws JRException {
        String fieldName = fieldRequested.getName();
        Object retVal = null;
        if (fieldName.equals("subreportData")) {
            retVal = outerData.get(outerPosition);
        } else if (fieldName.equals("title")) {
            retVal =  this.titles[this.outerPosition];
        }
        return retVal;
    }
}
