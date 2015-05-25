/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.SalesTax;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_sales_tax_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(SalesTax salesTax) {
        if (salesTax.getSalesTaxId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{salesTax.getSalesTax(),
                salesTax.getExportId(), salesTax.getSalesTaxRate()});
        } else {
            isInsert = false;
            super.setPreparedStatement(new Object[]{salesTax.getSalesTax(),
                salesTax.getExportId(), salesTax.getSalesTaxRate(),
                salesTax.getSalesTaxId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO sales_tax ");
            sql.append("(sales_tax, export_id, sales_tax_rate)");
            sql.append("VALUES ");
            sql.append("(?, ?, ?);");
        } else {
            sql.append("UPDATE sales_tax ");
            sql.append("SET ");
            sql.append("sales_tax = ?, ");
            sql.append("export_id = ?, ");
            sql.append("sales_tax_rate = ? ");
            sql.append("WHERE ");
            sql.append("sales_tax_id = ? ");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
