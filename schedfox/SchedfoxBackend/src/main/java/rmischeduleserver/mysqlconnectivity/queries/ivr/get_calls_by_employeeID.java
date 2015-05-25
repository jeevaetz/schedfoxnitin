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
public class get_calls_by_employeeID extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM inbound_call");
        sql.append(" WHERE ");
        sql.append("employeeID ");
        sql.append(" = ");
        sql.append("?");

        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
