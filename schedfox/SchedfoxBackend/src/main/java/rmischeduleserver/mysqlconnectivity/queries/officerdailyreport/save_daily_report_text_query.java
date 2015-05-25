/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.OfficerDailyReport;
import schedfoxlib.model.OfficerDailyReportText;

/**
 *
 * @author user
 */
public class save_daily_report_text_query extends GeneralQueryFormat {

    private boolean isInsert;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(OfficerDailyReportText officerDailyReport, boolean isInsert) {
        this.isInsert = isInsert;
        if (isInsert) {
            super.setPreparedStatement(new Object[]{
                officerDailyReport.getOfficerDailyReportId(), officerDailyReport.getText(), 
                officerDailyReport.getEnteredBy(), new java.sql.Timestamp(officerDailyReport.getEnteredOn().getTime()), 
                officerDailyReport.getOfficerDailyReportTextId()
            });
        } else {
            super.setPreparedStatement(new Object[]{
                officerDailyReport.getOfficerDailyReportId(), officerDailyReport.getText(), 
                officerDailyReport.getEnteredBy(), new java.sql.Timestamp(officerDailyReport.getEnteredOn().getTime()), 
                officerDailyReport.getOfficerDailyReportTextId()
            });
        }
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO officer_daily_report_text ");
            sql.append("(officer_daily_report_id, text, entered_by, entered_on, officer_daily_report_text_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE officer_daily_report_text ");
            sql.append("SET ");
            sql.append("officer_daily_report_id = ?, text = ?, entered_by = ?, entered_on = ? ");
            sql.append("WHERE ");
            sql.append("officer_daily_report_text_id = ?");
        }
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
