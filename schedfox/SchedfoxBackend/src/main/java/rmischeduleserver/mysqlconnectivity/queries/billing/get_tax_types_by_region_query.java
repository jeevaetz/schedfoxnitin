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
public class get_tax_types_by_region_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT tax_types.* FROM tax_types ");
        sql.append("INNER JOIN ").append(super.getManagementSchema()).append(".tax_region ON tax_region.tax_region_id = tax_types.tax_region_id ");
        sql.append("WHERE ");
        sql.append("region = ? ");
        sql.append("ORDER BY tax_name ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
