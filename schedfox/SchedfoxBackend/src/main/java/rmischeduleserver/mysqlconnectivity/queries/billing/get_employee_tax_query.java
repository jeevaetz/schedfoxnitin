/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employee_tax_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("employee_tax ");
        sql.append("INNER JOIN tax_types ON tax_types.tax_type_id = employee_tax.tax_type ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".tax_region ON tax_region.tax_region_id = tax_types.tax_region_id ");
        sql.append("WHERE ");
        sql.append("employee_id = ?;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
