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
public class get_corporate_communicator_contact_query extends GeneralQueryFormat {

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
        sql.append("SELECT * FROM ");
        sql.append("client_to_user_contact ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".\"user\" ON \"user\".user_id = client_to_user_contact.user_id ");
        sql.append("WHERE ");
        sql.append("client_id = ?");
        return sql.toString();
    }

}
