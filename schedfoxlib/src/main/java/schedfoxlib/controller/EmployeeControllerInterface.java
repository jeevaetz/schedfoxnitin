/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public interface EmployeeControllerInterface {

    public ArrayList<Employee> getAllEmployeesByBranch(Integer branchId, Boolean includeDeleted) throws RetrieveDataException;
    
    public ArrayList<EmployeePayments> loadEmployeePayments(Integer employeeId) throws RetrieveDataException;
    
    public ArrayList<EmployeePaymentWage> getWages(int employee_payment_id) throws RetrieveDataException;
    
    public Employee getEmployeeBySSNLastName(String ssn, String lastName, String companyDb) throws RetrieveDataException;
    
    public Employee getEmployeebyLogin(String userName, String password, String companyDb) throws RetrieveDataException;
    
    String generateNewUskedId(Employee employee) throws RetrieveDataException;

    ArrayList<Employee> getAllActiveEmployees() throws RetrieveDataException;
    
    public ArrayList<Employee> getActiveAndRecentlyTerminatedEmployeesByBranch(int branchId) throws RetrieveDataException;
    
    public ArrayList<Employee> getAllActiveEmployeesByBranch(int branchId) throws RetrieveDataException;

    public Employee getEmployeeById(int employee) throws RetrieveDataException;
    
    public Employee getEmployeeByScanId(String scanId) throws RetrieveDataException;
    
    public ArrayList<Employee> getEmployeeByPhone(String phoneNumber) throws RetrieveDataException;

    ArrayList<EmployeeTypes> getEmployeeTypes() throws RetrieveDataException;

    public ArrayList<Employee> getEmployeesRemovedFromSchedule(Date dateToCheck) throws RetrieveDataException;
    
    public ArrayList<Employee> getActiveEmployeesBySSN(String ssn) throws RetrieveDataException;
    
    public Boolean toggleActiveInactiveEmployee(Integer employeeId, Boolean isActive) throws SaveDataException, RetrieveDataException;
    
    public Employee getEmployeeBySSN(String ssn) throws RetrieveDataException;
    /**
     * Returns all the employees that were terminated on the specified date.
     *
     */
    ArrayList<Employee> getEmployeesTerminatedOnDate(Date dateToCheck) throws RetrieveDataException;

    public Integer saveEmployee(Employee employee) throws SaveDataException, RetrieveDataException;
    
    public EmployeeDeductionTypes getEmployeeDeductionType(int deduction_type_id) throws RetrieveDataException;
    
    public Vector<EmployeeCertification> getEmployeeCertificationsForEmployee(int employee_id) throws RetrieveDataException;
    
    public ArrayList<EmployeePaymentHours> getHours(int employee_payment_id) throws RetrieveDataException;
    
    public ArrayList<EmployeePaymentDeduction> getDeductions(int employee_payment_id) throws RetrieveDataException;
    
    public ArrayList<EmployeePaymentTaxes> getTaxes(int employee_payment_id) throws RetrieveDataException;
    
    public EmployeeTest getEmployeeTest(int employee_test_id) throws RetrieveDataException;
    
    public EmployeeWageTypes getWageType(int employee_wage_id) throws RetrieveDataException;
}
