/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employees_worked_at_client_query extends GeneralQueryFormat {

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
        sql.append("assemble_schedule(DATE(NOW() - interval '2 month'), DATE(NOW()), -1, -1, Array[?]) as sched ");
        sql.append("INNER JOIN employee ON employee.employee_id = sched.eid ");
        sql.append("WHERE employee.employee_is_deleted != 1 ");
        sql.append("ORDER BY employee.employee_last_name, employee.employee_first_name");
        return sql.toString();
    }
        
}
