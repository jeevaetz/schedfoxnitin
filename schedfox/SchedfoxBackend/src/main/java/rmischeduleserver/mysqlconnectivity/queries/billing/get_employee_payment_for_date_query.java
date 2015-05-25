/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class get_employee_payment_for_date_query extends GeneralQueryFormat {

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT employee_payments.*, ");
        sql.append("(SELECT SUM(amount) FROM employee_payment_deduction WHERE employee_payment_id = employee_payments.employee_payments_id) as dedamt, ");
        sql.append("(SELECT SUM(de_amount) FROM employee_payment_taxes WHERE employee_payment_id = employee_payments.employee_payments_id) as taxamt ");
        sql.append("FROM employee_payments ");
        sql.append("WHERE employee_id = ? AND ");
        sql.append("date_of_trans BETWEEN DATE(? - interval '2 days') AND DATE(? + interval '2 days') ");
        sql.append("ORDER BY date_of_trans DESC");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

}
