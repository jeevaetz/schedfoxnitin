/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.Branch;

/**
 *
 * @author ira
 */
public class save_branch_query extends GeneralQueryFormat {

    private boolean isUpdate;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Branch branch, boolean isUpdate) {
        this.isUpdate = isUpdate;
        super.setPreparedStatement(new Object[]{
                    branch.getBranchName(), branch.getBranchManagementId(), branch.getTimezone(), branch.getUsBranch(), branch.getBranchId()
                });
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isUpdate) {
            sql.append("UPDATE ");
            sql.append(super.getManagementSchema()).append(".branch ");
            sql.append("SET ");
            sql.append("branch_name = ?, branch_management_id = ?, timezone = ?, us_branch = ? ");
            sql.append("WHERE ");
            sql.append("branch_id = ?; ");
        } else {
            sql.append("INSERT INTO ");
            sql.append(super.getManagementSchema()).append(".branch ");
            sql.append("(branch_name, branch_management_id, timezone, us_branch, branch_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?); ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
