/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_branch_by_id_query extends GeneralQueryFormat {
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *, branch.branch_id as branch_id FROM ");
        sql.append(super.getManagementSchema()).append(".branch ");
        sql.append("LEFT JOIN ").append(super.getManagementSchema()).append(".branch_info ON branch_info.branch_id = branch.branch_id ");
        sql.append("WHERE ");
        sql.append("branch.branch_id = ?;");
        return sql.toString();
    }
}
