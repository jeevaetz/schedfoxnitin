/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class clear_equipment_returned_query extends GeneralQueryFormat {

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
        sql.append("UPDATE employee_equipment ");
        sql.append("SET ");
        sql.append("date_returned = NULL, ");
        sql.append("received_by = NULL ");
        sql.append("WHERE ");
        sql.append("employee_equipment_id = ?;");
        return sql.toString();
    }

}
