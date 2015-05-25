/*
 * check_in_query.java
 *
 * Created on January 24, 2005, 9:41 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.*;
/**
 *
 * @author ira
 */
public class check_in_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            " SELECT " +
            " shift_id,           " +
            " checkin_date,              " +
            " start_time,  " +
            " end_time,          " +
            " time_stamp,        " +
            " person_checked_in as pid, " +
            " person_checked_out as pod, " +
            " time_stamp_out,    " +
            " checkin.employee_id,       " +
            " CURRENT_TIMESTAMP as lastupdate, "
            );
    
    private String StartDay;
    private String EndDay;
    private String lastUpdated;
    private String branchId;
    private String whereclause;
    
    /** Creates a new instance of check_in_query */
    public check_in_query() {
        myReturnString = new String();
        branchId = null;
        whereclause = "";
    }
    
    public void update(String startDay, String endDay, String lastupdated) {
        StartDay = startDay;
        EndDay = endDay;
        lastUpdated = lastupdated;
    }

    /**
     * Ok why in gods name am I overriding this function?!? Here is why the heartbeat checks for 
     * the appropriate heartbeat checkin for the given branch and company, however in the checkin I
     * have decided to only run it at the company level so we aren't bouncing back and forth a bunch
     * of data, so this way it will return the checkin heartbeat at the company level...wonderful
     */
    public String getBranch() {
        return "<branch>";
    }
    
    /**
     * Used by our hearbeat...
     */
    @Override
    public void setLastUpdated(String LU) {
        lastUpdated = LU;
    }
    
    public String generateLastUpdated() {
        if (lastUpdated != null && lastUpdated.length() > 0) {
            return " checkin_last_updated >= '" + lastUpdated + "' AND ";
        }
        return "";
    }
    
    /**
     * This is the Check In Query...G...
     */
    public boolean isCheckInQuery() {
        return true;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return MY_QUERY + "(inUser.user_last_name || ', ' || inUser.user_first_name) as username_checked_in, " +
                          "(outUser.user_last_name || ', ' || outUser.user_first_name) as username_checked_out, " +
                          "(employee.employee_last_name || ', ' || employee.employee_first_name) as employee_name " +
                          "FROM " +
                          "checkin " +
                          "LEFT JOIN " + getManagementSchema() + "." + getDriver().getTableName("user") + " inUser ON " +
                          "inUser.user_id = person_checked_in " +
                          "LEFT JOIN " + getManagementSchema() + "." + getDriver().getTableName("user") + " outUser ON " +
                          "outUser.user_id = person_checked_out " +
                          "LEFT JOIN employee ON " +
                          "employee.employee_id = checkin.employee_id " +
                          "WHERE " +
                          generateLastUpdated() +
                          "checkin_date BETWEEN DATE('" + StartDay + "') AND DATE('" + EndDay + "') " +
                          "ORDER BY employee_name;";

    }
    
}
