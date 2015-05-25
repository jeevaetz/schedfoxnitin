/*
 * schedule_master_update.java
 *
 * Created on January 26, 2005, 12:22 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class schedule_master_update extends GeneralQueryFormat{
    private String xempId;        
    private String xclient_id;  
    private String xstart; 
    private String xend;          
    private String xday;        
    private String xgroup; 
    private String xstartdate;    
    private String xenddate;    
    private String xschedule_id;   
    private String xtmp_sched_id;

    /** Creates a new instance of schedule_master_update */
    public schedule_master_update() {
        myReturnString = "";
    }
    
    public void update(
        String empId,        String client_id,  String start, 
        String end,          String day,        String group, 
        String startdate,    String enddate,    String schedule_id,   
        String tmp_sched_id
    ){
        xend            = end;
        xday            = day;
        xempId          = empId;
        xstart          = start;
        xgroup          = group;
        xenddate        = enddate;
        xclient_id      = client_id;
        xstartdate      = startdate;
        xschedule_id    = schedule_id;
        xtmp_sched_id   = tmp_sched_id;
        
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }

    public boolean hasAccess(){
        return true;
    }        
    
    public String toString(){
        return "Select f_sched_master_update(" +
                xschedule_id      + ", " +
                xtmp_sched_id     + ", " +
                xempId            + ", " +
                xclient_id        + ", " +
                xstart            + ", " +
                xend              + ", " +
                xday              + ", " +
                xgroup            + ", " +
                "date'" + xstartdate + "'," +
                "date '" + xenddate   +
                "') as id";
    }
}
