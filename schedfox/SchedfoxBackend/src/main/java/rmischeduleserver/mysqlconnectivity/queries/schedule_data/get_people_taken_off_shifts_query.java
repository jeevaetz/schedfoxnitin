/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.schedule_data;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_people_taken_off_shifts_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT branch_name, client.client_id, employee.employee_id FROM ");
        sql.append("( ");
        sql.append("	SELECT eid, cid FROM ");
        sql.append("	( ");
        sql.append("		SELECT  ");
        sql.append("		eid, cid, DATE(DATE_TRUNC('WEEK', \"date\")), COUNT(*) as shift_count ");
        sql.append("		FROM assemble_schedule(DATE(DATE_TRUNC('WEEK', NOW() - interval '6 weeks')), DATE(DATE_TRUNC('WEEK', NOW()) - interval '1 day'), ?) ");
        sql.append("		GROUP BY eid, cid, DATE(DATE_TRUNC('WEEK', \"date\")) ");
        sql.append("		ORDER BY cid, eid, DATE(DATE_TRUNC('WEEK', \"date\")) ");
        sql.append("	) as past_data ");
        sql.append("	GROUP BY eid, cid ");
        sql.append("	HAVING COUNT(*) = 6 ");
        sql.append(") as past_weeks ");
        sql.append("LEFT JOIN ( ");
        sql.append("	SELECT eid, cid FROM ");
        sql.append("	( ");
        sql.append("		SELECT  ");
        sql.append("		eid, cid, DATE(DATE_TRUNC('WEEK', \"date\")), COUNT(*) as shift_count ");
        sql.append("		FROM assemble_schedule(DATE(DATE_TRUNC('WEEK', NOW() + interval '1 weeks')), DATE(DATE_TRUNC('WEEK', NOW()) + interval '13 day'), ?) ");
        sql.append("		GROUP BY eid, cid, DATE(DATE_TRUNC('WEEK', \"date\")) ");
        sql.append("		ORDER BY cid, eid, DATE(DATE_TRUNC('WEEK', \"date\")) ");
        sql.append("	) as past_data ");
        sql.append("	GROUP BY eid, cid ");
        sql.append("	HAVING COUNT(*) = 1 ");
        sql.append(") as future_weeks ON future_weeks.eid = past_weeks.eid AND future_weeks.cid = past_weeks.cid ");
        sql.append("LEFT JOIN personnel_changes ON personnel_changes.client_id = past_weeks.cid AND date_of_change BETWEEN NOW() - interval '2 weeks' AND NOW() AND personnel_changes.reason_id != -1 ");
        sql.append("INNER JOIN client ON client.client_id = past_weeks.cid ");
        sql.append("INNER JOIN employee ON employee.employee_id = past_weeks.eid ");
        sql.append("INNER JOIN control_db.branch ON branch.branch_id = client.branch_id ");
        sql.append("WHERE ");
        sql.append("future_weeks.cid IS NULL AND personnel_changes.personnel_change_id IS NULL ");
        sql.append("AND client.branch_id = ? ");
        sql.append("ORDER BY branch_name, client_name"); 
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
