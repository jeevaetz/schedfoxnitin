/*
 * save_problem_solver_query.java
 *
 * Created on August 14, 2006, 3:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author shawn
 */
public class save_problem_solver_query extends GeneralQueryFormat {
    
    /** Creates a new instance of save_problem_solver_query */
    public save_problem_solver_query(String id, String clientId, String userId, String prob, String sol, String schInst,
                                     String supInst, String dmInst, String hrInst, String postInst,
                                     String offInst, String payInst, String dispInst, String checkInst, String date) {
        
        String delete = "";
        if(id.trim().length() > 0) {
            delete = "DELETE FROM problemsolver WHERE ps_id = " + id + ";";
        }
        
        String insert = "INSERT INTO problemsolver (client_id, user_id, ps_date, problem, solution, scheduler_inst, " +
                                                  " supervisor_inst, dm_inst, hr_inst, postcom_inst, " +
                                                  " officer_inst, payroll_inst, dispatch_inst, checkin_inst) " +
                        "VALUES (" + clientId + ", " + userId + ", '" + date + "', '" + prob + "', '" + sol + "', '" + schInst +
                                 "', '" + supInst + "', '" + dmInst + "', '" + hrInst + "', '" + postInst + "', '" +
                                 offInst + "', '" + payInst + "', '" + dispInst + "', '" + checkInst + "');";
        
        this.myReturnString = delete + insert;
    }
    
    public boolean hasAccess() { return true; }
}
