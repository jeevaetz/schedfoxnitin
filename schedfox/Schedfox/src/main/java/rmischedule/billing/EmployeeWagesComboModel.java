/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.EmployeeWageTypes;

/**
 *
 * @author user
 */
public class EmployeeWagesComboModel implements ComboBoxModel {

    private ArrayList<EmployeeWageTypes> employeeTypes;

    private int selectedIndex = -1;

    public EmployeeWagesComboModel() {
        employeeTypes = new ArrayList<EmployeeWageTypes>();
    }

    public void addItem(EmployeeWageTypes empWage) {
        employeeTypes.add(empWage);
    }

    public void clearItems() {
        employeeTypes.clear();
    }

    public void setSelectedItem(Object anItem) {
        selectedIndex = employeeTypes.indexOf(anItem);
    }

    public Object getSelectedItem() {
        try {
            return employeeTypes.get(selectedIndex);
        } catch (Exception e) {
            return "Select a wage type";
        }
    }

    public int getSize() {
        return employeeTypes.size();
    }

    public Object getElementAt(int index) {
        return employeeTypes.get(index);
    }

    public void addListDataListener(ListDataListener l) {
        
    }

    public void removeListDataListener(ListDataListener l) {
        
    }

}
