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
public class save_employee_sub_contractor_query extends GeneralQueryFormat {

    private Boolean val;
    private Integer employeeId;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Boolean val, Integer employeeId) {
        this.val = val;
        this.employeeId = employeeId;
    }
    
    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE employee ");
        sql.append("SET ");
        sql.append("is_sub_contractor = ").append(val).append(" ");
        sql.append("WHERE ");
        sql.append("employee_id = ").append(employeeId);
        return sql.toString();
    }
}
