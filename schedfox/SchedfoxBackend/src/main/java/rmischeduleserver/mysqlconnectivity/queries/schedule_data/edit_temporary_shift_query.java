/*
 * edit_temporary_shift_query.java
 *
 * Created on June 15, 2005, 11:40 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class edit_temporary_shift_query extends GeneralQueryFormat {

    private String sid;
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
    private String payOpt;
    private String billOpt;
    private int rateCode;
    
    /** Creates a new instance of edit_temporary_shift_query */
    public edit_temporary_shift_query() {
        myReturnString = new String();
    }
    
    public void update(
        String employee_id, String client_id, String override, String date, String start, String end,
        String day, String type, String group, String master_id, String deleted, String sched_id,
        String pay_opt, String bill_opt, int rate_code
    ){
        emp_id = employee_id;
        cli_id = client_id;
        over = override;
        d = date;
        st = start;
        ed = end;
        dy = day;
        tp = type;
        gp = group;
        mid = master_id;
        del = deleted;
        
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

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT edit_temporary_schedule(" + sid + ", " + emp_id + ", ");
        sql.append(cli_id + ", " + over + "," + 0 + ",'" + d + "'," + st + "," + ed + ",");
        sql.append(dy + "," + tp + "," + gp + "," + mid + "," + del + ",'" + payOpt + "', '" );
        sql.append(billOpt + "', " + rateCode + ", " + getUser() + ");");

        return sql.toString();
    }

    @Override
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
