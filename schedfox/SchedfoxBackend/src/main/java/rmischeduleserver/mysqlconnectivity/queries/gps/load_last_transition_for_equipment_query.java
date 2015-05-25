/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.gps;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class load_last_transition_for_equipment_query extends GeneralQueryFormat {

    private ArrayList<Integer> clientEquipmentIds;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(ArrayList<Integer> clientEquipmentIds) {
        super.setPreparedStatement(clientEquipmentIds.toArray());
        this.clientEquipmentIds = clientEquipmentIds;
    }
    
    public String getPreparedStatemenString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("geo_fencing_transition ");
        sql.append("WHERE ");
        sql.append("client_equipment_id IN (");
        for (int c = 0; c < clientEquipmentIds.size(); c++) {
            if (c > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        return sql.toString();
    }
    
}
