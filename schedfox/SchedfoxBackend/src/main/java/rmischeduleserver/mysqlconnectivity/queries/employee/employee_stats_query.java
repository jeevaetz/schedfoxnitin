/*
 * employee_stats_query.java
 *
 * Created on December 8, 2005, 10:09 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
/**
 *
 * @author Ira Juneau
 */
public class employee_stats_query extends generic_assemble_schedule_query {
    
    private String employeeId;
    private String currDay;
    
    /** Creates a new instance of employee_stats_query */
    public employee_stats_query() {
        myReturnString = new String();
    }
    
    public void update(String eid, String today) {
        employeeId = eid;
        currDay = today;
        super.update("", eid, "1900-10-10", today, "", "", false);
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    /**
     * Overload to do funky things like a join on this or so on...
     */
    protected String getFinalSelectStatement() {

        return "SELECT * FROM f_employee_stats_query(" + employeeId + ",'" +
                currDay + "'," + this.getBranch() + ");"; 
    }
}
