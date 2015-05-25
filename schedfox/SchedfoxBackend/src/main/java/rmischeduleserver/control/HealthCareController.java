/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.sql.SQLException;
import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.delete_employee_healthcare_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.get_employee_healthcare_history_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.get_employee_healthcare_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.get_healthcare_option_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.get_healthcare_options_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.save_employee_healthcare_query;
import rmischeduleserver.mysqlconnectivity.queries.healthcare.save_healthcare_query;
import schedfoxlib.controller.HealthCareControllerInterface;
import schedfoxlib.model.EmployeeHealthcare;
import schedfoxlib.model.HealthCareOption;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class HealthCareController implements HealthCareControllerInterface {

    private String companyId;

    private HealthCareController(String companyId) {
        this.companyId = companyId;
    }

    public static HealthCareController getInstance(String companyId) {
        return new HealthCareController(companyId);
    }

    public void saveEmployeeHealthcare(EmployeeHealthcare care) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        save_employee_healthcare_query saveQuery = new save_employee_healthcare_query();
        saveQuery.update(care);
        saveQuery.setCompany(companyId);
        conn.executeUpdate(saveQuery, "");
    }
    
    public void removeHealthcareFromEmployee(int employeeId) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        delete_employee_healthcare_query deleteQuery = new delete_employee_healthcare_query();
        deleteQuery.setPreparedStatement(new Object[]{employeeId});
        deleteQuery.setCompany(companyId);
        conn.executeUpdate(deleteQuery, "");
    }
    
    public EmployeeHealthcare getEmployeeHealthcare(int employeeId) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        EmployeeHealthcare retVal = new EmployeeHealthcare();

        get_employee_healthcare_query healthcareQuery = new get_employee_healthcare_query();
        healthcareQuery.setPreparedStatement(new Object[]{employeeId});
        healthcareQuery.setCompany(companyId);
        Record_Set rst = conn.executeQuery(healthcareQuery, "");

        for (int r = 0; r < rst.length(); r++) {
            retVal = new EmployeeHealthcare(rst);
            rst.moveNext();
        }

        return retVal;
    }
    
    public ArrayList<HealthCareOption> getHealthCareOptions() throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<HealthCareOption> retVal = new ArrayList<HealthCareOption>();

        get_healthcare_options_query healthcareQuery = new get_healthcare_options_query();
        healthcareQuery.setPreparedStatement(new Object[]{});
        healthcareQuery.setCompany(companyId);
        Record_Set rst = conn.executeQuery(healthcareQuery, "");

        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new HealthCareOption(rst));
            rst.moveNext();
        }

        return retVal;
    }
    
    public void saveHealthCareOption(HealthCareOption option) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        save_healthcare_query saveQuery = new save_healthcare_query();
        saveQuery.update(option);
        saveQuery.setCompany(companyId);
        conn.executeUpdate(saveQuery, "");
    }

    public HealthCareOption getOptionById(int healthOptionId) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        HealthCareOption retVal = new HealthCareOption();

        get_healthcare_option_by_id_query healthcareQuery = new get_healthcare_option_by_id_query();
        healthcareQuery.setPreparedStatement(new Object[]{healthOptionId});
        healthcareQuery.setCompany(companyId);
        Record_Set rst = conn.executeQuery(healthcareQuery, "");

        for (int r = 0; r < rst.length(); r++) {
            retVal = new HealthCareOption(rst);
            rst.moveNext();
        }

        return retVal;
    }
    
    public ArrayList<EmployeeHealthcare> getHealthcareHistory(int employeeId) throws SQLException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<EmployeeHealthcare> retVal = new ArrayList<EmployeeHealthcare>();

        get_employee_healthcare_history_query healthcareQuery = new get_employee_healthcare_history_query();
        healthcareQuery.setPreparedStatement(new Object[]{employeeId});
        healthcareQuery.setCompany(companyId);
        Record_Set rst = conn.executeQuery(healthcareQuery, "");

        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new EmployeeHealthcare(rst));
            rst.moveNext();
        }

        return retVal;
    }
}
