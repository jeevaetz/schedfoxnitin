/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.IncidentReportContact;

/**
 *
 * @author user
 */
public class save_incident_contact_query extends GeneralQueryFormat {
    
    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(IncidentReportContact incidentContact, boolean isInsert) {
        this.isInsert = isInsert;
        if (incidentContact.getIncidentReportContactId() == 0) {
            super.setPreparedStatement(new Object[]{
                incidentContact.getIncidentReportId(), incidentContact.getFirstName(), incidentContact.getLastName(),
                incidentContact.getCell(), incidentContact.getPhone(), incidentContact.getPhone2(), 
                incidentContact.getAddress(), incidentContact.getAddress2(), incidentContact.getCity(),
                incidentContact.getState(), incidentContact.getZip()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                incidentContact.getIncidentReportId(), incidentContact.getFirstName(), incidentContact.getLastName(),
                incidentContact.getCell(), incidentContact.getPhone(), incidentContact.getPhone2(), 
                incidentContact.getAddress(), incidentContact.getAddress2(), incidentContact.getCity(),
                incidentContact.getState(), incidentContact.getZip(), incidentContact.getIncidentReportContactId()
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
            sql.append("incident_report_contacts ");
            sql.append("(incident_report_id, first_name, last_name, cell, ");
            sql.append("phone, phone2, address, address2, ");
            sql.append("city, state, zip) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); ");
        } else {
            sql.append("UPDATE ");
            sql.append("incident_report_contacts ");
            sql.append("SET ");
            sql.append("incident_report_id = ?, first_name = ?, last_name = ?, cell = ?, ");
            sql.append("phone = ?, phone2 = ?, address = ?, address2 = ?, ");
            sql.append("city = ?, state = ?, zip = ? ");
            sql.append("WHERE ");
            sql.append("incident_report_contact_id = ?;");
        }
        return sql.toString();
    }
    
}
