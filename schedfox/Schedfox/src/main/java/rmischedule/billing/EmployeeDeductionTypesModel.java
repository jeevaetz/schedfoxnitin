/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EmployeeDeductionTypes;
import schedfoxlib.model.EmployeeWageTypes;

/**
 *
 * @author user
 */
public class EmployeeDeductionTypesModel extends AbstractTableModel {

    private ArrayList<EmployeeDeductionTypes> employeeDeductionType;
    
    public EmployeeDeductionTypesModel() {
        employeeDeductionType = new ArrayList<EmployeeDeductionTypes>();
    }

    public int getRowCount() {
        return employeeDeductionType.size();
    }

    public void clearData() {
        employeeDeductionType.clear();
        super.fireTableDataChanged();
    }

    public EmployeeDeductionTypes getTypeAt(int row) {
        return employeeDeductionType.get(row);
    }

    public void addDeductionType(EmployeeDeductionTypes wageType) {
        employeeDeductionType.add(wageType);
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
        EmployeeDeductionTypes wageType = employeeDeductionType.get(rowIndex);
        if (columnIndex == 0) {
            return wageType.getDescription();
        } else if (columnIndex == 1) {
            return wageType.getOneTime();
        }
        return "";
    }

}
