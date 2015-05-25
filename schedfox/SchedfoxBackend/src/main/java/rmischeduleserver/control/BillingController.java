/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import schedfoxlib.controller.BillingControllerInterface;
import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.ClientBillFrequency;
import schedfoxlib.model.ClientRateCode;
import schedfoxlib.model.EmployeeDeductionTypes;
import schedfoxlib.model.EmployeeDeductions;
import schedfoxlib.model.EmployeeRateCode;
import schedfoxlib.model.EmployeeTax;
import schedfoxlib.model.EmployeeWageTypes;
import schedfoxlib.model.EmployeeWages;
import schedfoxlib.model.HourType;
import schedfoxlib.model.RateCode;
import schedfoxlib.model.SalesTax;
import schedfoxlib.model.TaxTypes;
import rmischeduleserver.mysqlconnectivity.queries.billing.delete_client_rate_code_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.delete_employee_rate_code_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_all_client_rates_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_all_employee_rates_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_all_hour_types_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_all_rate_codes_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_all_sales_tax_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_employee_deduction_types_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_employee_deductions_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_employee_tax_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_employee_wage_types_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.get_tax_types_by_region_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_client_rate_code_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_deduction_types_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_employee_deduction_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_employee_rate_code_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_employee_wage_query;
import rmischeduleserver.mysqlconnectivity.queries.billing.save_wage_types_query;
import rmischeduleserver.mysqlconnectivity.queries.client.billing.get_client_billing_frequency_query;
import rmischeduleserver.mysqlconnectivity.queries.client.billing.get_hour_type_query;
import rmischeduleserver.mysqlconnectivity.queries.client.billing.get_rate_code_by_id_query;

/**
 *
 * @author user
 */
public class BillingController implements BillingControllerInterface {

    private String companyId;

    public BillingController(String companyId) {
        this.companyId = companyId;
    }

    public static BillingController getInstance(String companyId) {
        return new BillingController(companyId);
    }

    @Override
    public ArrayList<EmployeeTax> getEmployeeTax(Integer employeeId) {
        ArrayList<EmployeeTax> retVal = new ArrayList<EmployeeTax>();
        get_employee_tax_query getTaxQuery = new get_employee_tax_query();
        getTaxQuery.setPreparedStatement(new Object[]{employeeId});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        getTaxQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getTaxQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeTax(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public ArrayList<TaxTypes> getTaxTypesByRegion(String region) throws RetrieveDataException {
        ArrayList<TaxTypes> retVal = new ArrayList<TaxTypes>();
        get_tax_types_by_region_query getWageTypesQuery = new get_tax_types_by_region_query();
        getWageTypesQuery.setCompany(companyId);
        getWageTypesQuery.setPreparedStatement(new Object[]{region});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            Record_Set rst = conn.executeQuery(getWageTypesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new TaxTypes(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<EmployeeDeductions> getEmployeeDeductions(int employeeId) throws RetrieveDataException {
        ArrayList<EmployeeDeductions> retVal = new ArrayList<EmployeeDeductions>();
        get_employee_deductions_query deductionQuery = new get_employee_deductions_query();
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            deductionQuery.setPreparedStatement(new Object[]{employeeId});
            deductionQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(deductionQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeDeductions(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public void deleteClientRateCode(ClientRateCode rateCode) throws SaveDataException {
        delete_client_rate_code_query deleteRateCode = new delete_client_rate_code_query();
        deleteRateCode.setPreparedStatement(new Object[]{rateCode.getClientRateCodeId()});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        deleteRateCode.setCompany(companyId);
        try {
            conn.executeUpdate(deleteRateCode, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<EmployeeWageTypes> getEmployeeWageTypes() throws RetrieveDataException {
        ArrayList<EmployeeWageTypes> retVal = new ArrayList<EmployeeWageTypes>();
        get_employee_wage_types_query getWageTypesQuery = new get_employee_wage_types_query();
        getWageTypesQuery.setCompany(companyId);
        getWageTypesQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            Record_Set rst = conn.executeQuery(getWageTypesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeWageTypes(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public ArrayList<EmployeeDeductionTypes> getEmployeeDeductionTypes() throws RetrieveDataException {
        ArrayList<EmployeeDeductionTypes> retVal = new ArrayList<EmployeeDeductionTypes>();
        get_employee_deduction_types_query getWageTypesQuery = new get_employee_deduction_types_query();
        getWageTypesQuery.setCompany(companyId);
        getWageTypesQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            Record_Set rst = conn.executeQuery(getWageTypesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new EmployeeDeductionTypes(rst));
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void saveEmployeeDeductionType(EmployeeDeductionTypes employeeDeduction) throws SaveDataException {
        save_deduction_types_query saveTypesQuery = new save_deduction_types_query();
        saveTypesQuery.update(employeeDeduction);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveTypesQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveTypesQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveEmployeeWageType(EmployeeWageTypes employeeWage) throws SaveDataException {
        save_wage_types_query saveTypesQuery = new save_wage_types_query();
        saveTypesQuery.update(employeeWage);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveTypesQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveTypesQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void deleteEmployeeRateCode(EmployeeRateCode rateCode) throws SaveDataException {
        delete_employee_rate_code_query deleteRateCode = new delete_employee_rate_code_query();
        deleteRateCode.setPreparedStatement(new Object[]{rateCode.getEmployeeRateCodeId()});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        deleteRateCode.setCompany(companyId);
        try {
            conn.executeUpdate(deleteRateCode, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveEmployeeDeduction(EmployeeDeductions empDeduction) throws SaveDataException {
        save_employee_deduction_query saveWageQuery = new save_employee_deduction_query();
        saveWageQuery.update(empDeduction);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveWageQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveWageQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveEmployeeWage(EmployeeWages empWages) throws SaveDataException {
        save_employee_wage_query saveWageQuery = new save_employee_wage_query();
        saveWageQuery.update(empWages);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveWageQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveWageQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<SalesTax> getSalesTax() throws RetrieveDataException {
        get_all_sales_tax_query salesTaxQuery = new get_all_sales_tax_query();
        salesTaxQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        salesTaxQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(salesTaxQuery, "");

            ArrayList<SalesTax> salesTaxes = new ArrayList<SalesTax>();

            for (int r = 0; r < rst.length(); r++) {
                salesTaxes.add(new SalesTax(rst));
                rst.moveNext();
            }
            return salesTaxes;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<ClientBillFrequency> getBillingFrequencies() throws RetrieveDataException {
        get_client_billing_frequency_query frequencyQuery = new get_client_billing_frequency_query();
        frequencyQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        frequencyQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(frequencyQuery, "");
            ArrayList<ClientBillFrequency> billFrequencies = new ArrayList<ClientBillFrequency>();

            for (int r = 0; r < rst.length(); r++) {
                billFrequencies.add(new ClientBillFrequency(rst));
                rst.moveNext();
            }
            return billFrequencies;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<ClientRateCode> getClientRateCodes(int client_id) throws RetrieveDataException {
        get_all_client_rates_query clientRateQuery = new get_all_client_rates_query();
        clientRateQuery.setPreparedStatement(new Object[]{client_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        clientRateQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(clientRateQuery, "");
            ArrayList<ClientRateCode> rateCodes = new ArrayList<ClientRateCode>();

            for (int r = 0; r < rst.length(); r++) {
                rateCodes.add(new ClientRateCode(rst));
                rst.moveNext();
            }
            return rateCodes;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<EmployeeRateCode> getEmployeeRateCodes(int employee_id) throws RetrieveDataException {
        get_all_employee_rates_query clientRateQuery = new get_all_employee_rates_query();
        clientRateQuery.setPreparedStatement(new Object[]{employee_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        clientRateQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(clientRateQuery, "");
            ArrayList<EmployeeRateCode> rateCodes = new ArrayList<EmployeeRateCode>();

            for (int r = 0; r < rst.length(); r++) {
                rateCodes.add(new EmployeeRateCode(rst));
                rst.moveNext();
            }
            return rateCodes;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<RateCode> getRateCodes() throws RetrieveDataException {
        get_all_rate_codes_query rateCodesQuery = new get_all_rate_codes_query();
        rateCodesQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        rateCodesQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(rateCodesQuery, "");
            ArrayList<RateCode> rateCodes = new ArrayList<RateCode>();

            for (int r = 0; r < rst.length(); r++) {
                rateCodes.add(new RateCode(rst));
                rst.moveNext();
            }
            return rateCodes;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<HourType> getHourTypes() throws RetrieveDataException {
        get_all_hour_types_query hourTypesQuery = new get_all_hour_types_query();
        hourTypesQuery.setPreparedStatement(new Object[]{});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        hourTypesQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(hourTypesQuery, "");
            ArrayList<HourType> hourTypes = new ArrayList<HourType>();

            for (int r = 0; r < rst.length(); r++) {
                hourTypes.add(new HourType(rst));
                rst.moveNext();
            }
            return hourTypes;
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public void saveClientRateCode(ClientRateCode clientRateCode) throws SaveDataException {
        save_client_rate_code_query saveClientRate = new save_client_rate_code_query();
        saveClientRate.update(clientRateCode);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveClientRate.setCompany(companyId);
        try {
            conn.executeUpdate(saveClientRate, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    @Override
    public void saveEmployeeRateCode(EmployeeRateCode employeeRateCode) throws SaveDataException {
        save_employee_rate_code_query saveEmployeeRate = new save_employee_rate_code_query();
        saveEmployeeRate.update(employeeRateCode);
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        saveEmployeeRate.setCompany(companyId);
        try {
            conn.executeUpdate(saveEmployeeRate, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }
    }

    public RateCode getRateCode(int rate_code_id) throws RetrieveDataException {
        get_rate_code_by_id_query rateCodeQuery = new get_rate_code_by_id_query();
        rateCodeQuery.setPreparedStatement(new Object[]{rate_code_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        rateCodeQuery.setCompany(companyId);
        RateCode retVal = new RateCode();
        try {
            Record_Set rst = conn.executeQuery(rateCodeQuery, "");
            rst.decompressData();
            for (int r = 0; r < rst.length(); r++) {
                retVal = new RateCode(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    public HourType getHourTypeObj(int hour_type_id) throws RetrieveDataException {
        get_hour_type_query rateCodeQuery = new get_hour_type_query();
        rateCodeQuery.setPreparedStatement(new Object[]{hour_type_id});
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        HourType retVal = new HourType();
        try {
            Record_Set rst = conn.executeQuery(rateCodeQuery, "");
            rst.decompressData();
            if (rst.length() == 0) {
                throw new Exception("No result");
            }
            for (int r = 0; r < rst.length(); r++) {
                retVal = new HourType(rst);
                rst.moveNext();
            }
        } catch (Exception e) {
            throw new RetrieveDataException();
        }
        return retVal;
    }
}
