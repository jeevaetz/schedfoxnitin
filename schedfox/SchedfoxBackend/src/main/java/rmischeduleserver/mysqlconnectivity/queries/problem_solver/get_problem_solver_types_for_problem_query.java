/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_problem_solver_types_for_problem_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT problemsolver_types.* FROM ");
        sql.append("problemsolver_types ");
        sql.append("INNER JOIN problemsolver_to_types ON problemsolver_to_types.problemsolver_type_id = problemsolver_types.problemsolver_type_id ");
        sql.append("WHERE ");
        sql.append("ps_id = ?; ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
