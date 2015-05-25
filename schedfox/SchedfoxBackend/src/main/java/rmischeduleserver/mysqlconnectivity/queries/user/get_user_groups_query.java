/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_user_groups_query extends GeneralQueryFormat {

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
        sql.append("SELECT groups.* FROM ");
        sql.append(getManagementSchema()).append(".user_groups ");
        sql.append("INNER JOIN ").append(getManagementSchema()).append(".groups ON groups.groups_id = user_groups.groups_id ");
        sql.append("WHERE ");
        sql.append("user_groups.user_id = ?;");
        return sql.toString();
    }

}
