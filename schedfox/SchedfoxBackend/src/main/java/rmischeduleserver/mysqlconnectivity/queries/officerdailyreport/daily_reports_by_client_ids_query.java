/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import java.util.ArrayList;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class daily_reports_by_client_ids_query extends GeneralQueryFormat {

    private boolean showAll;
    private Integer idSize;
    
    public daily_reports_by_client_ids_query(boolean showAll, ArrayList<Integer> ids) {
        this.showAll = showAll;
        this.idSize = ids.size();
    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("officer_daily_report_text.*, officer_daily_report.*, employee.* "); 
        sql.append("FROM ");
        sql.append("officer_daily_report ");
        sql.append("LEFT JOIN officer_daily_report_text ON officer_daily_report_text.officer_daily_report_id = officer_daily_report.officer_daily_report_id ");
        sql.append("LEFT JOIN employee ON employee.employee_id = officer_daily_report.employee_id ");
        sql.append("WHERE ");
        sql.append("client_id IN (");
        for (int i = 0; i < idSize; i++) {
            if (i > 0) {
                sql.append(",");
            }
            sql.append("?");
        }
        sql.append(") ");
        sql.append("AND active != false ");
        sql.append("ORDER BY logged_in DESC ");
        sql.append("LIMIT 1000 ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
