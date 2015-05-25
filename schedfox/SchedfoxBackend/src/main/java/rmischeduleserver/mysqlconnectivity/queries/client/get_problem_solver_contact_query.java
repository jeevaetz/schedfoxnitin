/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_problem_solver_contact_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT problem_solver_contact.* FROM ");
        sql.append("problem_solver_contact ");
        sql.append("INNER JOIN problemsolver ON problemsolver.ps_id = problem_solver_contact.ps_id ");
        sql.append("WHERE ");
        sql.append("problemsolver.client_id = ? ");
        sql.append("ORDER BY problem_solver_contact.contact_date DESC ");
        sql.append("LIMIT 1");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
