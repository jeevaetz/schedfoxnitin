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
import java.util.Date;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.SalesControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Lead;
import schedfoxlib.model.SalesCall;
import schedfoxlib.model.SalesExpense;
import schedfoxlib.model.SalesExpenseImage;
import schedfoxlib.model.SalesExpenseType;
import schedfoxlib.model.SalesItinerary;
import schedfoxlib.model.SalesItineraryType;

/**
 *
 * @author ira
 */
public class SalesService implements SalesControllerInterface {

    private static String location = "Sales/";
    private static String mysqllocation = "SalesMysql/";
    private String companyId;

    public static void main(String args[]) {
        try {
            SalesService salesService = new SalesService("5");
            salesService.getSalesExpenseTypes();
            System.out.println("here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    
    public SalesService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public SalesService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }
    
    private String getMysqlLocation() {
        return SchedfoxLibServiceVariables.serverLocation + mysqllocation;
    }
    
    public ArrayList<Lead> getLeads() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getMysqlLocation() + "getleads/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<Lead>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }
    
    @Override
    public ArrayList<SalesExpenseType> getSalesExpenseTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesexpensetypes/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesExpenseType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesExpense> getSalesExpense(Integer userId, boolean isDeleted) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesexpensesdeleted/" + userId + "/" + isDeleted);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesExpense>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }
    
    @Override
    public ArrayList<SalesExpense> getSalesExpense(Integer userId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesexpenses/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesExpense>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveSalesExpense(SalesExpense sales) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savesalesexpense/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.post(new JsonRepresentation(gson.toJson(sales))).getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesItineraryType> getSalesItineraryTypes() throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesitinerarytypes/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesItineraryType>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveCallLogs(ArrayList<SalesCall> calls) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savecalllogs/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(calls)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesCall> getCallLogs(Integer userId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getcalllogs/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesCall>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveSalesItinerary(SalesItinerary itinerary) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savesalesitinerary/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(itinerary)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesItinerary> getSalesItineraryForUserId(int userId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesitineraryforuserid/" + userId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesItinerary>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesItinerary> getSalesItineraryForMonth(Integer userId, Date monthDate) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getsalesitineraryformonth/" + userId + "/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesItinerary>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(gson.toJson(monthDate)));
            return gson.fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveSalesImage(SalesExpenseImage image) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savesalesimage/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(image)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public SalesExpenseImage getExpenseImage(Integer imageId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getexpenseimage/" + imageId + "/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), SalesExpenseImage.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<SalesExpenseImage> getImagesForExpense(Integer expense, boolean loadByteData) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getimagesforexpense/" + expense + "/" + loadByteData);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        try {
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<SalesExpenseImage>>() {
            }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    
}
