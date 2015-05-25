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
public class get_sales_itinerary_for_month_query extends GeneralQueryFormat {
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("sales_itinerary ");
        sql.append("WHERE ");
        sql.append("user_id = ? AND DATE_PART('month', DATE(?)) = DATE_PART('month', date_of_itinerary) AND ");
        sql.append("DATE_PART('year', DATE(?)) = DATE_PART('year', date_of_itinerary); ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
