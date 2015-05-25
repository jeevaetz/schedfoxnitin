/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_first_day_worked_query extends GeneralQueryFormat {

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
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT * FROM (SELECT ");
        sql.append("MIN(schedule_date) as minDate ");
        sql.append("FROM schedule ");
        sql.append("WHERE ");
        sql.append("schedule.employee_id = ? AND schedule.client_id = ? ");
        sql.append("AND schedule_is_deleted = 0) as one ");
        sql.append("UNION ");
        sql.append("(SELECT ");
        sql.append("MIN(schedule_master_date_started) as minDate ");
        sql.append("FROM schedule_master ");
        sql.append("WHERE ");
        sql.append("schedule_master.employee_id = ? AND schedule_master.client_id = ?) ");
        return sql.toString();
    }

}
