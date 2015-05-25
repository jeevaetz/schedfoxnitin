/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.util.ArrayList;
import javax.swing.ComboBoxModel;
import javax.swing.event.ListDataListener;
import schedfoxlib.model.EmployeeDeductionTypes;

/**
 *
 * @author user
 */
public class EmployeeDeductionTypeModel implements ComboBoxModel {
    private ArrayList<EmployeeDeductionTypes> employeeDeductions;

    private int selectedIndex = -1;

    public EmployeeDeductionTypeModel() {
        employeeDeductions = new ArrayList<EmployeeDeductionTypes>();
    }

    public void addItem(EmployeeDeductionTypes empWage) {
        employeeDeductions.add(empWage);
    }

    public void clearItems() {
        employeeDeductions.clear();
    }

    public void setSelectedItem(Object anItem) {
        for (int d = 0; d < employeeDeductions.size(); d++) {
            if (anItem instanceof Integer && employeeDeductions.get(d).getEmployeeDeductionTypeId().equals(anItem)) {
                selectedIndex = d;
            } else if (employeeDeductions.get(d).equals(anItem)) {
                selectedIndex = employeeDeductions.indexOf(anItem);
            }
        }
    }

    public Object getSelectedItem() {
        try {
            return employeeDeductions.get(selectedIndex);
        } catch (Exception e) {
            return "Select a deduction type";
        }
    }

    public int getSize() {
        return employeeDeductions.size();
    }

    public Object getElementAt(int index) {
        return employeeDeductions.get(index);
    }

    public void addListDataListener(ListDataListener l) {

    }

    public void removeListDataListener(ListDataListener l) {

    }
}
