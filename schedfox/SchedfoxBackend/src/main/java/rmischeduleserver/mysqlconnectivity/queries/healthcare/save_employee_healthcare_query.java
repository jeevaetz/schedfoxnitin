/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.mysqlconnectivity.queries.healthcare;

import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import schedfoxlib.model.EmployeeHealthcare;

/**
 *
 * @author ira
 */
public class save_employee_healthcare_query extends GeneralQueryFormat {

    private boolean isInsert;

    @Override
    public boolean hasAccess() {
        return true;
    }

    public void update(EmployeeHealthcare healthcare) {
        if (healthcare.getEmployeeHealthcareId() == null) {
            isInsert = true;
            super.setPreparedStatement(new Object[]{healthcare.getEmployeeId(), healthcare.getHealthcareOptionId(),
                healthcare.getActive(), new java.sql.Timestamp(healthcare.getSetOn().getTime()), healthcare.getInactiveOn()});
        } else {
            super.setPreparedStatement(new Object[]{healthcare.getEmployeeId(), healthcare.getHealthcareOptionId(),
                healthcare.getActive(), new java.sql.Timestamp(healthcare.getSetOn().getTime()), healthcare.getInactiveOn(), 
                healthcare.getEmployeeHealthcareId()});
        }
    }

    @Override
    public String getPreparedStatementString() {
        StringBuilder sql = new StringBuilder();
        if (isInsert) {
            sql.append("INSERT INTO employee_healthcare ");
            sql.append("(employee_id, healthcare_option_id, active, set_on, inactive_on) ");
            sql.append("VALUES ");
            sql.append("(?, ?, ?, ?, ?);");
        } else {
            sql.append("UPDATE employee_healthcare ");
            sql.append("SET ");
            sql.append("employee_id = ?, healthcare_option_id = ?, active = ?, set_on = ?, inactive_on = ? ");
            sql.append("WHERE ");
            sql.append("employee_healthcare_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }
}
