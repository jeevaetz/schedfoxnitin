/*
 * retrieveHistoryForGivenGroup.java
 *
 * Created on December 16, 2005, 10:05 AM
 *
 * Copyright: SchedFox 2005
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data.history;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author Ira Juneau
 */
public class retrieveHistoryForGivenGroup extends GeneralQueryFormat {
    
    
    /** Creates a new instance of retrieveHistoryForGivenGroup */
    public retrieveHistoryForGivenGroup() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client.client_id, employee.employee_id, schedule_date, schedule_start, schedule_end, schedule_day, ");
        sql.append("master_id, (employee.employee_first_name || employee.employee_last_name) as ename, client.client_name as cname, ");
        //sql.append("MAX(last_update) as last_update, isdeleted, schedule_history.rate_code_id, schedule_type, history_group_id,  ");
        sql.append("last_update as last_update, isdeleted, schedule_history.rate_code_id, schedule_type, history_group_id,  ");
        sql.append("schedule_history.pay_opt, schedule_history.bill_opt, COALESCE(user_login, 'Employee Changed') as user_login, date_ended, date_started ");
        sql.append("FROM schedule_history ");
        sql.append("LEFT JOIN employee ON employee.employee_id = schedule_history.employee_id ");
        sql.append("LEFT JOIN client ON client.client_id = schedule_history.client_id ");
        sql.append("LEFT JOIN control_db.user ON control_db.user.user_id = schedule_history.last_user ");
        sql.append("WHERE ");
        sql.append("schedule_history.history_group_id = (SELECT schedule_history.history_group_id FROM schedule_history WHERE ");
        sql.append("schedule_id = ? LIMIT 1) ");
        sql.append("AND schedule_history.isdeleted = 0 AND (date_ended >= date_started OR (date_started IS NULL)) ");
        sql.append("ORDER BY history_id ASC ");
        //sql.append("GROUP BY client.client_id, employee.employee_id, schedule_date, schedule_start, schedule_end, schedule_day, master_id, (employee.employee_first_name || employee.employee_last_name), client.client_name, isdeleted, schedule_history.rate_code_id, schedule_type, history_group_id, schedule_history.pay_opt, schedule_history.bill_opt, user_login, date_ended, date_started ");
        //sql.append("ORDER BY MAX(schedule_history.history_id);");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
