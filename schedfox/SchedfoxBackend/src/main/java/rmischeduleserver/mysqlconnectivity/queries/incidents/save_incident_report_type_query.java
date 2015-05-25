/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.IncidentReportType;

/**
 *
 * @author ira
 */
public class save_incident_report_type_query extends GeneralQueryFormat {

    private boolean isUpdate;
    
    public void update(IncidentReportType incidentType) {
        if (incidentType.getIncidentReportTypeId() == 0) {
            isUpdate = false;
            super.setPreparedStatement(new Object[]{incidentType.getReportType(), incidentType.getActive()});
        } else {
            isUpdate = true;
            super.setPreparedStatement(new Object[]{incidentType.getReportType(), incidentType.getActive(), incidentType.getIncidentReportTypeId()});
        }
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (!isUpdate) {
            sql.append("INSERT INTO ");
            sql.append("incident_report_type ");
            sql.append("(report_type, active) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("incident_report_type ");
            sql.append("SET ");
            sql.append("report_type = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("incident_report_type_id = ?;");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
