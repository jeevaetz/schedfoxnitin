/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.components;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.ManagementClientInvoice;

/**
 *
 * @author user
 */
public class ManagementClientInvoiceModel extends AbstractTableModel {

    private ArrayList<ManagementClientInvoice> clientInvoices;

    public ManagementClientInvoiceModel() {
        clientInvoices = new ArrayList<ManagementClientInvoice>();
    }

    public int getRowCount() {
        return clientInvoices.size();
    }

    public int getColumnCount() {
        return 6;
    }

    public void addInvoice(ManagementClientInvoice invoice) {
        clientInvoices.add(invoice);
        super.fireTableDataChanged();
    }

    public void clearData() {
        clientInvoices.clear();
        super.fireTableDataChanged();
    }

    public ManagementClientInvoice getAccrual(int rowIndex) {
        return clientInvoices.get(rowIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ManagementClientInvoice currInvoice = clientInvoices.get(rowIndex);
        if (columnIndex == 0) {
            return currInvoice.getInvoiced_on();
        } else if (columnIndex == 1) {
            return currInvoice.getWeekly_charge();
        } else if (columnIndex == 2) {
            return currInvoice.getAmount_invoiced();
        } else if (columnIndex == 3) {
            return currInvoice.getEmployee_count();
        } else if (columnIndex == 4) {
            return currInvoice.calcTotalEmpCharge();
        } else if (columnIndex == 5) {
            return new BigDecimal(currInvoice.calcTotalEmpCharge().floatValue() + currInvoice.getAmount_invoiced().floatValue());
        } else {
            return 0;
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Invoice Date";
        } else if (columnIndex == 1) {
            return "Weekly Charge";
        } else if (columnIndex == 2) {
            return "Monthly Charge";
        } else if (columnIndex == 3) {
            return "Number of Emps";
        } else if (columnIndex == 4) {
            return "Emp Charge";
        } else {
            return "Amount Due";
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Date.class;
        } else if (columnIndex == 1) {
            return BigDecimal.class;
        } else if (columnIndex == 2) {
            return BigDecimal.class;
        }
        return String.class;
    }

}
