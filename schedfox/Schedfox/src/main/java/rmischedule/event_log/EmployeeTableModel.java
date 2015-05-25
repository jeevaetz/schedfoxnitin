/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.Employee;

/**
 *
 * @author ira
*/
public class EmployeeTableModel extends AbstractTableModel {

    private ArrayList<Employee> employee;
    private EventLoggerMainPanel parent;

    public EmployeeTableModel() {
        employee = new ArrayList<Employee>();
    }
    
    public void setMyParent(EventLoggerMainPanel parent) {
        this.parent = parent;
    }

    public Employee getEmployeeAt(Integer row) {
        return employee.get(row);
    }

    public void setEmployees(ArrayList<Employee> emp) {
        this.employee = emp;
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return employee.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Employee";
        } else if (column == 1) {
            return "Branch";
        } else if (column == 2) {
            return "Address";
        }
        return "";
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Employee currEmployee = employee.get(rowIndex);
        if (columnIndex == 0) {
            return currEmployee.getEmployeeFullName();
        } else if (columnIndex == 1) {
            return this.parent.getCachedBranches().get(currEmployee.getBranchId()).getBranchName();
        } else if (columnIndex == 2) {
            return currEmployee.getAddress1() + " " + currEmployee.getCity() + ", " + currEmployee.getState() + " " + currEmployee.getZip();
        }
        return "";
    }
}
