package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.Problemsolver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author ira
 */
public class save_new_problem_solver_query extends GeneralQueryFormat {

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
                problem.getPsId(), problem.getPsId()
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
            sql.append(" ?, ?, ?, ?) RETURNING ps_id as myid; ");
        } else {
            sql.append("UPDATE problemsolver SET ");
            sql.append("client_id = ?, user_id = ?, ps_date = ?, problem = ?, ");
            sql.append("solution = ?, scheduler_inst = ?, supervisor_inst = ?, ");
            sql.append("dm_inst = ?, hr_inst = ?, postcom_inst = ?, officer_inst = ?, ");
            sql.append("payroll_inst = ?, dispatch_inst = ?, checkin_inst = ?, ");
            sql.append("private_communication = ? ");
            sql.append("WHERE ");
            sql.append("ps_id = ?;");
            sql.append("SELECT ? as myid; ");
        }
        return sql.toString();
    }
}
