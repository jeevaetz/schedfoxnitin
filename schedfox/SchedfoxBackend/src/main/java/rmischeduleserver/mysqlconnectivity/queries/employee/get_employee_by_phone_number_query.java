/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_employee_by_phone_number_query extends GeneralQueryFormat {

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
        sql.append("employee_is_deleted != 1 AND ");
        sql.append("(");
        sql.append("    regexp_replace(regexp_replace(employee_phone, '[^0-9]', '', 'g'), '^1', '', 'g') = regexp_replace(regexp_replace(?, '[^0-9]', '', 'g'), '^1', '', 'g') OR ");
        sql.append("    regexp_replace(regexp_replace(employee_phone2, '[^0-9]', '', 'g'), '^1', '', 'g') = regexp_replace(regexp_replace(?, '[^0-9]', '', 'g'), '^1', '', 'g') OR ");
        sql.append("    regexp_replace(regexp_replace(employee_cell, '[^0-9]', '', 'g'), '^1', '', 'g') = regexp_replace(regexp_replace(?, '[^0-9]', '', 'g'), '^1', '', 'g') OR ");
        sql.append("    regexp_replace(regexp_replace(employee_pager, '[^0-9]', '', 'g'), '^1', '', 'g') = regexp_replace(regexp_replace(?, '[^0-9]', '', 'g'), '^1', '', 'g')");
        sql.append(")");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
