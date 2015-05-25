/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.ClientContactControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Client;
import schedfoxlib.model.ClientContact;
import schedfoxlib.model.ClientContactType;
import schedfoxlib.model.Employee;

/**
 *
 * @author user
 */
public class ClientContractService implements ClientContactControllerInterface {

    private static String location = "Contracts/";
    private String companyId = "2";

    public ClientContractService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public ClientContractService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public void autoRenewContracts() {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "autorenewcontracts/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            cr.get();
        } catch (Exception exe) {

        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContact> getClientContracts(int client_id) {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontracts/" + client_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientContact>>() {
            }.getType();
            return (ArrayList<ClientContact> ) gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ClientContactType getClientContactById(int client_contact_id) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontactbyid/" + client_contact_id);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return (ClientContactType) gson.fromJson(cr.get().getReader(), ClientContactType.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContactType> getContactTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getcontacttypes/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<ClientContactType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveClientContact(ClientContact clientContact) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveclientcontact/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(clientContact))).getText();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientContact> getClientContracts(Integer clientId, Boolean includeDeleted) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getclientcontracts/" + clientId + "/" + includeDeleted);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientContact>>() {
            }.getType();
            return (ArrayList<ClientContact> ) gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
