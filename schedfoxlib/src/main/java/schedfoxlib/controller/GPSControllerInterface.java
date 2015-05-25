/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;
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
public interface GPSControllerInterface {
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezoneStr(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException;
    public void checkPolygonsAndSendAlerts(ArrayList<GPSCoordinate> gpsCoords);
    public GPSTransitionHelper loadTranslations(Set<Integer> clientIds) throws RetrieveDataException;
    public Integer saveGeoContact(GeoFencingContact geoFenceContact) throws SaveDataException;
    public ArrayList<GeoFencingContact> getGeoFencingContacts(Integer geoFencingId) throws RetrieveDataException;
    public ArrayList<GeoFencingPoints> getGeoFencingPoints(Integer geoFencingId) throws RetrieveDataException;
    public ArrayList<GeoFencing> getGeoFencing(Integer clientId) throws RetrieveDataException;
    public Integer saveGeoFencePoint(GeoFencingPoints point) throws SaveDataException;
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException;
    public int saveGPSCoordinate(GPSCoordinate gps) throws RetrieveDataException, SaveDataException;
    public void saveGPSCoordinates(ArrayList<GPSCoordinate> gpsCoords) throws RetrieveDataException, SaveDataException;
    public Employee getLastEmployeeThatUsedDevice(Integer clientEquipmentId) throws RetrieveDataException;
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(String startDate, String endDate, Integer clientId, String timeZone) throws RetrieveDataException;
    public ArrayList<WayPointCoordinate> getWayPointsCoordinate(Date startDate, Date endDate, Integer clientId) throws RetrieveDataException;
    public ArrayList<WayPointCoordinate> getWayPointsCoordinateByTimezone(Date startDate, Date endDate, Integer clientId, String timeZone) throws RetrieveDataException;
    public ArrayList<GPSCoordinate> getCoordinatesForDate(int equipmentId, Date startDate, Date endDate) throws RetrieveDataException;
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> ids, Integer accuracy) throws RetrieveDataException;
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClientAndTimezone(Date startDate, Date endDate, ArrayList<Integer> ids, String timeZone, Integer accuracy) throws RetrieveDataException;
    public ArrayList<GPSCoordinate> getCoordinatesForDateForClient(Date startDate, Date endDate, ArrayList<Integer> ids) throws RetrieveDataException;
}
