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
public class get_active_and_recent_term_employees_by_branch_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee ");
        sql.append("WHERE ");
        sql.append("(employee_is_deleted != 1 OR (employee_is_deleted = 1 AND employee_term_date BETWEEN DATE(NOW() - interval '14 days') AND DATE(NOW()))) AND branch_id = ? ");
        sql.append("ORDER BY ");
        sql.append("employee_last_name, employee_first_name");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
