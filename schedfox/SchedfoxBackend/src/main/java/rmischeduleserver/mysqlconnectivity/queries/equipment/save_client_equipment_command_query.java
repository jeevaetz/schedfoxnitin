/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientEquipment;

/**
 *
 * @author user
 */
public class save_client_equipment_command_query extends GeneralQueryFormat {

    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void setIsInsert(boolean isIn) {
        this.isInsert = isIn;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("client_equipment_command ");
            sql.append("(");
            sql.append("    client_equipment_id, command, data, date_sent, ");
            sql.append("    date_received, active, client_equipment_command_id ");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("client_equipment_command ");
            sql.append("SET ");
            sql.append("client_equipment_id = ?, command = ?, data = ?, date_sent = ?, ");
            sql.append("date_received = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("client_equipment_command_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
