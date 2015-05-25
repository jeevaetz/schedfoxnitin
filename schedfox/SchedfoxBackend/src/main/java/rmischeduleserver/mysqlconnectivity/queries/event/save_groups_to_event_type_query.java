/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.event;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class save_groups_to_event_type_query extends GeneralQueryFormat {

    private int size;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer eventTypeId, ArrayList<Integer> groupIds) {
        size = groupIds.size();
        ArrayList<Object> data = new ArrayList<Object>();
        data.add(eventTypeId);
        for (int g = 0; g < groupIds.size(); g++) {
            data.add(eventTypeId);
            data.add(groupIds.get(g));
        }
        super.setPreparedStatement(data.toArray());
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append("event_type_to_groups ");
        sql.append("WHERE ");
        sql.append("event_type_id = ?; ");
        for (int s = 0; s < size; s++) {
            sql.append("INSERT INTO event_type_to_groups ");
            sql.append("(event_type_id, group_id) ");
            sql.append(" VALUES ");
            sql.append("(?, ?);");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
