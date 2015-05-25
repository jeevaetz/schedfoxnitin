/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import org.restlet.data.Method;
import org.restlet.ext.json.JsonRepresentation;
import org.restlet.representation.Representation;
import org.restlet.resource.ClientResource;
import schedfoxlib.controller.WayPointControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientWaypointScan;
import schedfoxlib.model.ProblemSolverContacts;
import schedfoxlib.model.WayPoint;

/**
 *
 * @author user
 */
public class WayPointService implements WayPointControllerInterface {

    private static String location = "WayPoint/";
    private String companyId;

    public static void main(String args[]) {

        try {
            WayPointService wayPointService = new WayPointService("5021");
            //ArrayList<WayPoint> wayPoints = wayPointService.getWayPointsForClientIdAndType(3400, 2);
            wayPointService.getNextWayPointId();
            System.out.println("Here");
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    private String getLocation() {
        return SchedfoxLibServiceVariables.serverLocation + location;
    }

    public WayPointService() {
        this(SchedfoxLibServiceVariables.companyId);
    }

    public WayPointService(String companyId) {
        this.companyId = companyId;
    }

    @Override
    public ArrayList<WayPoint> getWayPointsForClientId(int clientId) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getwaypointsforclient/" + clientId);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<WayPoint>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer saveWayPoint(WayPoint wayPoint, Boolean isInsert) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savewaypoint/" + isInsert);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(wayPoint)));
            return gson.fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void saveWayPoint(WayPoint wayPoint) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savewaypoint/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(wayPoint)));
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public WayPoint getWayPointByData(int clientId, String data) throws RetrieveDataException {
        try {
            data = URLEncoder.encode(data, "UTF-8");
        } catch (Exception exe) {
        }
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getwaypointbydata/" + clientId + "/" + data);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), WayPoint.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveWayPointScan(ClientWaypointScan wayPointScan) throws RetrieveDataException, SaveDataException {
        ClientResource cr = new ClientResource(Method.POST, getLocation() + "savewaypointscan/");
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Representation rep = cr.post(new JsonRepresentation(SchedfoxLibServiceVariables.getGson().toJson(wayPointScan)));
            return gson.fromJson(rep.getText(), Integer.class);
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientWaypointScan> getWayPoints(int clientId, Date startDate, Date endDate) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypoints/" + clientId + "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8")
                    + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientWaypointScan>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientWaypointScan> getWayPointScans(int wayPointId, Date startDate, Date endDate) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointscans/" + wayPointId + "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8")
                    + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientWaypointScan>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public WayPoint getWayPointById(int wayPointId) throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointbyid/" + wayPointId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), WayPoint.class);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public int saveWayPointScanWithData(String data, Integer clientId, Integer userId) throws RetrieveDataException, SaveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "savewaypointscanwithdata/" + data + "/" + clientId
                    + "/" + userId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Integer.class);
        } catch (Exception exe) {
            return 0;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public Integer getNextWayPointId() throws RetrieveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getnextwaypointid/");
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            return gson.fromJson(cr.get().getReader(), Integer.class);
        } catch (Exception exe) {
            return 0;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<WayPoint> getWayPointsForClientIdAndType(int clientId, int type) throws RetrieveDataException {
        ClientResource cr = new ClientResource(Method.GET, getLocation() + "getwaypointsforclientandtype/" + clientId + "/" + type);
        SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
        cr.setNext(SchedfoxLibServiceVariables.getClient());
        try {
            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<WayPoint>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public void deactivateWayPoint(Integer wayPointId) throws SaveDataException {
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "deactivatewaypoint/" + wayPointId);
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            cr.get();
        } catch (Exception exe) {
            throw new SaveDataException();
        } finally {
            cr.getResponse().release();
        }
    }

    @Override
    public ArrayList<ClientWaypointScan> getWayPointScansWithTimezone(int wayPointId, Date startDate, Date endDate, String timeZone) throws RetrieveDataException {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        ClientResource cr = null;
        try {
            cr = new ClientResource(Method.GET, getLocation() + "getwaypointscanswithtimezone/" + wayPointId + "/" + URLEncoder.encode(myFormat.format(startDate), "UTF-8")
                    + "/" + URLEncoder.encode(myFormat.format(endDate), "UTF-8") + "/" + URLEncoder.encode(timeZone, "UTF-8"));
            SchedfoxLibServiceVariables.setRequestHeaders(cr, companyId);
            cr.setNext(SchedfoxLibServiceVariables.getClient());

            Gson gson = SchedfoxLibServiceVariables.getGson();
            Type collectionType = new TypeToken<Collection<ClientWaypointScan>>() {
            }.getType();
            return gson.fromJson(cr.get().getReader(), collectionType);
        } catch (Exception exe) {
            return null;
        } finally {
            cr.getResponse().release();
        }
    }
}
