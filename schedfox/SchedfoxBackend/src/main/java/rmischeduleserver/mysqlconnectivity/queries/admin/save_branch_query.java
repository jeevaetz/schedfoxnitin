/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.admin;

import schedfoxlib.model.Branch;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_branch_query extends GeneralQueryFormat {

    private Branch branch;
    private boolean insert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void setBranch(Branch branch, boolean insert) {
        this.branch = branch;
        this.insert = insert;
        if (insert) {
            super.setPreparedStatement(new Object[]{branch.getBranchId(),
                branch.getBranchName(), branch.getBranchManagementId(),
                branch.getTimezone()});
        } else {
            super.setPreparedStatement(new Object[]{
                branch.getBranchName(), branch.getBranchManagementId(),
                branch.getTimezone(), branch.getBranchId()});
        }
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (insert) {
            sql.append("INSERT INTO ");
            sql.append(getManagementSchema()).append(".branch ");
            sql.append("(branch_id, branch_name, branch_management_id, timezone) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?)");
        } else {
            sql.append("UPDATE ");
            sql.append(getManagementSchema()).append(".branch ");
            sql.append("SET ");
            sql.append("branch_name = ?, branch_management_id = ?, timezone = ? ");
            sql.append("WHERE ");
            sql.append("branch_id = ?");
        }
        return sql.toString();
    }

}
