/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeWageTypes;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_wage_types_query extends GeneralQueryFormat {

    private EmployeeWageTypes employeeWageType;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeWageTypes employeeWageType) {
        this.employeeWageType = employeeWageType;
        if (employeeWageType.getEmployeeWageTypeId() == null) {
            super.setPreparedStatement(new Object[]{employeeWageType.getDescription(),
                employeeWageType.getOneTime()});
        } else {
            super.setPreparedStatement(new Object[]{employeeWageType.getDescription(),
                employeeWageType.getOneTime(), employeeWageType.getEmployeeWageTypeId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (employeeWageType.getEmployeeWageTypeId() == null) {
            sql.append("INSERT INTO ");
            sql.append("employee_wage_types ");
            sql.append("(description, one_time) ");
            sql.append("VALUES ");
            sql.append("(?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("employee_wage_types ");
            sql.append("SET ");
            sql.append("description = ?, one_time = ? ");
            sql.append("WHERE ");
            sql.append("employee_wage_type_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
