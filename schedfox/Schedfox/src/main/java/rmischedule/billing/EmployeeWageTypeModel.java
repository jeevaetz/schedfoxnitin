/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EmployeeWageTypes;

/**
 *
 * @author user
 */
public class EmployeeWageTypeModel extends AbstractTableModel {

    private ArrayList<EmployeeWageTypes> employeeWagesType;
    
    public EmployeeWageTypeModel() {
        employeeWagesType = new ArrayList<EmployeeWageTypes>();
    }

    public int getRowCount() {
        return employeeWagesType.size();
    }

    public void clearData() {
        employeeWagesType.clear();
        super.fireTableDataChanged();
    }

    public EmployeeWageTypes getTypeAt(int row) {
        return employeeWagesType.get(row);
    }

    public void addWageType(EmployeeWageTypes wageType) {
        employeeWagesType.add(wageType);
        super.fireTableDataChanged();
    }

    public int getColumnCount() {
        return 2;
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Description";
        } else if (columnIndex == 1) {
            return "Is One Time?";
        }
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        EmployeeWageTypes wageType = employeeWagesType.get(rowIndex);
        if (columnIndex == 0) {
            return wageType.getDescription();
        } else if (columnIndex == 1) {
            return wageType.getOneTime();
        }
        return "";
    }

}
