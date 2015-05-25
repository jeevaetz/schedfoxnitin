/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.export;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author vnguyen
 */
public class get_employees_for_usked_query extends GeneralQueryFormat {

    public get_employees_for_usked_query() {

    }
    
    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT *, ");
        sql.append("fed_tax.marriage_status as fed_status, ");
        sql.append("fed_tax.exemptions as fed_exemption, ");
        sql.append("fed_tax.additional_withholding as fed_withholding ");
        sql.append("FROM ");
        sql.append("employee ");
        sql.append("INNER JOIN usked_employee ON usked_employee.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN employee_types ON employee.employee_type_id = employee_types.employee_type_id ");
        sql.append("LEFT JOIN employee_tax AS fed_tax ON fed_tax.employee_id = employee.employee_id ");
        sql.append("LEFT JOIN tax_types AS fed_tax_type ON fed_tax_type.tax_type_id = fed_tax.tax_type AND fed_tax_type.tax_region_id = 1 ");
        
        sql.append("WHERE ");
        sql.append("employee.branch_id = ?;");
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
