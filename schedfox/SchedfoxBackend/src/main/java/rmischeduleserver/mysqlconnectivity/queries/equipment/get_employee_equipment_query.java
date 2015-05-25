/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.equipment;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employee_equipment_query extends GeneralQueryFormat {

    private int sizeOfArray;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer equipmentId, String searchStr, boolean showAll, ArrayList<Integer> selBranches, Date startDate, Date endDate, Integer numberOfContacts) {
        sizeOfArray = selBranches.size();
        ArrayList<Object> params = new ArrayList<Object>();
        params.add(equipmentId);
        params.add(showAll);
        params.add(showAll);
        params.add(showAll);
        params.add(searchStr);
        params.add("%" + searchStr + "%");
        params.add("%" + searchStr + "%");
        params.addAll(selBranches);
        params.add(new java.util.Date(startDate.getTime()));
        params.add(new java.util.Date(endDate.getTime()));
        params.add(numberOfContacts);
        params.add(numberOfContacts);
        
        super.setPreparedStatement(params.toArray());
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *, COALESCE(num_send, 0) as num_sent, employee.employee_id as employee_id FROM ");
        sql.append("employee_equipment ");
        sql.append("INNER JOIN employee ON employee.employee_id = employee_equipment.employee_id ");
        sql.append("INNER JOIN equipment ON equipment.equipment_id = employee_equipment.equipment_id ");
        sql.append("INNER JOIN employee_equipment_to_deduction ON employee_equipment_to_deduction.employee_equipment_id = employee_equipment.employee_equipment_id ");
        sql.append("INNER JOIN employee_deductions ON employee_deductions.employee_deduction_id = employee_equipment_to_deduction.employee_deduction_id AND deduction_written_off != true ");
        sql.append("LEFT JOIN (");
        sql.append("    SELECT employee_id, COUNT(*) as num_send, MAX(datetimesent) as last_send ");
        sql.append("    FROM ");
        sql.append("    messaging_communication_batch ");
        sql.append("    INNER JOIN messaging_communication ON messaging_communication.messaging_communication_batch_id = messaging_communication_batch.messaging_communication_batch_id ");
        sql.append("    WHERE ");
        sql.append("    messaging_souce IN (9, 10) AND datetimesent IS NOT NULL ");
        sql.append("    GROUP BY employee_id");
        sql.append(") as send_data ON send_data.employee_id = employee.employee_id ");
        sql.append("WHERE ");
        sql.append("equipment.equipment_id = ? AND ");
        sql.append("equipment.should_be_returned = true AND ");
        sql.append("(((employee_equipment.date_returned IS NULL AND ? = false) AND (employee_equipment.return_waived_on IS NULL AND ? = false)) OR (? = true)) AND ");
        sql.append("(?::text IS NULL OR (employee_first_name ILIKE ?::text OR employee_last_name ILIKE ?::text)) AND ");
        sql.append("employee.branch_id IN (");
        for (int s = 0; s < this.sizeOfArray; s++) {
            if (s != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") AND ");
        sql.append("employee.employee_is_deleted = 1 AND employee.employee_term_date BETWEEN ? AND ? AND ");
        sql.append("(COALESCE(num_send, 0) = ? OR ? = -1) ");
        sql.append("ORDER BY ");
        sql.append("employee_is_deleted DESC, employee_last_name, employee_first_name; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
