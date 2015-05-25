/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import schedfoxlib.controller.GenericControllerInterface;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.notes.get_note_types_query;
import rmischeduleserver.mysqlconnectivity.queries.util.get_current_time_query;
import schedfoxlib.model.NoteType;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class GenericController implements GenericControllerInterface {

    private String companyId;
    private static HashMap<Integer, NoteType> noteTypeCache;
    
    private GenericController(String companyId) {
        this.companyId = companyId;
        if (noteTypeCache == null) {
            noteTypeCache = new HashMap<Integer, NoteType>();
        }
    }
    
    public static GenericController getInstance(String companyId) {
        return new GenericController(companyId);
    }

    public NoteType getNoteTypeByIdFromCache(Integer noteType) {
        if (noteTypeCache.get(noteType) == null) {
            noteTypeCache.clear();
        }
        if (noteTypeCache.size() == 0) {
            ArrayList<NoteType> vals = getAllNoteTypes();
            for (int v = 0; v < vals.size(); v++) {
                noteTypeCache.put(vals.get(v).getNoteTypeId(), vals.get(v));
            }
        }
        return noteTypeCache.get(noteType);
    }
    
    public ArrayList<NoteType> getAllNoteTypes() {
        ArrayList<NoteType> retVal = new ArrayList<NoteType>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_note_types_query mySelectQuery = new get_note_types_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new NoteType(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
    @Override
    public long getCurrentTimeMillis() {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_current_time_query mySelectQuery = new get_current_time_query();
        mySelectQuery.setCompany(companyId);
        mySelectQuery.setPreparedStatement(new Object[]{});
        long retVal = 0;
        try {
            Record_Set rst = conn.executeQuery(mySelectQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getTimestamp(0).getTime();
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }
    
}
