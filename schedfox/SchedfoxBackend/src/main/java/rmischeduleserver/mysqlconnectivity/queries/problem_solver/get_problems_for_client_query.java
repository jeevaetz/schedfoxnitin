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
public class get_problems_for_client_query extends GeneralQueryFormat {

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
        StringBuffer sql = new StringBuffer();
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
        sql.append("AND private_communication != true ");
        sql.append("ORDER BY ps_date DESC; ");
        return sql.toString();
    }

}
