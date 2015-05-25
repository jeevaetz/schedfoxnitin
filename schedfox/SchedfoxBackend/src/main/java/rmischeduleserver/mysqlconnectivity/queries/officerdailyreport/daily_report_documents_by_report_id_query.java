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
public class daily_report_documents_by_report_id_query extends GeneralQueryFormat {

    private boolean loadImages;
    
    public daily_report_documents_by_report_id_query(boolean loadImages) {
        this.loadImages = loadImages;
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (loadImages) {
            sql.append("document_data, ");
        }
        sql.append("MAX(officer_daily_report_document_id) as officer_daily_report_document_id, mime_type, MAX(time_entered) as time_entered, ");
        sql.append("officer_daily_report_text_id, thumbnail_data ");
        sql.append("FROM ");
        sql.append("officer_daily_report_documents ");
        sql.append("WHERE ");
        sql.append("officer_daily_report_text_id = ? ");
        sql.append("GROUP BY document_data, thumbnail_data, officer_daily_report_text_id, mime_type ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
