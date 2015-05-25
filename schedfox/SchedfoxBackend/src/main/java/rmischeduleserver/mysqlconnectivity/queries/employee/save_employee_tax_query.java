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
public class save_employee_tax_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("DELETE FROM employee_tax ");
        sql.append("WHERE ");
        sql.append("employee_id = ?;");
        sql.append("INSERT INTO employee_tax ");
        sql.append("(tax_type, marriage_status, exemptions, additional_withholding, employee_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?);");
        sql.append("INSERT INTO employee_tax ");
        sql.append("(tax_type, marriage_status, exemptions, additional_withholding, employee_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?);");
        sql.append("INSERT INTO employee_tax ");
        sql.append("(tax_type, marriage_status, exemptions, additional_withholding, employee_id) ");
        sql.append("VALUES ");
        sql.append("(?, ?, ?, ?, ?);");

        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
