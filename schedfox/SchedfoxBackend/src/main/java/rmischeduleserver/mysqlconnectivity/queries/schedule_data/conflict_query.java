/*
 * conflict_query.java
 *
 * Created on April 11, 2005, 7:50 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.conflict_sub_query;
/**
 *
 * @author ira
 */
public class conflict_query extends GeneralQueryFormat {
    
    private String edate;
    private String sdate;
    private String branch;
    private conflict_sub_query mySubQuery;
    
    /** Creates a new instance of conflict_query */
    public conflict_query() {
        myReturnString = new String();
    }
    
    public void update(String Sdate, String Edate, String Branch) {
        sdate = Sdate;
        edate = Edate;
        branch = Branch;
        mySubQuery = new conflict_sub_query();
        mySubQuery.update(Sdate, Edate, Branch);
    }
    
    public void update(String Sdate, String Edate, String Branch, String empId) {
        sdate = Sdate;
        edate = Edate;
        branch = Branch;
        mySubQuery = new conflict_sub_query();
        mySubQuery.update(Sdate, Edate, Branch, empId);
    }
    
    public String toString() {
        return "SELECT " +
               "Query1.dow as dow1, " +
               "Query2.dow as dow2, " +
               "Query1.schedule_id as schedule_id1, " +
               "Query2.schedule_id as schedule_id2, " +
               "Query1.client_name as cname1, " +
               "Query2.client_name as cname2, " +
               "Query1.doy as doy1, " +
               "Query2.doy as doy2, " +
               "Query1.stime as stime1, " +
               "Query2.stime as stime2, " +
               "Query1.etime as etime1, " +
               "Query2.etime as etime2 " +
               ", Query1.schedule_master_id as schedule_master_id1, " +
               "Query2.schedule_master_id as schedule_master_id2 " + 
               " FROM (" + mySubQuery.toString() + ") as Query1 FULL JOIN (" + mySubQuery.toString() + ") as Query2 " +
               "ON " +
               "Query1.emp_id = Query2.emp_id AND " +
               "Query1.doy = Query2.doy WHERE " +
               "f_is_time_overlap(Query1.stime, Query1.etime, Query2.stime, Query2.etime) AND " +
               "(Query1.schedule_id <> Query2.schedule_id or Query1.schedule_master_id <> Query2.schedule_master_id)";

    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
