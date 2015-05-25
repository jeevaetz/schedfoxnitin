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
public class get_incidents_by_dates_and_client_query extends GeneralQueryFormat {

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
        sql.append("incident_report.*, ");
        sql.append("(SELECT COUNT(*) FROM incident_report_documents WHERE incident_report_documents.incident_report_id = incident_report.incident_report_id) as image_count ");
        sql.append("FROM ");
        sql.append("incident_report ");
        sql.append("WHERE ");
        sql.append("DATE(date_entered) BETWEEN DATE(?) AND DATE(?) AND client_id = ? ");
        sql.append("ORDER BY ");
        sql.append("date_entered DESC ");
        return sql.toString();
    }
    
}
