/*
 * client_note_update_query.java
 *
 * Created on January 26, 2005, 3:48 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.*;
import java.util.*;
/**
 *
 * @author jason.allen
 */
public class client_note_update_query  extends GeneralQueryFormat {
    
    private ArrayList<String> myNoteId;
    private ArrayList<String> myNotes;
    private ArrayList<String> myNoteTypes;
    private ArrayList<String> myDate;
    private ArrayList<String> myUsers;
    private String cliId;
    private String uid;
    
    /** Creates a new instance of client_note_update_query */
    public client_note_update_query(String cid) {
        myReturnString = "";
        cliId = cid;
        myNoteId = new ArrayList();
        myNotes = new ArrayList();
        myNoteTypes = new ArrayList();
        myDate = new ArrayList();
        myUsers = new ArrayList();
    }
    
    public void update(String nid, String nt, String note, String userid, String date){
        myNoteId.add(nid);
        myNotes.add(note);
        myNoteTypes.add(nt);
        myDate.add(date);
        myUsers.add(userid);
    }
    
    public String toString() {
        StringBuilder myBuilder = new StringBuilder();        
        myBuilder.append("DELETE FROM client_notes WHERE client_id = " + cliId + ";");
        for (int i = 0; i < myNoteId.size(); i++) {
            String newId = myNoteId.get(i);

            if (newId.equals("0")) {
                myBuilder.append("INSERT INTO client_notes(user_id, client_id, note_type_id,client_notes_date_time,client_notes_notes) VALUES" +
                        "(" + myUsers.get(i) + "," + cliId + "," + myNoteTypes.get(i) + ",'" + myDate.get(i) + "','" + myNotes.get(i) + "');");
            } else {
                myBuilder.append("INSERT INTO client_notes(client_notes_id, user_id, client_id, note_type_id,client_notes_date_time,client_notes_notes) VALUES" +
                        "(" + newId + "," + myUsers.get(i) + "," + cliId + "," + myNoteTypes.get(i) + ",'" + myDate.get(i) + "','" + myNotes.get(i) + "');");
            }
        }
        return myBuilder.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
