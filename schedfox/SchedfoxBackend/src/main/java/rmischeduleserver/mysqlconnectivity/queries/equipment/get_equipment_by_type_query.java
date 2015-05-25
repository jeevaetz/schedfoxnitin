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
public class get_equipment_by_type_query extends GeneralQueryFormat {

    private boolean isEmployeeEquipment;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Class<EntityEquipment> entityType) {
        isEmployeeEquipment = true;
        if (entityType.equals(ClientEquipment.class)) {
                isEmployeeEquipment = false;
        }
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
        sql.append("FROM ");
        sql.append("equipment ");
        sql.append("LEFT JOIN client_equipment ON client_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN client ON client.client_id = client_equipment.client_id ");
        sql.append("LEFT JOIN employee_equipment ON employee_equipment.equipment_id = equipment.equipment_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = employee_equipment.employee_id ");
        sql.append("WHERE ");
        sql.append("equipment.equipment_id = ? ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
