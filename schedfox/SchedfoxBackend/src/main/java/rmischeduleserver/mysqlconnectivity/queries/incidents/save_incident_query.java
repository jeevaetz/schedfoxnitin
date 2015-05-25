/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.IncidentReport;

/**
 *
 * @author user
 */
public class save_incident_query extends GeneralQueryFormat {
    
    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(IncidentReport incidentReport, boolean isInsert) {
        this.isInsert = isInsert;
        if (incidentReport.getIncidentReportId() == null) {
            super.setPreparedStatement(new Object[]{
                incidentReport.getIncidentReportTypeId(),
                incidentReport.getClientId(), incidentReport.getEmployeeId(), incidentReport.getShiftId(),
                incidentReport.getIncident(), new java.sql.Timestamp(incidentReport.getDateEntered().getTime()), 
                incidentReport.getUserId(), incidentReport.getClientVisible(), incidentReport.getIncidentNumberOverride(),
                incidentReport.getIncidentReportId()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                incidentReport.getIncidentReportTypeId(),
                incidentReport.getClientId(), incidentReport.getEmployeeId(), incidentReport.getShiftId(),
                incidentReport.getIncident(), new java.sql.Timestamp(incidentReport.getDateEntered().getTime()), 
                incidentReport.getUserId(), incidentReport.getClientVisible(), incidentReport.getIncidentNumberOverride(),
                incidentReport.getIncidentReportId()
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
            sql.append("incident_report ");
            sql.append("(incident_report_type_id, client_id, employee_id, shift_id, incident, date_entered, user_id, client_visible, incident_number_override, incident_report_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ");
        } else {
            sql.append("UPDATE ");
            sql.append("incident_report ");
            sql.append("SET ");
            sql.append("incident_report_type_id = ?, client_id = ?, employee_id = ?, shift_id = ?, incident = ?, ");
            sql.append("date_entered = ?, user_id = ?, client_visible = ?, incident_number_override = ?  ");
            sql.append("WHERE ");
            sql.append("incident_report_id = ?;");
        }
        return sql.toString();
    }
    
}
