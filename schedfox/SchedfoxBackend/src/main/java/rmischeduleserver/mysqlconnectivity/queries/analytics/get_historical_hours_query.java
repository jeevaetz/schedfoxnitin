/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.analytics;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_historical_hours_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT \"date\", ");
        sql.append("SUM(tot) AS tot, ");
        sql.append("SUM(CASE WHEN isbillable THEN tot ELSE 0 END) AS billablehours, ");
        sql.append("SUM(CASE WHEN isbillable THEN 0 ELSE tot END) AS nonbillablehours ");
        sql.append("FROM ");
        sql.append("(");
        sql.append("SELECT date_trunc('week', \"date\") - interval '0' day as \"date\", \"type\", (floor(\"type\" % 100000 / 10000.0) != 1) as isbillable, ");
        sql.append("((CASE WHEN end_time < (CASE WHEN start_time = 0 THEN 1440 ELSE start_time END) THEN (end_time + 1440) - (CASE WHEN start_time = 0 THEN 1440 ELSE start_time END) ELSE end_time - (CASE WHEN start_time = 0 THEN 1440 ELSE start_time END) END) / 60.0) as tot ");
        sql.append("FROM champion_db.assemble_schedule(?, ?, ?, null::integer[]) ");
        sql.append(") as hoursworked ");
        sql.append("GROUP BY \"date\" ");
        sql.append("ORDER BY \"date\"");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
