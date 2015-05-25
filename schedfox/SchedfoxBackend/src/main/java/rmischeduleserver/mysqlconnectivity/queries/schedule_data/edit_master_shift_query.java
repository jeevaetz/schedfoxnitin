/*
 * edit_master_shift_query.java
 *
 * Created on June 2, 2005, 11:39 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import java.util.StringTokenizer;
/**
 *
 * @author ira
 */
public class edit_master_shift_query extends GeneralQueryFormat {
    
    private String myId;
    private String myDate;
    private String Stime;
    private String Etime;
    private String sft;
    private String payOpt;
    private String billOpt;
    private String Type;
    private int rateCode;
    private int weekRotation = 1;
    
    /** Creates a new instance of edit_master_shift_query */
    public edit_master_shift_query() {
        myReturnString = new String();
    }
    
    public void update(
        String shiftId, String client_id, String employee_id, String schedule_master_day, 
        String schedule_master_start, String schedule_master_end, 
        String group, String shift, String date, String pay_opt, String bill_opt, String type,
        int rate_code, int weekRotation
    ){
        try {
            StringTokenizer myToken = new StringTokenizer(shiftId, "/");
            myId = (Integer.parseInt(myToken.nextToken()) * -1) + "";
            myDate = myToken.nextToken();
        } catch (Exception e) {
            myId = shift;
            myDate = date;
        }
        
        Stime = schedule_master_start;
        Etime = schedule_master_end;
        sft = shift;
        this.weekRotation = weekRotation;
        
        payOpt  = pay_opt;
        billOpt = bill_opt;
        Type = type;

        rateCode    = rate_code;

        if (!sft.equals("0")) {
            myId = sft;
        }
    }
    
    public boolean hasAccess() {
        return true;
    }

    @Override
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT edit_permanent_schedule(" + myId + ", '" + myDate + "', ");
        sql.append(Stime + "," + Etime + "," + sft + ", '" + payOpt + "', '" + billOpt + "', ");
        sql.append(Type + ", " + rateCode + "," + weekRotation + "," + this.getUser() + ")");
        return sql.toString();
    }
    
}
