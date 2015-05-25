/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.commandline;

import java.util.ArrayList;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.BranchController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class ParseBranchAddressInfo {

    public static void main(String args[]) {
        String company = "2";

        AddressController addressController = AddressController.getInstance("");
        BranchController branchController = BranchController.getInstance(company);

        try {
            ArrayList<Branch> branches = branchController.getBranches();
            for (int c = 0; c < branches.size(); c++) {

                Branch branch = branches.get(c);
                boolean exists = addressController.checkIfAddressLatLongExists(branch.getAddressObj());
                if (!exists || branch.getBranchId() == 30) {
                    addressController.fetchLatLong(branch.getAddressObj());
                    addressController.persistAddressLatLong(branch.getAddressObj());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
