/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.incidents;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_number_of_incidents_over_past_days_query extends GeneralQueryFormat {

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
        sql.append("SELECT (COUNT(*)  - 1) as num, doy FROM ");
        sql.append("generate_date_series(DATE(NOW() - interval '1 day' * ?), DATE(NOW())) as s ");
        sql.append("LEFT JOIN incident_report ON DATE(incident_report.date_entered) = s.doy AND incident_report.client_id = ? ");
        sql.append("GROUP BY doy ");
        sql.append("ORDER BY doy ");
        return sql.toString();
    }
    
}
