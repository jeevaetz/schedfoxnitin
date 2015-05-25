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
public class get_employee_test_scores_query extends GeneralQueryFormat {

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
//        sql.append("SELECT DISTINCT employee_test_score.*, employee_test.employee_test_id ");
        sql.append("SELECT DISTINCT employee_test_score.*, employee_test.employee_test_id,employee_test.employee_test,employee_test.default_to_na ");
        sql.append("FROM employee_test ");
        sql.append("LEFT JOIN test_to_client ON test_to_client.test_id = employee_test.employee_test_id ");
        sql.append("LEFT JOIN client ON client.client_id = test_to_client.client_id OR client.client_worksite = test_to_client.client_id ");
        sql.append("LEFT JOIN employee_test_score ON employee_test_score.employee_test_score_id = ");
	sql.append("(SELECT employee_test_score.employee_test_score_id FROM employee_test_score WHERE ");
	sql.append(" employee_id = ? AND test_id = employee_test.employee_test_id ORDER BY score DESC LIMIT 1) ");
        sql.append("WHERE (test_to_client.test_to_client_id IS NULL OR test_to_client.client_id = ? OR client.client_id = ?) AND is_active = true ");
        return sql.toString();
    }

}
