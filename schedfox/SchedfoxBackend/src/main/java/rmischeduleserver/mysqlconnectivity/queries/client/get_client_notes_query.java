/*
 * get_client_notes_query.java
 *
 * Created on February 15, 2006, 1:02 PM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class get_client_notes_query extends GeneralQueryFormat {
    
     private String cid;
    
    /** Creates a new instance of get_client_notes_query */
    public get_client_notes_query() {
    }
    
    public void update(String cid) {
        this.cid = cid;
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    public String toString() {
        return  " select user_login, client_notes_notes as notes, client_notes_date_time as notes_date_time, " +
                " (CASE WHEN client_notes.note_type_id < 0 THEN global_note_types.note_type_name ELSE note_type.note_type_name END) AS note_type_name from client_notes " +
                " left join control_db.user on control_db.user.user_id = client_notes.user_id " +        
                " left join note_type on note_type.note_type_id = client_notes.note_type_id " +
                " left join " + getManagementSchema() + ".global_note_types on global_note_types.note_type_id = " +
                " client_notes.note_type_id " +
                " where client_id = '" + cid + "' Order by client_notes_date_time DESC;" ;
    }
    
    
}
