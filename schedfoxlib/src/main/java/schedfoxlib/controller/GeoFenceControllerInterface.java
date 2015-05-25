/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package schedfoxlib.controller;

import java.util.ArrayList;
import schedfoxlib.controller.exceptions.RetrieveDataException;
import schedfoxlib.controller.exceptions.SaveDataException;
import schedfoxlib.model.GeoFencing;
import schedfoxlib.model.GeoFencingPoints;

/**
 *
 * @author ira
 */
public interface GeoFenceControllerInterface {
    public ArrayList<GeoFencing> getAllActiveGeoFences() throws RetrieveDataException;
    public Integer saveGeoFence(GeoFencing geoFence) throws SaveDataException;
    public void saveGeoFencePoints(ArrayList<GeoFencingPoints> points) throws SaveDataException;
    public ArrayList<GeoFencing> getGeoFencesForClient(Integer clientId) throws RetrieveDataException;
    public ArrayList<GeoFencingPoints> getGeoFencePointsForFence(Integer clientId) throws RetrieveDataException;
}
