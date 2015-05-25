/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischeduleserver.commandline;

import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.ClientController;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class MakeClientMap {
    public static void main (String args[]) {
        int client_id = 3299;
        ClientController controller = ClientController.getInstance("2");
        AddressController addressController = AddressController.getInstance("2");
        try {
            Client myClient = controller.getClientById(client_id);
            addressController.displayClientMap(myClient);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
