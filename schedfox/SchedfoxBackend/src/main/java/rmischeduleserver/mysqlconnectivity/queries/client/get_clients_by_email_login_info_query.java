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
public class get_clients_by_email_login_info_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM client ");
        sql.append("INNER JOIN client_contact ON client_contact.client_id = client.client_id AND client_contact_is_deleted != 1 ");
        sql.append("WHERE ");
        sql.append("upper(trim(client_contact_email)) = upper(trim(?)) AND ");
        sql.append("upper(trim(cpassword)) = upper(trim(?)) ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
