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
public class get_employees_by_search_params_query extends GeneralQueryFormat {
    
    private int branchSize = 0;
    
    public get_employees_by_search_params_query() {
        
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(String params, ArrayList<Integer> branches) {
        String param = "%" + params + "%";
        if (branches != null) {
            branchSize = branches.size();
        }
        
        Object[] paramsObj = new Object[6 + branches.size()];
        paramsObj[0] = param;
        paramsObj[1] = param;
        paramsObj[2] = param;
        paramsObj[3] = param;
        paramsObj[4] = param;
        paramsObj[5] = param;
        for (int b = 0; b < branches.size(); b++) {
            paramsObj[b + 6] = branches.get(b);
        }
        super.setPreparedStatement(paramsObj);
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee ");
        sql.append("WHERE ");
        sql.append("(");
        sql.append("    (employee_first_name || ' ' || employee_last_name) ilike (?) OR ");
        sql.append("    (employee_address || ', ' || employee_city || ' ' || employee_state) ilike (?) OR ");
        sql.append("    employee_ssn ilike (?) OR ");
        sql.append("    employee_phone ilike (?) OR ");
        sql.append("    employee_cell ilike (?) OR ");
        sql.append("    employee_phone2 ilike (?) ");
        sql.append(") ");
        sql.append("AND employee_is_deleted != 1 ");
        if (branchSize > 0) {
            sql.append("AND employee.branch_id IN (");
            for (int b = 0; b < branchSize; b++) {
                if (b > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(")");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
