/*
 * availability.java
 *
 * Created on January 25, 2005, 10:25 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.data_connection_types.SShiftServerDataWrapperClass;
import java.util.*;
/**
 *
 * @author Ira Juneau
 * EDIT THIS QUERY AND DIE A SLOW HORRIBLE DEATH!!!!!!!!!!!!!!!!!
 *                            _,.-------.,_
                        ,;~'             '~;, 
                      ,;                     ;,
                     ;                         ;
                    ,'                         ',
                   ,;                           ;,
                   ; ;      .           .      ; ;
                   | ;   ______       ______   ; | 
                   |  `/~"     ~" . "~     "~\'  |
                   |  ~  ,-~~~^~, | ,~^~~~-,  ~  |
                    |   |        }:{        |   | 
                    |   l       / | \       !   |
                    .~  (__,.--" .^. "--.,__)  ~. 
                    |     ---;' / | \ `;---     |  
                     \__.       \/^\/       .__/  
                      V| \                 / |V  
                       | |T~\___!___!___/~T| |  
                       | |`IIII_I_I_I_IIII'| |  
                       |  \,III I I I III,/  |  
                        \   `~~~~~~~~~~'    /
                          \   .       .   /     
                            \.    ^    ./   
                              ^~~~^~~~^   
 */
public class availability extends GeneralQueryFormat {
    private boolean showEmpsThatCanWork;
    private String eid;
    private Vector myVectorOData;
    
    /** Creates a new instance of availability */
    public availability() {
        myReturnString = new String();
        myVectorOData = new Vector();
    }
    
    
    public void update(Vector myVectorOfShifts, boolean showEmpsThatCanWork, String empId) {
        
    }
    
    public void update(Vector myVectorOfShifts, boolean showEmployeesThatCanWork) {
        myVectorOData = myVectorOfShifts;
        showEmpsThatCanWork = showEmployeesThatCanWork;
    }
    
    public void update(String Stime, String Etime, String Doy, boolean showEmployeesThatCanWork) {
        //stime = Stime;
        //etime = Etime;
        //doy = Doy;
        showEmpsThatCanWork = showEmployeesThatCanWork;
    }
    
    public String toString() {
        StringBuilder returnString = new StringBuilder();
        returnString.append("SELECT * FROM ");
        SShiftServerDataWrapperClass currData;
        for (int i = 0; i < myVectorOData.size(); i++) {
            if (i > 0) {
                returnString.append(" JOIN ");
            }
            currData = (SShiftServerDataWrapperClass)myVectorOData.get(i);
            returnString.append("(" + generateSQL(currData.stime, currData.etime, currData.doy) + ") as foo" + i);
            if (i < myVectorOData.size() && i > 0) {
                returnString.append(" ON foo" + i + ".id = foo" + (i - 1) + ".id");
            }
        }
        
        return returnString.toString();
    }

    private String generateSQL(int STIME, int ETIME, String DOY) {
        String exceptString = " Select employee.employee_id as id From employee where employee_is_deleted = 0 And employee.branch_id = " + getBranch() + " Except (";
        String addMasterSource = "";        
        String addTempSource   = "";
        String addMasterAvailS = "";
        String addTempAvailS   = "";
        String empId = "";
        String MasterJoinSQL = "";
        String TempJoinSQL = "";
        StringBuilder myQueryToReturn = new StringBuilder();
        
        if (showEmpsThatCanWork) {
            myQueryToReturn.append(exceptString);
        }
        
        //Show details of conflicts....
        if (!this.showEmpsThatCanWork) {
            addMasterSource = ",'Source: Master Schedule' as source, " +
                              "('Client Name: ' || client.client_name) as cname, " +
                              "schedule_master_start as stime, " +
                              "('Day Of Week: ' || schedule_master_day) as date, " +
                              "schedule_master_end as etime ";
            addTempSource   = ", 'Source: Schedule' as source, " +
                              "('Client Name: ' || client.client_name) as cname, " +
                              "schedule_start as stime, " +
                              "('Date: ' || schedule_date) as date, " +
                              "schedule_end as etime ";
            addMasterAvailS = ", 'Source: Master Availability' as source, " +
                              "'' as cname, " +
                              "avail_m_time_started as stime, " +
                              "('Day Of Week: ' || avail_m_day_of_week) as date, " +
                              "avail_m_time_ended as etime ";
            addTempAvailS   = ", 'Source: Availability' as source, " +
                              "'' as cname, " +
                              "avail_start_time as stime, " +
                              "('Date: ' || avail_day_of_year) as date, " +
                              "avail_end_time as etime ";
            empId = "employee.employee_id = " + eid + " AND ";      
            
            MasterJoinSQL   = " LEFT JOIN client on client.client_id = schedule_master.client_id ";
            TempJoinSQL     = " LEFT JOIN client on client.client_id = schedule.client_id ";
        }
        
        String masterQuery = 
                " (Select employee.employee_id " + addMasterSource +
                " From employee " +
                " Left Join schedule_master on " +
                " schedule_master.employee_id = employee.employee_id  " + MasterJoinSQL +
                " Where " + empId + 
                " employee_is_deleted = 0 And " +
                " employee.branch_id = " + getBranch() + " And " +
                " (((date ('" + DOY + "')) <  schedule_master_date_ended AND " +
                " (((date ('" + DOY + "')) >  schedule_master_date_started)))) AND " +
                " (f_dow2date( date '" + DOY + "' , schedule_master_day) BETWEEN ((date '" + DOY + "' - integer '1'))  AND ((date '" + DOY + "' + integer '1'))) And " +
//                " (((schedule_master_day + 1) >= weekday('" + DOY  + "')) AND  " +
//                " ((schedule_master_day - 1) <= (weekday('" + DOY  + "')))) And " +
                " NOT f_msched_overide(f_dow2date( date '" + DOY + "' , schedule_master_day), schedule_master_id) AND " +
                " f_is_time_overlap( " +
                  STIME + " ,  " +
                  ETIME + " ,  " +
                " schedule_master_start + (1440 * (schedule_master_day - (weekday('" + DOY + "')))),  " +
                " schedule_master_end   + (1440 * (schedule_master_day - (weekday('" + DOY + "'))))))";
          
        String masterAvail = 
                " Select employee.employee_id " + addMasterAvailS +
                " From employee " +
                " Left Join availability_master ON " +
                " employee.employee_id = availability_master.employee_id " + 
                " Where " + empId + 
                " f_check_master_avail(avail_m_id, f_dow2date(date '" + DOY + "' , avail_m_day_of_week)) = 1 AND " +
                " ((date '" + DOY + "' BETWEEN avail_m_date_started  AND avail_m_date_ended)) AND " +
                " (((avail_m_day_of_week + 1) >= (weekday('" + DOY + "'))) AND " +
                " ((avail_m_day_of_week - 1) <= (weekday('" + DOY + "')))) AND " +
                " f_is_time_overlap( " +
                  STIME + " ,  " +
                  ETIME + " ,  " +
                " avail_m_time_started + (1440 * (avail_m_day_of_week - (weekday('" + DOY + "')))),  " +
                " (avail_m_time_ended + (1440 * (avail_m_day_of_week - (weekday('" + DOY + "'))))))";
        
        String tempQuery = 
                " Select employee.employee_id " + addTempSource + 
                " From employee " +
                " Left Join schedule On " +
                " schedule.employee_id = employee.employee_id " + TempJoinSQL +
                " Where " + empId + 
                " employee_is_deleted = 0 And " +
                " employee.branch_id = " + getBranch() + " and " +
                " (schedule_date BETWEEN ((date '" + DOY + "' - integer '1'))  AND ((date '" + DOY + "' + integer '1'))) And " +
                " f_is_time_overlap( " +
                  STIME + " ,  " +
                  ETIME + " ,  " +
                " (schedule_start + (1440 * (schedule_date -  (date('" + DOY + "'))))),  " +
                " (schedule_end + (1440 * (schedule_date - (date('" + DOY + "')))))) ";
        
        String tempAvail = 
                " Select employee_id " + addTempAvailS +
                " From availability  " +
                " Where  " + empId + 
                " date '" + DOY + "'  = avail_day_of_year And  " +
                " (avail_day_of_year BETWEEN ((date '" + DOY + "' - integer '1'))  AND ((date '" + DOY + "' + integer '1'))) And " +
                " avail_type > 1         And " +
                " f_is_time_overlap(" + STIME + "," + ETIME + ", avail_start_time, avail_end_time)";
        
        String union = "  UNION ";
        
        myQueryToReturn.append(tempQuery + union + masterQuery + union + masterAvail + union + tempAvail + ")");
        return myQueryToReturn.toString();
                
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
