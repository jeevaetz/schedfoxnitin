/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeDeductions;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_deduction_query extends GeneralQueryFormat {

    private EmployeeDeductions empDeduction;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeDeductions empDeduction) {
        this.empDeduction = empDeduction;
        if (empDeduction.getEmployeeDeductionId() == null) {
            super.setPreparedStatement(new Object[]{empDeduction.getAmount(),
                empDeduction.getBalance(), empDeduction.getEmployeeId(),
                empDeduction.getDeductionWrittenOff(), empDeduction.getWrittenOffBy(),
                empDeduction.getEmployeeDeductionTypeId()});
        } else {
            super.setPreparedStatement(new Object[]{empDeduction.getAmount(),
                empDeduction.getBalance(), empDeduction.getEmployeeId(),
                empDeduction.getDeductionWrittenOff(), empDeduction.getWrittenOffBy(),
                empDeduction.getEmployeeDeductionTypeId(),
                empDeduction.getEmployeeDeductionId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (empDeduction.getEmployeeDeductionId() == null) {
            sql.append("INSERT INTO ");
            sql.append("employee_deductions ");
            sql.append("(amount, balance, employee_id, deduction_written_off, written_off_by, employee_deduction_type_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("employee_deductions ");
            sql.append("SET amount = ?, balance = ?, employee_id = ?, deduction_written_off = ?, written_off_by = ?, employee_deduction_type_id = ? ");
            sql.append("WHERE ");
            sql.append("employee_deduction_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
