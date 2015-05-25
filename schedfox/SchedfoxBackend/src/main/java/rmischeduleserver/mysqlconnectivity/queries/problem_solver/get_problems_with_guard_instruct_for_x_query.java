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
public class get_problems_with_guard_instruct_for_x_query extends GeneralQueryFormat {

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
        sql.append("SELECT problemsolver.*, client.client_name FROM ");
        sql.append("problemsolver ");
        sql.append("LEFT JOIN client ON client.client_id = problemsolver.client_id ");
        sql.append("WHERE ");
        sql.append("( ");
        sql.append(" problemsolver.client_id = ? OR ");
        sql.append(" problemsolver.client_id IN ");
        sql.append("   (SELECT client_id FROM client as iclient ");
        sql.append("    WHERE iclient.client_worksite = ?) ");
        sql.append(") ");
        sql.append("AND officer_inst IS NOT NULL AND officer_inst > '' ");
        sql.append("AND ps_date >= NOW() - interval '1 day' * ? ");
        sql.append("AND private_communication != true ");
        sql.append("ORDER BY ps_date DESC; ");
        return sql.toString();
    }

}
