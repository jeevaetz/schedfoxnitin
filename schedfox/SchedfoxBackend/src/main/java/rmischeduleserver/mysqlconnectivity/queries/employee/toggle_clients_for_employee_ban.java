/*
 * employee_banned_update.java
 *
 * Created on January 25, 2005, 9:16 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 * @author ira
 */
public class toggle_clients_for_employee_ban extends GeneralQueryFormat {
    
    private boolean isBan;
    
    /** Creates a new instance of employee_banned_update */
    public toggle_clients_for_employee_ban() {

    }

    public void update(Integer clientId, Integer employeeId, boolean ban) {
        isBan = ban;
        if (isBan) {
            super.setPreparedStatement(new Object[]{employeeId, clientId, employeeId, clientId});
        } else {
            super.setPreparedStatement(new Object[]{employeeId, clientId});
        }
    }
    
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM employee_banned ");
        sql.append("WHERE ");
        sql.append("employee_id = ? AND client_id = ?; ");
        if (isBan) {
            sql.append("INSERT INTO ");
            sql.append("employee_banned ");
            sql.append("(employee_id, client_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
