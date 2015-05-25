/*
 * note_type_delete_query.java
 *
 * Created on August 31, 2006, 8:29 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class note_type_delete_query extends GeneralQueryFormat {
    
    /** Creates a new instance of note_type_delete_query */
    public note_type_delete_query(String typeId) {
        this.myReturnString = "DELETE FROM note_type WHERE note_type_id = " + typeId + ";";
    }
    
    public boolean hasAccess() { return true; }
    
}
