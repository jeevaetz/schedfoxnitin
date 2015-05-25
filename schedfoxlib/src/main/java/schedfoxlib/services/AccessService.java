/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
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
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.AccessControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.AccessIndividualLogs;
import schedfoxlib.model.AccessIndividualTypes;
import schedfoxlib.model.AccessIndividuals;

/**
 *
 * @author ira
 */
public class AccessService implements AccessControllerInterface {

    private static String location = "Access/";
    private String companyId;
    
    public AccessService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public AccessService(String companyId) {
        this.companyId = companyId;
    }
    
    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public ArrayList<AccessIndividualTypes> getAccessTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getaccesstypes");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<AccessIndividualTypes>>() {
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
    public void saveAccessTypes(AccessIndividualTypes type) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveaccesstype");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(type)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    public ArrayList<AccessIndividuals> getAccessIndividualsByEquipId(Integer equipmentId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getaccessindividualsbyid/" + equipmentId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<AccessIndividuals>>() {
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
    public ArrayList<AccessIndividuals> getAccessIndividuals(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getaccessindividuals/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<AccessIndividuals>>() {
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
    public void saveAccessIndividual(AccessIndividuals access) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveaccessindividual");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(access)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public AccessIndividuals getAccessIndividual(Integer accessIndividualId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getaccessindividual/" + accessIndividualId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), AccessIndividuals.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveAccessIndividualLog(AccessIndividualLogs access) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "saveaccessindividuallog/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {    
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(access)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
