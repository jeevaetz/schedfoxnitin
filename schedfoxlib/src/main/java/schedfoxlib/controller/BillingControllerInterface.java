/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.*;

/**
 *
 * @author user
 */
public interface BillingControllerInterface {

    void deleteClientRateCode(ClientRateCode rateCode) throws SaveDataException;

    void deleteEmployeeRateCode(EmployeeRateCode rateCode) throws SaveDataException;

    ArrayList<ClientBillFrequency> getBillingFrequencies() throws RetrieveDataException;

    ArrayList<ClientRateCode> getClientRateCodes(int client_id) throws RetrieveDataException;

    ArrayList<EmployeeDeductionTypes> getEmployeeDeductionTypes() throws RetrieveDataException;

    ArrayList<EmployeeDeductions> getEmployeeDeductions(int employeeId) throws RetrieveDataException;

    ArrayList<EmployeeRateCode> getEmployeeRateCodes(int employee_id) throws RetrieveDataException;

    ArrayList<EmployeeTax> getEmployeeTax(Integer employeeId);

    ArrayList<EmployeeWageTypes> getEmployeeWageTypes() throws RetrieveDataException;

    ArrayList<HourType> getHourTypes() throws RetrieveDataException;

    ArrayList<RateCode> getRateCodes() throws RetrieveDataException;

    ArrayList<SalesTax> getSalesTax() throws RetrieveDataException;

    ArrayList<TaxTypes> getTaxTypesByRegion(String region) throws RetrieveDataException;
    
    public RateCode getRateCode(int rate_code_id) throws RetrieveDataException;
    
    public HourType getHourTypeObj(int hour_type_id) throws RetrieveDataException;

    void saveClientRateCode(ClientRateCode clientRateCode) throws SaveDataException;

    void saveEmployeeDeduction(EmployeeDeductions empDeduction) throws SaveDataException;

    void saveEmployeeDeductionType(EmployeeDeductionTypes employeeDeduction) throws SaveDataException;

    void saveEmployeeRateCode(EmployeeRateCode employeeRateCode) throws SaveDataException;

    void saveEmployeeWage(EmployeeWages empWages) throws SaveDataException;

    void saveEmployeeWageType(EmployeeWageTypes employeeWage) throws SaveDataException;
    
}
