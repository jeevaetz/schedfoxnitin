/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_deduction_types_query extends GeneralQueryFormat {
    public boolean hasAccess() {
        return true;
    }

    public boolean hasPreparedStatement() {
        return true;
    }

    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee_deduction_types ");
        sql.append("ORDER BY ");
        sql.append("description ");
        return sql.toString();
    }
}
