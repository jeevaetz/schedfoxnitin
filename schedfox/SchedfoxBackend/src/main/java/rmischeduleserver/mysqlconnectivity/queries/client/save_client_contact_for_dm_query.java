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
public class save_client_contact_for_dm_query extends GeneralQueryFormat {

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
        sql.append("INSERT INTO ");
        sql.append("client_phone_calls ");
        sql.append("(client_id, user_id, date_of_contact, customer_rating, is_email, message_resolution) ");
        sql.append("VALUES ");
        sql.append("(?, ?, NOW(), ?, ?, ?)");
        return sql.toString();
    }

}
