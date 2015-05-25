/*
 * totals_by_location_query.java
 *
 * Created on December 12, 2005, 9:03 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
/**
 *
 * @author Ira Juneau
 */
public class totals_by_location_query extends generic_assemble_schedule_query {
    
    private String Sdate;
    
    /** Creates a new instance of over_under_report_query */
    public totals_by_location_query() {
        myReturnString = new String();
        super.setSelectedFields(super.myOverUnderReportFields);
    }
    
    public void update(String sdate, String edate) {
        super.update("", "", sdate,edate, "", "", false);
        Sdate = sdate;
        super.ShowDeleted = false;
    }
    
    @Override
    public String getBasicSelect() {
        return " ";
    }
   
    @Override
    public String additionalFields() {
        StringBuffer sql = new StringBuffer();
        sql.append(" SUM((CASE WHEN end_time <= start_time THEN end_time + 1440 ELSE end_time END) - start_time), date ");
        return sql.toString();
        
    }

    @Override
    public String generateOrderBy() {
        StringBuffer sql = new StringBuffer();
        sql.append("GROUP BY date ");
        sql.append("ORDER BY date");
        return sql.toString();
    }
}
