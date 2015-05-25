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
public class get_equipment_group_by_mdn_query extends GeneralQueryFormat {

    private boolean showLastUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(boolean showLastUpdate) {
        this.showLastUpdate = showLastUpdate;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client_equipment.* ");
        if (showLastUpdate) {
            sql.append(", (SELECT MAX(recorded_on) ");
            sql.append("    FROM ");
            sql.append("    gps_coordinate ");
            sql.append("    WHERE gps_coordinate.equipment_id = client_equipment.client_equipment_id) as last_contact_date ");
            sql.append(", (SELECT version FROM client_equipment_contact WHERE client_equipment_contact.client_equipment_id = client_equipment.client_equipment_id ORDER BY client_equipment_contact_id DESC LIMIT 1) as working_version ");
        }
        sql.append("FROM ");
        sql.append("equipment ");
        sql.append("LEFT JOIN client_equipment ON client_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN client ON client.client_id = client_equipment.client_id ");
        sql.append("LEFT JOIN employee_equipment ON employee_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = employee_equipment.employee_id ");
        sql.append("LEFT JOIN sales_equipment ON sales_equipment.client_equipment_id = client_equipment.client_equipment_id ");
        sql.append("WHERE ");
        sql.append("(equipment.equipment_id = ? OR ? = -1) AND (client.client_id = ? OR -1 = ?) AND sales_equipment.sales_equipment_id IS NULL AND ");
        sql.append("(client_equipment.active = true OR client_equipment.active IS NULL) AND client_equipment.client_equipment_id IN ");
        sql.append("(");
        sql.append("    SELECT MAX(client_equipment_id) ");
        sql.append("    FROM client_equipment ");
        sql.append("    WHERE (equipment_id = ? OR ? = -1) AND (client_equipment.active = true OR client_equipment.active IS NULL) ");
        sql.append("    GROUP BY mdn");
        sql.append(")");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
