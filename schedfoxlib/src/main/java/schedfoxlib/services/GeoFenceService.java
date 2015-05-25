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
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.GeoFenceControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.GeoFencing;
import schedfoxlib.model.GeoFencingPoints;

/**
 *
 * @author ira
 */
public class GeoFenceService implements GeoFenceControllerInterface {

    private static String location = "GeoFence/";
    private String companyId = "2";
    
    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public GeoFenceService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public GeoFenceService(String companyId) {
        this.companyId = companyId;
    }
    
    @Override
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savegeofence");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(geoFence)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveGeoFencePoints(ArrayList<GeoFencingPoints> points) throws SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savegeofencepoints");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(points)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GeoFencing> getGeoFencesForClient(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getgeofencesforclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencing>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GeoFencingPoints> getGeoFencePointsForFence(Integer fenceId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getgeofencesforfence/" + fenceId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencingPoints>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GeoFencing> getAllActiveGeoFences() throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "getallactivegeofences/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencing>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
