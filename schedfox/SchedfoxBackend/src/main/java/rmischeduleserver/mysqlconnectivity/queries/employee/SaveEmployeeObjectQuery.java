/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.mysqlconnectivity.queries.employee;

import schedfoxlib.model.Employee;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;

/**
 *
 * @author user
 */
public class SaveEmployeeObjectQuery extends GeneralQueryFormat {

    private boolean newEmployee = false;

    public void updateWithEmployee(Employee emp, boolean isNewEmployee) {
        newEmployee = isNewEmployee;
        if (isNewEmployee) {
            super.setPreparedStatement(emp.getEmployeeId(), emp.getEmployeeFirstName(), emp.getEmployeeMiddleInitial(),
                emp.getEmployeeLastName(), emp.getBranchId(), emp.getAccruedVacation(), emp.getEmailMessaging(), emp.getEmployeeAddress(),
                emp.getEmployeeAddress2(), emp.getEmployeeCity(), emp.getEmployeeState(), emp.getEmployeeZip(), emp.getEmployeeBirthdate(),
                emp.getEmployeeCell(), emp.getEmployeePhone(), emp.getEmployeePhone2(), emp.getEmployeePager(), emp.getEmployeeHireDate(),
                emp.getEmployeeIsDeleted(), emp.getEmployeeSsn(), emp.getEmployeeTermDate(), emp.getEmployeeType(), emp.getEmployeeTypeId(),
                emp.getIsLoginAvailable(), emp.getWebLogin(), emp.getWebPw(), emp.getEmployeeLogin(), emp.getEmployeePassword(),
                emp.getEmployeeEmail(), emp.getEmployeeEmail2(), emp.getMarkInvisible(), emp.getGender(), emp.getRace(),
                emp.getPhoneContact(), emp.getAltPhoneContact(), emp.getCellContact(), emp.getEmailContact(), emp.getAltEmailContact(),
                emp.getFullTime(), emp.getEmployeeScanId(), emp.getEmployeeEmergency());
        } else {
            super.setPreparedStatement(emp.getEmployeeFirstName(), emp.getEmployeeMiddleInitial(),
                emp.getEmployeeLastName(), emp.getBranchId(), emp.getAccruedVacation(), emp.getEmailMessaging(), emp.getEmployeeAddress(),
                emp.getEmployeeAddress2(), emp.getEmployeeCity(), emp.getEmployeeState(), emp.getEmployeeZip(), emp.getEmployeeBirthdate(),
                emp.getEmployeeCell(), emp.getEmployeePhone(), emp.getEmployeePhone2(), emp.getEmployeePager(), emp.getEmployeeHireDate(),
                emp.getEmployeeIsDeleted(), emp.getEmployeeSsn(), emp.getEmployeeTermDate(), emp.getEmployeeType(), emp.getEmployeeTypeId(),
                emp.getIsLoginAvailable(), emp.getWebLogin(), emp.getWebPw(), emp.getEmployeeLogin(), emp.getEmployeePassword(),
                emp.getEmployeeEmail(), emp.getEmployeeEmail2(), emp.getMarkInvisible(), emp.getGender(), emp.getRace(), 
                emp.getPhoneContact(), emp.getAltPhoneContact(), emp.getCellContact(), emp.getEmailContact(), emp.getAltEmailContact(),
                emp.getFullTime(), emp.getEmployeeScanId(), emp.getEmployeeEmergency(), emp.getEmployeeId());
        }
    }

    @Override
    public String getPreparedStatementString() {

        StringBuilder sql = new StringBuilder();
        if (newEmployee) {
            //This is a new employee.
           sql.append("INSERT INTO employee ");
           sql.append("(employee_id, employee_first_name, employee_middle_initial, employee_last_name, ");
           sql.append(" branch_id, accrued_vacation, email_messaging, employee_address, employee_address2, ");
           sql.append(" employee_city, employee_state, employee_zip, employee_birthdate, employee_cell, ");
           sql.append(" employee_phone, employee_phone2, employee_pager, employee_hire_date, employee_is_deleted, ");
           sql.append(" employee_ssn, employee_term_date, employee_type, employee_type_id, ");
           sql.append(" is_login_available, web_login, web_pw, employee_login, employee_password, ");
           sql.append(" employee_email, employee_email2, mark_invisible, gender, race,  ");
           sql.append(" phone_contact, phone2_contact, cell_contact, email_contact, alt_email_contact, full_time, employee_scan_id, employee_emergency ");
           sql.append(") ");
           sql.append("    VALUES ");
           sql.append("(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
           sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
           sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
           sql.append(" ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ");
           sql.append(" ?)");
        } else {
           sql.append("UPDATE employee ");
           sql.append("SET ");
           sql.append("employee_first_name = ?, employee_middle_initial = ?, employee_last_name = ?, ");
           sql.append("branch_id = ?, accrued_vacation = ?, email_messaging = ?, employee_address = ?, employee_address2 = ?, ");
           sql.append("employee_city = ?, employee_state = ?, employee_zip = ?, employee_birthdate = ?, employee_cell = ?, ");
           sql.append("employee_phone = ?, employee_phone2 = ?, employee_pager = ?, employee_hire_date = ?, employee_is_deleted = ?, ");
           sql.append("employee_last_updated = NOW(), employee_ssn = ?, employee_term_date = ?, employee_type = ?, employee_type_id = ?, ");
           sql.append("is_login_available = ?, web_login = ?, web_pw = ?, employee_login = ?, employee_password = ?, ");
           sql.append("employee_email = ?, employee_email2 = ?, mark_invisible = ?, gender = ?, race = ?, ");
           sql.append("phone_contact = ?, phone2_contact = ?, cell_contact = ?, email_contact = ?, alt_email_contact = ?, ");
           sql.append("full_time = ?, employee_scan_id = ?, employee_emergency = ? ");
           sql.append("WHERE ");
           sql.append("employee_id = ?;");
        }
        return sql.toString();
    }

    @Override
    public boolean hasAccess() {
        return true;
    }

    @Override
    public boolean hasPreparedStatement() {
        return true;
    }

}
