/*
 * client_worksite_list_query.java
 *
 * Created on March 28, 2005, 8:25 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.client;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
/**
 *
 * @author ira
 */
public class client_worksite_list_query extends GeneralQueryFormat {
    
    /** Creates a new instance of client_worksite_list_query */
    public client_worksite_list_query() {
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
        sql.append(" client_name, client_id, ");
        sql.append(" client_phone, client_worksite, ");
        sql.append(" client_worksite_order, rate_code_id ");
        sql.append(" FROM client ");
        sql.append(" WHERE ");
        sql.append(" client_worksite = ? AND client_is_deleted = 0 ");
        return sql.toString();
    }
    
    public boolean hasAccess() {
        return true;
    }
    
}
