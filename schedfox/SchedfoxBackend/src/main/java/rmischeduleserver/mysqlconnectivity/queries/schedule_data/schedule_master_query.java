/*
 * schedule_master.java
 *
 * Created on January 19, 2005, 12:35 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author ira
 */
public class schedule_master_query extends GeneralQueryFormat {

    private String clientsSQL;
    private String employeesSQL;
    private String typeSQL;
    private String endWeekSQL;
    private String begWeekSQL;
            
    private static final String MY_QUERY = (
        "Select " +
        "   schedule_master.schedule_master_id     as sid, " +
        "   schedule_master.employee_id            as eid, " +
        "   schedule_master.client_id              as cid, " +
        "   schedule_master.schedule_master_day    as dy, " +
        "   schedule_master.schedule_master_start  as st, " +
        "   schedule_master.schedule_master_end    as ed, " +
        "   schedule_master.schedule_master_group  as gp, " +
        "   schedule_master.schedule_master_shift  as sf, " +
        "   ''                                     as smid, " +
        "   0                                      as tp, " +
        "   ''                                     as dt, " +
        "   schedule_master.schedule_master_date_started  as dsd, " +
        "   schedule_master.schedule_master_date_ended    as ded, " +
        "   schedule_master.schedule_pay_opt              as pay_opt,  " +
        "   schedule_master.schedule_bill_opt             as bill_opt, " +
        "   schedule_master.rate_code_id                  as rate_code_id, " +
        "   Now() as lastupdate  " +            
        " From schedule_master " +
        " LEFT JOIN client ON client.client_id = schedule_master.client_id " +
        " LEFT JOIN employee ON employee.employee_id = schedule_master.employee_id " +
        " Where " +
        "   schedule_master.client_id = client.client_id And client.branch_id = ");
    
    private String clientsToInclude;
    private String employeesToInclude;
    private String type;
    private String begginingWeek;
    private String endWeek;
    private String lastUpdated;
    
    /** Creates a new instance of schedule_master */
    public schedule_master_query() {
        myReturnString = new String();
    }

    /**
     * Query to query schedule_master
     *  clientsToInclude = A comma Delimited list of Customer Ids specifying what customers you want returned
     *  if you don't want to use this parameter pass a blank string...
     *  employeesToInclude = same as clients
     *  type, type to return again if not used pass blank string
     *  BegginingWeek, same rules
     *  EndWeek, more of same
     */
    public void update(String ClientsToInclude, String EmployeesToInclude, String Type, String BegginingWeek, String EndWeek, String LastUpdated) {
        clientsToInclude = ClientsToInclude;
        employeesToInclude = EmployeesToInclude;
        type = Type;
        begginingWeek = BegginingWeek;
        endWeek = EndWeek;
        lastUpdated = LastUpdated;
    }
             
    
    /**
     * Takes comma delimited List and generates SQL for it....fun fun
     */
    private String generateToIncludeSQL(String listToInclude, String fieldToCheck) {
        StringBuffer returnString = new StringBuffer(50);
        if (listToInclude.length() > 0) {
            StringTokenizer myTokenizer = new StringTokenizer(listToInclude, ",");
            if (myTokenizer.countTokens() > 1) {
                returnString.append(" And (");
                while(myTokenizer.hasMoreTokens()) {
                    returnString.append(" schedule_master." + fieldToCheck + " = " + myTokenizer.nextToken());
                    if (myTokenizer.countTokens() >= 1) {
                        returnString.append(" or ");
                    }
                }
                returnString.append(") ");
            } else {
                returnString.append(" And schedule_master." + fieldToCheck + " = " + myTokenizer.nextToken());
            }
        } 
        return returnString.toString().trim();
    }
    
    private String generateLastUpdatedSQL(String lu) {
        if (lu.length() <= 1) {
            return "'1900-01-01'";
        }
//         return "'1900-01-01'";
        return "'" + lu + "'";
    }
    
    /**
     * Generates Type SQL
     */
    private String generateTypeSQL(String type) {
        if (type.length() > 0) {
            return " And ( schedule_master.schedule_type = " + type  + ") ";
        }
        return "";
    }
    
    /*
     * I've commented out the dateranges because it seemed to have broke things
     * I will add it in once I figure out the correct logic for it.
     */
    
    private String generateEndWeek(String endWeek) {
        if (endWeek.length() > 1) {
//            return " And (schedule_master.schedule_master_date_ended <= '" + endWeek + "' or" + 
//                   " schedule_master.schedule_master_date_ended = '0000-00-00')";
        }
        return "";
    }
    
    private String generateBegWeek(String begWeek) {
        if (begWeek.length() > 1) {
//            return " And (schedule_master.schedule_master_date_started >= '" + begWeek + "' or" + 
//                   " schedule_master.schedule_master_date_started = '0000-00-00')";
        }
        return "";
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY + "'" + getBranch() + "' And schedule_master.schedule_master_last_updated >= " +
                         generateLastUpdatedSQL(lastUpdated) + generateTypeSQL(type) + generateToIncludeSQL(clientsToInclude, "client_id") +
                         generateToIncludeSQL(employeesToInclude, "employee_id") + generateBegWeek(begginingWeek) +
                         generateEndWeek(endWeek) + " Order by schedule_master.schedule_master_day, schedule_master.schedule_master_start";

    }

    
}
