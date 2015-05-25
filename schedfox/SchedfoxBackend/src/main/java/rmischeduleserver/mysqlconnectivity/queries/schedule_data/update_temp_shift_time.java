/*
 * update_temp_shift_time.java
 *
 * Created on February 13, 2006, 1:21 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class update_temp_shift_time extends GeneralQueryFormat {
    
    private String shiftId;
    
    /** Creates a new instance of update_temp_shift_time */
    public update_temp_shift_time() {
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String shiftId) {
        this.shiftId = shiftId;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    public String toString() {
        return "UPDATE schedule SET schedule_last_updated = NOW() WHERE schedule_id = " + shiftId + ";";
    }
    
}
