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
public class company_branch_list_query extends GeneralQueryFormat {
    private String schema;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(String schema, int client_id) {
        this.schema = schema;
        super.setPreparedStatement(new Object[]{client_id});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("branch.* ");
        sql.append("FROM " + this.schema + ".client ");
        sql.append("INNER JOIN " + this.getManagementSchema() + ".branch ON branch.branch_id = client.branch_id ");
        sql.append("WHERE ");
        sql.append("client.client_id = ?");
        return sql.toString();
    }
}
