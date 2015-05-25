/*
 * delete_temporary_schedule_query.java
 *
 * Created on June 2, 2005, 9:32 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class delete_temporary_schedule_query extends GeneralQueryFormat {
    
    private String ShiftId;
    
    /** Creates a new instance of delete_temporary_schedule_query */
    public delete_temporary_schedule_query() {
        myReturnString = new String();
    }
    
    public void update(String sid) {
        ShiftId = sid;
    }
    
    public String toString() {
        String createTemp = "CREATE TEMPORARY TABLE myTempSched(LIKE schedule);";
        String selectFrom  = "INSERT INTO myTempSched SELECT * FROM schedule WHERE schedule_id = " + ShiftId + ";";
        String update = "UPDATE myTempSched SET schedule_is_deleted = 1, schedule_last_updated = NOW(), " +
                "last_user_changed = " + getUser() + ";";
        String deleteOld = "DELETE FROM schedule WHERE schedule_id = " + ShiftId + ";";
        String insertBack = "INSERT INTO schedule SELECT * FROM myTempSched;";
        String dropTable = "DROP TABLE myTempSched;";
        return createTemp + selectFrom + update + deleteOld + insertBack + dropTable;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
}
