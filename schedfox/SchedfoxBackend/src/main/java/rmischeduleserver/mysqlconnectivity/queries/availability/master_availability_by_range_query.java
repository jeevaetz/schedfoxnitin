/*
 * master_availability_by_range_query.java
 *
 * Created on January 25, 2005, 9:38 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class master_availability_by_range_query extends GeneralQueryFormat {
    
    public static final String QUERY_1 = (
            "Select -avail_m_id as avail_id, " +
            " avail_m_date_started as sdate," +
            " avail_m_date_ended as fdate, " +
            " avail_m_time_started as stime, " +
            " avail_m_time_ended as etime, " +
            " avail_m_day_of_week as dow, " +
            " avail_m_row as avail_row " +
            " from availability_master where "
    );      
    
    /** Creates a new instance of master_availability_by_range_query */
    public master_availability_by_range_query() {
        myReturnString = new String();
    }
    
    public void update(String EmployeeId, String StartDate, String EndDate) {
        myReturnString = myReturnString + QUERY_1 +  buildDateString(StartDate, EndDate) + "employee_id = "  + EmployeeId;
    }
    
    private String buildDateString(String sdate, String edate) {
        if (sdate.length() > 0) {
            return " (('" + sdate + "' <= avail" +
                    "_m_date_ended and '" + sdate + "' >= avail_m_date_started)" +
                    " or ('" + edate + "' <= avail_m_date_ended and '" + edate + "' >= " +
                    "avail_m_date_started) or ('" + edate + "' > avail_m" +
                    "_date_ended and '" + sdate + "' < avail_m_date_started)) and ";
        }
        return "";
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
