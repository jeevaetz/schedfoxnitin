/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.SalesMysqlInterface;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Lead;
import schedfoxlib.model.LeadNote;
import schedfoxlib.model.LeadType;
import schedfoxlib.model.SalesCallQueue;

/**
 *
 * @author ira
 */
public class SalesMysqlService implements SalesMysqlInterface {

    private static String location = "SalesMysql/";

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public static void main(String[] args) {
        SalesMysqlService salesService = new SalesMysqlService();

        LeadNote newNote = new LeadNote();
        newNote.setIsDeleted(false);
        newNote.setTitle("");
        newNote.setLeadId(125);
        newNote.setNote("test");
        newNote.setUserId(0);

        try {
            salesService.saveLeadNote(newNote);
            System.out.println("Here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    @Override
    public ArrayList<Lead> getLeadsById(ArrayList<SalesCallQueue> queue) {
        ClientResource cr = new ClientResource(getLocation() + "getleadsbyid/");
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(queue))).getText();
            return gson.fromJson(json, collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<Lead> getLeadsByUpdate(Date date) {
        ClientResource cr = new ClientResource(getLocation() + "getleadsbupdate/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(date))).getText();
            return gson.fromJson(json, collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<Lead> getLeads() {
        ClientResource cr = new ClientResource(getLocation() + "getleads/");
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<Lead> getCallQueue(Integer userId) {
        ClientResource cr = new ClientResource(getLocation() + "getcallqueue/" + userId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public Integer getLeadsCount() {
        ClientResource cr = new ClientResource(getLocation() + "getleadscount/");
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Integer.class);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<Lead> getLeadsByOffset(Integer startOffset, Integer limit) {
        ClientResource cr = new ClientResource(getLocation() + "getleadoffset/" + startOffset + "/" + limit);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<LeadType> getLeadTypes() {
        ClientResource cr = new ClientResource(getLocation() + "getleadtypes/");
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<LeadType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public ArrayList<LeadNote> getLeadNotes(Integer leadId) {
        ClientResource cr = new ClientResource(getLocation() + "getleadnotes/" + leadId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<LeadNote>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
        return null;
    }

    @Override
    public void saveLead(Lead lead) {
        ClientResource cr = new ClientResource(getLocation() + "savelead/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(lead)));
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveLeadNote(LeadNote leadNote) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "saveleadnote/");
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(leadNote)));
            return 0;
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

}
