/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.commandline;

import java.util.ArrayList;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.EmployeeController;
import schedfoxlib.model.exception.NoMappingResultsException;
import schedfoxlib.model.Employee;

/**
 *
 * @author user
 */
public class ParseEmployeeAddressInfo {

    public static void main(String args[]) {
        String company = "2";

        AddressController addressController = AddressController.getInstance(company);
        EmployeeController employeeController = EmployeeController.getInstance(company);

        try {
            ArrayList<Employee> employees = employeeController.getAllActiveEmployees();
            for (int e = 0; e < employees.size(); e++) {
                try {
                    Employee employee = employees.get(e);
                    boolean exists = addressController.checkIfAddressLatLongExists(employee.getAddressObj());
                    if (!exists) {
                        addressController.fetchLatLong(employee.getAddressObj());
                        addressController.persistAddressLatLong(employee.getAddressObj());
                    }
                } catch (NoMappingResultsException nmre) {
                    
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
