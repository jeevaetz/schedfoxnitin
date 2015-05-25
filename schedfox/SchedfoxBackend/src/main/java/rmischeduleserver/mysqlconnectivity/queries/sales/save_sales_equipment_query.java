/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.SalesEquipment;

/**
 *
 * @author ira
 */
public class save_sales_equipment_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(SalesEquipment salesEquipment) {
        super.setPreparedStatement(new Object[]{salesEquipment.getClientEquipmentId(), 
            salesEquipment.getClientEquipmentId(), salesEquipment.getUserId(),
            salesEquipment.getDateAssigned(), salesEquipment.getActive()});
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("UPDATE ");
        sql.append("sales_equipment ");
        sql.append("SET ");
        sql.append("active = false ");
        sql.append("WHERE ");
        sql.append("client_equipment_id = ?; ");
        sql.append("INSERT INTO ");
        sql.append("sales_equipment ");
        sql.append("(client_equipment_id, user_id, date_assigned, active) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?) ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
