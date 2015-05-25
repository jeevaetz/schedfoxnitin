/*
 * genericReportCellTextFormatter.java
 *
 * Created on December 29, 2005, 1:43 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.xprint.templates.genericreportwithsummarycomponents;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.data_connection_types.*;
/**
 *
 * @author Ira Juneau
 * Override this class and use in conjunction with the genericReportFormatter if you 
 * want to do any sort of complicated things....in processing the text for this column...
 */
public class genericReportCellTextFormatter {
    
    private String myDataName;
    
    /** Creates a new instance of genericReportCellTextFormatter */
    public genericReportCellTextFormatter(String dataColumn) {
        myDataName = dataColumn;
    }
    
    /**
     * Create a new class extending this class and override this method if you want
     * to do something other than just get the column
     */
    public String processData(Record_Set rs) {
        return rs.getString(myDataName);
    }
    
    /**
     * Override if you need to do additional testing on this row to see if it should be 
     * displayed or not...
     */
    public boolean displayThisRow(Record_Set rs) {
        return true;
    }
    
}
