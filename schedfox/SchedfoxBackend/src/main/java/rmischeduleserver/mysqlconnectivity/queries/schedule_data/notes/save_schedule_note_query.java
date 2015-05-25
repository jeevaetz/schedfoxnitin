/*
 * save_schedule_note_query.java
 *
 * Created on February 8, 2006, 12:37 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.notes;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author Ira Juneau
 */
public class save_schedule_note_query extends GeneralQueryFormat {
    
    private String shiftId;
    private String note;
    private String noteType;
    
    /** Creates a new instance of save_schedule_note_query */
    public save_schedule_note_query() {
    }
    
    public void update(String shiftId, String note, String noteType) {
        this.shiftId = shiftId;
        this.note = note;
        this.noteType = noteType;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public int getUpdateStatus() {
        return GeneralQueryFormat.UPDATE_SCHEDULE;
    }
    
    @Override
    public String toString() {
        return "INSERT INTO schedule_notes (note_id, shift_id, user_id, note_type_id, shift_notes, shift_notes_date_time) VALUES (" +
                "(CASE WHEN (SELECT MAX(note_id) FROM schedule_notes) IS NULL THEN 0 ELSE (SELECT (MAX(note_id) + 1) FROM schedule_notes) END), " +
                "'" + shiftId + "', " + getUser() + ", " + noteType + ", '" + note + "', NOW());";
    }
    
    
}
