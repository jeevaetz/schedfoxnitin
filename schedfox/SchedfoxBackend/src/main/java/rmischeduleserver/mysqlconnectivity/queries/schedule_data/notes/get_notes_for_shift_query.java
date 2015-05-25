/*
 * get_notes_for_shift_query.java
 *
 * Created on February 10, 2006, 1:29 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.notes;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_notes_for_shift_query extends GeneralQueryFormat {
    
    private String shiftId;
    
    /** Creates a new instance of get_notes_for_shift_query */
    public get_notes_for_shift_query() {
        
    }
    
    public void update(String shiftId) {
        this.shiftId = shiftId;
    }
    
    public String toString() {
        return "select user_login, shift_notes as notes, shift_notes_date_time as notes_date_time, (CASE WHEN schedule_notes.note_type_id < 0 THEN global_note_types.note_type_name ELSE " +
                "note_type.note_type_name END) as note_type_name from schedule_notes left join control_db.user on control_db.user.user_id = schedule_notes.user_id " +        
               "left join note_type on note_type.note_type_id = schedule_notes.note_type_id left join " + getManagementSchema() + ".global_note_types on global_note_types.note_type_id = " +
                "schedule_notes.note_type_id where shift_id = '" + shiftId + "' Order by shift_notes_date_time DESC;" ;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
