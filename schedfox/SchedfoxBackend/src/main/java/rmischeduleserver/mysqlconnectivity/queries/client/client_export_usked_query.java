/*
 * client_export_usked_query.java
 *
 * Created on February 14, 2005, 9:07 AM
 */
package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.*;

/**
 *
 * @author ira
 */
public class client_export_usked_query extends GeneralQueryFormat {

    /** Creates a new instance of employee_export_usked_query */
    public client_export_usked_query() {
        myReturnString = new String();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("* ");
        sql.append("FROM usked_client ");
        sql.append("WHERE ");
        sql.append("usked_client.client_id = ?;");
        return sql.toString();
    }

    public boolean hasAccess() {
        return true;
    }
}
