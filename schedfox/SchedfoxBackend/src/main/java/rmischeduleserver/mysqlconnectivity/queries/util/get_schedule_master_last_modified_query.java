/*
 * get_schedule_master_last_modified_query.java
 *
 * Created on January 18, 2006, 9:14 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_schedule_master_last_modified_query extends GeneralQueryFormat {
    
    /** Creates a new instance of get_schedule_last_modified_query */
    public get_schedule_master_last_modified_query() {
        myReturnString = "";
    }
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        return "SELECT control_db.get_schedule_master_max_mod(?) as maxsched";
    }

    public boolean hasAccess() {
        return true;
    }
    
}
