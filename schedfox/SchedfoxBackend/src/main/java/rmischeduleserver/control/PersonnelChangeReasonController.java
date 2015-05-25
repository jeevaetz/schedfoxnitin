/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.personnelchange.get_personnel_change_reasons_query;
import rmischeduleserver.mysqlconnectivity.queries.personnelchange.get_unsent_personnel_changes_query;
import rmischeduleserver.mysqlconnectivity.queries.personnelchange.save_personnel_change_reason_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.PersonnelChange;
import schedfoxlib.model.PersonnelChangeReason;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class PersonnelChangeReasonController {
    private String companyId;

    private PersonnelChangeReasonController(String companyId) {
        this.companyId = companyId;
    }

    public static PersonnelChangeReasonController getInstance(String companyId) {
        return new PersonnelChangeReasonController(companyId);
    }
    
    public void savePersonnelChange(PersonnelChange reason) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<PersonnelChangeReason> retVal = new ArrayList<PersonnelChangeReason>();

        save_personnel_change_reason_query infoQuery = new save_personnel_change_reason_query();
        infoQuery.update(reason);
        infoQuery.setCompany(companyId);
        try {
            conn.executeUpdate(infoQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
    public ArrayList<PersonnelChange> getUnsentChanges() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<PersonnelChange> retVal = new ArrayList<PersonnelChange>();

        get_unsent_personnel_changes_query infoQuery = new get_unsent_personnel_changes_query();
        infoQuery.setPreparedStatement(new Object[]{});
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new PersonnelChange(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
    
    public ArrayList<PersonnelChangeReason> getPersonalChangeReasons() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<PersonnelChangeReason> retVal = new ArrayList<PersonnelChangeReason>();

        get_personnel_change_reasons_query infoQuery = new get_personnel_change_reasons_query();
        infoQuery.setPreparedStatement(new Object[]{});
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new PersonnelChangeReason(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }
}
