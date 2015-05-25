/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.client;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_client_stats_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("MIN(\"date\") first_date,  MAX(\"date\") last_date, ");
	sql.append("SUM(get_difference(start_time, end_time)) totalminutes, ");
        sql.append("SUM(get_difference(start_time, end_time)) / ");
	sql.append("    (CASE WHEN ((((MAX(\"date\")) - (MIN(\"date\"))) / 7)) = 0 THEN 1 ");
	sql.append("     ELSE ((((MAX(\"date\")) - (MIN(\"date\"))) / 7)) END) average ");
	sql.append("FROM assemble_schedule ");
	sql.append("( ");
	sql.append("DATE(NOW() - interval '1 year'), ");
        sql.append("DATE(NOW()), -1, -1, null, ?::integer[]) WHERE cid = ? AND isdeleted = 0 ");
        sql.append("GROUP BY cid");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
