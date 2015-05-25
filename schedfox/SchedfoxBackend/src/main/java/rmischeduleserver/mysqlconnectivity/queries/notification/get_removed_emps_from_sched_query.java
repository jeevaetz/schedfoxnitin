/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.notification;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_removed_emps_from_sched_query extends GeneralQueryFormat {

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
        sql.append("SELECT * FROM ");
        sql.append("(");
        sql.append("    SELECT ");
        sql.append("    eid, ");
        sql.append("    (CASE WHEN MIN(mydate) = date_trunc('week', DATE(NOW() - interval '2 weeks')) THEN true ELSE false END) worked_first_week, ");
        sql.append("    (CASE WHEN MAX(mydate) = date_trunc('week', DATE(NOW())) THEN true ELSE false END) worked_last_week ");
        sql.append("    FROM ");
        sql.append("    ( ");
        sql.append("        SELECT eid, date_trunc('week', \"date\") as mydate, COUNT(*) as shift_count ");
        sql.append("        FROM assemble_schedule(DATE(NOW() - interval '4 weeks'), DATE(NOW()), -1) ");
        sql.append("        GROUP BY eid, date_trunc('week', \"date\") ");
        sql.append("        ORDER BY eid ");
        sql.append("    ) as data ");
        sql.append("    GROUP BY eid ");
        sql.append(") as odata ");
        sql.append("WHERE ");
        sql.append("odata.worked_first_week = true AND ");
        sql.append("odata.worked_last_week = false");
        return sql.toString();
    }
    
}
