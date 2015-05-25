/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.equipment;

import java.util.HashMap;
import schedfoxlib.model.Branch;
import schedfoxlib.model.EmployeeEquipment;

/**
 *
 * @author user
 */
public interface EquipmentRefreshWindow {
    public HashMap<Integer, Branch> getBranches();
    public void refreshEquipmentData();
    public void displayEquipmentReturnWindow(EmployeeEquipment empEquip, String companyId);
}
