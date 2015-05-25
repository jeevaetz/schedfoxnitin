/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.commissions;

import schedfoxlib.model.Commission;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_commissions_query extends GeneralQueryFormat {

    private boolean isInsert = false;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(Commission commission) {
        if (commission.getCommission_id() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{commission.getCommission_name(), commission.isIsdefault(),
                commission.isActive(), commission.getFirst_pmt_sales_percentage(), commission.getFirst_year_sales_percentage(),
                commission.getFirst_year_manager_percentage(), commission.getSecond_year_sales_percentage(),
                commission.getSecond_year_manager_percentage()});
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{commission.getCommission_name(), commission.isIsdefault(),
                commission.isActive(), commission.getFirst_pmt_sales_percentage(), commission.getFirst_year_sales_percentage(),
                commission.getFirst_year_manager_percentage(), commission.getSecond_year_sales_percentage(),
                commission.getSecond_year_manager_percentage(), commission.getCommission_id()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO commissions ");
            sql.append("(");
            sql.append("    commission_name, isdefault, active, first_pmt_sales_percentage, first_year_sales_percentage, ");
            sql.append("    first_year_manager_percentage, second_year_sales_percentage, second_year_manager_percentage ");
            sql.append(")");
            sql.append("VALUES ");
            sql.append("(");
            sql.append("    ?, ?, ?, ?, ?, ");
            sql.append("    ?, ?, ? ");
            sql.append(");");
        } else {
            sql.append("UPDATE commissions ");
            sql.append("SET ");
            sql.append(" commission_name = ?, isdefault = ?, active = ?, first_pmt_sales_percentage = ?, first_year_sales_percentage = ?, ");
            sql.append(" first_year_manager_percentage = ?, second_year_sales_percentage = ?, second_year_manager_percentage = ? ");
            sql.append("WHERE ");
            sql.append("commission_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
