/*
 * assemble_temp_avail_query.java
 *
 * Created on March 14, 2005, 10:05 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.availability;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class assemble_total_avail_query extends GeneralQueryFormat {
    
    private String empID;
    private String sDate;
    private String eDate;
            
    
    /** Creates a new instance of assemble_temp_avail_query */
    public assemble_total_avail_query() {
        myReturnString = "";
    }
    
    public void update(String empId, String sdate, String edate) {
        empID = empId;
        sDate = sdate;
        eDate = edate;
    }

    @Override
    public String toString() {
        StringBuffer retVal = new StringBuffer();
        String updated = getLastUpdatedString();
        retVal.append("SELECT avail.* FROM ( ");
        retVal.append("     SELECT * FROM ");
        retVal.append("     assemble_temp_availability(DATE('" + sDate + "'), DATE('" + eDate + "')," + getEmployeeString() + ", " + updated + ") ");
        retVal.append("     UNION ");
        retVal.append("     SELECT * FROM ");
        retVal.append("     assemble_perm_availability(DATE('" + sDate + "'), DATE('" + eDate + "')," + getEmployeeString() + ", " + updated + ") ");
        retVal.append(") as avail ");
        retVal.append("LEFT JOIN control_db.\"user\" AS u ON u.user_id = avail.requestedby ");
        retVal.append(getWhereClause());
        retVal.append("ORDER BY doy, requestedon ASC ");
        return retVal.toString();
    }

    public String getWhereClause() { return ""; }

    public String getEmployeeString() {
        String empStr = "null";
        if (empID.trim().length() > 0) {
            empStr = empID;
        }
        return empStr;
    }

    public String getLastUpdatedString() {
        return "null";
    }

    public boolean hasAccess() {
        return true;
    }
    
}
