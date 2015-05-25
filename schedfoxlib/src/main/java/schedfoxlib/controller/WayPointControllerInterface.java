/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.controller;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientWaypointScan;
import schedfoxlib.model.WayPoint;

/**
 *
 * @author user
 */
public interface WayPointControllerInterface {

    public int saveWayPointScanWithData(String data, Integer clientId, Integer userId) throws RetrieveDataException, SaveDataException;
            
    public void deactivateWayPoint(Integer wayPointId) throws SaveDataException;
    
    public ArrayList<ClientWaypointScan> getWayPoints(int clientId, Date startDate, Date endDate) throws RetrieveDataException;
            
    public ArrayList<ClientWaypointScan> getWayPointScans(int wayPointId, Date startDate, Date endDate) throws RetrieveDataException;
    
    public ArrayList<ClientWaypointScan> getWayPointScansWithTimezone(int wayPointId, Date startDate, Date endDate, String timeZone) throws RetrieveDataException;
    
    public ArrayList<WayPoint> getWayPointsForClientId(int clientId) throws RetrieveDataException;

    public void saveWayPoint(WayPoint wayPoint) throws RetrieveDataException, SaveDataException;
    
    public Integer saveWayPoint(WayPoint wayPoint, Boolean isInsert) throws RetrieveDataException, SaveDataException;
    
    public WayPoint getWayPointByData(int clientId, String data) throws RetrieveDataException;
    
    public int saveWayPointScan(ClientWaypointScan wayPointScan) throws RetrieveDataException, SaveDataException;
    
    public WayPoint getWayPointById(int wayPointId) throws RetrieveDataException;
    
    public ArrayList<WayPoint> getWayPointsForClientIdAndType(int clientId, int type) throws RetrieveDataException;
    
    public Integer getNextWayPointId() throws RetrieveDataException;
    
}
