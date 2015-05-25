/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.gps;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_last_employee_used_device_query extends GeneralQueryFormat {

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
        sql.append("employee_id IN ");
        sql.append("(");
        sql.append("    SELECT employee_id FROM ");
        sql.append("    gps_coordinate ");
        sql.append("    WHERE ");
        sql.append("    equipment_id = ? ");
        sql.append("    ORDER BY recorded_on DESC ");
        sql.append("    LIMIT 1");
        sql.append(")");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
