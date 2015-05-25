/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.components;

import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;

/**
 *
 * @author user
 */
public class RaceModel implements ComboBoxModel {

    private int selectedIndex = -1;
    private String[] raceValues = {"Not Set", "Black", "Nat Amer", "Asian", "White", "Hispanic", "Pacf Isle", "Other", "N/A"};

    public void setSelectedItem(Object anItem) {
        for (int r = 0; r < raceValues.length; r++) {
            if (raceValues[r].equalsIgnoreCase(anItem.toString())) {
                selectedIndex = r;
            }
        }
    }

    public Object getSelectedItem() {
        try {
            return raceValues[selectedIndex];
        } catch (Exception e) {
            return "";
        }
    }

    public int getSize() {
        return raceValues.length;
    }

    public Object getElementAt(int index) {
        return raceValues[index];
    }

    public void setItem(int sel) {
        selectedIndex = sel;
    }

    public Integer getSelected() {
        return selectedIndex;
    }

    public void addListDataListener(ListDataListener l) {
    }

    public void removeListDataListener(ListDataListener l) {
    }
}
