/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.login;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employees_by_cid extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM employee ");
        sql.append("WHERE ");
        sql.append("(regexp_replace(employee_phone, '[^0-9]', '', 'g') = regexp_replace(?, '[^0-9]', '', 'g') ");
        sql.append(" OR ");
        sql.append(" regexp_replace(employee_phone2, '[^0-9]', '', 'g') = regexp_replace(?, '[^0-9]', '', 'g') ");
        sql.append(" OR ");
        sql.append(" regexp_replace(employee_cell, '[^0-9]', '', 'g') = regexp_replace(?, '[^0-9]', '', 'g') ");
        sql.append(") AND employee_is_deleted != 1; ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
