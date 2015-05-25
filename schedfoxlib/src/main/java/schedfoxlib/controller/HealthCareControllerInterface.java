/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import schedfoxlib.model.EmployeeHealthcare;
import schedfoxlib.model.HealthCareOption;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public interface HealthCareControllerInterface {
    public void saveEmployeeHealthcare(EmployeeHealthcare care) throws SQLException;
    
    public void removeHealthcareFromEmployee(int employeeId) throws SQLException;
    
    public EmployeeHealthcare getEmployeeHealthcare(int employeeId) throws SQLException;
    
    public ArrayList<HealthCareOption> getHealthCareOptions() throws SQLException;
    
    public HealthCareOption getOptionById(int healthOptionId) throws SQLException;
    
    public void saveHealthCareOption(HealthCareOption option) throws SQLException;
    
    public ArrayList<EmployeeHealthcare> getHealthcareHistory(int employeeId) throws SQLException;
}
