/*
 * overtime_report_query.java
 *
 * Created on December 28, 2005, 1:16 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.reports;
import java.util.*;
/**
 *
 * @author Ira Juneau
 */
public class overtime_report_query extends over_under_report_query {
    
    /** Creates a new instance of overtime_report_query */
    public overtime_report_query() {
    }
    
    public String generateOrderBy() {
        String myOrder = super.generateOrderBy();
        String first = myOrder.substring(0, myOrder.indexOf("GROUP BY"));
        String last = myOrder.substring(myOrder.indexOf("GROUP BY") + 8, myOrder.length());
        return first + " AND total_time > 40 GROUP BY " + last;
    }
    
}
