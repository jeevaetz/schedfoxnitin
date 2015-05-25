/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class daily_reports_by_client_id_and_dates_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("officer_daily_report.* ");
        sql.append("FROM ");
        sql.append("officer_daily_report ");
        sql.append("WHERE ");
        sql.append("client_id = ? AND logged_in BETWEEN ? AND ?;");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
