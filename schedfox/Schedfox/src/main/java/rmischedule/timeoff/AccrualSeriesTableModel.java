/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.timeoff;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.TimeOffSeries;

/**
 *
 * @author user
 */
public class AccrualSeriesTableModel extends AbstractTableModel {

    private ArrayList<TimeOffSeries> timeOffSeries;

    public AccrualSeriesTableModel() {
        timeOffSeries = new ArrayList<TimeOffSeries>();
    }

    public int getRowCount() {
        return timeOffSeries.size();
    }

    public int getColumnCount() {
        return 2;
    }

    public void addAccrual(TimeOffSeries accrual) {
        timeOffSeries.add(accrual);
        super.fireTableDataChanged();
    }

    public void clearData() {
        timeOffSeries.clear();
        super.fireTableDataChanged();
    }

    public TimeOffSeries getAccrual(int rowIndex) {
        return timeOffSeries.get(rowIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TimeOffSeries currSeries = timeOffSeries.get(rowIndex);
        if (columnIndex == 0) {
            return currSeries.getTimeOffSeries();
        } else {
            return currSeries.getActive();
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Series Name";
        } else {
            return "Active";
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Boolean.class;
        }
        return String.class;
    }

}
