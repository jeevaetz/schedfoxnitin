/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.notes.get_note_types_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.NoteType;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class NoteController {

    private String companyId;

    private NoteController(String companyId) {
        this.companyId = companyId;
    }

    public static NoteController getInstance(String companyId) {
        return new NoteController(companyId);
    }

    public ArrayList<NoteType> getNoteTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<NoteType> retVal = new ArrayList<NoteType>();
        get_note_types_query noteTypesQuery = new get_note_types_query();
        noteTypesQuery.setPreparedStatement(new Object[]{});
        noteTypesQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(noteTypesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new NoteType(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
