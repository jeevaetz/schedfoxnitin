/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.management;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class generate_invoice_query extends GeneralQueryFormat {

    private String db;

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO ").append(this.getManagementSchema()).append(".management_client_invoice ");
        sql.append("(management_id, amount_invoiced, invoiced_on, amount_per_employee, total_employees, total_invoiced, weekly_charge, num_weeks) ");

        sql.append("SELECT ");
	sql.append("?, data.amt_bill * 4, day_of_year, amt_per, emp_count, ");
	sql.append("(data.amt_bill * 4) + (CASE WHEN (data.emp_count - 25) * amt_per > 0 THEN (data.emp_count - 25) * amt_per ELSE 0 END)  as total, ");
        sql.append("amt_bill, 4 ");
        sql.append("FROM ");
        sql.append("(");
        sql.append("SELECT (SELECT amount_to_bill FROM ").append(this.getManagementSchema()).append(".management_clients WHERE management_id = ? LIMIT 1) as amt_bill, generate_dates_from_intervals as day_of_year, ");
        sql.append("(SELECT amount_per_employee FROM control_db.management_clients WHERE management_id = ? LIMIT 1) as amt_per, ");
        sql.append("(SELECT control_db.get_company_emp_count(generate_dates_from_intervals, ?)) as emp_count ");
        sql.append("FROM ");
        sql.append("").append(this.getManagementSchema()).append(".generate_dates_from_intervals((SELECT bill_start_date FROM ").append(this.getManagementSchema()).append(".management_clients WHERE management_id = ? LIMIT 1), (SELECT bill_interval FROM ").append(this.getManagementSchema()).append(".management_clients WHERE management_id = ? LIMIT 1)) ");
        sql.append("WHERE ");
        sql.append("generate_dates_from_intervals NOT IN (SELECT invoiced_on FROM ").append(this.getManagementSchema()).append(".management_client_invoice WHERE management_id = ?) AND  ");
        sql.append("(");
        sql.append("    generate_dates_from_intervals > (SELECT MAX(invoiced_on) FROM ").append(this.getManagementSchema()).append(".management_client_invoice WHERE management_id = ?) ");
        sql.append("    OR ");
        sql.append("    (SELECT MAX(invoiced_on) FROM ").append(this.getManagementSchema()).append(".management_client_invoice WHERE management_id = ?) IS NULL ");
        sql.append(")");
        sql.append(") as data");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
