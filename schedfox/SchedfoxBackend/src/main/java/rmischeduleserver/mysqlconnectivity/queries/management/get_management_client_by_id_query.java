/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.management;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_management_client_by_id_query extends GeneralQueryFormat {

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append(this.getManagementSchema()).append(".management_clients ");
        sql.append("WHERE ");
        sql.append("management_id = ? ");
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
}
