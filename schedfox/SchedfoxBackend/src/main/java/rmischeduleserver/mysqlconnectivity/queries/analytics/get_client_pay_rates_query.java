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
public class get_client_pay_rates_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("client.client_name, mydates.my_date, ");
        sql.append("(SUM(COALESCE(paid_amount, 0)) / SUM(greatest(COALESCE(bill_amount, 0), 1))) AS ratio, ");
        sql.append("(SUM(COALESCE(overtime_amount, 0)) / SUM(greatest(COALESCE(bill_overtime_amount, 0), 1))) AS overtimeratio, ");
        sql.append("((SUM(COALESCE(paid_amount, 0)) + SUM(COALESCE(overtime_amount, 0))) / ");
        sql.append(" (CASE WHEN ");
        sql.append("     SUM(COALESCE(bill_amount, 0)) + SUM(COALESCE(bill_overtime_amount, 0)) = 0 ");
        sql.append("  THEN ");
        sql.append("     1 ");
        sql.append("  ELSE ");
        sql.append("     SUM(COALESCE(bill_amount, 0)) + SUM(COALESCE(bill_overtime_amount, 0)) ");
        sql.append("  END)) AS tot_ratio, ");
        sql.append("SUM(paid_amount) as paidamt, ");
        sql.append("SUM(bill_amount) as billamt, ");
        sql.append("SUM(overtime_amount) as overtimeamt, ");
        sql.append("SUM(bill_overtime_amount) as overtimebillamt ");
        sql.append("FROM ");
        sql.append("( ");
	sql.append("    SELECT DATE(date_trunc('week', doy)) as my_date FROM generate_date_series(?, ?) ");
	sql.append("    GROUP BY DATE(date_trunc('week', doy)) ");
	sql.append("    ORDER BY DATE(date_trunc('week', doy)) ");
        sql.append("    ) as mydates ");
        sql.append("LEFT JOIN client ON 1 = 1 AND client.client_is_deleted != 1 AND client.branch_id != 4 ");
        sql.append("LEFT JOIN get_client_pay_amounts(?, ?, ?::integer, null::integer[]) as amt ON amt.week_started = mydates.my_date AND client.client_id = cid ");
        sql.append("WHERE (client.branch_id = ? OR ? = -1) ");
        sql.append("GROUP BY client.client_name, mydates.my_date ");
        sql.append("ORDER BY client.client_name ASC, mydates.my_date ");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
