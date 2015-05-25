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
public class get_clients_id_by_login_info_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM client ");
        sql.append("WHERE ");
        sql.append("upper(trim(cusername)) = upper(trim((SELECT cusername FROM client WHERE client_id = ? LIMIT 1))) AND ");
        sql.append("upper(trim(cpassword)) = upper(trim((SELECT cpassword FROM client WHERE client_id = ? LIMIT 1))) ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
