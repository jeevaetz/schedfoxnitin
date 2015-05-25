/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.ClientEquipmentContact;

/**
 *
 * @author ira
 */
public class save_client_equipment_contact_query extends GeneralQueryFormat {

    private boolean isInsert = true;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    public void update(ClientEquipmentContact contact, boolean isInsert) {
        this.isInsert = isInsert;
        if (isInsert) {
            super.setPreparedStatement(new Object[]{contact.getClientEquipmentId(), contact.getVersion(), contact.getContactDate(),
                contact.getGpson(), contact.getInairplanemode(), contact.getSignalPercentage(), contact.getHasGooglePlayServices(),
                contact.getNetworkLocationOn(), contact.getBatteryPercentage(), contact.getPowerOn(), contact.getPowerOff()});
        } else {
            super.setPreparedStatement(new Object[]{contact.getClientEquipmentId(), contact.getVersion(), contact.getContactDate(),
                contact.getGpson(), contact.getInairplanemode(), contact.getSignalPercentage(),  contact.getHasGooglePlayServices(),
                contact.getNetworkLocationOn(), contact.getBatteryPercentage(), contact.getPowerOn(), contact.getPowerOff(),
                contact.getClientEquipmentContactId()});
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("client_equipment_contact ");
            sql.append("(client_equipment_id, version, contact_date, gpson, inairplanemode, signal_percentage, has_google_play_services, network_location_on, battery_percentage, power_on, power_off) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ");
        } else {
            sql.append("UPDATE ");
            sql.append("client_equipment_contact ");
            sql.append("SET ");
            sql.append("client_equipment_id = ?, version = ?, contact_date = ?, gpson = ?, inairplanemode = ?, signal_percentage = ?, has_google_play_services = ?, network_location_on = ?, battery_percentage = ?, power_on = ?, power_off = ? ");
            sql.append("WHERE ");
            sql.append("client_equipment_contact_id = ? ");
        }
        return sql.toString();
    }
}
