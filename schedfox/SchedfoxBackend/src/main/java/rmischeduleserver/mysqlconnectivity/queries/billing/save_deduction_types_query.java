/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeDeductionTypes;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_deduction_types_query extends GeneralQueryFormat {

    private EmployeeDeductionTypes employeeDeductionType;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeDeductionTypes employeeDeductionType) {
        this.employeeDeductionType = employeeDeductionType;
        if (employeeDeductionType.getEmployeeDeductionTypeId() == null) {
            super.setPreparedStatement(new Object[]{employeeDeductionType.getDescription(),
                employeeDeductionType.getOneTime()});
        } else {
            super.setPreparedStatement(new Object[]{employeeDeductionType.getDescription(),
                employeeDeductionType.getOneTime(), employeeDeductionType.getEmployeeDeductionTypeId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (employeeDeductionType.getEmployeeDeductionTypeId() == null) {
            sql.append("INSERT INTO ");
            sql.append("employee_deduction_types ");
            sql.append("(description, one_time) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("employee_deduction_types ");
            sql.append("SET ");
            sql.append("description = ?, one_time = ? ");
            sql.append("WHERE ");
            sql.append("employee_deduction_type_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
