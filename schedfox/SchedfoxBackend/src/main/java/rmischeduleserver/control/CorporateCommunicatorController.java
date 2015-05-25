/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Problemsolver;
import schedfoxlib.model.ScheduleData;
import rmischeduleserver.log.MyLogger;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.get_problem_solvers_for_employee_phone_query;
import rmischeduleserver.mysqlconnectivity.queries.problem_solver.mark_communication_as_done_query;

/**
 *
 * @author user
 */
public class CorporateCommunicatorController {

    public static String SECURITY_GUARD_PHONE = "sec_phone";

    private String companyId;
    private MyLogger log = null;

    public CorporateCommunicatorController(String companyId,MyLogger log) {
        this.log = log;
        this.companyId = companyId;
    }

    public ArrayList<Problemsolver> getCorporateCommunicatorForSchedule(ScheduleData schedule, String type) throws RetrieveDataException {
        ArrayList<Problemsolver> retVal = new ArrayList<Problemsolver>();
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        get_problem_solvers_for_employee_phone_query guardQuery =
                new get_problem_solvers_for_employee_phone_query();
        guardQuery.setCompany(companyId);
        guardQuery.setPreparedStatement(new Object[]{schedule.getClientId(),
            type, schedule.getEmployeeId()});
        try {
            Record_Set rst = conn.executeQuery(guardQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Problemsolver(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public void markCorporateCommunicatorAsContacted(Problemsolver ps, int employeeid, String type)
        throws SaveDataException {
        RMIScheduleServerImpl conn = new RMIScheduleServerImpl(true);
        mark_communication_as_done_query guardQuery =
                new mark_communication_as_done_query();
        guardQuery.setCompany(companyId);
        guardQuery.setPreparedStatement(new Object[]{type, employeeid, ps.getPsId()});
        try {
            conn.executeUpdate(guardQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }
    
}
