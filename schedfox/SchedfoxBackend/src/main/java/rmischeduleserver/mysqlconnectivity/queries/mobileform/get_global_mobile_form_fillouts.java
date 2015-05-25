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
public class get_global_mobile_form_fillouts extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("DISTINCT company_id, company_name, mobile_form_name, date_entered, show_in_summary, mobile_form_fillout_id, mobile_data ");
        sql.append("FROM ");
        sql.append("control_db.patrol_pro_mobile_data_exchange_fetch(?, ?, ?) ");
        sql.append("ORDER BY date_entered ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
