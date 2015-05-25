/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Address;
import schedfoxlib.model.Client;
import schedfoxlib.model.exception.NoMappingResultsException;

/**
 *
 * @author user
 */
public interface AddressControllerInterface {

    boolean checkIfAddressLatLongExists(Address address) throws RetrieveDataException;

    String displayClientMap(Client client);

    public Address fetchLatLong(Address address) throws RetrieveDataException, NoMappingResultsException;
    
    public ArrayList<Address> fetchLatLongsFromDatabase(ArrayList<Address> addresses);
    
    public Address fetchLatLongFromDatabase(Address address);

    void persistAddressLatLong(Address address) throws SaveDataException;
    
}
