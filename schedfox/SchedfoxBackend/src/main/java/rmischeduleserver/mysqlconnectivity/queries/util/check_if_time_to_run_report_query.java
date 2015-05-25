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
public class check_if_time_to_run_report_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("(NOW()::time - (SELECT COALESCE(report_time, '08:00:00'::time) FROM control_db.branch WHERE branch_id = 1670)::time) < '00:45:00'::time AND ");
        sql.append("(NOW()::time - (SELECT COALESCE(report_time, '08:00:00'::time) FROM control_db.branch WHERE branch_id = 1670)::time) > '00:00:00'::time ");
        sql.append("as isrun");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
