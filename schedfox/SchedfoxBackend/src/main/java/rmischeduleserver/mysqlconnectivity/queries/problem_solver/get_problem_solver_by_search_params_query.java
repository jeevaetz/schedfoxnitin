/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.problem_solver;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_problem_solver_by_search_params_query extends GeneralQueryFormat {

    private String query;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<Integer> clientIds, ArrayList<Integer> employeeIds, 
            ArrayList<Integer> types, Date startDate, Date endDate, String queryText) {
        ArrayList<Object> params = new ArrayList<Object>();
        
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("problemsolver ");
        sql.append("WHERE ");
        sql.append("1 = 1 ");
        if (clientIds.isEmpty() || employeeIds.isEmpty() || types.isEmpty()) {
            sql.append("AND 1 = 2 ");
        }
        sql.append("AND DATE(ps_date) BETWEEN ? AND ? ");
        params.add(startDate);
        params.add(endDate);
        if (queryText != null && queryText.length() > 0) {
            String ilikeTxt = "%" + queryText + "%";
            sql.append("AND (");
            sql.append("    problem ilike (?) OR solution ilike (?) OR scheduler_inst ilike (?) OR supervisor_inst ilike (?) OR ");
            sql.append("    dm_inst ilike (?) OR hr_inst ilike (?) OR postcom_inst ilike (?) OR officer_inst ilike (?) OR ");
            sql.append("    payroll_inst ilike (?) OR dispatch_inst ilike (?) OR checkin_inst ilike (?) OR ps_id::text ilike (?) ");
            sql.append(") ");
            
            for (int p = 0; p < 12; p++) {
                params.add(ilikeTxt);
            }
        }
        
        for (int c = 0; c < clientIds.size(); c++) {
            if (c == 0) {
                sql.append("AND client_id IN (");
            } else {
                sql.append(",");
            }
            sql.append("?");
            params.add(clientIds.get(c));
            if (c == clientIds.size() -1) {
                sql.append(") ");
            }
        }
        
        for (int c = 0; c < employeeIds.size(); c++) {
            if (c == 0) {
                sql.append("AND user_id IN (");
            } else {
                sql.append(",");
            }
            sql.append("?");
            params.add(employeeIds.get(c));
            if (c == employeeIds.size() -1) {
                sql.append(") ");
            }
        }
        
        for (int c = 0; c < types.size(); c++) {
//            if (c == 0) {
//                sql.append("AND client_id IN (");
//            } else {
//                sql.append(",");
//            }
//            sql.append("?");
//            params.add(types.get(c));
//            if (c == types.size() -1) {
//                sql.append(") ");
//            }
        }
        sql.append("ORDER BY ps_date ");
        query = sql.toString();
        
        super.setPreparedStatement(params.toArray());
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        return query;
    }
    
    
}
