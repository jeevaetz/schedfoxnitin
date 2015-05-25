/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeRateCode;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_rate_code_query extends GeneralQueryFormat {

    private EmployeeRateCode employeeRateCode;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeRateCode employeeRateCode) {
        this.employeeRateCode = employeeRateCode;
        if (employeeRateCode.getEmployeeRateCodeId() == null) {
            super.setPreparedStatement(new Object[]{employeeRateCode.getRateCodeId(),
                employeeRateCode.getPayAmount(), employeeRateCode.getOvertimeAmount(),
                employeeRateCode.getBillAmount(), employeeRateCode.getOvertimeBill(),
                employeeRateCode.getHourType(), employeeRateCode.getDescription(),
                employeeRateCode.getEmployeeId()});
        } else {
            super.setPreparedStatement(new Object[]{employeeRateCode.getRateCodeId(),
                employeeRateCode.getPayAmount(), employeeRateCode.getOvertimeAmount(),
                employeeRateCode.getBillAmount(), employeeRateCode.getOvertimeBill(),
                employeeRateCode.getHourType(), employeeRateCode.getDescription(),
                employeeRateCode.getEmployeeId(), employeeRateCode.getEmployeeRateCodeId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (employeeRateCode.getEmployeeRateCodeId() == null) {
            sql.append("INSERT INTO employee_rate_code ");
            sql.append("(rate_code_id, pay_amount, overtime_amount, bill_amount, ");
            sql.append(" overtime_bill, hour_type, description, employee_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?, ?, ?, ?)");
        } else {
            sql.append("UPDATE employee_rate_code ");
            sql.append("SET ");
            sql.append("rate_code_id = ?, pay_amount = ?, overtime_amount = ?, ");
            sql.append("bill_amount = ?, overtime_bill = ?, hour_type = ?, ");
            sql.append("description = ?, employee_id = ? ");
            sql.append("WHERE ");
            sql.append("employee_rate_code_id = ? ");
        }

        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
