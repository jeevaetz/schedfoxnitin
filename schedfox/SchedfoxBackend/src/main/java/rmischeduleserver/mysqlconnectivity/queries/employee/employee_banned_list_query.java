/*
 * employee_banned_list_query.java
 *
 * Created on January 25, 2005, 9:07 AM
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import rmischeduleserver.mysqlconnectivity.queries.*;
/**
 *
 * @author ira
 */
public class employee_banned_list_query extends GeneralQueryFormat {
    
    private String EmployeeId;

    private PreparedStatement myStatement;
    
    /** Creates a new instance of employee_banned_list_query */
    public employee_banned_list_query() {
        myReturnString = new String();
    }
    
    public boolean hasAccess() {
        return true;
    }

    /**
     * Does this use prepared statements
     * @return boolean
     */
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        return "SELECT * FROM f_employee_banned_list_query(?);";
    }

    public void update(String employeeId) {
        EmployeeId = employeeId;
    }
    
    public String toString() {
     return "SELECT * FROM f_employee_banned_list_query(" + EmployeeId + ");"; 
    }
    
}
