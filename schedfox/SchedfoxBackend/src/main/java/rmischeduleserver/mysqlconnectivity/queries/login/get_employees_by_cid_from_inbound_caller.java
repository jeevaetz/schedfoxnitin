/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.login;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author dalbers
 */
public class get_employees_by_cid_from_inbound_caller extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT employee.* FROM employee ");
        sql.append("INNER JOIN communication_transaction ON ");
        sql.append("    communication_transaction.associated_user_id = employee.employee_id AND ");
        sql.append("    communication_transaction.associated_user_type = 1 ");
        sql.append("INNER JOIN communication_source ON ");
        sql.append("    communication_source.communication_source_id = communication_transaction.communication_source_id ");
        sql.append("WHERE ");
        sql.append("communication_source.source = ? ");
        return sql.toString();
    }

    

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
