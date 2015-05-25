/*
 * schedule_master_delete_query.java
 *
 * Created on January 21, 2005, 1:50 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class schedule_master_delete_query extends GeneralQueryFormat {
    
    String myId;
    String myDate;
    
    /** Creates a new instance of schedule_master_delete_query */
    public schedule_master_delete_query() {
        myReturnString = new String();
    }
    
    public void update(String sched_id) {
        StringTokenizer myToken = new StringTokenizer(sched_id, "/");
        myId = (Integer.parseInt(myToken.nextToken()) * -1) + "";
        myDate = myToken.nextToken();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    public String toString() {
        String createTable = "CREATE TEMPORARY TABLE temp_schedule_master (LIKE schedule_master);";
        String selectInto = "INSERT INTO temp_schedule_master SELECT * FROM schedule_master WHERE " +
                "schedule_master_id  = " + myId + ";";
        String updateInfo = "UPDATE temp_schedule_master SET schedule_master_date_ended  " +
                "= date '" + myDate + "'  - integer '1', schedule_master_last_updated = NOW(), " +
                "last_user_changed = " + getUser() + ";";
        String deleteOldInfo = "DELETE FROM schedule_master WHERE schedule_master_id  = " + myId + ";";
        String insertInfo = "INSERT INTO schedule_master SELECT * FROM temp_schedule_master;";

        //UPDATE INFO IN SCHEDULE SO THAT IT WILL OVERWRITE DATA PROPERLY IN PAST
        String createTempTable = "CREATE TEMPORARY TABLE temp_schedule (LIKE schedule);";
        String selectTempInto = "INSERT INTO temp_schedule SELECT * FROM schedule WHERE schedule_master_id = " + myId + ";";
        String updateTemp = "UPDATE temp_schedule SET schedule_last_updated = NOW();";
        String deleteOldTemp = "DELETE FROM schedule WHERE schedule_master_id = " + myId + ";";
        String insertTempInto = "INSERT INTO schedule SELECT * FROM temp_schedule;";
        String dropAll = "DROP TABLE temp_schedule_master; DROP TABLE temp_schedule;";
        
        return createTable + selectInto + updateInfo + deleteOldInfo + insertInfo + createTempTable + selectTempInto + updateTemp + deleteOldTemp + insertTempInto + dropAll;
    }
    
}
