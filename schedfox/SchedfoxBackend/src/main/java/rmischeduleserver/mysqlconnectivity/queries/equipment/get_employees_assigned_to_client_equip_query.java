/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_employees_assigned_to_client_equip_query extends GeneralQueryFormat {

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
        sql.append("SELECT DISTINCT employee.* FROM ");
        sql.append("officer_daily_report ");
        sql.append("INNER JOIN employee ON employee.employee_id = officer_daily_report.employee_id  ");
        sql.append("WHERE ");
        sql.append("officer_daily_report.client_id = ? ");
        sql.append("ORDER BY employee_first_name, employee_last_name ");
        return sql.toString();
    }
    
}
