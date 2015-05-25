/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_client_export_rate_codes_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT usked_client.*, client_rate_code.*, hour_type.*, rate_code.*, 'R' as hour_type, ");
        sql.append("(CASE WHEN hour_type.description = 'Regular' THEN 'R' WHEN hour_type.description = 'Vacation' THEN 'V' WHEN hour_type.description = 'Holiday' THEN 'H' END) as hour_type, ");
        sql.append("(CASE WHEN hour_type.description = 'Holiday' THEN 'Y' ELSE 'N' END) as is_holliday_pay, ");
        sql.append("(CASE WHEN hour_type.description = 'Holiday' THEN 'Y' ELSE 'N' END) as is_holliday_bill ");
        sql.append("FROM client ");
        sql.append("INNER JOIN usked_client ON usked_client.client_id = client.client_id ");
        sql.append("INNER JOIN client_rate_code ON client_rate_code.client_id = client.client_id ");
        sql.append("INNER JOIN hour_type ON hour_type.hour_type_id = client_rate_code.hour_type ");
        sql.append("INNER JOIN rate_code ON rate_code.rate_code_id = client_rate_code.rate_code_id ");
        sql.append("WHERE ");
        sql.append("client.branch_id = ?");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
