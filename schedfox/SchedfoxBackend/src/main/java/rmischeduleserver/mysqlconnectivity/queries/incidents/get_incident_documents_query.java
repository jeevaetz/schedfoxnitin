/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_incident_documents_query extends GeneralQueryFormat {

    private boolean loadImageData;
    
    public get_incident_documents_query(boolean loadImageData) {
        this.loadImageData = loadImageData;
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        if (this.loadImageData) {
            sql.append("document_data, ");
        }
        sql.append("incident_report_document_id, incident_report_id, ");
        sql.append("mime_type, time_entered, latitude, longitude, thumbnail_data ");
        sql.append("FROM ");
        sql.append("incident_report_documents ");
        sql.append("WHERE ");
        sql.append("incident_report_id = ?;");
        return sql.toString();
    }
    
}
