/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_employees_by_branches_query extends GeneralQueryFormat {

    private Integer sizeBranches;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<Integer> branches) {
        sizeBranches = branches.size();
        super.setPreparedStatement(branches.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee ");
        sql.append("WHERE ");
        sql.append("employee_is_deleted != 1 AND branch_id IN ");
        sql.append("(");
            for (int i = 0; i < sizeBranches; i++) {
                if (i > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
        sql.append(") ");
        sql.append("ORDER BY ");
        sql.append("employee_first_name, employee_last_name");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
