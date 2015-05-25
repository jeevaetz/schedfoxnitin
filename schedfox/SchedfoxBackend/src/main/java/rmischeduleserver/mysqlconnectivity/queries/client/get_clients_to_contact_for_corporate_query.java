/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_clients_to_contact_for_corporate_query extends GeneralQueryFormat {

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
        StringBuilder loadUsers = new StringBuilder();
        loadUsers.append(" IN (SELECT user_id FROM ").append(getManagementSchema()).append(".user_groups ");
        loadUsers.append("INNER JOIN ").append(getManagementSchema()).append(".groups ON groups.groups_id = user_groups.groups_id ");
        loadUsers.append("WHERE ");
        loadUsers.append("UPPER(groups_name) IN ('CORPORATE USER', UPPER('Courtesy Call Manager'))) ");
        
        StringBuilder sql = new StringBuilder();
        
        sql.append("SELECT * FROM ");
        sql.append("( ");
        sql.append("	SELECT  ");
        sql.append("	ccs_last_week + contacts_last_week as last_week_total, ");
        sql.append("	ccs_this_week + contacts_this_week as current_week_total, ");
        sql.append("	* ");
        sql.append("	FROM ");
        sql.append("	( ");
        sql.append("		SELECT DISTINCT client.*, ");
        sql.append("		(SELECT COUNT(*) FROM problemsolver WHERE problemsolver.client_id = client.client_id AND problemsolver.user_id ").append(loadUsers.toString()).append(" AND problemsolver.ps_date > NOW() - interval '1 month') as ccs_this_week, ");
        sql.append("		(SELECT COUNT(*) FROM problemsolver WHERE problemsolver.client_id = client.client_id AND problemsolver.user_id ").append(loadUsers.toString()).append(" AND problemsolver.ps_date BETWEEN (NOW() - interval '2 month') AND (NOW() - interval '1 month')) as ccs_last_week, ");
        sql.append("		(SELECT COUNT(*) FROM client_phone_calls WHERE client_phone_calls.client_id = client.client_id AND client_phone_calls.user_id ").append(loadUsers.toString()).append(" AND  client_phone_calls.date_of_contact > NOW() - interval '1 month') as contacts_this_week, ");
        sql.append("		(SELECT COUNT(*) FROM client_phone_calls WHERE client_phone_calls.client_id = client.client_id AND client_phone_calls.user_id ").append(loadUsers.toString()).append(" AND  client_phone_calls.date_of_contact BETWEEN (NOW() - interval '2 month') AND (NOW() - interval '1 month')) as contacts_last_week ");
        sql.append("		FROM client  ");
        sql.append("		WHERE  ");
        sql.append("		client_is_deleted = 0 AND client.display_client_in_call_queue = true  ");
        sql.append("		ORDER BY client.client_name  ");
        sql.append("	) as inner_data ");
        sql.append(") as contact_data ");
        sql.append("ORDER BY (CASE WHEN last_week_total + current_week_total = 0 THEN 2 WHEN current_week_total = 0 THEN 1 ELSE 0 END) DESC, client_name ");

        return sql.toString();
    }

}
