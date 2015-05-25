/*
 * update_check_in_out_time_query.java
 *
 * Created on June 30, 2005, 1:16 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class update_check_in_out_time_query extends GeneralQueryFormat {
    
    private String newTime;
    private boolean isCheckIN;
    private String shiftId;
    private String Date;
    private Integer userChanged;
    
    /** Creates a new instance of update_check_in_out_time_query */
    public update_check_in_out_time_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() { 
        return GeneralQueryFormat.UPDATE_SCHEDULE;               
    }
    
    public void update(String newtime, String shiftid,  boolean isCheckIn, String date, Integer userChanged) {
        isCheckIN = isCheckIn;
        newTime = newtime;
        shiftId = shiftid;
        Date = date;
        this.userChanged = userChanged;
    }
    
    public String getMField(String field, String id) {
        return "(SELECT " + field + " FROM schedule_master WHERE schedule_master_id = " + id + ")";
    }

    @Override
    public String toString() {
        String completeQuery = new String();
        if (shiftId.charAt(0) == '-') {
            String m = shiftId.substring(1, shiftId.indexOf('/'));
            String s = getMField("schedule_master_start", m);
            String e = getMField("schedule_master_end", m);
            if (isCheckIN) {
                s = newTime;
            } else {
                e = newTime;
            }
            StringBuffer sql = new StringBuffer();
            sql.append("INSERT INTO schedule ");
            sql.append("(client_id, employee_id, schedule_day, schedule_pay_opt, schedule_is_deleted, ");
            sql.append("schedule_bill_opt, rate_code_id, schedule_start, schedule_end, schedule_date, schedule_master_id, schedule_override, ");
            sql.append("schedule_last_updated, last_user_changed) ");
            sql.append("SELECT client_id, employee_id, schedule_master_day, schedule_master_pay_opt, 0, schedule_master_bill_opt, ");
            sql.append("rate_code_id, " + s + ", " + e + ", '" + Date + "', schedule_master_id, 2, NOW(), " + this.userChanged + " ");
            sql.append("FROM schedule_master ");
            sql.append("WHERE ");
            sql.append("schedule_master_id = " + m + ";");
            sql.append("SELECT MAX (schedule_id) as sid FROM schedule;");
            completeQuery = sql.toString();
        } else {
            if (!isCheckIN) {
                completeQuery = "UPDATE schedule " +
                        "SET schedule_end = " + newTime + ", schedule_last_updated = NOW(), last_user_changed = " + this.userChanged + " " +
                        "WHERE schedule.schedule_id = " + shiftId + "; SELECT " + shiftId + " as sid";
            } else {
                completeQuery = "UPDATE schedule " +
                        "SET schedule_start = " + newTime + ", schedule_last_updated = NOW(), last_user_changed = " + this.userChanged + " " +
                        "WHERE schedule.schedule_id = " + shiftId + "; SELECT " + shiftId + " as sid";
            }
            
        }
        return completeQuery;
    }
    
}
