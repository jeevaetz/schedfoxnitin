/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.event;

import java.util.ArrayList;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_events_for_employee_query extends GeneralQueryFormat {

    private Integer employeeId;
    private ArrayList<Integer> eventTypes;
    private String startDate;
    private String endDate;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append(" * ");
        sql.append("FROM ");
        sql.append("event ");
        sql.append("WHERE ");
        sql.append("1 = 1 ");
        sql.append("AND DATE(entered_on) BETWEEN DATE('").append(startDate).append("') AND DATE('").append(endDate).append("') AND employee_id = ").append(employeeId).append(" ");
        sql.append("AND event_type_id IN (");
        for (int s = 0; s < eventTypes.size(); s++) {
            if (s > 0) {
                sql.append(",");
            }
            sql.append(eventTypes.get(s));
        }
        sql.append(") ");
        sql.append("ORDER BY entered_on DESC ");
        return sql.toString();
    }
    
    public void update(ArrayList<Integer> eventTypes, String startDate, String endDate, Integer employeeId) {
        this.eventTypes = eventTypes;
        this.employeeId = employeeId;
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
}
