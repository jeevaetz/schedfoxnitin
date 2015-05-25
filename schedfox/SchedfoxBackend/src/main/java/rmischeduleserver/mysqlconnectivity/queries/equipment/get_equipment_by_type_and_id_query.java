/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientEquipment;
import schedfoxlib.model.EntityEquipment;

/**
 *
 * @author user
 */
public class get_equipment_by_type_and_id_query extends GeneralQueryFormat {

    private boolean isEmployeeEquipment;
    private boolean showLastUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Class<EntityEquipment> entityType, boolean showLastUpdate) {
        isEmployeeEquipment = true;
        if (entityType.equals(ClientEquipment.class)) {
                isEmployeeEquipment = false;
        }
        this.showLastUpdate = showLastUpdate;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (isEmployeeEquipment) {
            sql.append("employee_equipment.* ");
        } else {
            sql.append("client_equipment.* ");
        }
        if (showLastUpdate) {
            sql.append(", last_contact_date ");
            sql.append(", (SELECT version FROM client_equipment_contact WHERE client_equipment_contact.client_equipment_id = client_equipment.client_equipment_id ORDER BY client_equipment_contact_id DESC LIMIT 1) as working_version ");
        }
        sql.append("FROM ");
        sql.append("equipment ");
        sql.append("LEFT JOIN client_equipment ON client_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN client ON client.client_id = client_equipment.client_id ");
        sql.append("LEFT JOIN employee_equipment ON employee_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = employee_equipment.employee_id ");
        if (showLastUpdate) {
            sql.append("LEFT JOIN ");
            sql.append("(");
            sql.append("    SELECT equipment_id, MAX(recorded_on) as last_contact_date ");
            sql.append("    FROM gps_coordinate ");
            sql.append("    GROUP BY ");
            sql.append("    equipment_id");
            sql.append(") as lastdata ON lastdata.equipment_id = client_equipment.client_equipment_id  "); 
        }
        sql.append("WHERE ");
        sql.append("equipment.equipment_id = ? AND (client.client_id = ? OR -1 = ?) ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
