/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.FileControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.model.FileModel;

/**
 *
 * @author user
 */
public class FileService implements FileControllerInterface {

    private static String location = "File/";
    private String companyId;

    public FileService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public FileService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    @Override
    public ArrayList<String> getPostInstructionsForClient(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getpostinstructions/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<String>>() {
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
    public FileModel getFile(String fileName) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getfile/" + URLEncoder.encode(fileName, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), FileModel.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            try {
                cr.getResponse().release();
            } catch (Exception exe) {}
        }
    }

    @Override
    public HashMap<Integer, ArrayList<String>> getPostInstructionsForClient(ArrayList<Integer> clientIds) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getpostinstructionsforallclients/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(clientIds)));
            String jsonReturned = rep.getText();
            return gson.fromJson(jsonReturned, HashMap.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
    
}
