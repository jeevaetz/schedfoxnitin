/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.GPSControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.Employee;
import schedfoxlib.model.GPSCoordinate;
import schedfoxlib.model.GPSTransitionHelper;
import schedfoxlib.model.GeoFencing;
import schedfoxlib.model.GeoFencingContact;
import schedfoxlib.model.GeoFencingPoints;
import schedfoxlib.model.WayPointCoordinate;

/** 
 *
 * @author user
 */
public class GPSService implements GPSControllerInterface {

    private static String location = "GPS/";
    private String companyId;

    public GPSService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public GPSService(String companyId) {
        this.companyId = companyId;
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public static void main(String args[]) {
        GeoFencingContact contact = new GeoFencingContact();
        contact.setGeoFenceId(1);
        contact.setActive(true);
        contact.setContactName("test");
        contact.setContactType(1);
        contact.setContactValue("test");
        
        
        GPSService service = new GPSService();
        try {
            service.saveGeoContact(contact);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }
    
    @Override
    public int saveGPSCoordinate(GPSCoordinate gps) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savegps/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(gps))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveGPSCoordinates(ArrayList<GPSCoordinate> gpsCoords) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savemultgps/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(gpsCoords)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDate(int equipmentId, Date startDate, Date endDate) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getcoordinatesbydate/" + equipmentId + 
                "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GPSCoordinate>>() {
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
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> ids) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.POST, getLocation() + "getcoordinatesbydateandclient" +
                        "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GPSCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(ids)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Employee getLastEmployeeThatUsedDevice(Integer clientEquipmentId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(getLocation() + "getlastemployeethatuseddevice/" + clientEquipmentId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Employee.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> ids, Integer accuracy) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.POST, getLocation() + "getcoordinatesbydateandclient" +
                        "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") +
                        "/" + accuracy);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GPSCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(ids)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<WayPointCoordinate> getWayPointsCoordinate(Date startDate, Date endDate, Integer clientId) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.POST, getLocation() + "getwaypointsbydateandclient" +
                        "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") +
                        "/" + clientId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<WayPointCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(Date startDate, Date endDate, ArrayList<Integer> ids, String timeZone, Integer accuracy) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.POST, getLocation() + "getcoordinatesbydateandclientandtimezone" +
                        "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") +
                        "/" + accuracy + "/" + URLEncoder.encode(timeZone, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GPSCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(ids)));
            return SchedfoxLibServiceVariables.getGson().fromJson(rep.getText(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezone(Date startDate, Date endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointsbydateandclientbytimezone" +
                        "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8") + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") +
                        "/" + clientId + "/" + URLEncoder.encode(timeZone, "UTF-8"));
            
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<WayPointCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointsbydateandclientidbytimezone" +
                        "/" + URLEncoder.encode(startDate, "UTF-8") + "/" + URLEncoder.encode(endDate, "UTF-8") +
                        "/" + clientId + "/" + URLEncoder.encode(timeZone, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<GPSCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savegeofence/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(geoFence))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveGeoFencePoint(GeoFencingPoints point) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savegeofencepoint/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(point))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<GeoFencing> getGeoFencing(Integer clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getgeofencing/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencing>>() {
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
    public ArrayList<GeoFencingPoints> getGeoFencingPoints(Integer geoFencingId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getgeofencingpoints/" + geoFencingId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencingPoints>>() {
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
    public ArrayList<GeoFencingContact> getGeoFencingContacts(Integer geoFencingId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "getgeofencingcontacts/" + geoFencingId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Type collectionType = new TypeToken<Collection<GeoFencingContact>>() {
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
    public Integer saveGeoContact(GeoFencingContact geoFenceContact) throws SaveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savegeofencingcontact/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(geoFenceContact))).getText();
            return gson.fromJson(json, Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }


    @Override
    public GPSTransitionHelper loadTranslations(Set<Integer> clientIds) throws RetrieveDataException {
        ClientResource cr = new ClientResource(getLocation() + "savegeofencingcontact/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            String json = cr.post(new JsonRepresentation(gson.toJson(clientIds))).getText();
            return gson.fromJson(json, GPSTransitionHelper.class);
        } catch (Exception exe) {
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void checkPolygonsAndSendAlerts(ArrayList<GPSCoordinate> gpsCoords) {
        ClientResource cr = new ClientResource(getLocation() + "checkpolygonsandsendalerts/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.post(new JsonRepresentation(gson.toJson(gpsCoords)));
        } catch (Exception exe) {
            exe.printStackTrace();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezoneStr(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointsbydateandclientidbytimezonestr" +
                        "/" + URLEncoder.encode(startDate, "UTF-8") + "/" + URLEncoder.encode(endDate, "UTF-8") +
                        "/" + clientId + "/" + URLEncoder.encode(timeZone, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());
            Type collectionType = new TypeToken<Collection<WayPointCoordinate>>() {
                }.getType();
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            exe.printStackTrace();
            throw new RetrieveDataException();
        } finally {
            cr.getResponse().release();
        }
    }
}
