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
public class get_mobile_forms_for_client extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }
    
    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT mobile_forms.* FROM ");
        sql.append("mobile_forms ");
        sql.append("INNER JOIN mobile_forms_to_client ON mobile_forms_to_client.mobile_forms_id = mobile_forms.mobile_forms_id AND mobile_forms_to_client.client_id = ? ");
        sql.append("ORDER BY mobile_form_name ");
        return sql.toString();
    }
    
    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
    
}
