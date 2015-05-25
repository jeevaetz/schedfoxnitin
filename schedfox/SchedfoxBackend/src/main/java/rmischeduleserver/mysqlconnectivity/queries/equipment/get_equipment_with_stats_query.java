/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.equipment;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_equipment_with_stats_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("client_equipment ");
        sql.append("INNER JOIN equipment ON client_equipment.equipment_id = equipment.equipment_id ");
        sql.append("INNER JOIN ");
        sql.append("( ");
        sql.append("	SELECT DISTINCT ON (client_equipment_id) ");
        sql.append("	client_equipment_id, ");
        sql.append("	first_value(contact_time) OVER wnd as contact_time, ");
        sql.append("	first_value(gpson) OVER wnd as gpson, ");
        sql.append("	first_value(version) OVER wnd as version, ");
        sql.append("	first_value(inairplanemode) OVER wnd as inairplanemode, ");
        sql.append("	first_value(signal_percentage) OVER wnd as signal_percentage, ");
        sql.append("	first_value(has_google_play_services) OVER wnd as has_google_play_services, ");
        sql.append("	first_value(network_location_on) OVER wnd as network_location_on, ");
        sql.append("	first_value(battery_percentage) OVER wnd as battery_percentage, ");
        sql.append("	first_value(power_on) OVER wnd as power_on, ");
        sql.append("	first_value(power_off) OVER wnd as power_off ");
        sql.append("	FROM ");
        sql.append("	client_equipment_contact  ");
        sql.append("	WHERE ");
        sql.append("	contact_time > NOW() - interval '30 days' ");
        sql.append("	WINDOW wnd AS ( ");
        sql.append("	PARTITION BY client_equipment_id ORDER BY client_equipment_contact_id DESC ");
        sql.append("	ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ");
        sql.append("	) ");
        sql.append(") as mydata ON mydata.client_equipment_id = client_equipment.client_equipment_id ");
        sql.append("LEFT JOIN client ON client.client_id = client_equipment.client_id ");
        sql.append("WHERE ");
        sql.append("(equipment.equipment_id = ? OR ? = -1) AND (client_equipment.client_id = ? OR -1 = ?) AND ");
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
