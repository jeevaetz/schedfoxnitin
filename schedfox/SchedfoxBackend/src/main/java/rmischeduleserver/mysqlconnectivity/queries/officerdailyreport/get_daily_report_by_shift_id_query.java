/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.officerdailyreport;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 * Gets shifts with passed in shift id, within one day, need to use a date because of
 * permanent shifts that reuse that data.
 * @author ira
 */
public class get_daily_report_by_shift_id_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("officer_daily_report ");
        sql.append("WHERE ");
        sql.append("shift_id = ? AND logged_in > NOW() - interval '12 hour' AND logged_out IS NULL ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
