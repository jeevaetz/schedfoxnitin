/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_user_associated_with_sales_equipment_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \"user\".* FROM ");
        sql.append("control_db.user ");
        sql.append("INNER JOIN sales_equipment ON sales_equipment.user_id = \"user\".user_id ");
        sql.append("INNER JOIN client_equipment ON client_equipment.client_equipment_id = sales_equipment.client_equipment_id ");
        sql.append("WHERE unique_id = ?");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
