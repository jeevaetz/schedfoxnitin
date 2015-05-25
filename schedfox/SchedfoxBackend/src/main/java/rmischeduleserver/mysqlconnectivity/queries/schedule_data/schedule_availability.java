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
public class schedule_availability extends GeneralQueryFormat {
    private boolean showEmpsThatCanWork;
    private String eid;
    private Vector myVectorOData;
    private int buffer = (7 * 60);
    
    /** Creates a new instance of availability */
    public schedule_availability() {
        myReturnString = new String();
        myVectorOData = new Vector();
    }
    
    public void setBuffer(int b){
        buffer = b * 60;
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
        if (ETIME < STIME) {
            ETIME += 1440;
        }
        STIME -= buffer;
        ETIME += buffer;
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
                " (SELECT DISTINCT employee.employee_id " + addMasterSource +
                " FROM generate_date_series (date ('" + DOY + "') - integer '1', date ('" + DOY + "') + integer '1') as s(doy) " +
                " LEFT JOIN schedule_master ON " +
                " schedule_master_day = dow AND " +
                " schedule_master_date_started <= doy AND " +
                " schedule_master_date_ended >= doy " +
                " AND doy >= date(NOW()) " +
                " LEFT JOIN employee ON " +
                " employee.employee_id = schedule_master.employee_id " +
                " Where " + empId +
                " employee_is_deleted = 0 And " +
                " employee.branch_id = " + getBranch() + " And " +
                "    (NOT EXISTS  " +
                "      (Select schedule_master_id  " +
                "       From schedule  " +
                "       Where schedule_date = doy AND schedule.schedule_override > 0 AND schedule.schedule_master_id = schedule_master.schedule_master_id " +
                "      ) " +
                "    ) AND " +
                " ((date ('" + DOY + "') + interval '1 min' * " + STIME + "), (date ('" + DOY + "') + interval '1 min' * " + ETIME + ")) OVERLAPS " +
                " ((date (doy) + interval '1 min' * (schedule_master_start * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))), " +
                " (date (doy) + interval '1 min' * ((schedule_master_end + (CASE WHEN schedule_master_end < schedule_master_start THEN 1440 ELSE 0 END )) * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))))) ";
        
        String masterAvail = 
                " (Select DISTINCT employee.employee_id " + addMasterAvailS +
                " FROM generate_date_series (date ('" + DOY + "') - integer '1', date ('" + DOY + "') + integer '1') as s(doy) " +
                " LEFT JOIN availability_master ON " +
                " availability_master.avail_m_day_of_week = dow AND " +
                " availability_master.avail_m_date_started <= doy AND " +
                " availability_master.avail_m_date_ended >= doy " +
                " LEFT JOIN employee ON " +
                " employee.employee_id = availability_master.employee_id " +
                " WHERE " + empId +
                " employee_is_deleted = 0 AND " +
                " employee.branch_id = " + getBranch() + " AND " + 
                "    (NOT EXISTS  " +
                "      (Select avail_shift " +
                "       From availability  " +
                "       Where avail_day_of_year = doy " +
                "      ) " +
                "    ) AND " +
                " ((date ('" + DOY + "') + interval '1 min' * " + STIME + "), (date ('" + DOY + "') + interval '1 min' * " + ETIME + ")) OVERLAPS " +
                " ((date (doy) + interval '1 min' * (avail_m_time_started * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))), " +
                " (date (doy) + interval '1 min' * ((avail_m_time_ended + (CASE WHEN avail_m_time_ended < avail_m_time_started THEN 1440 ELSE 0 END )) * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))))) ";
       
        String tempQuery = 
                " Select DISTINCT employee.employee_id " + addTempSource + 
                " FROM generate_date_series (date ('" + DOY + "') - integer '1', date ('" + DOY + "') + integer '1') as s(doy) " +
                " LEFT JOIN schedule ON " +
                " schedule.schedule_date = doy AND " +
                " schedule.schedule_is_deleted = 0 " +
                " LEFT JOIN employee " +
                " ON employee.employee_id = schedule.employee_id " +
                " Where " + empId + 
                " employee_is_deleted = 0 And " +
                " employee.branch_id = " + getBranch() + " AND " +
                " ((date ('" + DOY + "') + interval '1 min' * " + STIME + "), (date ('" + DOY + "') + interval '1 min' * " + ETIME + ")) OVERLAPS " +
                " ((date (doy) + interval '1 min' * (schedule_start * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))), " +
                " (date (doy) + interval '1 min' * (((schedule_end + (CASE WHEN schedule_end < schedule_start THEN 1440 ELSE 0 END )) * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))))) ";
        
        
        String tempAvail = 
                " (Select DISTINCT employee.employee_id " + addTempAvailS +
                " FROM generate_date_series (date ('" + DOY + "') - integer '1', date ('" + DOY + "') + integer '1') as s(doy) " +
                " LEFT JOIN availability ON " +
                " availability.avail_day_of_year = doy AND " +
                " availability.avail_type > 0 " +
                " LEFT JOIN employee " +
                " ON employee.employee_id = availability.employee_id " +
                " Where  " + empId + 
                " employee_is_deleted = 0 And " +
                " employee.branch_id = " + getBranch() + " AND " +
                " ((date ('" + DOY + "') + interval '1 min' * " + STIME + "), (date ('" + DOY + "') + interval '1 min' * " + ETIME + ")) OVERLAPS " +
                " ((date (doy) + interval '1 min' * (avail_start_time * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END))), " +
                " (date (doy) + interval '1 min' * (((avail_end_time + (CASE WHEN avail_end_time < avail_start_time THEN 1440 ELSE 0 END )) * (CASE WHEN date '" + DOY + "' - doy >= 0 THEN 1 ELSE -1 END)))))) ";

        String union = "  UNION ";
        
        myQueryToReturn.append(tempQuery + union + masterQuery + union + masterAvail + union + tempAvail + ")");
        //myQueryToReturn.append(tempQuery + union + masterQuery + ")");
        //myQueryToReturn.append(masterQuery + ")");
        return myQueryToReturn.toString();
                
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
