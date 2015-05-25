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
public class daily_report_texts_by_id_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("officer_daily_report_text.*, ");
        sql.append("(SELECT COUNT(*) FROM officer_daily_report_documents WHERE officer_daily_report_text.officer_daily_report_text_id = officer_daily_report_documents.officer_daily_report_text_id) as piccount ");
        sql.append("FROM ");
        sql.append("officer_daily_report_text ");
        sql.append("WHERE ");
        sql.append("officer_daily_report_text_id = ?");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
