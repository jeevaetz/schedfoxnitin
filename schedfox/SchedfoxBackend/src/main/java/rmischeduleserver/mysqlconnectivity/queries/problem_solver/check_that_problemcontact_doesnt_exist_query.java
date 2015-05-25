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
public class check_that_problemcontact_doesnt_exist_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("problem_solver_contact ");
        sql.append("INNER JOIN problem_solver_contacts ON problem_solver_contacts.problem_solver_contact_id = problem_solver_contact.problem_solver_contact_id ");
        sql.append("WHERE ");
        sql.append("ps_id = ? AND contact_id = ? AND contact_type = ?");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
