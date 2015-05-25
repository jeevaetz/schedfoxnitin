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
public class get_notified_checkins_that_have_not_resolved_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("checkin_notifications ");
        sql.append("LEFT JOIN checkin ON checkin.shift_id = checkin_notifications.shift_id ");
        sql.append("WHERE ");
        sql.append("notification_time > NOW() - interval '2 hours' AND resolution_sent != true AND checkin.shift_id IS NOT NULL AND client_contact_id != 0 ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
