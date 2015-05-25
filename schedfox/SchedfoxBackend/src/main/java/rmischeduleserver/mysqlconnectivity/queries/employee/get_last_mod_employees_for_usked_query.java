/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.employee;

import rmischeduleserver.mysqlconnectivity.queries.export.*;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class get_last_mod_employees_for_usked_query extends GeneralQueryFormat {

    public get_last_mod_employees_for_usked_query() {

    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT DISTINCT *, ");
        sql.append("fed_tax.marriage_status as fed_status, ");
        sql.append("fed_tax.exemptions as fed_exemption, ");
        sql.append("fed_tax.additional_withholding as fed_withholding, ");
        sql.append("state_tax_type.tax_usked_id as state_code, ");
        sql.append("state_tax.marriage_status as state_status, ");
        sql.append("state_tax.exemptions as state_exemption, ");
        sql.append("state_tax.additional_withholding as state_withholding, ");
        sql.append("city_tax_type.tax_usked_id as city_code, ");
        sql.append("city_tax.marriage_status as city_status, ");
        sql.append("city_tax.exemptions as city_exemption, ");
        sql.append("city_tax.additional_withholding as city_withholding, ");
        sql.append("employee_accounts.*, ");
        sql.append("(CASE WHEN first_rate.hour_type = 1 THEN 'R' WHEN first_rate.hour_type = 3 THEN 'V' WHEN first_rate.hour_type = 2 THEN 'H' ELSE '' END) as first_rate_hour, ");
        sql.append("(CASE WHEN first_rate.pay_amount IS NOT NULL THEN '7720' ELSE first_rate.pay_amount END) as first_comp_code, ");
        sql.append("first_rate.pay_amount as first_pay, ");
        sql.append("(first_rate.pay_amount * 2) as first_dbl_pay, ");
        sql.append("first_rate.bill_amount as first_bill, ");
        sql.append("first_rate.overtime_amount as first_over_pay, ");
        sql.append("first_rate.overtime_bill as first_over_bill, ");
        sql.append("first_rate_code.usked_rate_code AS first_rate_code, ");
        sql.append("(CASE WHEN second_rate.hour_type = 1 THEN 'R' WHEN second_rate.hour_type = 3 THEN 'V' WHEN second_rate.hour_type = 2 THEN 'H' ELSE '' END) as second_rate_hour, ");
        sql.append("(CASE WHEN second_rate.pay_amount IS NOT NULL THEN '7720' ELSE second_rate.pay_amount END) as second_comp_code, ");
        sql.append("second_rate.pay_amount as second_pay, ");
        sql.append("(second_rate.pay_amount * 2) as second_dbl_pay, ");
        sql.append("second_rate.bill_amount as second_bill, ");
        sql.append("second_rate.overtime_amount as second_over_pay, ");
        sql.append("second_rate.overtime_bill as second_over_bill, ");
        sql.append("second_rate_code.usked_rate_code AS second_rate_code ");
        sql.append("FROM ");
        sql.append("employee ");
        sql.append("INNER JOIN usked_employee ON usked_employee.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN employee_types ON employee.employee_type_id = employee_types.employee_type_id ");
        sql.append("LEFT JOIN employee_tax AS fed_tax ON fed_tax.employee_id = employee.employee_id AND ");
	sql.append("    EXISTS (SELECT * FROM tax_types WHERE tax_types.tax_type_id = fed_tax.tax_type AND tax_types.tax_region_id = 1) ");
        sql.append("LEFT JOIN employee_tax AS state_tax ON state_tax.employee_id = employee.employee_id AND ");
        sql.append("    EXISTS (SELECT * FROM tax_types WHERE tax_types.tax_type_id = state_tax.tax_type AND tax_types.tax_region_id = 2) ");
        sql.append("LEFT JOIN tax_types AS state_tax_type ON state_tax_type.tax_type_id = state_tax.tax_type AND state_tax_type.tax_region_id = 2 ");
        sql.append("LEFT JOIN employee_tax AS city_tax ON city_tax.employee_id = employee.employee_id AND ");
	sql.append("    EXISTS (SELECT * FROM tax_types WHERE tax_types.tax_type_id = city_tax.tax_type AND tax_types.tax_region_id = 3 ) ");
        sql.append("LEFT JOIN tax_types AS city_tax_type ON city_tax_type.tax_type_id = city_tax.tax_type AND city_tax_type.tax_region_id = 3 ");
        sql.append("LEFT JOIN employee_accounts ON employee_accounts.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN f_get_employee_rate_code(1) AS first_rate ON first_rate.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN rate_code AS first_rate_code ON first_rate_code.rate_code_id = first_rate.rate_code_id ");
        sql.append("LEFT JOIN f_get_employee_rate_code(2) AS second_rate ON second_rate.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN rate_code AS second_rate_code ON second_rate_code.rate_code_id = second_rate.rate_code_id ");
        sql.append("WHERE ");
        sql.append("employee.branch_id = ? AND ");
        sql.append("employee.employee_last_updated > NOW() - interval '1 minute' * ?;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
