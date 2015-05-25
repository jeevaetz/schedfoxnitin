/*
 * availability_ranges_query.java
 *
 * Created on January 26, 2005, 3:45 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class availability_ranges_query extends GeneralQueryFormat {
    
    private static final String MY_QUERY = (
            " SELECT " +
            " availability_day_of_year, " +
            " availability_type.availability_type_val as value, " +
            " availability.avail_type " +
            " FROM " +
            " availability " +
            " LEFT JOIN availability_type " +
            " ON availability.avail_type = availability_type.availability_type_id "
    );
    
    private static final String MY_QUERY2 = (
        " and (availability.availability_type_id >=  3 and" +
        " availability.availability_type_id <= 15) "
    );
    
    /** Creates a new instance of availability_ranges_query */
    public availability_ranges_query() {
        myReturnString = new String();
    }
    
    public String toString() {
        return myReturnString;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String EmpId, String sdate, String edate) {
        
    }
    
    private String generateDateSQL(String sdate, String edate) {
        if (sdate.length() > 0 && edate.length() > 0 ) {
            return " and availability_day_of_year >= '" + sdate + "'"
                 + " and availability_day_of_year <= '" + edate + "'";
        } else if (sdate.length() > 0  && edate.length() == 0 ) {
            return " and availability_day_of_year >= '" + sdate + "'";
        }
        return "";
    }
    
}
