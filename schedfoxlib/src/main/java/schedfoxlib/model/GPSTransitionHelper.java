/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package schedfoxlib.model;

import java.io.Serializable;
import java.util.HashMap;

/**
 *
 * @author ira
 */
public class GPSTransitionHelper implements Serializable {

    private HashMap<Integer, HashMap<Integer, GeoFencingTransition>> clientEquipmentTable;

    public GPSTransitionHelper() {
        clientEquipmentTable = new HashMap<Integer, HashMap<Integer, GeoFencingTransition>>();
    }

    public void loadDataForFenceAndEquipment(Integer fenceId, Integer equipId, GeoFencingTransition trans) {
        if (clientEquipmentTable.get(fenceId) == null) {
            clientEquipmentTable.put(fenceId, new HashMap<Integer, GeoFencingTransition>());
        }
        HashMap<Integer, GeoFencingTransition> fenceTrans = clientEquipmentTable.get(fenceId);
        fenceTrans.put(equipId, trans);
    }

    public GeoFencingTransition getDataForFenceAndEquipment(Integer fenceId, Integer equipId) {
        try {
            return clientEquipmentTable.get(fenceId).get(equipId);
        } catch (Exception exe) {
            return null;
        }
    }
}
