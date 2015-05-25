/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_training_for_client_query extends GeneralQueryFormat {

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
        sql.append("SELECT DISTINCT employee_test.* FROM ");
        sql.append("employee_test ");
        sql.append("LEFT JOIN test_to_client ON test_to_client.test_id = employee_test.employee_test_id ");
        sql.append("LEFT JOIN client ON client.client_id = test_to_client.client_id OR client.client_worksite = test_to_client.client_id ");
        sql.append("WHERE (test_to_client.test_to_client_id IS NULL OR test_to_client.client_id = ? OR client.client_id = ?) AND is_active = true");
        return sql.toString();
    }
}
