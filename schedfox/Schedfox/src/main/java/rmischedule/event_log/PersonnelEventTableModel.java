/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.JButton;
import javax.swing.table.AbstractTableModel;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.Event;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public class PersonnelEventTableModel extends AbstractTableModel {

    private ArrayList<Event> events;
    private ArrayList<Boolean> isSelected;
    private CachedEventData myParent;

    public PersonnelEventTableModel(CachedEventData myParent) {
        events = new ArrayList<Event>();
        isSelected = new ArrayList<Boolean>();
        this.myParent = myParent;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        if (column == 8) { 
            return true;
        }
        return false;
    }

    @Override
    public void setValueAt(Object val, int row, int column) {
        try {
            if (column == 8) {
                Boolean value = (Boolean)val;
                this.isSelected.set(row, value);
                super.fireTableDataChanged();
            }
        } catch (Exception exe) {}
    }
    
    public ArrayList<Event> getSelectedEvents() {
        ArrayList<Event> retVal = new ArrayList<Event>();
        for (int s = 0; s < this.events.size(); s++) {
            if (this.isSelected.get(s)) {
                retVal.add(events.get(s));
            }
        }
        return retVal;
    }
    
    public Event getEventAt(Integer row) {
        return events.get(row);
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        isSelected = new ArrayList<Boolean>();
        for (int s = 0; s < events.size(); s++) {
            isSelected.add(false);
        }
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return events.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Date";
        } else if (column == 1) {
            return "Employee";
        } else if (column == 2) {
            return "Client";
        } else if (column == 3) {
            return "Event Type";
        } else if (column == 4) {
            return "Entered By";
        } else if (column == 5) {
            return "Shift Date";
        } else if (column == 6) {
            return "Start";
        } else if (column == 7) {
            return "End";
        } else if (column == 8) {
            return "Select?";
        }
        return "";
    }
    
    @Override
    public Class getColumnClass(int column) {
        if (column == 8) {
            return Boolean.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event currEvent = events.get(rowIndex);
        if (columnIndex == 0) {
            try {
                return myParent.getMyFormat().format(currEvent.getEnteredOn());
            } catch (Exception exe) {
            }
        } else if (columnIndex == 1) {
            if (currEvent.getEmployeeId() != null) {
                Employee emp = myParent.fetchEmployeeData(currEvent.getEmployeeId());
                return emp.getEmployeeFullName();
            } else {
                return "No Employee";
            }
        } else if (columnIndex == 2) {
            if (currEvent.getClientId() != null) {
                Client cli = myParent.fetchClientData(currEvent.getClientId());
                return cli.getClientName();
            } else {
                return "No Client";
            }
        } else if (columnIndex == 3) {
            try {
                return myParent.fetchEventTypeData(currEvent.getEventTypeId()).getEvent();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 4) {
            try {
                return myParent.fetchUserData(currEvent.getEnteredBy()).getUserFullName();
            } catch (Exception exe) {}
        } else if (columnIndex == 5) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                return myParent.getMyFormat().format(data.getDate());
            } catch (Exception exe) {
            }
        } else if (columnIndex == 6) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                return data.getStartTimeStr();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 7) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                return data.getEndTimeStr();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 8) {
            try {
                return this.isSelected.get(rowIndex);
            } catch (Exception exe) {}
        }
        return "";
    }
    
}
