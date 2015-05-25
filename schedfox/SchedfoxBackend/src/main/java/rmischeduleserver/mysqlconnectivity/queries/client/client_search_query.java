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
public class client_search_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT client.* FROM client ");
        sql.append("LEFT JOIN client_contact ON client_contact.client_id = client.client_id ");
        sql.append("WHERE ");
        sql.append("(client_name ILIKE (?) OR ? = '') AND ");
        sql.append("(replace(?, '-', '') IN (replace(client_phone, '-', ''), replace(client_phone2, '-', ''), replace(client_fax, '-', ''), replace(client_contact_phone, '-', ''), replace(client_contact_cell, '-', ''), replace(client_contact_fax, '-', '')) OR ? = '') AND ");
        sql.append("(client_contact_email ILIKE (?) OR ? = '') AND ");
        sql.append("(client_city ILIKE (?) OR ? = '') AND ");
        sql.append("(client_zip ILIKE (?) OR ? = '') ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
