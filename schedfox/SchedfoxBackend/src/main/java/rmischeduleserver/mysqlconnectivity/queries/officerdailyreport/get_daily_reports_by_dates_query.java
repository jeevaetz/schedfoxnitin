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
public class get_daily_reports_by_dates_query extends GeneralQueryFormat {

    private boolean showAll;
    
    public get_daily_reports_by_dates_query(boolean showAll) {
        this.showAll = showAll;
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("officer_daily_report.* "); 
        sql.append("FROM ");
        sql.append("officer_daily_report ");
        if (!showAll) {
            sql.append("INNER JOIN officer_daily_report_text ON officer_daily_report_text.officer_daily_report_id = officer_daily_report.officer_daily_report_id ");
        }
        sql.append("WHERE ");
        sql.append("DATE(logged_in) BETWEEN DATE(?) AND DATE(?) ");
        sql.append("ORDER BY logged_in DESC ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
