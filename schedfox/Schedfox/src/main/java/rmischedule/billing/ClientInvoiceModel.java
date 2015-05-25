/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.ClientInvoice;

/**
 *
 * @author user
 */
public class ClientInvoiceModel extends AbstractTableModel {

    private ArrayList<ClientInvoice> clientInvoices;
    private NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private DateFormat simpleFormat = new SimpleDateFormat("MM/dd/yyyy");

    public ClientInvoiceModel() {
        clientInvoices = new ArrayList<ClientInvoice>();
    }

    public void addInvoice(ClientInvoice clientInvoice) {
        clientInvoices.add(clientInvoice);
        super.fireTableDataChanged();
    }

    public void clearData() {
        clientInvoices.clear();
        super.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Date";
        } else if (columnIndex == 1) {
            return "Amount";
        } else if (columnIndex == 2) {
            return "Balance";
        } else if (columnIndex == 3) {
            return "Taxes";
        } else if (columnIndex == 4) {
            return "Reg Hours";
        } else if (columnIndex == 5) {
            return "Ovt Hours";
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return clientInvoices.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        ClientInvoice clientInvoice = clientInvoices.get(rowIndex);
        try {
            if (columnIndex == 0) {
                return simpleFormat.format(clientInvoice.getIssued_on());
            } else if (columnIndex == 1) {
                return currencyFormat.format(clientInvoice.getAmount_due());
            } else if (columnIndex == 2) {
                return currencyFormat.format(clientInvoice.getBalance_due());
            } else if (columnIndex == 3) {
                return currencyFormat.format(clientInvoice.getAmount_due().doubleValue() - clientInvoice.getTotalTaxes().doubleValue());
            } else if (columnIndex == 4) {
                return clientInvoice.getTotalRegHrs();
            } else if (columnIndex == 5) {
                return clientInvoice.getTotalOverHrs();
            }
        } catch(Exception e) {}
        return "";
    }

}
