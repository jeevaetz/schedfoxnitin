/*
 * save_schedule_history_query.java
 *
 * Created on December 13, 2005, 9:43 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.history;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class save_schedule_history_query extends GeneralQueryFormat {
    
    String shiftId;
    String cid;
    String eid;
    String date;
    String start;
    String end;
    String day;
    String type;
    String master_id;
    String group;
    String lu;
    String deleted;
    String pay_opt;
    String bill_opt;
    String rate_code;
    
    /** Creates a new instance of save_schedule_history_query */
    public save_schedule_history_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String Shiftid, String Cid, String Eid, String Date, String Start, String End, String Day, String Type, String Masterid,
            String Group, String LU, String Deleted, String Pay, String Bill, String Rate) {
        shiftId = Shiftid;
        cid = Cid;
        eid = Eid;
        date = Date;
        start = Start;
        end = End;
        day = Day;
        type = Type;
        master_id = Masterid;
        group = Group;
        lu = LU;
        deleted = Deleted;
        pay_opt = Pay;
        bill_opt = Bill;
        rate_code = Rate;
    }
    
    public String getNewHistoryId() {
        return ("(CASE WHEN (SELECT MAX(history_id) FROM schedule_history IS NULL) THEN 1 ELSE " +
                "(SELECT MAX(history_id) FROM schedule_history IS NULL) END)");
    }
    
    public String toString() {
//        return "";
        return "INSERT INTO schedule_history (history_id, history_link, schedule_id, client_id, " +
                "employee_id, schedule_date, schedule_start, schedule_end, schedule_day, schedule_type, " +
                "master_id, schedule_group, last_update, isdeleted, pay_opt, bill_opt, rate_code_id) VALUES (" +
                getNewHistoryId() + ", 0, " + shiftId + ", " + cid + ", " + eid + ", '" + date + "', " + start + ", " +
                end + ", " + day + ", " + type + ", " + master_id + ", " + group + ", " + lu + ", " + deleted + ", " +
                pay_opt + ", " + bill_opt + ", " + rate_code + ");";
    }
    
}
