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
public class employee_branch_list_query extends GeneralQueryFormat {

    private String schema;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(String schema, int employee_id) {
        this.schema = schema;
        super.setPreparedStatement(new Object[]{employee_id});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("branch.* ");
        sql.append("FROM " + this.schema + ".employee ");
        sql.append("INNER JOIN " + this.getManagementSchema() + ".branch ON branch.branch_id = employee.branch_id ");
        sql.append("WHERE ");
        sql.append("employee.employee_id = ?");
        return sql.toString();
    }
}
