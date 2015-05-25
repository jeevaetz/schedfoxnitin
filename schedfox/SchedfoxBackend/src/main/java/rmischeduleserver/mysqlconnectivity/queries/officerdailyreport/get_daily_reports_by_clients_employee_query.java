/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_daily_reports_by_clients_employee_query extends GeneralQueryFormat {
    private Integer sizeOfClientArray;
    private Integer sizeOfEmployeeArray;
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    public void update(Integer sizeOfClientArray, Integer sizeOfEmployeeArray) {
        this.sizeOfClientArray = sizeOfClientArray;
        this.sizeOfEmployeeArray = sizeOfEmployeeArray;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("officer_daily_report_text.*, officer_daily_report.*, employee.employee_first_name, employee.employee_last_name, client.client_name, ");
        sql.append("(SELECT COUNT(*) FROM officer_daily_report_documents WHERE officer_daily_report_documents.officer_daily_report_text_id = officer_daily_report_text.officer_daily_report_text_id) as doc_count, ");
        sql.append("(SELECT COUNT(*) FROM incident_report WHERE incident_report.employee_id = officer_daily_report.employee_id AND incident_report.client_id = officer_daily_report.client_id AND incident_report.date_entered BETWEEN logged_in AND COALESCE(logged_out, NOW())) as incident_count, ");
        sql.append("(SELECT COUNT(*) FROM client_waypoint_scan INNER JOIN client_waypoint ON client_waypoint.client_waypoint_id = client_waypoint_scan.client_waypoint_id WHERE client_waypoint.client_id = officer_daily_report.client_id AND client_waypoint_scan.user_id = officer_daily_report.employee_id AND client_waypoint_scan.date_scanned BETWEEN logged_in AND COALESCE(logged_out, NOW())) as waypoint_count ");
        sql.append("FROM ");
        sql.append("officer_daily_report ");
        sql.append("INNER JOIN officer_daily_report_text ON officer_daily_report_text.officer_daily_report_id = officer_daily_report.officer_daily_report_id ");
        sql.append("INNER JOIN client ON client.client_id = officer_daily_report.client_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = officer_daily_report.employee_id ");
        sql.append("WHERE ");
        sql.append("active != false ");
        if (sizeOfClientArray > 0) {
            sql.append("AND officer_daily_report.client_id IN (");
            for (int s = 0; s < sizeOfClientArray; s++) {
                if (s > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(") ");
        }
        if (sizeOfEmployeeArray > 0) {
            sql.append("AND officer_daily_report.employee_id IN (");
            for (int s = 0; s < sizeOfEmployeeArray; s++) {
                if (s > 0) {
                    sql.append(",");
                }
                sql.append("?");
            }
            sql.append(") ");
        }
        sql.append("AND (client.branch_id = ? OR ? = -1) AND ");
        sql.append("DATE(logged_in) BETWEEN ? AND ? ");
        sql.append("ORDER BY logged_in DESC ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
