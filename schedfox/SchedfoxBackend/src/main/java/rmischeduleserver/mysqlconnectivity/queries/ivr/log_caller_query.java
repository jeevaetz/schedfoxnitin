/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.ivr;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author dalbers
 */
public class log_caller_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO inboundCalls (");
        sql.append("employeeID, ");
        sql.append("locationID, ");
        sql.append("CID, ");
        sql.append("timestamp ) ");
        sql.append("VALUES (");
        sql.append("?, ");
        sql.append("?, ");
        sql.append("?, ");
        sql.append("Now())");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
