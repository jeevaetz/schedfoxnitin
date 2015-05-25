/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import java.util.Date;
import schedfoxlib.controller.WayPointControllerInterface;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.waypoints.*;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.ClientWaypointScan;
import schedfoxlib.model.WayPoint;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author user
 */
public class WayPointController implements WayPointControllerInterface {

    private static WayPointController myInstance;
    private String companyId;

    public WayPointController(String companyId) {
        this.companyId = companyId;
    }

    public ArrayList<ClientWaypointScan> getWayPointScans(int wayPointId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientWaypointScan> wayPoints = new ArrayList<ClientWaypointScan>();

        try {
            get_waypoint_scans_query waypointsForClient = new get_waypoint_scans_query();
            waypointsForClient.setPreparedStatement(new Object[]{wayPointId, startDate, endDate});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                wayPoints.add(new ClientWaypointScan(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPoints;
    }

    public ArrayList<ClientWaypointScan> getWayPoints(int clientId, Date startDate, Date endDate) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientWaypointScan> wayPoints = new ArrayList<ClientWaypointScan>();

        try {
            get_waypoints_by_client_id_and_date_query waypointsForClient = new get_waypoints_by_client_id_and_date_query();
            waypointsForClient.setPreparedStatement(new Object[]{clientId, startDate, endDate});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                wayPoints.add(new ClientWaypointScan(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPoints;
    }

    public WayPoint getWayPointById(int wayPointId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        WayPoint wayPoint = new WayPoint();

        try {
            get_waypoints_by_id_query waypointsForClient = new get_waypoints_by_id_query();
            waypointsForClient.setPreparedStatement(new Object[]{wayPointId});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                wayPoint = new WayPoint(rs);
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPoint;
    }

    public WayPoint getWayPointByData(int clientId, String data) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        WayPoint wayPoint = new WayPoint();

        try {
            get_waypoints_by_data_query waypointsForClient = new get_waypoints_by_data_query();
            waypointsForClient.setPreparedStatement(new Object[]{clientId, data});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                wayPoint = new WayPoint(rs);
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPoint;
    }

    public ArrayList<WayPoint> getWayPointsForClientIdAndType(int clientId, int type) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<WayPoint> retVal = new ArrayList<WayPoint>();
        try {
            get_waypoints_by_client_id_and_type_query waypointsForClient = new get_waypoints_by_client_id_and_type_query();
            waypointsForClient.setPreparedStatement(new Object[]{clientId, type});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new WayPoint(rs));
                rs.moveNext();
            }
            return retVal;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public ArrayList<WayPoint> getWayPointsForClientId(int clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<WayPoint> retVal = new ArrayList<WayPoint>();
        try {
            get_waypoints_by_client_id_query waypointsForClient = new get_waypoints_by_client_id_query();
            waypointsForClient.setPreparedStatement(new Object[]{clientId});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal.add(new WayPoint(rs));
                rs.moveNext();
            }
            return retVal;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public Integer saveWayPoint(WayPoint wayPoint, Boolean isInsert) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        if (wayPoint.getClientWaypointId() == null || wayPoint.getClientWaypointId() == 0) {
            isInsert = true;
            try {
                get_waypoints_sequence_query sequenceQuery = new get_waypoints_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                sequenceQuery.setCompany(companyId);

                Record_Set rs = conn.executeQuery(sequenceQuery, "");
                wayPoint.setClientWaypointId(rs.getInt("val"));
            } catch (Exception exe) {
                throw new RetrieveDataException();
            }
        }

        if (!isInsert) {
            try {
                WayPoint checkWayPoint = this.getWayPointById(wayPoint.getClientWaypointId());
                if (checkWayPoint == null || checkWayPoint.getClientWaypointId() == null || checkWayPoint.getClientWaypointId() == 0) {
                    isInsert = true;
                }
            } catch (Exception exe) {
            }
        }

        try {
            save_waypoint_query waypointsForClient = new save_waypoint_query();
            waypointsForClient.setCompany(companyId);
            waypointsForClient.update(wayPoint, isInsert);
            conn.executeUpdate(waypointsForClient, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
        return wayPoint.getClientWaypointId();
    }

    @Override
    public void saveWayPoint(WayPoint wayPoint) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        boolean isInsert = false;
        if (wayPoint.getClientWaypointId() == null || wayPoint.getClientWaypointId() == 0) {
            isInsert = true;
            try {
                get_waypoints_sequence_query sequenceQuery = new get_waypoints_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                sequenceQuery.setCompany(companyId);

                Record_Set rs = conn.executeQuery(sequenceQuery, "");
                wayPoint.setClientWaypointId(rs.getInt("val"));
            } catch (Exception exe) {
                throw new RetrieveDataException();
            }
        }

        if (!isInsert) {
            try {
                WayPoint checkWayPoint = this.getWayPointById(wayPoint.getClientWaypointId());
                if (checkWayPoint == null || checkWayPoint.getClientWaypointId() == null || checkWayPoint.getClientWaypointId() == 0) {
                    isInsert = true;
                }
            } catch (Exception exe) {
            }
        }

        try {
            save_waypoint_query waypointsForClient = new save_waypoint_query();
            waypointsForClient.setCompany(companyId);
            waypointsForClient.update(wayPoint, isInsert);
            conn.executeUpdate(waypointsForClient, "");
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    public int saveWayPointScanWithData(String data, Integer clientId, Integer userId) throws RetrieveDataException, SaveDataException {
        WayPoint waypoint = getWayPointByData(clientId, data);

        ClientWaypointScan scan = new ClientWaypointScan();
        scan.setUserId(userId);
        scan.setClientWaypointId(waypoint.getClientWaypointId());

        return this.saveWayPointScan(scan);
    }

    @Override
    public int saveWayPointScan(ClientWaypointScan wayPointScan) throws RetrieveDataException, SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        boolean isInsert = false;
        if (wayPointScan.getClientWaypointScanId() == null || wayPointScan.getClientWaypointScanId() == 0) {
            isInsert = true;
            try {
                get_waypoint_scan_sequence_query sequenceQuery = new get_waypoint_scan_sequence_query();
                sequenceQuery.setPreparedStatement(new Object[]{});
                sequenceQuery.setCompany(companyId);

                Record_Set rs = conn.executeQuery(sequenceQuery, "");
                wayPointScan.setClientWaypointScanId(rs.getInt("val"));
            } catch (Exception exe) {
                throw new RetrieveDataException();
            }
        }
        
        try {
            save_waypoint_scan_query waypointsForClient = new save_waypoint_scan_query();
            waypointsForClient.setCompany(companyId);
            waypointsForClient.update(wayPointScan, isInsert);
            conn.executeUpdate(waypointsForClient, "");
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPointScan.getClientWaypointScanId();
    }

    public Integer getNextWayPointId() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        Integer retVal = new Integer(0);
        try {
            get_next_waypoint_id_query waypointsForClient = new get_next_waypoint_id_query();
            waypointsForClient.setPreparedStatement(new Object[]{});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                retVal = rs.getInt(0);
            }
            return retVal;
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
    }

    @Override
    public void deactivateWayPoint(Integer wayPointId) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            deactivate_waypoint_query sequenceQuery = new deactivate_waypoint_query();
            sequenceQuery.setPreparedStatement(new Object[]{wayPointId});
            sequenceQuery.setCompany(companyId);

            conn.executeUpdate(sequenceQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }

    }

    @Override
    public ArrayList<ClientWaypointScan> getWayPointScansWithTimezone(int wayPointId, Date startDate, Date endDate, String timeZone) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<ClientWaypointScan> wayPoints = new ArrayList<ClientWaypointScan>();

        try {
            get_waypoint_scans_timezones_query waypointsForClient = new get_waypoint_scans_timezones_query();
            waypointsForClient.setPreparedStatement(new Object[]{timeZone, wayPointId, timeZone, startDate, endDate});
            waypointsForClient.setCompany(companyId);
            Record_Set rs = conn.executeQuery(waypointsForClient, "");
            for (int r = 0; r < rs.length(); r++) {
                wayPoints.add(new ClientWaypointScan(rs));
                rs.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return wayPoints;
    }

}
