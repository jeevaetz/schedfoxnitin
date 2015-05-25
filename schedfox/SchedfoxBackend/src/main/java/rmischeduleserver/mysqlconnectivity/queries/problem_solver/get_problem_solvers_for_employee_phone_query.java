/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_problem_solvers_for_employee_phone_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("problemsolver ");
        sql.append("INNER JOIN problem_solver_contact ON problem_solver_contact.ps_id = problemsolver.ps_id ");
        sql.append("INNER JOIN problem_solver_contacts ON problem_solver_contacts.problem_solver_contact_id = problem_solver_contact.problem_solver_contact_id ");
        sql.append("WHERE ");
        sql.append("problemsolver.client_id = ? AND contact_type = ? AND contact_id = ? AND message_delivered_date IS NULL");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
