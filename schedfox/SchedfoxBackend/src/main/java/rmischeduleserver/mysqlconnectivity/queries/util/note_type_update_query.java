/*
 * note_type_update_query.java
 *
 * Created on January 26, 2005, 1:36 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.util;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class note_type_update_query extends GeneralQueryFormat {
    
    /** Creates a new instance of note_type_update_query */
    public note_type_update_query(String noteId, String noteName) {
        if(noteId.length() == 0)
            this.myReturnString = "INSERT INTO note_type (note_type_name) VALUES ('" + noteName + "');";
        else
            this.myReturnString = "UPDATE note_type SET note_type_name = '" + noteName+ "' WHERE note_type_id = " + noteId + ";";
    }
    
    public boolean hasAccess() { return true; }   
}
