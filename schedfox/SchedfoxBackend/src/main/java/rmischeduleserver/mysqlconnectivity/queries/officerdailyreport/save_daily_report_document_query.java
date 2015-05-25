/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.OfficerDailyReportDocument;

/**
 *
 * @author user
 */
public class save_daily_report_document_query extends GeneralQueryFormat {
    
    private boolean isInsert;
    
    public void update(OfficerDailyReportDocument document, boolean isInsert) {
        this.isInsert = isInsert;
        if (document.getOfficerDailyReportDocumentId() == null || document.getOfficerDailyReportDocumentId() == 0) {
            super.setPreparedStatement(new Object[] {
                document.getOfficerDailyReportTextId(), document.getDocumentData(), document.getThumbnailData(),
                document.getMimeType(), document.getOfficerDailyReportDocumentId()
            });
        } else {
            super.setPreparedStatement(new Object[] {
                document.getOfficerDailyReportTextId(), document.getDocumentData(), document.getThumbnailData(),
                document.getMimeType(), document.getOfficerDailyReportDocumentId()
            });
        }
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO ");
            sql.append("officer_daily_report_documents ");
            sql.append("(officer_daily_report_text_id, document_data, thumbnail_data, mime_type, officer_daily_report_document_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("officer_daily_report_documents ");
            sql.append("SET ");
            sql.append("officer_daily_report_text_id = ?, document_data = ?, thumbnail_data = ?, mime_type = ? ");
            sql.append("WHERE ");
            sql.append("officer_daily_report_document_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    
}
