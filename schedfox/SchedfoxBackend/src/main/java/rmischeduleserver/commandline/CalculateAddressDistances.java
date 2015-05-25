/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.commandline;

import java.util.ArrayList;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmployeeController;
import schedfoxlib.model.Address;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.exception.NoMappingResultsException;

/**
 *
 * @author ira
 */
public class CalculateAddressDistances {

    public static void main(String args[]) {
        CompanyController companyController = new CompanyController();
        try {
            ArrayList<Branch> branches = companyController.getBranchesForCompany(2);
            EmployeeController employeeController = EmployeeController.getInstance("2");
            ClientController clientController = ClientController.getInstance("2");
            AddressController addressController = AddressController.getInstance("2");
            for (int b = 0; b < branches.size(); b++) {
                ArrayList<Employee> employees = employeeController.getAllActiveEmployeesByBranch(branches.get(b).getBranchId());
                ArrayList<Client> clients = clientController.getClientsByBranch(branches.get(b).getBranchId());
                for (int e = 0; e < employees.size(); e++) {
                    Employee emp = employees.get(e);
                    try {
                        Address empAddress = addressController.fetchLatLongFromDatabase(emp.getAddressObj());
                        if (empAddress.getAddress1() != null && empAddress.getAddress1().length() > 0 && empAddress.getCity() != null && empAddress.getCity().length() > 0) {
                            //If not idea, save to db and reload data.
                            if (empAddress.getAddressGeocodeId() == null) {
                                empAddress = addressController.fetchLatLongFromGoogle(empAddress);
                                addressController.persistAddressLatLong(empAddress);
                                empAddress = addressController.fetchLatLongFromDatabase(emp.getAddressObj());
                            }

                            for (int c = 0; c < clients.size(); c++) {
                                Client cli = clients.get(c);
                                Address cliAddress = addressController.fetchLatLongFromDatabase(cli.getAddressObj());
                                if (cliAddress.getAddress1() != null && cliAddress.getAddress1().length() > 0 && cliAddress.getCity() != null && cliAddress.getCity().length() > 0) {
                                    //If not idea, save to db and reload data.
                                    if (cliAddress.getAddressGeocodeId() == null) {
                                        cliAddress = addressController.fetchLatLongFromGoogle(cliAddress);
                                        addressController.persistAddressLatLong(cliAddress);
                                        cliAddress = addressController.fetchLatLongFromDatabase(cli.getAddressObj());
                                    }

                                    if (!addressController.checkIfDistanceExists(empAddress, cliAddress)) {
                                        addressController.generateDistanceBetweenTwoPoints(empAddress, cliAddress);
                                    }
                                }
                            }
                        }
                    } catch (NoMappingResultsException npre) {
                    }
                }
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        System.exit(0);
    }
}
