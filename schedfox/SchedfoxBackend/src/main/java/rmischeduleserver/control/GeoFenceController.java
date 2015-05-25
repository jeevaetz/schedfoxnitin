/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischeduleserver.control;

import java.util.ArrayList;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_all_active_geofencing_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fence_points_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fences_for_client_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_geo_fencing_by_id_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.get_next_geo_fence_seq_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.mark_geo_fence_transition_as_sent_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.save_geo_fence_coordinates_query;
import rmischeduleserver.mysqlconnectivity.queries.geofence.save_geo_fence_query;
import schedfoxlib.controller.GeoFenceControllerInterface;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.GeoFencing;
import schedfoxlib.model.GeoFencingPoints;
import schedfoxlib.model.GeoFencingTransition;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class GeoFenceController implements GeoFenceControllerInterface {

    private String companyId;

    private GeoFenceController(String companyId) {
        this.companyId = companyId;
    }

    public static GeoFenceController getInstance(String companyId) {
        return new GeoFenceController(companyId);
    }

    public ArrayList<GeoFencing> getAllActiveGeoFences() throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GeoFencing> retVal = new ArrayList<GeoFencing>();

        get_all_active_geofencing_query geoFencingQuery = new get_all_active_geofencing_query();
        geoFencingQuery.setCompany(companyId);
        geoFencingQuery.setPreparedStatement(new Object[]{});

        try {
            Record_Set rst = conn.executeQuery(geoFencingQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencing(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }

        return retVal;
    }

    @Override
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        boolean isUpdate = true;
        if (geoFence.getGeoFencingId() == null) {
            isUpdate = false;
            get_next_geo_fence_seq_query nextGeoQuery = new get_next_geo_fence_seq_query();
            nextGeoQuery.setCompany(companyId);
            nextGeoQuery.setPreparedStatement(new Object[]{});

            try {
                Record_Set rst = conn.executeQuery(nextGeoQuery, "");
                geoFence.setGeoFencingId(rst.getInt("myid"));
            } catch (Exception exe) {
            }
        }

        try {
            save_geo_fence_query saveQuery = new save_geo_fence_query();
            saveQuery.setCompany(companyId);
            saveQuery.update(geoFence, isUpdate);
            conn.executeUpdate(saveQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
        return geoFence.getGeoFencingId();
    }

    public void markTransitionAsSent(GeoFencingTransition trans) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        try {
            mark_geo_fence_transition_as_sent_query transitionQuery = new mark_geo_fence_transition_as_sent_query();
            transitionQuery.setCompany(companyId);
            transitionQuery.setPreparedStatement(new Object[]{trans.getGeoFencingTransitionId()});
            conn.executeUpdate(transitionQuery, "");
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    public GeoFencing loadGeoFence(Integer geoFenceId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        GeoFencing retVal = null;
        try {
            get_geo_fencing_by_id_query saveGeoFenceQuery = new get_geo_fencing_by_id_query();
            saveGeoFenceQuery.setCompany(companyId);
            saveGeoFenceQuery.setPreparedStatement(new Object[]{geoFenceId});
            Record_Set rst = conn.executeQuery(saveGeoFenceQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal = new GeoFencing(rst);
                rst.moveNext();
            }
        } catch (Exception exe) {
            throw new RetrieveDataException();
        }
        return retVal;
    }

    @Override
    public void saveGeoFencePoints(ArrayList<GeoFencingPoints> points) throws SaveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();

        try {
            if (points.size() > 0) {
                save_geo_fence_coordinates_query saveGeoFenceQuery = new save_geo_fence_coordinates_query();
                saveGeoFenceQuery.setCompany(companyId);
                saveGeoFenceQuery.update(points, points.get(0).getGeoFencingId());
                conn.executeUpdate(saveGeoFenceQuery, "");
            }
        } catch (Exception exe) {
            throw new SaveDataException();
        }
    }

    @Override
    public ArrayList<GeoFencing> getGeoFencesForClient(Integer clientId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        ArrayList<GeoFencing> retVal = new ArrayList<GeoFencing>();
        get_geo_fences_for_client_query getFencesQuery = new get_geo_fences_for_client_query();
        getFencesQuery.setPreparedStatement(new Object[]{clientId});
        getFencesQuery.setCompany(companyId);
        try {
            Record_Set rst = conn.executeQuery(getFencesQuery, "");
            for (int r = 0; r < rst.length(); r++) {
                retVal.add(new GeoFencing(rst));
                rst.moveNext();
            }
        } catch (Exception exe) {
        }
        return retVal;
    }

    @Override
    public ArrayList<GeoFencingPoints> getGeoFencePointsForFence(Integer fenceId) throws RetrieveDataException {
        RMIScheduleServerImpl conn = RMIScheduleServerImpl.getInstance();
        get_geo_fence_points_query getFencePoints = new get_geo_fence_points_query();
        ArrayList<GeoFencingPoints> retVal = new ArrayList<GeoFencingPoints>();
        try {
            getFencePoints.setCompany(companyId);
            getFencePoints.setPreparedStatement(new Object[]{fenceId});

            try {
                Record_Set rst = conn.executeQuery(getFencePoints, "");
                for (int r = 0; r < rst.length(); r++) {
                    retVal.add(new GeoFencingPoints(rst));
                    rst.moveNext();
                }
            } catch (Exception exe) {
            }
        } catch (Exception exe) {
            exe.printStackTrace();
        }
        return retVal;
    }

}
