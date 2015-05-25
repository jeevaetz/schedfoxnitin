/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.xadmin;

import java.text.NumberFormat;
import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.UserCommissionCaps;

/**
 *
 * @author user
 */
public class CommissionCapModel extends AbstractTableModel {

    private ArrayList<UserCommissionCaps> userCommissionCaps;
    private NumberFormat currencyFormat;
    
    public CommissionCapModel() {
        userCommissionCaps = new ArrayList<UserCommissionCaps>();
        currencyFormat = NumberFormat.getCurrencyInstance();
    }

    public void clear() {
        userCommissionCaps.clear();
        super.fireTableDataChanged();
    }
    
    public int getRowCount() {
        return userCommissionCaps.size();
    }

    public int getColumnCount() {
        return 2;
    }
    
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Year";
        } else if (columnIndex == 1) {
            return "Cap";
        }
        return "";
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        UserCommissionCaps cap = userCommissionCaps.get(rowIndex);
        if (columnIndex == 0) {
            return cap.getYearNumber();
        } else if (columnIndex == 1) {
            return currencyFormat.format(cap.getYearlyCaps());
        }
        return "";
    }
    
    public void addCap(UserCommissionCaps cap) {
        userCommissionCaps.add(cap);
        super.fireTableDataChanged();
    }
    
    public UserCommissionCaps getCapAt(int rowIndex) {
        return userCommissionCaps.get(rowIndex);
    }

}
