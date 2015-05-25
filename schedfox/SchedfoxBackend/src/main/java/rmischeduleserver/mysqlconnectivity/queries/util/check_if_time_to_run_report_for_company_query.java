/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.util;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class check_if_time_to_run_report_for_company_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("control_db.company ");
        sql.append("INNER JOIN control_db.branch ON branch.branch_management_id = company.company_management_id ");
        sql.append("WHERE ");
        sql.append("company.company_id = ? AND ");
        sql.append("(");
        sql.append("	(NOW()::time - (COALESCE(report_time, '08:00:00'::time))::time) < '00:45:00'::time AND ");
        sql.append("	(NOW()::time - (COALESCE(report_time, '08:00:00'::time))::time) > '00:00:00'::time ");
        sql.append(") = true ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
