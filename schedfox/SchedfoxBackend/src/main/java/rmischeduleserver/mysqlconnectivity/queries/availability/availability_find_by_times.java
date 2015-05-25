/*
 * availability_find_by_times.java
 *
 * Created on February 18, 2005, 7:33 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class availability_find_by_times extends GeneralQueryFormat {
    
    private static final String MY_QUERY_1 = (
            "(SELECT availability.avail_start_time as stime, " +
            " availability.avail_end_time as etime, " +
            " availability.avail_type as type, " +
            " 'TRUE' as temp " +
            " FROM availability " + 
            " WHERE "
            );
    
    private static final String MY_QUERY_2 = (
            ")UNION(" +
            "SELECT availability_master.avail_m_time_started as stime, " +
            "availability_master.avail_m_time_ended as etime, " +
            "1 as type, " +
            " 'FALSE' as temp " +
            " FROM availability_master " +
            " WHERE "
            );
    
    /** Creates a new instance of availability_find_by_times */
    public availability_find_by_times() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    
    public void update(String day, String dayOfWeek, String stime, String etime, String empId) {
        int s = Integer.parseInt(stime) - 1;
        int e = Integer.parseInt(etime) + 1;
        int d = Integer.parseInt(dayOfWeek);
        if (d == 7) {
            dayOfWeek = "0";
        }
        dayOfWeek = d +"";
        if (s > e && e != 1) {
            s -= 1440;
            
        }
        if (e == 1) {
            e = 1441;
        }
        stime = s + "";
        etime = e + "";
        
        myReturnString = MY_QUERY_1 + generateTimeSQL("avail_start_time", "avail_end_time", stime, etime) +
                         " avail_day_of_year = '" + day + "' AND " +
                         " employee_id = " + empId + " AND " +
                         " avail_day_of_year = '" + day + "'" +
                         MY_QUERY_2 + generateTimeSQL("avail_m_time_started", "avail_m_time_ended", stime, etime) +
                         "  '" + day + "' >= avail_m_date_started AND " +
                         "  '" + day + "' <= avail_m_date_ended AND " +
                         " availability_master.employee_id = " + empId + " AND " +
                         "  " + dayOfWeek + " = avail_m_day_of_week AND NOT EXISTS (" +
                         " SELECT availability.avail_day_of_year FROM availability WHERE employee_id = " + empId +
                         " AND avail_day_of_year = '" + day + "' AND " +
                         generateTimeSQL("avail_start_time", "avail_end_time", stime, etime) +
                         " avail_type = 1))";
    }
    
    public String generateTimeSQL(String stable, String etable, String stime, String etime) {
        return  " ('" + stime + "' BETWEEN " + fixStartTime(stable, etable) + " AND " + fixEndTime(etable) + " OR   " +
                "  '" + etime + "' BETWEEN " + fixStartTime(stable, etable) + " AND " + fixEndTime(etable) + ") AND ";
    }
    
    public String fixEndTime(String field) {
        return "CASE WHEN " + field + " = 0 THEN 1440 ELSE " + field + " END"; 
    }
    
    public String fixStartTime(String field, String endTime) {
        return ("CASE WHEN " + field + " > " + endTime + " THEN " + field + " - 1440 ELSE " + field + " END ");
    }
}
