/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.gps;

import java.util.Set;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class load_gps_transitions_query extends GeneralQueryFormat {

    private Integer mySize;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Set<Integer> myList) {
        super.setPreparedStatement(myList.toArray());
        mySize = myList.size();
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("first_value(geo_fencing_transition_id) OVER wnd as geo_fencing_transition_id, ");
        sql.append("first_value(geo_fence_id) OVER wnd as geo_fence_id, ");
        sql.append("first_value(transition_type) OVER wnd as transition_type, ");
        sql.append("first_value(transition_date) OVER wnd as transition_date, ");
        sql.append("first_value(alert_sent_on) OVER wnd as alert_sent_on, ");
        sql.append("first_value(client_equipment_id) OVER wnd as client_equipment_id ");
        sql.append("FROM ");
        sql.append("geo_fencing_transition ");
        sql.append("WHERE ");
        sql.append("client_equipment_id IN (");
        for (int i = 0; i < this.mySize; i++) {
            if (i != 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("WINDOW wnd AS ");
        sql.append("( ");
        sql.append("	PARTITION BY geo_fence_id ORDER BY geo_fencing_transition_id DESC ");
        sql.append("	ROWS BETWEEN UNBOUNDED PRECEDING AND UNBOUNDED FOLLOWING ");
        sql.append(")");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
