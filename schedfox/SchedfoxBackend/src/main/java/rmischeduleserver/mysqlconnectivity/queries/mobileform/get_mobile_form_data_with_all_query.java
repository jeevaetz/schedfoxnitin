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
public class get_mobile_form_data_with_all_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ");
        sql.append("mobile_form_data ");
        sql.append("WHERE ");
        sql.append("mobile_forms_id = ? AND ");
        sql.append("(");
        sql.append("  (active = true AND ? = false) OR ");
        sql.append("  (? = true) ");
        sql.append(") ");
        sql.append("ORDER BY ordering, data_label ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
