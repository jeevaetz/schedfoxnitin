/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.user;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_user_by_group_and_branches_query extends GeneralQueryFormat {

    private Integer myParamSize;
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    public void update(Integer companyId, String groupName, ArrayList<Integer> branchIds) {
        Object[] params = new Object[branchIds.size() + 2];
        params[0] = groupName;
        params[1] = companyId;
        for (int b = 0; b < branchIds.size(); b++) {
            params[b + 2] = branchIds.get(b);
        }
        super.setPreparedStatement(params);
        myParamSize = branchIds.size();
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
        sql.append("groups_name = ? AND company_id = ? AND user_is_deleted != 1 AND ");
        sql.append("branch_id IN ( ");
        for (int p = 0; p < myParamSize; p++) {
            if (p > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("ORDER BY user_first_name, user_last_name");
        return sql.toString();
    }

}
