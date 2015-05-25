/*
 * create_temporary_shift_query.java
 *
 * Created on June 2, 2005, 12:16 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class create_temporary_shift_query extends GeneralQueryFormat {
    
    private String emp_id;
    private String cli_id;
    private String over;
    private String d;
    private String st;
    private String ed;
    private String dy;
    private String tp;
    private String gp;
    private String mid;
    private String del;
    private String sid;
    private String payOpt;
    private String billOpt;
    private int rateCode;
    
    /** Creates a new instance of create_temporary_shift_query */
    public create_temporary_shift_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(
        String employee_id, String client_id, String override, String date, 
        String start, String end, String day, String type, String group, 
        String master_id, String deleted, String sched_id, String pay_opt,
        String bill_opt, int rate_code
    ){

        emp_id = employee_id;
        cli_id = client_id;
        over   = override;
        d      = date;
        st     = start;
        ed     = end;
        dy     = day;
        tp     = type;
        gp     = group;
        mid    = master_id;
        del    = deleted;
        
        payOpt      = pay_opt;
        billOpt     = bill_opt;

        rateCode    = rate_code;
        
        try {
            Integer.parseInt(sched_id);
            sid = sched_id;
        } catch (Exception e) {
            sid = "";      
        }
    }
    
    public String toString() {
        String schedfield = "schedule_id";
        String normSid = sid;        
        String deleteString = "";
        if (sid.length() == 0 || sid.equals("0") || sid.equals("")) {
            sid = "";
            normSid = "";
            schedfield = "";
        } else {
            //sid = sid + ", ";
            schedfield = schedfield + ",";
            deleteString = "DELETE FROM schedule WHERE schedule_id = " + normSid + ";";
            normSid += ", ";
        }
        return  deleteString + 
                "INSERT into schedule " + 
                "  (" + schedfield + " client_id, employee_id, " +
                "    schedule_override, schedule_date, schedule_start, " + 
                "    schedule_end, schedule_day, schedule_week, schedule_type, " + 
                "    schedule_master_id, schedule_group, schedule_last_updated, " + 
                "    schedule_is_deleted, schedule_pay_opt, schedule_bill_opt, " + 
                "    rate_code_id, last_user_changed, history_link_id)" +
                " VALUES (" + normSid + cli_id + ", " + emp_id + ", " + 
                              over + ", '" + d + "', " + st + ", " + 
                              ed + ", " + dy + ", 0, " + tp + ", " + 
                              mid + ", " + gp + ", NOW(), " + 
                              del + ",'" + payOpt + "','" + billOpt + "'," +
                              rateCode + ", " + getUser() + ", " +
                              "(CASE WHEN (SELECT MAX(history_group_id) FROM schedule_history) IS NULL THEN 1 ELSE " +
                              "((SELECT MAX(history_group_id) FROM schedule_history) + 1) END));";
      
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
}
