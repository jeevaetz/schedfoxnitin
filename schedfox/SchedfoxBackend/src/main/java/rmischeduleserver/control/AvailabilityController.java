/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.availability.availability_save_query;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.AvailabilityNotes;
import rmischeduleserver.mysqlconnectivity.queries.availability.save_availability_note_query;
import schedfoxlib.model.Availability;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class AvailabilityController {

    private String companyId;

    private AvailabilityController(String companyId) {
        this.companyId = companyId;
    }

    public static AvailabilityController getInstance(String companyId) {
        return new AvailabilityController(companyId);
    }

    public void saveAvailNote(AvailabilityNotes availNotes) throws SaveDataException {
        save_availability_note_query saveAvailNoteQuery = new save_availability_note_query();
        saveAvailNoteQuery.update(availNotes);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveAvailNoteQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveAvailNoteQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    public String saveAvailability(Availability avail) throws SaveDataException {
        availability_save_query mySaveQuery = new availability_save_query();
        mySaveQuery.update(avail);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        mySaveQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(mySaveQuery, "");
            return rst.getString(1);
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
}
