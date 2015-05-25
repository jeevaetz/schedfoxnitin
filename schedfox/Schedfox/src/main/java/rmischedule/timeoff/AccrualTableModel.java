/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.timeoff;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.TimeOffAccrual;

/**
 *
 * @author user
 */
public class AccrualTableModel extends AbstractTableModel {

    private ArrayList<TimeOffAccrual> timeoff;

    public AccrualTableModel() {
        timeoff = new ArrayList<TimeOffAccrual>();
    }

    public int getRowCount() {
        return timeoff.size();
    }

    public int getColumnCount() {
        return 5;
    }

    public void addAccrual(TimeOffAccrual accrual) {
        timeoff.add(accrual);
        super.fireTableDataChanged();
    }

    public void clearData() {
        timeoff.clear();
        super.fireTableDataChanged();
    }

    public TimeOffAccrual getAccrual(int rowIndex) {
        return timeoff.get(rowIndex);
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        TimeOffAccrual currTimeOff = timeoff.get(rowIndex);
        if (columnIndex == 0) {
            return currTimeOff.getStartInterval();
        } else if (columnIndex == 1) {
            return currTimeOff.getEndInterval();
        } else if (columnIndex == 2) {
            return "Every " + currTimeOff.getTimeOffInterval();
        } else if (columnIndex == 3) {
            return currTimeOff.getDays();
        } else {
            return currTimeOff.getActive();
        }
    }

    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 0) {
            return "Start";
        } else if (columnIndex == 1) {
            return "End";
        } else if (columnIndex == 2) {
            return "Interval";
        } else if (columnIndex == 3) {
            return "Days Off";
        } else {
            return "Is Active?";
        }
    }

    @Override
    public Class getColumnClass(int columnIndex) {
        if (columnIndex == 4) {
            return Boolean.class;
        }
        return String.class;
    }

}
