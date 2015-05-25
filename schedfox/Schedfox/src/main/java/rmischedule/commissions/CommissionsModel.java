/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.commissions;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.ClientInvoice;
import schedfoxlib.model.Commission;


/**
 *
 * @author user
 */
public class CommissionsModel extends AbstractTableModel {
    private ArrayList<Commission> commissions;
    private NumberFormat percentageFormat = NumberFormat.getPercentInstance();

    public CommissionsModel() {
        commissions = new ArrayList<Commission>();
        percentageFormat.setMaximumFractionDigits(1);
    }

    public void addCommission(Commission commission) {
        commissions.add(commission);
        super.fireTableDataChanged();
    }

    public Commission getCommissionAt(int row) {
        return commissions.get(row);
    }

    public void clearData() {
        commissions.clear();
        super.fireTableDataChanged();
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Name";
        } else if (columnIndex == 1) {
            return "% of first payment";
        } else if (columnIndex == 2) {
            return "Sales 1st year";
        } else if (columnIndex == 3) {
            return "Manager 1st year";
        } else if (columnIndex == 4) {
            return "Sales 2nd year";
        } else if (columnIndex == 5) {
            return "Manager 2nd year";
        }
        return "";
    }

    @Override
    public int getRowCount() {
        return commissions.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        Commission commission = commissions.get(rowIndex);
        try {
            if (columnIndex == 0) {
                return commission.getCommission_name();
            } else if (columnIndex == 1) {
                return percentageFormat.format(commission.getFirst_pmt_sales_percentage());
            } else if (columnIndex == 2) {
                return percentageFormat.format(commission.getFirst_year_sales_percentage());
            } else if (columnIndex == 3) {
                return percentageFormat.format(commission.getFirst_year_manager_percentage());
            } else if (columnIndex == 4) {
                return percentageFormat.format(commission.getSecond_year_sales_percentage());
            } else if (columnIndex == 5) {
                return percentageFormat.format(commission.getSecond_year_manager_percentage());
            }
        } catch(Exception e) {}
        return "";
    }

}

