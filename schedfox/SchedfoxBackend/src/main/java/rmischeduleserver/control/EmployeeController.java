/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.text.SimpleDateFormat;
import schedfoxlib.controller.EmployeeControllerInterface;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.billing.*;
import rmischeduleserver.mysqlconnectivity.queries.client.get_employee_test_query;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.mysqlconnectivity.queries.login.first_time_employee_login_query;
import rmischeduleserver.mysqlconnectivity.queries.login.login_as_employee_query;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.Employee;
import schedfoxlib.model.EmployeeTypes;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.get_all_employee_types_query;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public class EmployeeController implements EmployeeControllerInterface {

    private String companyId;

    private EmployeeController(String companyId) {
        this.companyId = companyId;
    }

    public static EmployeeController getInstance(String companyId) {
        return new EmployeeController(companyId);
    }

    public ArrayList<EmployeePaymentBreakdown> getBreakdownsForClientAndDate(Integer clientId, Date date) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<EmployeePaymentBreakdown> retVal = new ArrayList<EmployeePaymentBreakdown>();

        get_employee_payment_breakdown_query breakdownQuery = new get_employee_payment_breakdown_query();
        breakdownQuery.setPreparedStatement(new Object[]{clientId, new SimpleDateFormat("yyyy-MM-dd").format(date)});
        breakdownQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(breakdownQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeePaymentBreakdown(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
        }
        return retVal;
    }

    public void banEmployeeFromPost(Integer employee, Integer clientId, Boolean ban) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        toggle_clients_for_employee_ban paramQuery = new toggle_clients_for_employee_ban();

        paramQuery.update(clientId, employee, ban);
        paramQuery.setCompany(companyId);

        try {
            conn.executeUpdate(paramQuery, "");
        } catch (Exception exe) {

        }
    }

    public EmployeeCalcData getEmployeeCalcData(Integer employeeId, String currDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        employee_stats_query paramQuery = new employee_stats_query();
        paramQuery.update(employeeId + "", currDate);
        paramQuery.setCompany(companyId);

        EmployeeCalcData retVal = new EmployeeCalcData();
        try {
            Record_Set rst = conn.executeQuery(paramQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                //retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception exe) {

        }
        return retVal;
    }

    public ArrayList<Employee> getEmployeesByParam(String searchParam, ArrayList<Integer> branchIds) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employees_by_search_params_query paramQuery = new get_employees_by_search_params_query();
        paramQuery.update(searchParam, branchIds);
        paramQuery.setCompany(companyId);

        ArrayList<Employee> retVal = new ArrayList<Employee>();
        try {
            Record_Set rst = conn.executeQuery(paramQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception exe) {

        }
        return retVal;
    }

    @Override
    public Employee getEmployeebyLogin(String userName, String password, String companyDb) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        login_as_employee_query loginQuery = new login_as_employee_query();
        loginQuery.update(companyDb, userName, password);
        loginQuery.setCompany(companyId);

        Employee retVal = new Employee(new Date());
        try {
            Record_Set rst = conn.executeQuery(loginQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception exe) {

        }
        return retVal;
    }

    @Override
    public Employee getEmployeeBySSNLastName(String ssn, String lastName, String companyDb) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        first_time_employee_login_query loginQuery = new first_time_employee_login_query();
        loginQuery.update(companyDb, lastName, ssn, 0);
        loginQuery.setCompany(companyId);

        Employee retVal = new Employee(new Date());
        try {
            Record_Set rst = conn.executeQuery(loginQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
                rst.moveNext();
            }
        } catch (Exception exe) {

        }
        return retVal;
    }

    public EmployeePayments loadEmployeePayment(Integer employeePaymentId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        EmployeePayments retVal = null;
        get_employee_payment_by_id_query paymentQuery = new get_employee_payment_by_id_query();
        paymentQuery.setCompany(companyId);
        paymentQuery.setPreparedStatement(new Object[]{employeePaymentId});
        try {
            Record_Set rst = conn.executeQuery(paymentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new EmployeePayments(rst);
                rst.moveNext();
            }
        } catch (Exception exe) {
        }
        return retVal;
    }

    @Override
    public ArrayList<EmployeePayments> loadEmployeePayments(Integer employeeId) throws RetrieveDataException {
        ArrayList<EmployeePayments> retVal = new ArrayList<EmployeePayments>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_employee_payments_query paymentQuery = new get_employee_payments_query();
        paymentQuery.setCompany(companyId);
        paymentQuery.setPreparedStatement(new Object[]{employeeId});
        try {
            Record_Set rst = conn.executeQuery(paymentQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeePayments(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
        }
        return retVal;
    }

    @Override
    public ArrayList<Employee> getEmployeeByPhone(String phoneNumber) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_employee_by_phone_number_query infoQuery = new get_employee_by_phone_number_query();
        infoQuery.setPreparedStatement(new Object[]{phoneNumber, phoneNumber, phoneNumber, phoneNumber});
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Employee getEmployeeBySSN(String ssn) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Employee retVal = new Employee();

        get_employee_by_ssn_query infoQuery = new get_employee_by_ssn_query();
        infoQuery.setPreparedStatement(ssn);
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public ArrayList<Employee> getEmployeesById(ArrayList<Integer> employeeIds) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_employees_by_ids_query infoQuery = new get_employees_by_ids_query();
        infoQuery.update(employeeIds.size());
        infoQuery.setPreparedStatement(employeeIds.toArray());
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Employee getEmployeeById(int employee) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Employee retVal = new Employee();

        employee_info_query infoQuery = new employee_info_query();
        infoQuery.update(employee + "", "");
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Employee> getActiveAndRecentlyTerminatedEmployeesByBranch(int branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_active_and_recent_term_employees_by_branch_query activeEmpsQuery = new get_active_and_recent_term_employees_by_branch_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.setPreparedStatement(new Object[]{branchId});
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public ArrayList<EmployeePayments> getUnsentEmployeePaymentsForWeek(Integer branchId, Date startWeek, Date endWeek) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<EmployeePayments> retVal = new ArrayList<EmployeePayments>();

        get_unsent_employee_payments_query activeEmpsQuery = new get_unsent_employee_payments_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.setPreparedStatement(new Object[]{new java.sql.Date(startWeek.getTime()), new java.sql.Date(endWeek.getTime()), branchId});
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeePayments(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    public ArrayList<Employee> getAllActiveEmployeesByBranch(ArrayList<Integer> branches) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_employees_by_branches_query activeEmpsQuery = new get_employees_by_branches_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.update(branches);
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Employee> getAllActiveEmployeesByBranch(int branchId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_all_active_employees_by_branch_query activeEmpsQuery = new get_all_active_employees_by_branch_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.setPreparedStatement(new Object[]{branchId});
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Employee> getAllEmployeesByBranch(Integer branchId, Boolean includeDeleted) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_all_employees_by_branch_query activeEmpsQuery = new get_all_employees_by_branch_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.setPreparedStatement(new Object[]{branchId, includeDeleted, includeDeleted});
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Employee> getAllActiveEmployees() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_all_active_employees_query activeEmpsQuery = new get_all_active_employees_query();
        activeEmpsQuery.setCompany(companyId);
        activeEmpsQuery.setPreparedStatement(new Object[]{});
        try {
            Record_Set rst = conn.executeQuery(activeEmpsQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public String generateNewUskedId(Employee employee) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        String retVal = "";
        String cName = employee.getEmployeeLastName() + employee.getEmployeeFirstName();
        cName = cName.replaceAll(" ", "").toUpperCase();
        if (cName.length() > 8) {
            cName = cName.substring(0, 8);
        }
        boolean exists = true;
        int i = 0;
        while (exists) {
            check_if_employee_usked_id_exists_query checkUskedIdQuery = new check_if_employee_usked_id_exists_query();
            checkUskedIdQuery.setPreparedStatement(cName + i);
            checkUskedIdQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(checkUskedIdQuery, "");
                String uskedId = rst.getString("usked_emp_id");
                if (uskedId.length() == 0) {
                    exists = false;
                    retVal = cName + i;
                }
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
            i++;
        }
        return retVal;
    }

    @Override
    public ArrayList<EmployeeTypes> getEmployeeTypes() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<EmployeeTypes> retVal = new ArrayList<EmployeeTypes>();
        get_all_employee_types_query checkUskedIdQuery = new get_all_employee_types_query();

        checkUskedIdQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(checkUskedIdQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeTypes(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Integer saveEmployee(Employee employee) throws SaveDataException, RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewEmployee = false;
        if (employee.getEmployeeId() == null || employee.getEmployeeId() == 0) {
            isNewEmployee = true;
            GetNextEmpIDQuery employeeSequenceQuery = new GetNextEmpIDQuery();
            employeeSequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(employeeSequenceQuery, "");
                int nextEmployeeId = rst.getInt(0);
                employee.setEmployeeId(nextEmployeeId);
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        SaveEmployeeObjectQuery saveEmployeeQuery = new SaveEmployeeObjectQuery();
        saveEmployeeQuery.updateWithEmployee(employee, isNewEmployee);
        saveEmployeeQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveEmployeeQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
        return employee.getEmployeeId();
    }

    /**
     * Returns all the employees that were terminated on the specified date.
     *
     */
    @Override
    public ArrayList<Employee> getEmployeesRemovedFromSchedule(Date dateToCheck) throws RetrieveDataException {
        ArrayList<Employee> retVal = new ArrayList<Employee>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        employee_removed_from_sched_on_date_query terminationQuery = new employee_removed_from_sched_on_date_query();
        terminationQuery.setPreparedStatement(new Object[]{dateToCheck});
        terminationQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(terminationQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    /**
     * Returns all the employees that were terminated on the specified date.
     *
     */
    @Override
    public ArrayList<Employee> getEmployeesTerminatedOnDate(Date dateToCheck) throws RetrieveDataException {
        ArrayList<Employee> retVal = new ArrayList<Employee>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        employee_terminated_on_date_query terminationQuery = new employee_terminated_on_date_query();
        terminationQuery.setPreparedStatement(new Object[]{dateToCheck});
        terminationQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(terminationQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public EmployeeDeductionTypes getEmployeeDeductionType(int deduction_type_id) throws RetrieveDataException {
        get_employee_deduction_type_query deductionQuery = new get_employee_deduction_type_query();
        deductionQuery.setCompany(companyId);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        EmployeeDeductionTypes retVal = new EmployeeDeductionTypes();
        try {
            deductionQuery.setPreparedStatement(new Object[]{deduction_type_id});
            Record_Set rst = conn.executeQuery(deductionQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new EmployeeDeductionTypes(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public Vector<EmployeeCertification> getEmployeeCertificationsForEmployee(int employee_id) throws RetrieveDataException {
        employee_certifications_list_query myQuery = new employee_certifications_list_query();
        myQuery.update(employee_id + "", true);
        myQuery.setCompany(companyId);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Vector<EmployeeCertification> retVal = new Vector<EmployeeCertification>();
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                EmployeeCertification cert = new EmployeeCertification(rst);

                retVal.add(cert);
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public ArrayList<EmployeePaymentHours> getHours(int employee_payment_id) throws RetrieveDataException {
        get_employee_payment_hours_query myQuery = new get_employee_payment_hours_query();
        myQuery.setPreparedStatement(new Object[]{employee_payment_id});
        myQuery.setCompany(companyId);
        ArrayList<EmployeePaymentHours> retVal = new ArrayList<EmployeePaymentHours>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                EmployeePaymentHours cert = new EmployeePaymentHours(rst);

                retVal.add(cert);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<EmployeePaymentWage> getWages(int employee_payment_id) throws RetrieveDataException {
        get_employee_wage_query myQuery = new get_employee_wage_query();
        myQuery.setPreparedStatement(new Object[]{employee_payment_id});
        myQuery.setCompany(companyId);
        ArrayList<EmployeePaymentWage> retVal = new ArrayList<EmployeePaymentWage>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                EmployeePaymentWage wage = new EmployeePaymentWage(rst);

                retVal.add(wage);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<EmployeePaymentDeduction> getDeductions(int employee_payment_id) throws RetrieveDataException {
        get_employee_deduction_query myQuery = new get_employee_deduction_query();
        myQuery.setPreparedStatement(new Object[]{employee_payment_id});
        myQuery.setCompany(companyId);
        ArrayList<EmployeePaymentDeduction> retVal = new ArrayList<EmployeePaymentDeduction>();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                EmployeePaymentDeduction cert = new EmployeePaymentDeduction(rst);

                retVal.add(cert);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public ArrayList<EmployeePaymentTaxes> getTaxes(int employee_payment_id) throws RetrieveDataException {
        ArrayList<EmployeePaymentTaxes> retVal = new ArrayList<EmployeePaymentTaxes>();
        get_employee_taxes_query myQuery = new get_employee_taxes_query();
        myQuery.setPreparedStatement(new Object[]{employee_payment_id});
        myQuery.setCompany(companyId);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                EmployeePaymentTaxes cert = new EmployeePaymentTaxes(rst);

                retVal.add(cert);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public EmployeeTest getEmployeeTest(int employee_test_id) throws RetrieveDataException {
        get_employee_test_query myQuery = new get_employee_test_query();
        myQuery.setPreparedStatement(new Object[]{employee_test_id});
        myQuery.setCompany(companyId);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        EmployeeTest retVal = new EmployeeTest();
        try {
            Record_Set rst = conn.executeQuery(myQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new EmployeeTest(rst);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public EmployeeWageTypes getWageType(int employee_wage_id) throws RetrieveDataException {
        get_employee_wage_type_query wageQuery = new get_employee_wage_type_query();
        wageQuery.setCompany(companyId);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        EmployeeWageTypes retVal = new EmployeeWageTypes();
        try {
            wageQuery.setPreparedStatement(new Object[]{employee_wage_id});
            Record_Set rst = conn.executeQuery(wageQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new EmployeeWageTypes(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public Employee getEmployeeByScanId(String scanId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Employee retVal = new Employee();

        get_employee_info_by_scan_query scanQuery = new get_employee_info_by_scan_query();
        scanQuery.setCompany(companyId);
        scanQuery.setPreparedStatement(new Object[]{scanId});
        try {
            Record_Set rst = conn.executeQuery(scanQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public ArrayList<Employee> getActiveEmployeesBySSN(String ssn) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<Employee> retVal = new ArrayList<Employee>();

        get_active_employee_by_ssn_query infoQuery = new get_active_employee_by_ssn_query();
        infoQuery.setPreparedStatement(ssn);
        infoQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(infoQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new Employee(new Date(), rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Boolean toggleActiveInactiveEmployee(Integer employeeId, Boolean isActive) throws SaveDataException, RetrieveDataException {
        Employee emp = this.getEmployeeById(employeeId);
        if (emp == null || emp.getEmployeeId() == null) {
            return false;
        }
        try {
            RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
            toggle_active_inactive_query activeInactiveQuery = new toggle_active_inactive_query();
            activeInactiveQuery.setCompany(companyId);
            activeInactiveQuery.setPreparedStatement(new Object[]{isActive, employeeId});
            conn.executeUpdate(activeInactiveQuery, "");
            return true;
        } catch (Exception exe) {
            return false;
        }
    }
}
