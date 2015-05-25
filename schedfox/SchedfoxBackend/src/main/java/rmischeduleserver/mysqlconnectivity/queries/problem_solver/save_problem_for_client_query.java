/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import schedfoxlib.model.Problemsolver;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_problem_for_client_query extends GeneralQueryFormat {

    private Problemsolver problem;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Problemsolver problem) {
        this.problem = problem;
        if (this.problem.getPsId() == 0) {
            super.setPreparedStatement(new Object[] {
                problem.getClientId(), problem.getUserId(), new java.sql.Timestamp(problem.getPsDate().getTime()),
                problem.getProblem(), problem.getSolution(), problem.getSchedulerInst(),
                problem.getSupervisorInst(), problem.getDmInst(), problem.getHrInst(), 
                problem.getPostcomInst(), problem.getOfficerInst(), problem.getPayrollInst(),
                problem.getDispatchInst(), problem.getCheckinInst(), problem.isPrivateCommunication()
            });
        } else {
            super.setPreparedStatement(new Object[] {
                problem.getClientId(), problem.getUserId(),new java.sql.Timestamp(problem.getPsDate().getTime()),
                problem.getProblem(), problem.getSolution(), problem.getSchedulerInst(),
                problem.getSupervisorInst(), problem.getDmInst(), problem.getHrInst(), 
                problem.getPostcomInst(), problem.getOfficerInst(), problem.getPayrollInst(),
                problem.getDispatchInst(), problem.getCheckinInst(), problem.isPrivateCommunication(),
                problem.getPsId()
            });
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuffer sql = new StringBuffer();
        if (problem.getPsId() == 0) {
            sql.append("INSERT INTO problemsolver ");
            sql.append("(client_id, user_id, ps_date, problem, solution, scheduler_inst, ");
            sql.append(" supervisor_inst, dm_inst, hr_inst, postcom_inst, officer_inst, ");
            sql.append(" payroll_inst, dispatch_inst, checkin_inst, private_communication) ");
            sql.append(" VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?, ?, ");
            sql.append(" ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE problemsolver SET ");
            sql.append("client_id = ?, user_id = ?, ps_date = ?, problem = ?, ");
            sql.append("solution = ?, scheduler_inst = ?, supervisor_inst = ?, ");
            sql.append("dm_inst = ?, hr_inst = ?, postcom_inst = ?, officer_inst = ?, ");
            sql.append("payroll_inst = ?, dispatch_inst = ?, checkin_inst = ?, ");
            sql.append("private_communication = ? ");
            sql.append("WHERE ");
            sql.append("ps_id = ?;");
        }
        return sql.toString();
    }

}
