/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EmployeeWages;
/**
 *
 * @author user
 */
public class WageModel extends AbstractTableModel {

        private ArrayList<EmployeeWages> employeeWages;
        private int companyId;

        public WageModel(int companyId) {
            employeeWages = new ArrayList<EmployeeWages>();
            this.companyId = companyId;
        }

        public EmployeeWages getEmployeeWage(int row) {
            return employeeWages.get(row);
        }

        public int getRowCount() {
            return employeeWages.size();
        }

        public void clearData() {
            employeeWages = new ArrayList<EmployeeWages>();
            this.fireTableDataChanged();
        }

        public void addWage(EmployeeWages rateCode) {
            employeeWages.add(rateCode);
            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return 3;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Description";
            } else if (columnIndex == 1) {
                return "Amount";
            } else if (columnIndex == 2) {
                return "One Time?";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            EmployeeWages employeeWage = employeeWages.get(rowIndex);
            
            if (columnIndex == 0) {
                return employeeWage.getWageType(companyId + "").getDescription();
            } else if (columnIndex == 1) {
                return employeeWage.getWages();
            } else if (columnIndex == 2) {
                return employeeWage.getWageType(companyId + "").getOneTime();
            }
            return "";
        }

    }
