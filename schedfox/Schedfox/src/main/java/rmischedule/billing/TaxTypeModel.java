/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.billing;

import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.TaxTypes;

/**
 *
 * @author user
 */
public class TaxTypeModel extends AbstractTableModel {

    private ArrayList<TaxTypes> taxes;
    private NumberFormat currencyFormat = NumberFormat.getPercentInstance();

    public TaxTypeModel() {
        taxes = new ArrayList<TaxTypes>();
    }

    public void addTaxType(TaxTypes tax) {
        this.taxes.add(tax);
        this.fireTableDataChanged();
    }

    public TaxTypes getTaxType(int row) {
        return this.taxes.get(row);
    }

    public int getRowCount() {
        return taxes.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TaxTypes currTax = taxes.get(rowIndex);
        if (columnIndex == 0) {
            return currTax.getTaxName();
        } else if (columnIndex == 1) {
            return currencyFormat.format(currTax.getTaxRate().doubleValue() / 100.0);
        }
        return currTax;
    }

    public void setSelectedItem(TaxTypes taxType) {
        for (int t = 0; t < taxes.size(); t++) {
            if (taxes.get(t).equals(taxType)) {

            }
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Tax Name";
        } else if (columnIndex == 1) {
            return "Rate";
        }
        return "";
    }

}
