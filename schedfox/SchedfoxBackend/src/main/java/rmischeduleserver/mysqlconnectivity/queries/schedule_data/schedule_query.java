/*
 * schedule_query.java
 *
 * Created on January 20, 2005, 2:48 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author ira
 */
public class schedule_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            "Select " +
            "   schedule.schedule_id           as sid,  " +
            "   schedule.schedule_master_id    as smid, " +
            "   schedule.employee_id           as eid, " +
            "   schedule.client_id             as cid, " +
            "   schedule.schedule_override     as ov,  " +
            "   schedule.schedule_date         as dt,  " +
            "   schedule.schedule_day          as dy,  " +
            "   schedule.schedule_start        as st,  " +
            "   schedule.schedule_end          as ed,  " +
            "   schedule.schedule_week         as wk,  " +
            "   to_char(schedule.schedule_date, 'YYYY')   as maxyr, " +
            "   schedule.schedule_is_deleted   as del, " +
            "   schedule.schedule_group        as gp,  " +
            "   schedule.schedule_type         as tp,  " +
            "   ''                             as dsd, " +
            "   ''                             as ded, " +
            "   schedule.schedule_pay_opt               as pay_opt,  " +
            "   schedule.schedule_bill_opt              as bill_opt, " +
            "   schedule.rate_code_id          as rate_code_id, " +
            "   Now() as lastupdate  " +
            " From schedule " +
            " LEFT JOIN client ON client.client_id = schedule.client_id " +
            " LEFT JOIN employee ON employee.employee_id = schedule.employee_id " +
            " Where "
            );
    
    private String employees;
    private String clients;
    private String type;
    private String beginWeek;
    private String endWeek;
    private String lastUpdated;
    
    /** Creates a new instance of schedule_query */
    public schedule_query() {
        myReturnString = new String();
    }

    public boolean hasAccess() {
        return true;
    }
    
    public void update(String Employees, String Clients, String Type, String BeginWeek, String EndWeek, String LastUpdated) {
        employees = Employees;
        clients = Clients;
        type = Type;
        beginWeek = BeginWeek;
        endWeek = EndWeek;
        lastUpdated = LastUpdated;
    }
    
    
    /**
     * Generates Type SQL
     */
    private String generateTypeSQL(String type) {
        if (type.length() > 0) {
            return " And ( schedule.schedule_type = " + type  + ") ";
        }
        return "";
    }

    private String generateLastUpdatedSQL(String lu) {
        if (lu.length() <= 1) {
            return "'1900-01-01'";
        }
        return "'" + lu + "'";
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
                    returnString.append(" schedule." + fieldToCheck + " = " + myTokenizer.nextToken());
                    if (myTokenizer.countTokens() >= 1) {
                        returnString.append(" or ");
                    }
                }
                returnString.append(") ");
            } else {
                returnString.append(" And schedule." + fieldToCheck + " = " + myTokenizer.nextToken());
            }
        } 
        return returnString.toString().trim();
    }
    
    
    public String toString() {
        return MY_QUERY + " NOT (schedule.schedule_date <= " + "'" + beginWeek + "' OR schedule.schedule_date >= '" +
                         endWeek + "') And schedule.schedule_last_updated >= " + generateLastUpdatedSQL(lastUpdated) +
                         " And client.branch_id = "  + getBranch() + " " +
                         generateToIncludeSQL(clients, "client_id") + 
                         generateToIncludeSQL(employees, "employee_id") +
                         generateTypeSQL(type);
    }
    
}
