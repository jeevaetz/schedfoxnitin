/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.EmployeeNotes;

/**
 *
 * @author user
 */
public class delete_employee_note_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM ");
        sql.append("employee_notes ");
        sql.append("WHERE ");
        sql.append("employee_notes_id = ?; ");
        return sql.toString();
    }

}
