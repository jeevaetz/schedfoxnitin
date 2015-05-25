/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_sales_equipment_by_user_query extends GeneralQueryFormat {
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("sales_equipment ");
        sql.append("WHERE ");
        sql.append("user_id = ?; ");
        return sql.toString();
    }
    
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
