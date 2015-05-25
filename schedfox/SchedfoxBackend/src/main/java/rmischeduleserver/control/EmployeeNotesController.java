/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.text.SimpleDateFormat;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.employee.delete_employee_note_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.EmployeeNotes;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_note_update;
import rmischeduleserver.mysqlconnectivity.queries.employee.get_notes_by_type_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.save_employee_note_query;

/**
 *
 * @author user
 */
public class EmployeeNotesController {

    private String companyId;

    private EmployeeNotesController(String companyId) {
        this.companyId = companyId;
    }

    public static EmployeeNotesController getInstance(String companyId) {
        return new EmployeeNotesController(companyId);
    }

    public int getEmployeeNoteType(String noteType) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        int retVal = 0;

        get_notes_by_type_query notesQuery = new get_notes_by_type_query();
        notesQuery.setPreparedStatement(new Object[]{noteType});
        notesQuery.setCompany(companyId);

        Record_Set rst = null;
        try {
            rst = conn.executeQuery(notesQuery, "");

            retVal = rst.getInt("note_type_id");
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public void deleteEmployeeNote(EmployeeNotes empNote) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        delete_employee_note_query noteUpdate = new delete_employee_note_query();
        noteUpdate.setPreparedStatement(new Object[]{empNote.getEmployeeNotesId()});

        noteUpdate.setCompany(companyId);
        try {
            conn.executeUpdate(noteUpdate, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    public void saveEmployeeNote(EmployeeNotes empNotes) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        save_employee_note_query noteUpdate = new save_employee_note_query();
        noteUpdate.update(empNotes);

        noteUpdate.setCompany(companyId);
        try {
            conn.executeUpdate(noteUpdate, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    public void saveEmployeeNotes(EmployeeNotes empNotes) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");

        employee_note_update noteUpdate = new employee_note_update(empNotes.getEmployeeId());
        noteUpdate.update(empNotes.getEmployeeNotesId(), empNotes.getNoteTypeId(),
                empNotes.getEmployeeNotesNotes(), empNotes.getUserId(),
                myFormat.format(empNotes.getEmployeeNotesDateTime()));

        noteUpdate.setCompany(companyId);
        try {
            conn.executeUpdate(noteUpdate, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

    }
}
