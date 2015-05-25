/*
 * Generic_Report_Schedule_Query.java
 *
 * Created on November 8, 2005, 11:02 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.generic_assemble_schedule_query;
/**
 *
 * @author Ira Juneau
 */
public class Generic_Report_Schedule_Query extends generic_assemble_schedule_query{
    
    /** Creates a new instance of Generic_Report_Schedule_Query */
    public Generic_Report_Schedule_Query() {
        myReturnString = new String();
        super.setOrderByFields(super.myReportOrder);
        super.setSelectedFields(super.myReportFields);
    }
    
}
