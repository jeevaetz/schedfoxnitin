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
public class get_employee_payment_breakdown_query extends GeneralQueryFormat {

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT ");
        sql.append("employee_payment_breakdown.employee_payment_id, client_id, ");
        sql.append("reg_pay_hours, reg_pay_rate, reg_pay_amount, over_pay_hours, over_pay_rate, over_pay_amount, dbl_pay_hours, dbl_pay_rate, dbl_pay_amount, ");
        sql.append("reg_bill_hours, reg_bill_rate, reg_bill_amount, over_bill_hours, over_bill_rate, over_bill_amount, dbl_bill_hours, dbl_bill_rate, dbl_bill_amount, ");
        sql.append("work_date ");
        sql.append("FROM ");
        sql.append("employee_payment_breakdown ");
        sql.append("INNER JOIN employee_payments ON employee_payments.employee_payments_id = employee_payment_breakdown.employee_payment_id ");
        sql.append("WHERE ");
        sql.append("client_id = ? AND date_of_trans = DATE(?) ");
        sql.append("ORDER BY work_date DESC");
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
