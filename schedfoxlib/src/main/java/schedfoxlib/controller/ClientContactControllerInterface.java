/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContactType;

/**
 *
 * @author user
 */
public interface ClientContactControllerInterface {

    void autoRenewContracts();

    ArrayList<ClientContact> getClientContracts(int client_id);
    
    public ArrayList<ClientContact> getClientContracts(Integer clientId, Boolean includeDeleted) throws RetrieveDataException;
    
    public ClientContactType getClientContactById(int client_contact_id) throws RetrieveDataException;
    
    public ArrayList<ClientContactType> getContactTypes() throws RetrieveDataException;
    
    public void saveClientContact(ClientContact clientContact) throws SaveDataException;
}
