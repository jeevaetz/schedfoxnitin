/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.IncidentReportDocument;

/**
 *
 * @author user
 */
public class save_incident_document_query extends GeneralQueryFormat {
    
    private boolean isInsert;
    
    public void update(IncidentReportDocument document, boolean isInsert) {
        this.isInsert = isInsert;
        if (document.getIncidentReportDocumentId() == null || document.getIncidentReportDocumentId() == 0) {
            super.setPreparedStatement(new Object[] {
                document.getIncidentReportId(), document.getDocumentData(), document.getThumbnailData(),
                document.getMimeType(), document.getLongitude(), document.getLatitude(), 
                document.getIncidentReportDocumentId()
            });
        } else {
            super.setPreparedStatement(new Object[] {
                document.getIncidentReportId(), document.getDocumentData(), document.getThumbnailData(),
                document.getMimeType(), document.getLongitude(), document.getLatitude(), 
                document.getIncidentReportDocumentId()
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
            sql.append("incident_report_documents ");
            sql.append("(incident_report_id, document_data, thumbnail_data, mime_type, longitude, latitude, incident_report_document_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("incident_report_documents ");
            sql.append("SET ");
            sql.append("incident_report_id = ?, document_data = ?, thumbnail_data = ?, mime_type = ?, longitude = ?, latitude = ?, ");
            sql.append("WHERE ");
            sql.append("incident_report_document_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    
}
