/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.equipment;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.EmployeeDeductionTypes;
import schedfoxlib.model.Equipment;

/**
 *
 * @author user
 */
public class EquipmentComboModel implements ComboBoxModel {
    private ArrayList<Equipment> equipmentArray;

    private int selectedIndex = -1;

    public EquipmentComboModel() {
        equipmentArray = new ArrayList<Equipment>();
    }

    public Equipment getEquipmentAt(int row) {
        return equipmentArray.get(row);
    }
    
    public void addItem(Equipment equip) {
        equipmentArray.add(equip);
    }

    public void clearItems() {
        equipmentArray.clear();
    }

    public void setSelectedItem(Object anItem) {
        for (int d = 0; d < equipmentArray.size(); d++) {
            if (anItem instanceof Integer && equipmentArray.get(d).getEquipmentId().equals(anItem)) {
                selectedIndex = d;
            } else if (equipmentArray.get(d).equals(anItem)) {
                selectedIndex = equipmentArray.indexOf(anItem);
            }
        }
    }

    public Object getSelectedItem() {
        try {
            return equipmentArray.get(selectedIndex);
        } catch (Exception e) {
            return "Select Equipment";
        }
    }

    public int getSize() {
        return equipmentArray.size();
    }

    public Object getElementAt(int index) {
        return equipmentArray.get(index);
    }

    public void addListDataListener(ListDataListener l) {

    }

    public void removeListDataListener(ListDataListener l) {

    }
}
