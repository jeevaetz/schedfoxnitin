/*
 * employee_note_update.java
 *
 * Created on January 24, 2005, 3:11 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import java.text.SimpleDateFormat;
import java.util.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class employee_note_update extends GeneralQueryFormat {
    
    private ArrayList<Integer> myNoteId;
    private ArrayList<String> myNotes;
    private ArrayList<Integer> myNoteTypes;
    private ArrayList<Date> myDate;
    private ArrayList<Integer> myUid;
    private ArrayList<Integer> empIds;
    private Integer empId;
    
    private static SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    /** Creates a new instance of employee_note_update */
    public employee_note_update(Integer eid) {
        myReturnString = "";
        empId = eid;
        myNoteId = new ArrayList<Integer>();
        myNotes = new ArrayList<String>();
        myNoteTypes = new ArrayList<Integer>();
        myDate = new ArrayList<Date>();
        myUid = new ArrayList<Integer>();
        empIds = new ArrayList<Integer>();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {    
        return "SELECT f_employee_note_update(?, ?, ?, ?, ?, ?, ?);";
    }
    
    public void update(Integer nid, Integer nt, String note, Integer userid, String date){
        myNoteId.add(nid);
        myNotes.add(note.replaceAll("'", "`").replaceAll("\"", "`"));
        myNoteTypes.add(nt);
        try {
            myDate.add(myFormat.parse(date));
        } catch (Exception exe) {
            myDate.add(new Date());
        }
        myUid.add(userid);
        empIds.add(empId);
        
        super.setPreparedStatement(new Object[]{
            convertToPgSqlArray(myNoteId.toArray(new Integer[myNoteId.size()])), convertToPgSqlArray(myUid.toArray(new Integer[myUid.size()])),
            convertToPgSqlArray(myNoteTypes.toArray(new Integer[myNoteTypes.size()])), convertToPgSqlArray(myDate.toArray(new Date[myDate.size()])), 
            convertToPgSqlArray(myNotes.toArray(new String[myNotes.size()])), empId, myNoteId.size() 
            });
    }

    public boolean hasAccess() {
        return true;
    }
    
}
