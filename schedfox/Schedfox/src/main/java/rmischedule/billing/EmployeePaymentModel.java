/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.billing;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.EmployeePayments;
import rmischedule.utility.StringHlpr;

/**
 *
 * @author user
 */
public class EmployeePaymentModel extends AbstractTableModel {

    private ArrayList<EmployeePayments> payments;
    private int companyId;
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
    private NumberFormat myNumFormat = NumberFormat.getCurrencyInstance();

    public EmployeePaymentModel(int companyId) {
        this.companyId = companyId;
        this.payments = new ArrayList<EmployeePayments>();
    }

    public int getRowCount() {
        return payments.size();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Date";
        } else if (columnIndex == 1) {
            return "Type";
        } else if (columnIndex == 2) {
            return "Gross";
        } else if (columnIndex == 3) {
            return "Taxes";
        } else if (columnIndex == 4) {
            return "Deduction";
        } else if (columnIndex == 5) {
            return "Net";
        }
        return "";
    }

    public void clearData() {
        payments = new ArrayList<EmployeePayments>();
        this.fireTableDataChanged();
    }

    public EmployeePayments getPayment(int row) {
        return payments.get(row);
    }

    public void addPayment(EmployeePayments employeePayment) {
        payments.add(employeePayment);
        this.fireTableDataChanged();
    }

    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            try {
                return myFormat.format(payments.get(rowIndex).getDateOfTrans());
            } catch (Exception e) {
                return myNumFormat.format(new Double(0));
            }
        } else if (columnIndex == 1) {
            // When changing this to the check number leave the HTML and place the value where "Check" is
            // this colors it blue and underlines the text


            return "<HTML><font color=blue><u>Chk# " + StringHlpr.stripLeadingZeros(payments.get(rowIndex).getCheckNum()) + "</u></font></HTML>";
        } else if (columnIndex == 2) {
            try {
                return myNumFormat.format(payments.get(rowIndex).getGrossPay().doubleValue());
            } catch (Exception e) {
                return myNumFormat.format(new Double(0));
            }
        } else if (columnIndex == 3) {
            try {
                return myNumFormat.format(payments.get(rowIndex).getSumTax().doubleValue());
            } catch (Exception e) {
                return myNumFormat.format(new Double(0));
            }
        } else if (columnIndex == 4) {
            try {
                return myNumFormat.format(payments.get(rowIndex).getSumDeductions().doubleValue());
            } catch (Exception e) {
                return myNumFormat.format(new Double(0));
            }
        } else if (columnIndex == 5) {
            try {
                return myNumFormat.format(payments.get(rowIndex).getNetPay().doubleValue());
            } catch (Exception exe) {
                return myNumFormat.format(new Double(0));
            }
        }
        return "";
    }

    
}
