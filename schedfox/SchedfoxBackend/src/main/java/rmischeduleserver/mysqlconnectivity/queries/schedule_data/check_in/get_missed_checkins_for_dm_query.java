/*
 * new_assemble_schedule_for_checkin_query.java
 *
 * Created on August 30, 2005, 8:20 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.check_in;
import java.util.Date;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author Ira Juneau
 */
public class get_missed_checkins_for_dm_query extends GeneralQueryFormat {

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT branch_name, mytime.*, mydata.* FROM ");
        sql.append("assemble_schedule(DATE(NOW() - interval '1 day'), DATE(NOW()), -1, -1, null::timestamp, null::integer[], 60) as mydata ");
        sql.append("LEFT JOIN client ON client.client_id = mydata.cid ");
        sql.append("LEFT JOIN control_db.branch ON branch.branch_id = client.branch_id ");
        sql.append("LEFT JOIN checkin_notifications ON checkin_notifications.shift_id = mydata.sid AND user_id IS NOT NULL ");
        sql.append("LEFT JOIN ");
        sql.append("(SELECT NOW()::timestamp at time zone(current_setting('TIMEZONE')) AT time zone timezone, branch_id ");
        sql.append(" FROM control_db.branch) as mytime ON mytime.branch_id = client.branch_id ");
        sql.append("WHERE ");
        //Make sure that the shift hasn't been modified since start of shift.
        sql.append("mydata.lu < NOW() - interval '1 minutes' * (client.notify_dm_late_checkout_minutes) AND ");
        sql.append("(extract (hour from mytime.timezone) * 60 + extract (minute from mytime.timezone)) >= (mydata.start_time + client.notify_dm_late_checkout_minutes) AND ");
        sql.append("(extract (hour from mytime.timezone) * 60 + extract (minute from mytime.timezone)) < (mydata.start_time + ?) AND ");
        sql.append("person_checked_in IS NULL AND client.notify_dm_late_checkout_minutes > -1 AND client.client_is_deleted != 1 AND ");
        sql.append("checkin_notifications.shift_id IS NULL ");
        sql.append("ORDER BY mydata.branch_id, mydata.start_time, mydata.end_time");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
}
