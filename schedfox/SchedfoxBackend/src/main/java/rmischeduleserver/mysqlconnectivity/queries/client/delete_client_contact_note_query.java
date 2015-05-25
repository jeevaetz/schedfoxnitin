/*
 * delete_client_contact_note_query.java
 *
 * Created on August 9, 2006, 2:55 PM
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
public class delete_client_contact_note_query extends GeneralQueryFormat {
    
    /** Creates a new instance of delete_client_contact_note_query */
    public delete_client_contact_note_query(String noteId) {
        this.myReturnString = "DELETE FROM client_contact_notes WHERE note_id = " + noteId + ";";
    }
    
    public boolean hasAccess() { return true; }
    
}
