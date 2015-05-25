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
public class get_problemcontacts_for_problem_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT problem_solver_contacts.* FROM ");
        sql.append("problem_solver_contact ");
        sql.append("INNER JOIN problem_solver_contacts ON ");
        sql.append("    problem_solver_contacts.problem_solver_contact_id = problem_solver_contact.problem_solver_contact_id ");
        sql.append("WHERE ");
        sql.append("problem_solver_contact.ps_id = ? AND contact_type = ?;");
        return sql.toString();
    }

}
