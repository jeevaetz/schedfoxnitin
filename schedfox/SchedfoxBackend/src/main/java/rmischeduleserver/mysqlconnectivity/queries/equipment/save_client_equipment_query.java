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
public class save_client_equipment_query extends GeneralQueryFormat {

    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ClientEquipment clientEquipment, boolean isInsert) {
        this.isInsert = isInsert;
        if (isInsert) {
            super.setPreparedStatement(new Object[]{
                clientEquipment.getClientId(), clientEquipment.getRouteId(), clientEquipment.getEquipmentId(), clientEquipment.getDateIssued(),
                clientEquipment.getIssuedBy(), clientEquipment.getDateReturned(), clientEquipment.getReceivedBy(),
                clientEquipment.getReturnedCondition(), clientEquipment.getUniqueId(), clientEquipment.getNickname(), 
                clientEquipment.getMdn(), clientEquipment.getDateOfContractRenewal(), clientEquipment.getCost(),
                clientEquipment.getSerialNumber(), clientEquipment.getPhoneNumber(), clientEquipment.getVendorId(),
                clientEquipment.getClientEquipmentId()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                clientEquipment.getClientId(), clientEquipment.getRouteId(), clientEquipment.getEquipmentId(), clientEquipment.getDateIssued(),
                clientEquipment.getIssuedBy(), clientEquipment.getDateReturned(), clientEquipment.getReceivedBy(),
                clientEquipment.getReturnedCondition(), clientEquipment.getUniqueId(), clientEquipment.getNickname(), 
                clientEquipment.getMdn(), clientEquipment.getDateOfContractRenewal(), clientEquipment.getCost(),
                clientEquipment.getSerialNumber(), clientEquipment.getPhoneNumber(), clientEquipment.getVendorId(),
                clientEquipment.getActive(),
                clientEquipment.getClientEquipmentId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("client_equipment ");
            sql.append("(");
            sql.append("    client_id, route_id, equipment_id, date_issued, issued_by, date_returned, ");
            sql.append("    received_by, returned_condition, unique_id, nickname, mdn, ");
            sql.append("    date_of_contract_renewal, cost, serial_number, phone_number, vendor_id, ");
            sql.append("    client_equipment_id ");
            sql.append(") ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append("client_equipment ");
            sql.append("SET ");
            sql.append("client_id = ?, route_id = ?, equipment_id = ?, date_issued = ?, issued_by = ?, date_returned = ?, ");
            sql.append("received_by = ?, returned_condition = ?, unique_id = ?, nickname = ?, mdn = ?, ");
            sql.append("date_of_contract_renewal = ?, cost = ?, serial_number = ?, phone_number = ?, vendor_id = ?, ");
            sql.append("active = ? ");
            sql.append("WHERE ");
            sql.append("client_equipment_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
