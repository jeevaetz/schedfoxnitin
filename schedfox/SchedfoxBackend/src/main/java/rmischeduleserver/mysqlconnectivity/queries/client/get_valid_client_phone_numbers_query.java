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
public class get_valid_client_phone_numbers_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT client_phone FROM client WHERE client_id = ? ");
        sql.append("UNION ");
        sql.append("SELECT client_phone2 FROM client WHERE client_id = ? ");
        sql.append("UNION ");
        sql.append("SELECT client_contact_phone FROM client_contact WHERE client_id = ? AND client_contact_type_id = 6 ");
        sql.append("UNION ");
        sql.append("SELECT client_contact_cell FROM client_contact WHERE client_id = ? AND client_contact_type_id = 6 ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
