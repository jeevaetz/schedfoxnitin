/*
 * schedule_client_banned_query.java
 *
 * Created on June 13, 2005, 11:00 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
/**
 *
 * @author ira
 * ONLY USE THIS BANNED THINGY IN SCHEDULE DAMNIT!!!!!!
 *
 */
public class schedule_client_banned_query extends RunQueriesEx {
    
    /** Creates a new instance of schedule_client_banned_query */
    public schedule_client_banned_query() {
        myReturnString = new String();
    }
    
    public void update() {
        
    }
    
    public boolean isBannedQuery() {
        return true;
    }
    
    public String toString() {
        return  " SELECT employee.employee_id as eid, client.client_id as cid FROM employee_banned LEFT JOIN client ON " +
                " client.client_id = employee_banned.client_id LEFT JOIN employee ON employee.employee_id = employee_banned.employee_id " +
                " WHERE client.branch_id = " + getBranch() + 
                " AND client.client_is_deleted = 0 AND employee.employee_hire_date <= " + this.getDriver().getCurrentDateSQL();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
