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
public class get_sales_images_query extends GeneralQueryFormat {

    private Boolean includeData;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Boolean includeData) {
        this.includeData = includeData;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sales_expense_id, sales_expense_images_id ");
        if (includeData) {
            sql.append(", image ");
        }
        sql.append("FROM ");
        sql.append("sales_expense_images ");
        sql.append("WHERE ");
        sql.append("sales_expense_id = ? ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
