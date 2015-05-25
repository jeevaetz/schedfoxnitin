/*
 * employee_query.java
 *
 * Created on January 20, 2005, 3:44 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_query extends GeneralQueryFormat {
    
    private String lastUpdated;
    private int employee_id;
    private String showDeleted;
    
    /** Creates a new instance of employee_query */
    public employee_query() {
        myReturnString = new String();
        lastUpdated = new String();
        showDeleted = "false";
    }

    public void update(String LastUpdated, boolean showDeleted) {
        lastUpdated = LastUpdated;
        if (showDeleted) {
            this.showDeleted = "true";
        }
    }

    public void update(String LastUpdated, int employee_id, boolean showDeleted) {
        lastUpdated = LastUpdated;
        this.employee_id = employee_id;
        if (showDeleted) {
            this.showDeleted = "true";
        }
    }

    @Override
    public String toString() {
        String timeStamp = "null";
        if (lastUpdated.length() > 1) {
            timeStamp = "to_timestamp('" + lastUpdated + "', 'YYYY-MM-DD HH24:MI:SS')";
        }
        String branch = "null";
        if (getBranch() != null && !getBranch().equals("<branch>")) {
            branch = getBranch();
        } else if (getBranch().equals("<branch>")) {
            branch = "null";
        }
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * ");
        sql.append("FROM ");
        sql.append("grab_employees(" + branch + ", " + timeStamp + "," + showDeleted + ") ");
        if (employee_id != 0) {
            sql.append("WHERE ");
            sql.append("id = " + employee_id);
        }
        sql.append(" ORDER BY name");
        return sql.toString();
    }
    
    /**
     * Used by our hearbeat...
     */
    @Override
    public void setLastUpdated(String LU) {
        lastUpdated = LU;
    }    
    
    public boolean hasAccess() {
        return true;
    }
    
}
