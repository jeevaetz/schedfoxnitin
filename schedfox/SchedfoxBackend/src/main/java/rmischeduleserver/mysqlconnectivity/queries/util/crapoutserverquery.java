/*
 * crapoutserverquery.java
 *
 * Created on October 26, 2005, 10:13 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class crapoutserverquery extends GeneralQueryFormat {
    
    /** Creates a new instance of crapoutserverquery */
    public crapoutserverquery() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  "set local search_path = 'control_db','common_db';update schedule set schedule_start = 200 where schedule_id = 319505;";
    }
    
}
