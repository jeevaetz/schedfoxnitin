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
public class GenderModel implements ComboBoxModel {

    private int selectedIndex = 0;

    public void setSelectedItem(Object anItem) {
        if (anItem.equals("Not Set")) {
            selectedIndex = 0;
        }  else if (anItem.equals("Male")) {
            selectedIndex = 1;
        } else if (anItem.equals("Female")) {
            selectedIndex = 2;
        } else if (anItem.equals("N/A")) {
            selectedIndex = 3;
        }
    }

    public Object getSelectedItem() {
        if (selectedIndex == 0) {
            return "Not Set";
        } else if (selectedIndex == 1) {
            return "Male";
        } else if (selectedIndex == 2) {
            return "Female";
        } else if (selectedIndex == 3) {
            return "N/A";
        }
        return "";
    }

    public int getSize() {
        return 4;
    }

    public Object getElementAt(int index) {
        if (index == 0) {
            return "Not Set";
        } else if(index == 1) {
            return "Male";
        } else if (index == 2) {
            return "Female";
        } else if (index == 3) {
            return "N/A";
        }
        return "";
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
