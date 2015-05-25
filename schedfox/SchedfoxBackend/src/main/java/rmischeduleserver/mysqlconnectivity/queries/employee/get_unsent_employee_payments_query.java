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
public class get_unsent_employee_payments_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT employee_payments.* FROM ");
        sql.append("employee_payments ");
        sql.append("INNER JOIN employee ON employee.employee_id = employee_payments.employee_id ");
        sql.append("LEFT JOIN employee_payments_to_messaging_communication ON employee_payments.employee_payments_id = employee_payments_to_messaging_communication.employee_payments_id ");
        sql.append("WHERE ");
        sql.append("employee_payments.date_of_trans BETWEEN ? AND ? AND employee_payments_to_messaging_communication.employee_payments_id IS NULL AND ");
        sql.append("employee.branch_id = ?; ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
