/*
 * update_master_shift_time.java
 *
 * Created on February 13, 2006, 1:18 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class update_master_shift_time extends GeneralQueryFormat {
    
    private String shiftId;
    
    /** Creates a new instance of update_master_shift_time */
    public update_master_shift_time() {
        
    }
    
    public void update(String shiftId) {
        this.shiftId = shiftId;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    public String toString() {
        return "UPDATE schedule_master SET schedule_master_last_updated = NOW() WHERE schedule_master_id = " + shiftId + ";";
    }
    
}
