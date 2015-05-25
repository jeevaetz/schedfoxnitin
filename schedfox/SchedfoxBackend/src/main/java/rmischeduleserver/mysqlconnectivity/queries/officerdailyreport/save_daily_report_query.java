/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.OfficerDailyReport;

/**
 *
 * @author user
 */
public class save_daily_report_query extends GeneralQueryFormat {

    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(OfficerDailyReport officerDailyReport, boolean isInsert) {
        this.isInsert = isInsert;
        java.sql.Timestamp loggedIn = null;
        java.sql.Timestamp loggedOut = null;
        try {
            loggedIn = new java.sql.Timestamp(officerDailyReport.getLoggedIn().getTime());
        } catch (Exception exe) {}
        try {
            loggedOut = new java.sql.Timestamp(officerDailyReport.getLoggedOut().getTime());
        } catch (Exception exe) {}
        if (isInsert) {
            super.setPreparedStatement(new Object[]{
                officerDailyReport.getEmployee_id(), officerDailyReport.getClientId(), officerDailyReport.getShiftId(),
                loggedIn, loggedOut, officerDailyReport.getClientEquipmentId(), officerDailyReport.getActive(),
                officerDailyReport.getOfficerDailyReportId()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                officerDailyReport.getEmployee_id(), officerDailyReport.getClientId(), officerDailyReport.getShiftId(),
                loggedIn, loggedOut, officerDailyReport.getClientEquipmentId(), officerDailyReport.getActive(),
                officerDailyReport.getOfficerDailyReportId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO officer_daily_report ");
            sql.append("(employee_id, client_id, shift_id, logged_in, logged_out, client_equipment_id, active, officer_daily_report_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE officer_daily_report ");
            sql.append("SET ");
            sql.append("employee_id = ?, client_id = ?, shift_id = ?, logged_in = ?, logged_out = ?, client_equipment_id = ?, active = ? ");
            sql.append("WHERE ");
            sql.append("officer_daily_report_id = ?");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
