/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.mobileform;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author ira
 */
public class get_non_sent_immediate_form_fillout_for_form_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT mobile_form_fillout.* FROM ");
        sql.append("mobile_form_fillout ");
        sql.append("INNER JOIN mobile_forms ON mobile_forms.mobile_forms_id = mobile_form_fillout.mobile_form_id ");
        sql.append("WHERE ");
        sql.append("mobile_form_id = ? AND mobile_form_fillout.client_id = ? AND ");
        sql.append("send_immediately = true AND notification_sent IS NULL AND mobile_form_fillout.date_entered > NOW() - interval '1 hour' ");
        sql.append("ORDER BY date_entered DESC ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
