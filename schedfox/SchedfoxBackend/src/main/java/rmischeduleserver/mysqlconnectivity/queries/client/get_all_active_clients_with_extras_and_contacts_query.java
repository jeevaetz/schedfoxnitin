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
public class get_all_active_clients_with_extras_and_contacts_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("*, ");
        sql.append("(SELECT customer_rating FROM client_ratings WHERE client_ratings.client_id = client.client_id ORDER BY date_of_rating DESC LIMIT 1) as last_rating, ");
        sql.append("(CASE WHEN dynamic_field_value.dynamic_field_value = 'Select a value' THEN '' ELSE dynamic_field_value.dynamic_field_value END) AS client_industry, ");
        sql.append("client.client_id as client_id ");
        sql.append("FROM ");
        sql.append("client ");
        sql.append("LEFT JOIN dynamic_field_value ON dynamic_field_value.dynamic_field_def_id = 8 AND dynamic_field_value.key_for_value = client.client_id ");
        sql.append("LEFT JOIN client_contact ON client_contact.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 ");
        sql.append("ORDER BY client_name ");
        sql.append("LIMIT ? ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
