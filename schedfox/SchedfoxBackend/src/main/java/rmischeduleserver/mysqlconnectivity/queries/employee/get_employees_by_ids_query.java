/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_employees_by_ids_query extends GeneralQueryFormat {

    private Integer size;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer size) {
        this.size = size;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT * FROM ");
        sql.append("employee ");
        sql.append("WHERE ");
        sql.append("employee_id IN ");
        sql.append("(");
        for (int s = 0; s < size; s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(")");
        return sql.toString();
    }
}
