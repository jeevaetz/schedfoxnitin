/*
 * override_schedule_master_for_day_query.java
 *
 * Created on June 2, 2005, 8:57 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class override_schedule_master_for_day_query extends GeneralQueryFormat {
    
    private String myId;
    private String myDate;
    private String EID;
    private String CID;
    
    /** Creates a new instance of override_schedule_master_for_day_query */
    public override_schedule_master_for_day_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * Should be passed the id for the master shift, which consists of the id then the date 
     * seperated by a / ie
     * 5201/2005-01-06
     *
     * If shouldOverride then we overwrite the master with temp data, else delete the temp
     * and reapply the Master for that day...fun fun...
     */
    public void update(String masterIdAndDate, String eid, String cid) {
        StringTokenizer myToken = new StringTokenizer(masterIdAndDate, "/");
        myId = (Integer.parseInt(myToken.nextToken()) * -1) + "";
        myDate = myToken.nextToken();
        EID = eid;
        CID = cid;
    }
 
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    public String toString() {
        return ("INSERT INTO schedule (client_id, employee_id, schedule_override, schedule_date, schedule_start, schedule_end," +
                "schedule_day, schedule_week, schedule_type, schedule_master_id, schedule_group, schedule_is_deleted, " +
                "schedule_last_updated, last_user_changed) " +
                "VALUES (" + CID + ", "+ EID + ", 1, '" + myDate + "', 0, 0, 0, 0, 0, " + myId + ", 0, 1, NOW(), " + getUser() + ");");
    }
    
}
