/*
 * schedule_find_by_times.java
 *
 * Created on February 16, 2005, 1:23 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class schedule_find_by_times extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "( " +
            "  SELECT  " +
            "    schedule_master_start                  as start_time,  " +
            "    schedule_master.schedule_master_id     as sid, " +
            "    schedule_master_end as end_time, " +
            "    client.client_name                     as cname, "+
            "    employee_id                            as eid,  " +
            "    client.client_id                       as cid,  " +
            "    schedule_master_day                    as dow,  " +
            "    schedule_master_date_started           as sdate,  " +
            "    schedule_master_date_ended             as edate,  " +
            "    '1000-10-10'                           as date, " +
            "    '0'                                    as isDeleted  " +
            "  FROM   " +
            "    schedule_master  " +
            " LEFT JOIN client on client.client_id = schedule_master.client_id " +
            "  WHERE "
            );
    
    private static final String MY_QUERY_2 = (
            ")UNION( " +
            "  SELECT  " +
            "    schedule_start               as start_time,  " +
            "    (schedule.schedule_id * -1)  as sid, " +
            "    schedule_end as end_time, " +
            "    client.client_name           as cname, "+
            "    employee_id                  as eid,  " +
            "    client.client_id                    as cid,  " +
            "    schedule_day                 as dow,  " +
            "    '1000-10-10'                 as sdate,  " +
            "    '1000-10-10'                 as edate,  " +
            "    schedule_date                as date,  " +
            "    schedule_is_deleted          as isDeleted  " +
            "  FROM   " +
            "    schedule  " +
            " LEFT JOIN client on client.client_id = schedule.client_id " +
            "  WHERE " +
            "    schedule_is_deleted = 0 And "
            );
    
    /** Creates a new instance of schedule_find_by_times */
    public schedule_find_by_times() {
        myReturnString = new String();
    }
    
    public void update(String day, String dayOfWeek, String stime, String etime, String myId, String empId) {
        int s = Integer.parseInt(stime) - 1;
        int e = Integer.parseInt(etime) + 1;
        if (s > e && e != 1) {
            s -= 1440;
            
        }
        if (e == 1) {
            e = 1441;
            
        }
        stime = s + "";
        etime = e + "";
        myReturnString = MY_QUERY + generateTimeSQL("schedule_master_start", "schedule_master_end"  , stime, etime) +
                                    "  '" + day + "' >= schedule_master_date_started AND " +
                                    "  '" + day + "' <= schedule_master_date_ended AND " +
                                    "  " + dayOfWeek + " = schedule_master_day AND "  +
                                    " schedule_master.schedule_master_id != " + myId + " And " +
                                    " employee_id = " + empId + 
                         MY_QUERY_2 +  generateTimeSQL("schedule_start", "schedule_end", stime, etime) +
                                    " schedule_date = '" + day + "' AND" +
                                    " schedule.schedule_id != " + myId + " AND " + 
                                    " employee_id = " + empId + ")";
                
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
    
    public boolean hasAccess() {
        return true;
    }
    
}
