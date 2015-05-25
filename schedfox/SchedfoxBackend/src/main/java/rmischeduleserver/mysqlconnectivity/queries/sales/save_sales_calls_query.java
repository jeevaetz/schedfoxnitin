/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.SalesCall;

/**
 *
 * @author ira
 */
public class save_sales_calls_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(SalesCall call) {
        if (call.getSalesCallId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{call.getEquipmentId(), call.getPhoneNumber(),
                        new java.sql.Timestamp(call.getContactDate().getTime()), call.getContactDurationMs(), call.getLocalId(),
                        call.getPhoneName(), call.getCallType(), call.getLocalId(), call.getEquipmentId()});
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{call.getEquipmentId(), call.getPhoneNumber(),
                        new java.sql.Timestamp(call.getContactDate().getTime()), call.getContactDurationMs(), call.getLocalId(),
                        call.getPhoneName(), call.getCallType(), call.getSalesCallId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("sales_call ");
            sql.append("(equipment_id, phone_number, contact_date, contact_duration_ms, local_id, phone_name, call_type) ");
            sql.append("SELECT ?, ?, ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT local_id FROM sales_call WHERE local_id = ? AND equipment_id = ?) ");
        } else {
            sql.append("UPDATE ");
            sql.append("sales_call ");
            sql.append("SET ");
            sql.append("equipment_id = ?, phone_number = ?, contact_date = ?, contact_duration_ms = ?, local_id = ?, phone_name = ?, call_type = ? ");
            sql.append("WHERE ");
            sql.append("sales_call_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
