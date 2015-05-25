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
public class get_all_active_clients_with_instant_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client.* FROM ");
        sql.append("client ");
        sql.append("INNER JOIN mobile_forms_to_client ON mobile_forms_to_client.client_id = client.client_id ");
        sql.append("INNER JOIN mobile_forms ON mobile_forms_to_client.mobile_forms_id = mobile_forms.mobile_forms_id ");
        sql.append("INNER JOIN mobile_form_fillout ON mobile_form_fillout.mobile_form_id = mobile_forms.mobile_forms_id AND mobile_form_fillout.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("client_is_deleted != 1 AND send_immediately = true AND notification_sent IS NULL AND mobile_form_fillout.date_entered > NOW() - interval '1 hour' ");
        sql.append("ORDER BY client_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
