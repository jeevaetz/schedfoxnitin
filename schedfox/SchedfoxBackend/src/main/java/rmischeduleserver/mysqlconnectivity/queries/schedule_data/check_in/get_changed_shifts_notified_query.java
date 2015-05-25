/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_changed_shifts_notified_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("( ");
        sql.append("SELECT ");
        sql.append("COALESCE(schedule.employee_id, schedule_master.employee_id) as emp_id, ");
        sql.append("checkin_notifications.*  ");
        sql.append("FROM ");
        sql.append("checkin_notifications ");
        sql.append("LEFT JOIN schedule ON schedule.schedule_id::text = checkin_notifications.shift_id ");
        sql.append("LEFT JOIN schedule_master ON (-1 * schedule_master.schedule_master_id)::text = substring(checkin_notifications.shift_id, 0, strpos(checkin_notifications.shift_id, '/')) AND ");
        sql.append("	notification_time BETWEEN schedule_master.schedule_master_date_started AND schedule_master.schedule_master_date_ended ");
        sql.append("WHERE ");
        sql.append("notification_time > NOW() - interval '2 hours' AND client_contact_id != 0 AND resolution_sent != true ");
        sql.append(") as mydata ");
        sql.append("WHERE ");
        sql.append("mydata.emp_id != employee_id ");
        return sql.toString();
    }
    
}
