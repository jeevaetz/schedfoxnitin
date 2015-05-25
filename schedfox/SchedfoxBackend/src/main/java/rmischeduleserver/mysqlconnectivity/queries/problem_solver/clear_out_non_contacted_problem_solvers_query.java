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
public class clear_out_non_contacted_problem_solvers_query extends GeneralQueryFormat {

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
        sql.append("DELETE FROM problem_solver_contacts WHERE ");
        sql.append("contact_type = ? AND problem_solver_contact_id IN ");
        sql.append("    (SELECT problem_solver_contact_id FROM ");
        sql.append("     problem_solver_contact ");
        sql.append("     WHERE ");
        sql.append("     ps_id = ? ");
        sql.append("    )");
        return sql.toString();
    }

}
