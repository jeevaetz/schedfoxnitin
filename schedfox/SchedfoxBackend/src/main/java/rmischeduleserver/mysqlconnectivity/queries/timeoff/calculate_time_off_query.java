/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.timeoff;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class calculate_time_off_query extends GeneralQueryFormat {

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
        sql.append("SELECT *, (CASE WHEN reset_accrual AND cycles > 1 THEN 1 * days ELSE cycles * days END) days_off ");
        sql.append("FROM (");
        sql.append("SELECT *, ");
        sql.append("floor(floor((extract(epoch from qualified_time) / extract(epoch from time_off_interval)))) as cycles ");
        sql.append("FROM ");
        sql.append("( ");
        sql.append("	SELECT *, ");
        sql.append("    DATE(employee_hire_date + start_interval) as start_date, ");
	sql.append("    (CASE WHEN NOW() < DATE(employee_hire_date + end_interval) THEN DATE(NOW()) ELSE DATE(employee_hire_date + end_interval) END) as end_date, ");
        sql.append("	(CASE WHEN (time_worked - start_interval) > end_interval THEN end_interval WHEN (time_worked - start_interval) > interval '0 days' THEN (time_worked - start_interval) ELSE interval '0 days' END) as qualified_time ");
        sql.append("	FROM ");
        sql.append("	( ");
        sql.append("		SELECT employee_hire_date, (NOW() - employee_hire_date) as time_worked,  ");
        sql.append("		start_interval, end_interval, employee_id, reset_accrual, ");
        sql.append("		(CASE WHEN (NOW() - employee_hire_date) > (start_interval + time_off_interval) THEN true ELSE false END) as qualifies, ");
        sql.append("		time_off_interval, days ");
        sql.append("		FROM ");
        sql.append("		employee ");
        sql.append("		INNER JOIN time_off_series ON time_off_series.employee_type_id IN (SELECT employee_type_id FROM f_employee_list_types_query(employee.employee_id) WHERE employee_type_sel = true) AND time_off_series.time_off_type_id = ? ");
        sql.append("		INNER JOIN time_off_accrual ON time_off_accrual.time_off_series_id = time_off_series.time_off_series_id ");
        sql.append("            WHERE ");
        sql.append("            employee_id = ? ");
        sql.append("	) as timeoffinfo ");
        sql.append(") as qualifiedinfo ");
        sql.append(") as totalinfo ");

        return sql.toString();
    }

}
