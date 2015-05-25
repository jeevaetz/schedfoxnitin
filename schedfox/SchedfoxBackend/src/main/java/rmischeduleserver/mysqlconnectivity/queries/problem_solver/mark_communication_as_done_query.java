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
public class mark_communication_as_done_query extends GeneralQueryFormat {

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
        sql.append("UPDATE problem_solver_contacts ");
        sql.append("SET message_delivered_date = NOW() ");
        sql.append("WHERE ");
        sql.append("contact_type = ? AND contact_id = ? AND ");
        sql.append("problem_solver_contact_id IN ");
        sql.append("    (SELECT problem_solver_contact_id ");
        sql.append("     FROM ");
        sql.append("     problem_solver_contact ");
        sql.append("     WHERE ");
        sql.append("     ps_id = ? ");
        sql.append("    )");
        return sql.toString();
    }

}
