/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EmployeeDeductions;
/**
 *
 * @author user
 */
public class DeductionModel extends AbstractTableModel {

        private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();

        private ArrayList<EmployeeDeductions> employeeDeduction;
        private int companyId;

        public DeductionModel(int companyId) {
            employeeDeduction = new ArrayList<EmployeeDeductions>();
            this.companyId = companyId;
        }

        public EmployeeDeductions getEmployeeDeduction(int row) {
            return employeeDeduction.get(row);
        }

        public int getRowCount() {
            return employeeDeduction.size();
        }

        public void clearData() {
            employeeDeduction = new ArrayList<EmployeeDeductions>();
            this.fireTableDataChanged();
        }

        public void addDeduction(EmployeeDeductions deduction) {
            employeeDeduction.add(deduction);
            this.fireTableDataChanged();
        }

        public int getColumnCount() {
            return 4;
        }

        @Override
        public String getColumnName(int columnIndex) {
            if (columnIndex == 0) {
                return "Description";
            } else if (columnIndex == 1) {
                return "Amount";
            } else if (columnIndex == 2) {
                return "Balance";
            } else if (columnIndex == 3) {
                return "One Time?";
            }
            return "";
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            EmployeeDeductions employeeWage = employeeDeduction.get(rowIndex);
            
            if (columnIndex == 0) {
                return employeeWage.getEmployeeDeductionTypes(companyId + "").getDescription();
            } else if (columnIndex == 1) {
                return currencyFormat.format(employeeWage.getAmount().doubleValue());
            } else if (columnIndex == 2) {
                return currencyFormat.format(employeeWage.getBalance().doubleValue());
            } else if (columnIndex == 3) {
                return employeeWage.getEmployeeDeductionTypes(companyId + "").getOneTime();
            }
            return "";
        }

    }
