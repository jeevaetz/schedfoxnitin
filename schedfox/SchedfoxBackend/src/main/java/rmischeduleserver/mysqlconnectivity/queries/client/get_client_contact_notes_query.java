/*
 * get_client_contact_notes_query.java
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
public class get_client_contact_notes_query extends GeneralQueryFormat {

    private String contactID;

    /** Creates a new instance of get_client_contact_notes_query */
    public get_client_contact_notes_query(String contactID) {
        this.contactID = contactID;
    }
    
    public boolean hasAccess() { return true; }

    public String toString() {
        return " SELECT note_id," +
                                    " (user_first_name || ' ' || user_last_name) as user_name," +
                                    " note_timestamp as timestamp," +
                                    " note_subject, " +
                                    " note_text" +
                              " FROM client_contact_notes notes" +
                              " LEFT JOIN " + getManagementSchema() + "." + getDriver().getTableName("user") + " usr" +
                              " ON notes.user_id = usr.user_id" +
                              " WHERE notes.client_contact_id = " + contactID +
                              " ORDER BY timestamp DESC;";
    }
}
