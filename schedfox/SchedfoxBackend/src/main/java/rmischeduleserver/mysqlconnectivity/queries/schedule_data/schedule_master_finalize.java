/*
 * schedule_master_finalize.java
 *
 * Created on February 3, 2005, 8:02 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class schedule_master_finalize extends GeneralQueryFormat {

    public final String MY_QUERY = 
        "INSERT INTO schedule ( "  +
        "   client_id, "  +
        "   employee_id, "  +
        "   schedule_override, "  +
        "   schedule_date, "  +
        "   schedule_start, "  +
        "   schedule_end, "  +
        "   schedule_day, "  +
        "   schedule_week, "  +
        "   schedule_type, "  +
        "   schedule_master_id, "  +
        "   schedule_group, "  +
        "   schedule_pay_opt, " +
        "   schedule_bill_opt, " +
        "   rate_code_id, " +
        "   schedule_last_updated "  +
        "  ) "  +
        "SELECT  "  +
        "  schedule_master.client_id, "  +
        "  schedule_master.employee_id, "  +
        "  2, "  +
        "  CURRENT_DATE - 1, "  +
        "  schedule_master.schedule_master_start, "  +
        "  schedule_master.schedule_master_end, "  +
        "  schedule_master.schedule_master_day, "  +
        "  0, "  +
        "  0, "  +
        "  schedule_master.schedule_master_id, "  +
        "  0, "  +
        "  schedule_master.schedule_master_pay_opt, " +
        "  schedule_master.schedule_master_bill_opt, " +
        "  schedule_master.rate_code_id, " +
        "  NOW() "  +
        "FROM "  +
        "  schedule_master "  +
        "Where "  +
        "  schedule_master_id not in (" +
        "      Select schedule_master_id " + 
        "      From schedule " + 
        "      Where schedule_date = CURRENT_DATE - 1" +
        "  ) And "  +
        "  schedule_master_day = (WEEKDAY(CURRENT_DATE - 1)) And "  +
        "  (CURRENT_DATE Between schedule_master_date_started " + 
        "                      And schedule_master_date_ended) "  
     ;
    
    /** Creates a new instance of schedule_master_finalize */
    public schedule_master_finalize() {
        myReturnString = MY_QUERY;
    }
    
    public boolean hasAccess() {
        return true;
    }
}
