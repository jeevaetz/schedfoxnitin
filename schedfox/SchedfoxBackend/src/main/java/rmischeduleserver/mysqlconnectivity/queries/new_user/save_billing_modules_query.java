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
public class save_billing_modules_query extends GeneralQueryFormat {

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuffer sql = new StringBuffer();
        sql.append("INSERT INTO control_db.company_billing_info ");
        sql.append("(company_billing_id, company_id, start_billing_date, end_billing_date) ");
        sql.append("VALUES ");
        sql.append("(?, ?, NOW(), NOW() + interval '10 year')");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
