/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.new_user;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_number_of_free_days_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sql = new StringBuffer();
        sql.append("SELECT ");
        sql.append("company_billing_value, ");
        sql.append("to_char(NOW(), 'MM/DD/YYYY') as curtime, ");
        sql.append("to_char(NOW() + interval '1 day' * company_billing_value::integer, 'MM/DD/YYYY') as outtime ");
        sql.append("FROM ");
        sql.append("control_db.company_billing_options ");
        sql.append("WHERE ");
        sql.append("company_billing_key = 'number_days_demo';");
        return sql.toString();
    }

}
