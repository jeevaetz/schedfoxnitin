/*
 * create_master_shift_query.java
 *
 * Created on June 2, 2005, 10:09 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class create_master_shift_query extends GeneralQueryFormat {
    
    private String client_id;
    private String employee_id;
    private String day;
    private String start;
    private String end;;
    private String group;
    private String shift;
    private String Sdate;
    private String payOpt;
    private String billOpt;
    private int rateCode;
    private String myType;
    private int weeklyRotation;
    
    /** Creates a new instance of create_master_shift_query */
    public create_master_shift_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(
        String Cid, String Eid, String Day, String Start, String End, 
        String Group, String Shift, String sdate, String pay_opt, String bill_opt,
        int rate_code, String type, int weekly_rotation
    ){
        client_id   = Cid;
        employee_id = Eid;
        day         = Day;
        start       = Start;
        end         = End;
        group       = Group;
        shift       = Shift;
        Sdate       = sdate;
        payOpt      = pay_opt;
        billOpt     = bill_opt;
        rateCode    = rate_code;
        myType = type;
        this.weeklyRotation = weekly_rotation;
    }

    @Override
    public String toString() {
        return 
            "INSERT INTO schedule_master " +
            "  (client_id, employee_id, schedule_master_day, schedule_master_start, " +
            "    schedule_master_end, schedule_master_date_started, schedule_master_group, " +
            "    schedule_master_shift, schedule_master_pay_opt, schedule_master_bill_opt, " +
            "    rate_code_id, last_user_changed, schedule_master_type, weekly_num_rotation)" +
            " VALUES (" +
              client_id + ", " + employee_id + ", " + day + ", " + start + ", " + 
              end  + ", (CASE WHEN DATE('" + Sdate + "') < DATE(NOW()) THEN DATE(NOW()) ELSE '" + Sdate + "' END ), " +  group + ", " + 
             shift + ",'" + payOpt + "','" + billOpt + "'," + rateCode + ", " + getUser() + ", " + myType + ", " + this.weeklyRotation + ");";
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
}
