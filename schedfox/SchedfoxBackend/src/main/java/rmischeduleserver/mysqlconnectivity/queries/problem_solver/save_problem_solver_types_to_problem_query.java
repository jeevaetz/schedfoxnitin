/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class save_problem_solver_types_to_problem_query extends GeneralQueryFormat {

    private Integer ps_id;
    private ArrayList<Integer> types;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    public void update(Integer ps_id, ArrayList<Integer> types) {
        this.ps_id = ps_id;
        this.types = types;
        
        Integer[] params = new Integer[types.size() * 2 + 1];
        params[0] = ps_id;
        for (int i = 0; i < types.size(); i++) {
            params[1 + (i * 2)] = types.get(i);
            params[2 + (i * 2)] = ps_id;
        }
        super.setPreparedStatement(params);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM problemsolver_to_types WHERE ps_id = ?; ");
        for (int t = 0; t < types.size(); t++) {
            sql.append("INSERT INTO problemsolver_to_types (problemsolver_type_id, ps_id) VALUES (?, ?);");
        }
        return sql.toString();
    }
    
}
