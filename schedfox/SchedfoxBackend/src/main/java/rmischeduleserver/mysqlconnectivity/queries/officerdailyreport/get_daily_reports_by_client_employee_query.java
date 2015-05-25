/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_daily_reports_by_client_employee_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("* ");
        sql.append("FROM ");
        sql.append("officer_daily_report ");
        sql.append("INNER JOIN officer_daily_report_text ON officer_daily_report_text.officer_daily_report_id = officer_daily_report.officer_daily_report_id ");
        sql.append("WHERE ");
        sql.append("active != false AND ");
        sql.append("(client_id = ? OR ? = -1) AND ");
        sql.append("(employee_id = ? OR ? = -1) AND ");
        sql.append("DATE(logged_in) BETWEEN ? AND ? ");
        sql.append("ORDER BY logged_in DESC ");
        sql.append("LIMIT 200 ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
