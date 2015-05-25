/*
 * employee_info_query.java
 *
 * Created on January 24, 2005, 2:49 PM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class get_employee_info_by_scan_query extends GeneralQueryFormat {
    
    /** Creates a new instance of employee_info_query */
    public get_employee_info_by_scan_query() {

    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee ");
        sql.append("WHERE ");
        sql.append("employee_scan_id = ? AND employee_is_deleted != 1 ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
