/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.AddressControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Address;
import schedfoxlib.model.Client;

/**
 *
 * @author user
 */
public class AddressService implements AddressControllerInterface {

    private static String location = "Address/";
    private String companyId;
    
    public AddressService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public AddressService(String companyId) {
        this.companyId = companyId;
    }
    
    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public boolean checkIfAddressLatLongExists(Address address) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "checkifaddresslatlong");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(address)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), boolean.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public String displayClientMap(Client client) {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "displayclientmap");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(client)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), String.class);
        } catch (Exception exe) {
            return "";
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Address fetchLatLong(Address address) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "fetchlatlong");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(address)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Address.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void persistAddressLatLong(Address address) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "persistlatlong");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(address)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<Address> fetchLatLongsFromDatabase(ArrayList<Address> addresses) {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "fetchlatlongfromdatabase");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            Type collectionType = new TypeToken<Collection<Address>>() {
            }.getType();
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(addresses)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            return new ArrayList<Address>();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Address fetchLatLongFromDatabase(Address address) {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "fetchlatlongsfromdatabase");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(address)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Address.class);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }
    
}
