/*
 * xEmployee_Notes.java
 *
 * Created on September 15, 2005, 8:57 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischedule.employee.components;
import java.util.Date;
import javax.swing.JOptionPane;
import rmischedule.client.components.*;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.EmployeeNotesController;
import rmischeduleserver.control.GenericController;
import rmischeduleserver.mysqlconnectivity.queries.util.*;
import rmischeduleserver.mysqlconnectivity.queries.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_delete_note_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_note_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_note_update;
import schedfoxlib.model.ClientNotes;
import schedfoxlib.model.EmployeeNotes;
import schedfoxlib.model.NoteInterface;
import schedfoxlib.model.util.Record_Set;
/**
 *
 * @author Ira Juneau
 */
public class xEmployee_Notes extends xClient_Notes {

    private GenericEditForm myParent;

    /** Creates a new instance of xEmployee_Notes */
    public xEmployee_Notes(GenericEditForm myParent) {
        super(myParent);
        this.myParent = myParent;
    }
    
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQuery(boolean isSelected) {
        employee_note_query cnlq = new employee_note_query();
        note_type_list_query myNoteType = new note_type_list_query();
        RunQueriesEx myCompleteQuery = new RunQueriesEx();
        cnlq.update(myParent.getMyIdForSave());
        myCompleteQuery.update(myNoteType, cnlq);
        return myCompleteQuery;
    }
    
    public void saveNote() {
        if(myparent.getSelectedObject() == null) {
            return;
        }
        
        try {
            EmployeeNotes employeeNote = new EmployeeNotes();
            employeeNote.setEmployeeId(Integer.parseInt(myparent.getMyIdForSave()));
            employeeNote.setEmployeeNotesDateTime(new Date(GenericController.getInstance("2").getCurrentTimeMillis()));
            employeeNote.setEmployeeNotesNotes(NoteTxt.getText().trim());
            employeeNote.setNoteTypeId(getNoteIdByName((String)noteTypeLb.getSelectedItem()));
            employeeNote.setUserId(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            EmployeeNotesController noteController = EmployeeNotesController.getInstance(myparent.getConnection().myCompany);
            noteController.saveEmployeeNote((EmployeeNotes)employeeNote);
        } catch (Exception exe) {
            exe.printStackTrace();
        }

        this.reloadData();
    }
    
    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }
    
    protected NoteInterface loadNote(Record_Set rs) {
        return new EmployeeNotes(rs);
    }

    @Override
    public void deleteNote(NoteInterface employeeNote) {
        try {
            if (JOptionPane.showConfirmDialog(myparent, "Do you want to delete this note permanently?", "Delete Note?",
                    JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                EmployeeNotesController noteController = EmployeeNotesController.getInstance(myparent.getConnection().myCompany);
                noteController.deleteEmployeeNote((EmployeeNotes)employeeNote);
                reloadData();
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public void saveNote(NoteInterface employeeNote) {
        try {
            EmployeeNotesController noteController = EmployeeNotesController.getInstance(myparent.getConnection().myCompany);
            noteController.saveEmployeeNotes((EmployeeNotes)employeeNote);
            reloadData();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
}
