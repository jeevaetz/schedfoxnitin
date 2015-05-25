/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.commandline;

import java.util.ArrayList;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class ParseClientAddressInfo {

    public static void main(String args[]) {
        String company = "2";
        String gsicompany = "5021";
        //runClientMapping(gsicompany);
        runClientMapping(company);
    }
    
    private static void runClientMapping(String company) {
        AddressController addressController = AddressController.getInstance(company);
        ClientController clientController = ClientController.getInstance(company);

        try {
            ArrayList<Client> clients = clientController.getActiveClients();
            for (int c = 0; c < clients.size(); c++) {

                Client client = clients.get(c);
                boolean exists = addressController.checkIfAddressLatLongExists(client.getAddressObj());
                if (!exists) {
                    try {
                        addressController.fetchLatLong(client.getAddressObj());
                        addressController.persistAddressLatLong(client.getAddressObj());
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }
            }
            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
