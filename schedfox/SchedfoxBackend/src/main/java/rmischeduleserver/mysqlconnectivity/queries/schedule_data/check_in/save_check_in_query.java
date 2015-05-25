/*
 * save_check_in_query.java
 *
 * Created on January 25, 2005, 12:05 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class save_check_in_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "Insert into checkin (" +
            "shift_id," +
            "checkin_date," +
            "start_time," +
            "end_time," +
            "time_stamp," +
            "person_checked_in," +
            "employee_id, " +
            "checkin_last_updated" +
            ") values ("
            );
    
    /** Creates a new instance of save_check_in_query */
    public save_check_in_query() {
        myReturnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }
    
    public void update(String day, String ShiftId, String Etime, String Stime, String EmpId, 
                       String UserId, String tStamp) {
        StringBuilder sql = new StringBuilder();

        if (ShiftId.indexOf("-") == 0) {
            String smid = ShiftId.substring(1, ShiftId.indexOf("/"));
            sql.append("UPDATE schedule_master SET schedule_master_last_updated = NOW(), last_user_changed = " + UserId + " ");
            sql.append("WHERE ");
            sql.append("schedule_master_id = " + smid + ";");
        } else {
            sql.append("UPDATE schedule SET schedule_last_updated = NOW(), last_user_changed = " + UserId + " ");
            sql.append("WHERE ");
            sql.append("schedule_id = " + ShiftId + ";");
        }
        sql.append(MY_QUERY + "'" + ShiftId + "', '" + day + "', " + Stime + ", " + Etime + ", " +
                         tStamp + ", " + UserId + ", " + EmpId + ", NOW())");
        myReturnString = sql.toString();
    }


    public String toString() {
        return myReturnString;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_CHECK_IN;
    }
}
