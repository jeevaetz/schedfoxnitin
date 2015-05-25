/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.sales;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_sales_expense_types_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("sales_expense_types ");
        sql.append("ORDER BY ");
        sql.append("sales_expense_type ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
