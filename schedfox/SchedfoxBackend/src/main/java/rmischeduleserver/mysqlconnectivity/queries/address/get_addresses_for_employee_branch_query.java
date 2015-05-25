/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.address;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_addresses_for_employee_branch_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT address_geocode.* FROM ");
        sql.append("employee ");
        sql.append("INNER JOIN address_geocode ON ");
        sql.append("UPPER(address1) = UPPER(employee_address) AND UPPER(address2) = UPPER(employee_address2) AND ");
        sql.append("UPPER(city) = UPPER(employee_city) AND UPPER(state) = UPPER(employee_state) AND ");
        sql.append("UPPER(zip) = UPPER(employee_zip) ");
        sql.append("WHERE ");
        sql.append("branch_id = ? AND employee_is_deleted != 1 AND latitude IS NOT NULL ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
