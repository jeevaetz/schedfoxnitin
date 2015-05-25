/*
 * delete_client_contact_query.java
 *
 * Created on August 4, 2006, 12:42 PM
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
public class delete_client_contact_query extends GeneralQueryFormat {
    
    /** Creates a new instance of delete_client_contact_query */
    public delete_client_contact_query(String contactID) { 
        this.myReturnString = "DELETE FROM client_contact WHERE client_contact_id = " + contactID + ";" +
                              "DELETE FROM client_contact_notes WHERE client_contact_id = " + contactID + ";";
    }
    
    public boolean hasAccess() { return true; }
    
}
