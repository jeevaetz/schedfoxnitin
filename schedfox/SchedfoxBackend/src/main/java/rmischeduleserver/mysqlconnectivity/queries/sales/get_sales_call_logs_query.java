/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_sales_call_logs_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sales_call.* FROM ");
        sql.append("sales_call ");
        sql.append("INNER JOIN sales_equipment ON sales_equipment.client_equipment_id = sales_call.equipment_id ");
        sql.append("WHERE ");
        sql.append("user_id = ? ");
        sql.append("ORDER BY contact_date DESC ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
