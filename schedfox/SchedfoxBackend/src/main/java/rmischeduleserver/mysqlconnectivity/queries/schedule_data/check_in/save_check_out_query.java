/*
 * save_check_out_query.java
 *
 * Created on March 29, 2005, 7:56 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class save_check_out_query extends GeneralQueryFormat {
    
    String shiftId; 
    String tstamp;
    /** Creates a new instance of save_check_out_query */
    public save_check_out_query() {
        myReturnString = new String();
    }
 
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String ShiftId, String tStamp) {
        shiftId = ShiftId;
        tstamp = tStamp;
    }
    
    public String toString() {
        String createTempTable = "CREATE TEMPORARY TABLE myCheckOutTable(Like checkin) ON COMMIT DROP;";
        String selectInto = "INSERT INTO myCheckOutTable SELECT * FROM checkin WHERE shift_id = '" + shiftId + "';";
        String updateTemp = "UPDATE myCheckOutTable SET time_stamp_out = " + tstamp + ", person_checked_out = " + getUser() + ", checkin_last_updated = NOW();";
        String deleteOld = "DELETE FROM checkin WHERE shift_id = '" + shiftId + "';";
        String selectIntoPerm = "INSERT INTO checkin SELECT * FROM myCheckOutTable;";
        return createTempTable + selectInto + updateTemp + deleteOld + selectIntoPerm;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_CHECK_IN;
    }   
}
