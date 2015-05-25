/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.messaging;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_messaging_data_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("messaging_communication ");
        sql.append("WHERE ");
        sql.append("datetimesent IS NULL AND ((issms = true AND COALESCE(error_num, 0) < 50) OR (isemail = true AND COALESCE(error_num, 0) < 5 AND (last_error IS NULL OR last_error > NOW() - interval '30 minutes'))) ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
