/*
 * schedule_update_query.java
 *
 * Created on January 21, 2005, 1:10 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class schedule_update_query extends GeneralQueryFormat  {
    
    private static final String MY_QUERY = (
            ""
            );
    
    /** Creates a new instance of schedule_update_query */
    public schedule_update_query() {
        myReturnString = new String();
    }
    
    public void update(String employee_id, String client_id, String override, String date, String start, String end,
                String day, String type, String group, String master_id, String deleted, String sched_id) {
        if (group.length() > 0) {
            group = "0";
        }
            myReturnString = "Select f_update_schedule(" + sched_id + ", " + master_id + ", " + employee_id + ", "
                             + client_id + ", " + override + "," + type + ","  + start + "," + end + "," + group 
                             + "," + day + ",'" + date + "'," + deleted + ") as id";
                             
        /** DEPRICATED !
         * myReturnString = "Insert into schedule " +
                    " (employee_id, client_id, " +
                    "  schedule_override, schedule_date, " +
                    "  schedule_start, schedule_end, " +
                    "  schedule_day, schedule_type, " +
                    "  schedule_group, schedule_master_id, " +
                    "  schedule_is_deleted, schedule_last_updated) " +
                    " values(" + employee_id + ", " + client_id +
                          ", " + override + ", '" + date + "'" +
                          ", " + start + ", " + end + ", " + day +
                          ", " + type + ", " + group + ", " + master_id + 
                          ", " + deleted + ",NOW())";
            myReturnString = "Update schedule set " +
                    " employee_id        = " + employee_id + ", " +
                    " schedule_override  = "+ override +     ", " +
                    " schedule_date      = '" + date +       "'," +
                    " schedule_start     = " + start +       ", " +
                    " schedule_end       = " + end +         ", " +
                    " schedule_day       = " + day +         ", " +
                    " schedule_type      = " + type +        ", " +
                    " schedule_group     = " + group +       ", " +
                    " schedule_is_deleted = " + deleted +    ", " +
                    " schedule_last_updated = NOW()"             +
                    " Where schedule_id = " + sched_id;
         */
        
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }

    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
