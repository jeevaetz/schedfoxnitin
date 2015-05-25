/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.billing;

import schedfoxlib.model.EmployeeWages;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class save_employee_wage_query extends GeneralQueryFormat {

    private EmployeeWages empWages;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeWages empWages) {
        this.empWages = empWages;
        if (empWages.getEmployeeWageId() == null) {
            super.setPreparedStatement(new Object[]{empWages.getWages(),
                empWages.getEmployeeId(), empWages.getEmployeeWageTypeId()});
        } else {
            super.setPreparedStatement(new Object[]{empWages.getWages(),
                empWages.getEmployeeId(), empWages.getEmployeeWageTypeId(),
                empWages.getEmployeeWageId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (empWages.getEmployeeWageId() == null) {
            sql.append("INSERT INTO ");
            sql.append("employee_wages ");
            sql.append("(wages, employee_id, employee_wage_type_id) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?);");
        } else {
            sql.append("UPDATE ");
            sql.append("employee_wages ");
            sql.append("SET wages = ?, employee_id = ?, employee_wage_type_id = ? ");
            sql.append("WHERE ");
            sql.append("employee_wage_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
