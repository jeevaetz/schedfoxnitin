/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContactType;

/**
 *
 * @author user
 */
public interface ClientContractControllerInterface {

    void autoRenewContracts();

    ArrayList<ClientContact> getClientContracts(int client_id);
    
    public ClientContactType getClientContactById(int client_contact_id) throws RetrieveDataException;
    
}
