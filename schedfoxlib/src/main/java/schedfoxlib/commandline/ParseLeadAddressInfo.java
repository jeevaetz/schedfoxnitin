/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.commandline;

import java.util.ArrayList;
import schedfoxlib.model.Address;
import schedfoxlib.model.Lead;
import schedfoxlib.services.AddressService;
import schedfoxlib.services.SalesMysqlService;
import schedfoxlib.services.SchedfoxLibServiceVariables;

/**
 *
 * @author ira
 */
public class ParseLeadAddressInfo {

    private static final int leadsLimit = 1000;

    public static void main(String args[]) {
        SalesMysqlService salesService = new SalesMysqlService();
        AddressService addressController = new AddressService("2");
        ArrayList<Lead> currentLeads = null;
        int currSpot = 0;
        while (currentLeads == null || currentLeads.size() > 0) {
            currentLeads = salesService.getLeadsByOffset(currSpot, currSpot + leadsLimit);
            System.out.println("Fetching addresses " + currSpot + " to " + (currSpot + leadsLimit));
            currSpot += leadsLimit;
            for (int l = 0; l < currentLeads.size(); l++) {
                Lead currLead = currentLeads.get(l);
                Address currAddress = currLead.getAddressObj();
                try {
                    boolean exists = addressController.checkIfAddressLatLongExists(currAddress);
                    if (!exists) {
                        currAddress = addressController.fetchLatLong(currAddress);
                        addressController.persistAddressLatLong(currAddress);
                    }
                } catch (Exception exe) {
                    exe.printStackTrace();
                }
            }
        }
    }
}
