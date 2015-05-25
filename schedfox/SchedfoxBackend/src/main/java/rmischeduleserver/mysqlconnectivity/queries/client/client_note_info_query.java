/*
 * client_note_info_query.java
 *
 * Created on January 26, 2005, 3:38 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author jason.allen
 */
public class client_note_info_query extends GeneralQueryFormat {
    private final static String MY_QUERY =
        " Select " +
        "  client_notes.client_notes_notes as notes, " +
        "  client_notes.note_type_id       as ntid " +     
        " From client_notes "   +
        " Where client_notes.client_notes_id = ";
         
    private String cid;
    
    /** Creates a new instance of client_note_info_query */
    public client_note_info_query() {
        myReturnString = "";
    }
    
    public void update(String Cid){
        cid = Cid;
    }
    
    public String toString() {
        return MY_QUERY + cid;
    }
    
    public boolean hasAccess() {
        return true;
    }
}
