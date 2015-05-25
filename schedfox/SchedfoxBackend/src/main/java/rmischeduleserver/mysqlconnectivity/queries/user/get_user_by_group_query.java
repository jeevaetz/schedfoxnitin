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
public class get_user_by_group_query extends GeneralQueryFormat {

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
        sql.append("SELECT DISTINCT \"user\".* ");
        sql.append("FROM ").append(super.getManagementSchema()).append(".user ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".user_groups ON user_groups.user_id = \"user\".user_id ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".groups ON groups.groups_id = user_groups.groups_id ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".user_branch_company ON user_branch_company.user_id = \"user\".user_id ");
        sql.append("WHERE ");
        sql.append("groups_name = ? AND company_id = ? AND user_is_deleted != 1 AND (branch_id = ? OR ? = -1) ");
        sql.append("ORDER BY user_first_name, user_last_name");
        return sql.toString();
    }

}
