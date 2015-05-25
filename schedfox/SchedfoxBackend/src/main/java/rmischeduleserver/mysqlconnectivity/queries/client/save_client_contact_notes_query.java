/*
 * save_client_contact_notes_query.java
 *
 * Created on August 9, 2006, 1:53 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class save_client_contact_notes_query extends GeneralQueryFormat {
    
    /** Creates a new instance of save_client_contact_notes_query */
    public save_client_contact_notes_query(String noteID, String userID, String contactID, String text, String subject) {
        String delete = "";
        if(!noteID.equals("0"))
            delete = "DELETE FROM client_contact_notes WHERE note_id = " + noteID + ";";
        
        String insert = " INSERT INTO client_contact_notes (user_id, client_contact_id, note_subject, note_text) VALUES (" +
                        userID + ", " + contactID + ", '" + subject + "', '" + text + "');"; 
        
        this.myReturnString = delete + insert;
    }
    
    public boolean hasAccess() { return true; }
}
