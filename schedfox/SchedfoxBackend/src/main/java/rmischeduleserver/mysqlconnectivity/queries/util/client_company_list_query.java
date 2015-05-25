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
public class client_company_list_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    public void update(String schema) {
        super.setPreparedStatement(new Object[]{schema});
    }

    @Override
    public String getPreparedStatementString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("company.company_id, ");
        sql.append("company_name, ");
        sql.append("company_db ");
        sql.append("FROM " + this.getManagementSchema() + ".company ");
        sql.append("WHERE ");
        sql.append("company.company_db = ?");
        return sql.toString();
    }
}
