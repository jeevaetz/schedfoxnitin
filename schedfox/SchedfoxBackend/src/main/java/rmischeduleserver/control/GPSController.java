/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fences_contacts_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fences_points_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fences_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_unsent_geo_fence_transitions_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.save_geo_fence_contact_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.save_geo_fence_points_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.save_geo_fence_transition_query;

import rmischeduleserver.mysqlconnectivity.queries.gps.get_coordinates_for_client_id_with_accuracy_and_timezone_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_coordinates_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_coordinates_for_client_with_accuracy_and_timezone_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_coordinates_for_client_with_accuracy_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_coordinates_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_last_employee_used_device_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_next_gps_sequence_id_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_number_of_coordinates_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_way_point_scans_for_client_and_timezone_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_way_point_scans_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.get_way_point_scans_strings_for_client_and_timezone_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.load_gps_transitions_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.save_geo_fence_query;
import rmischeduleserver.mysqlconnectivity.queries.gps.save_gps_coordinates_query;
import schedfoxlib.controller.GPSControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientWaypointScan;
import schedfoxlib.model.Employee;
import schedfoxlib.model.GPSCoordinate;
import schedfoxlib.model.GPSTransitionHelper;
import schedfoxlib.model.GeoFencing;
import schedfoxlib.model.GeoFencingContact;
import schedfoxlib.model.GeoFencingPoints;
import schedfoxlib.model.GeoFencingTransition;
import schedfoxlib.model.WayPoint;
import schedfoxlib.model.WayPointCoordinate;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class GPSController implements GPSControllerInterface {

    private String companyId;

    public GPSController(String companyId) {
        this.companyId = companyId;
    }

    public static void main(String args[]) {
        GPSController service = new GPSController("5027");
        try {
            ArrayList<GPSCoordinate> coords = service.loadLastGPSCoordinates(1000);

            service.checkPolygonsAndSendAlerts(coords);
            System.exit(0);
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }

    public static GPSController getInstance(String companyId) {
        return new GPSController(companyId);
    }

    public ArrayList<GPSCoordinate> loadLastGPSCoordinates(Integer numberToLoad) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_number_of_coordinates_query geoFenceQuery = new get_number_of_coordinates_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.setPreparedStatement(new Object[]{});
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    public ArrayList<GeoFencingTransition> getUnsentTransitions() {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        
        ArrayList<GeoFencingTransition> retVal = new ArrayList<GeoFencingTransition>();
        try {
            get_unsent_geo_fence_transitions_query geoFenceQuery = new get_unsent_geo_fence_transitions_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.setPreparedStatement(new Object[]{});
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencingTransition(rst));
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
        
    }
    
    @Override
    public void checkPolygonsAndSendAlerts(ArrayList<GPSCoordinate> gpsCoords) {
        GPSTransitionHelper lastTransitionHash = new GPSTransitionHelper();

        GeometryFactory gf = new GeometryFactory();

        HashMap<Integer, Integer> clientHash = new HashMap<Integer, Integer>();
        HashMap<Integer, Integer> equipHash = new HashMap<Integer, Integer>();
        for (int g = 0; g < gpsCoords.size(); g++) {
            GPSCoordinate currGPS = gpsCoords.get(g);
            clientHash.put(currGPS.getClientId(), currGPS.getClientId());
            equipHash.put(currGPS.getEquipmentId(), currGPS.getEquipmentId());
        }

        try {
            Set<Integer> distinctEquipmentIds = equipHash.keySet();
            lastTransitionHash = loadTranslations(distinctEquipmentIds);
        } catch (Exception exe) {
        }

        HashMap<Integer, Polygon> allFences = new HashMap<Integer, Polygon>();
        Iterator<Integer> keys = clientHash.keySet().iterator();
        while (keys.hasNext()) {
            Integer clientId = keys.next();
            try {
                ArrayList<GeoFencing> fences = getGeoFencing(clientId);
                for (int f = 0; f < fences.size(); f++) {
                    GeoFencing fence = fences.get(f);
                    ArrayList<GeoFencingPoints> coords = getGeoFencingPoints(fence.getGeoFencingId());

                    ArrayList<Coordinate> points = new ArrayList<Coordinate>();
                    for (int c = 0; c < coords.size(); c++) {
                        GeoFencingPoints point = coords.get(c);
                        points.add(new Coordinate(point.getLongitude(), point.getLatitude()));
                    }

                    allFences.put(fence.getGeoFencingId(), gf.createPolygon(points.toArray(new Coordinate[points.size()])));
                }

            } catch (Exception exe) {
            }
        }

        for (int g = 0; g < gpsCoords.size(); g++) {
            Iterator<Integer> fenceKeys = allFences.keySet().iterator();
            while (fenceKeys.hasNext()) {
                Integer fenceId = fenceKeys.next();
                Polygon currPolygon = allFences.get(fenceId);

                GPSCoordinate point = gpsCoords.get(g);
                Coordinate currCoord = new Coordinate(point.getLongitude().doubleValue(), point.getLatitude().doubleValue());
                Point currPoint = gf.createPoint(currCoord);
                if (point.getAccuracy().doubleValue() < 10.0) {
                    if (!currPoint.within(currPolygon)) {
                        //Out of the polygon
                        GeoFencingTransition lastFenceTrans = lastTransitionHash.getDataForFenceAndEquipment(fenceId, gpsCoords.get(g).getEquipmentId());
                        if (lastFenceTrans == null || lastFenceTrans.getTransitionType().equals(GeoFencingTransition.TransitionType.INSIDE.getValue())) {
                            GeoFencingTransition currentTrans = new GeoFencingTransition();
                            currentTrans.setClientEquipmentId(gpsCoords.get(g).getEquipmentId());
                            currentTrans.setGeoFenceId(fenceId);
                            currentTrans.setTransitionType(GeoFencingTransition.TransitionType.OUTSIDE.getValue());
                            currentTrans.setGpsCoordinateId(point.getGpsCoordinateId());

                            try {
                                saveGeoFencingTransition(currentTrans);
                                lastTransitionHash.loadDataForFenceAndEquipment(fenceId, gpsCoords.get(g).getEquipmentId(), currentTrans);
                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                        }
                    } else if (currPoint.within(currPolygon)) {
                        //Inside the polygon
                        GeoFencingTransition lastFenceTrans = lastTransitionHash.getDataForFenceAndEquipment(fenceId, gpsCoords.get(g).getEquipmentId());
                        if (lastFenceTrans == null || lastFenceTrans.getTransitionType().equals(GeoFencingTransition.TransitionType.OUTSIDE.getValue())) {
                            GeoFencingTransition currentTrans = new GeoFencingTransition();
                            currentTrans.setClientEquipmentId(gpsCoords.get(g).getEquipmentId());
                            currentTrans.setGeoFenceId(fenceId);
                            currentTrans.setTransitionType(GeoFencingTransition.TransitionType.INSIDE.getValue());
                            currentTrans.setGpsCoordinateId(point.getGpsCoordinateId());

                            try {
                                saveGeoFencingTransition(currentTrans);
                                lastTransitionHash.loadDataForFenceAndEquipment(fenceId, gpsCoords.get(g).getEquipmentId(), currentTrans);
                            } catch (Exception exe) {
                                exe.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ArrayList<GeoFencingContact> getGeoFencingContacts(Integer geoFencingId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GeoFencingContact> retVal = new ArrayList<GeoFencingContact>();
        try {
            get_geo_fences_contacts_query geoFenceQuery = new get_geo_fences_contacts_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.setPreparedStatement(new Object[]{geoFencingId});
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencingContact(rst));
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public GPSTransitionHelper loadTranslations(Set<Integer> equipmentIds) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        GPSTransitionHelper retVal = new GPSTransitionHelper();
        try {
            load_gps_transitions_query transitionsQuery = new load_gps_transitions_query();
            transitionsQuery.setCompany(companyId);
            transitionsQuery.update(equipmentIds);
            Record_Set rst = conn.executeQuery(transitionsQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                GeoFencingTransition lastTrans = new GeoFencingTransition(rst);
                retVal.loadDataForFenceAndEquipment(lastTrans.getGeoFenceId(), lastTrans.getClientEquipmentId(), lastTrans);
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public ArrayList<GeoFencingPoints> getGeoFencingPoints(Integer geoFencingId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GeoFencingPoints> retVal = new ArrayList<GeoFencingPoints>();
        try {
            get_geo_fences_points_query geoFenceQuery = new get_geo_fences_points_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.setPreparedStatement(new Object[]{geoFencingId});
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencingPoints(rst));
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public ArrayList<GeoFencing> getGeoFencing(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GeoFencing> retVal = new ArrayList<GeoFencing>();
        try {
            get_geo_fences_query geoFenceQuery = new get_geo_fences_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.setPreparedStatement(new Object[]{clientId});
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencing(rst));
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    public Integer saveGeoFencingTransition(GeoFencingTransition trans) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        try {
            save_geo_fence_transition_query geoFenceQuery = new save_geo_fence_transition_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.update(trans);
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("myid");
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public Integer saveGeoFencePoint(GeoFencingPoints point) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        try {
            save_geo_fence_points_query geoFenceQuery = new save_geo_fence_points_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.update(point);
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("myid");
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        try {
            save_geo_fence_query geoFenceQuery = new save_geo_fence_query();
            geoFenceQuery.setCompany(companyId);
            geoFenceQuery.update(geoFence);
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("myid");
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public Employee getLastEmployeeThatUsedDevice(Integer clientEquipmentId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Employee retVal = new Employee();
        try {
            get_last_employee_used_device_query employeeUsedQuery = new get_last_employee_used_device_query();
            employeeUsedQuery.setCompany(companyId);
            employeeUsedQuery.setPreparedStatement(new Object[]{clientEquipmentId});
            Record_Set rst = conn.executeQuery(employeeUsedQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = new Employee(new Date(), rst);
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

    @Override
    public int saveGPSCoordinate(GPSCoordinate gps) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        boolean isNewReport = false;
        if (gps.getGpsCoordinateId() == null || gps.getGpsCoordinateId() == 0) {
            isNewReport = true;
            get_next_gps_sequence_id_query sequenceQuery = new get_next_gps_sequence_id_query();
            sequenceQuery.setPreparedStatement(new Object[]{});
            sequenceQuery.setCompany(companyId);
            try {
                Record_Set rst = conn.executeQuery(sequenceQuery, "");
                gps.setGpsCoordinateId(rst.getInt(0));
            } catch (Exception e) {
                throw new RetrieveDataException();
            }
        }

        save_gps_coordinates_query saveEquipmentQuery = new save_gps_coordinates_query();
        saveEquipmentQuery.update(gps, isNewReport);
        saveEquipmentQuery.setCompany(companyId);
        try {
            conn.executeUpdate(saveEquipmentQuery, "");
        } catch (Exception e) {
            throw new SaveDataException();
        }

        return gps.getGpsCoordinateId();
    }

    @Override
    public void saveGPSCoordinates(ArrayList<GPSCoordinate> gpsCoords) throws RetrieveDataException, SaveDataException {
        for (int g = 0; g < gpsCoords.size(); g++) {
            gpsCoords.get(g).setGpsCoordinateId(this.saveGPSCoordinate(gpsCoords.get(g)));
        }
        try {
            GeoFenceController geoController = GeoFenceController.getInstance(companyId);
            ArrayList<GeoFencing> fences = geoController.getAllActiveGeoFences();
            if (fences.size() > 0) {
                checkPolygonsAndSendAlerts(gpsCoords);
            }
        } catch (Exception exe) {}
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> equipmentIds) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_coordinates_for_client_query getContactQuery = new get_coordinates_for_client_query();
            getContactQuery.update(startDate, endDate, equipmentIds.toArray(new Integer[equipmentIds.size()]));
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDate(int equipmentId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_coordinates_query getContactQuery = new get_coordinates_query();
            getContactQuery.setPreparedStatement(new Object[]{startDate, endDate, equipmentId});
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> ids, Integer accuracy) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_coordinates_for_client_with_accuracy_query getContactQuery = new get_coordinates_for_client_with_accuracy_query();
            getContactQuery.update(startDate, endDate, ids.toArray(new Integer[ids.size()]), accuracy);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<WayPointCoordinate> getWayPointsCoordinate(Date startDate, Date endDate, Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<WayPointCoordinate> retVal = new ArrayList<WayPointCoordinate>();
        try {
            get_way_point_scans_for_client_query getContactQuery = new get_way_point_scans_for_client_query();
            getContactQuery.update(startDate, endDate, clientId);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                WayPoint wayPoint = new WayPoint(rst);
                ClientWaypointScan wayPointScan = new ClientWaypointScan(rst);
                retVal.add(new WayPointCoordinate(wayPoint, wayPointScan));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(Date startDate, Date endDate, ArrayList<Integer> ids, String timeZone, Integer accuracy) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_coordinates_for_client_with_accuracy_and_timezone_query getContactQuery = new get_coordinates_for_client_with_accuracy_and_timezone_query();
            getContactQuery.update(startDate, endDate, ids.toArray(new Integer[ids.size()]), accuracy, timeZone);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezoneStr(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<WayPointCoordinate> retVal = new ArrayList<WayPointCoordinate>();
        try {
            get_way_point_scans_strings_for_client_and_timezone_query getContactQuery = new get_way_point_scans_strings_for_client_and_timezone_query();
            getContactQuery.update(startDate, endDate, clientId, timeZone);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                WayPoint wayPoint = new WayPoint(rst);
                ClientWaypointScan wayPointScan = new ClientWaypointScan(rst);
                retVal.add(new WayPointCoordinate(wayPoint, wayPointScan));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }
    
    @Override
    @Deprecated
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezone(Date startDate, Date endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<WayPointCoordinate> retVal = new ArrayList<WayPointCoordinate>();
        try {
            get_way_point_scans_for_client_and_timezone_query getContactQuery = new get_way_point_scans_for_client_and_timezone_query();
            getContactQuery.update(startDate, endDate, clientId, timeZone);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                WayPoint wayPoint = new WayPoint(rst);
                ClientWaypointScan wayPointScan = new ClientWaypointScan(rst);
                retVal.add(new WayPointCoordinate(wayPoint, wayPointScan));
                rst.moveNext();

            }

        } catch (Exception e) {
        }
        return retVal;
    }

    @Override
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GPSCoordinate> retVal = new ArrayList<GPSCoordinate>();
        try {
            get_coordinates_for_client_id_with_accuracy_and_timezone_query getContactQuery = new get_coordinates_for_client_id_with_accuracy_and_timezone_query();
            getContactQuery.update(startDate, endDate, clientId, timeZone);
            getContactQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(getContactQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GPSCoordinate(rst));
                rst.moveNext();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

    @Override
    public Integer saveGeoContact(GeoFencingContact geoFenceContact) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        try {
            save_geo_fence_contact_query geoFenceQuery = new save_geo_fence_contact_query();

            geoFenceQuery.update(geoFenceContact);
            geoFenceQuery.setCompany(companyId);
            Record_Set rst = conn.executeQuery(geoFenceQuery, "");

            for (int r = 0; r < rst.length(); r++) {
                retVal = rst.getInt("myid");
                rst.moveNext();
            }

        } catch (Exception e) {
        }

        return retVal;
    }

}
