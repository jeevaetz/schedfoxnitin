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
public class EventTableModel extends AbstractTableModel {

    private ArrayList<Event> events;
    private ArrayList<Boolean> selectedEvents;
    private CachedEventData myParent;

    public EventTableModel(CachedEventData myParent) {
        events = new ArrayList<Event>();
        selectedEvents = new ArrayList<Boolean>();
        this.myParent = myParent;
    }

    public void selectDeselectAll(Boolean value) {
        try {
            for (int s = 0; s < selectedEvents.size(); s++) {
                selectedEvents.set(s, value);
            }
            super.fireTableDataChanged();
        } catch (Exception exe) {}
    }
    
    @Override
    public boolean isCellEditable(int row, int column) {
        if (column  == 9) {
            return true;
        }
        return false;
    }

    public Event getEventAt(Integer row) {
        return events.get(row);
    }
    
    public ArrayList<Event> getSelectedEvents() {
        ArrayList<Event> retVal = new ArrayList<Event>();
        for (int s = 0; s < this.events.size(); s++) {
            if (this.selectedEvents.get(s)) {
                retVal.add(events.get(s));
            }
        }
        return retVal;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
        selectedEvents = new ArrayList<Boolean>();
        for (int e = 0; e < events.size(); e++) {
            selectedEvents.add(false);
        }
        super.fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return events.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public String getColumnName(int column) {
        if (column == 0) {
            return "Branch";
        } else if (column == 1) {
            return "Entered On";
        } else if (column == 2) {
            return "Employee";
        } else if (column == 3) {
            return "Client";
        } else if (column == 4) {
            return "Event Type";
        } else if (column == 5) {
            return "Entered By";
        } else if (column == 6) {
            return "Shift Date";
        } else if (column == 7) {
            return "Start";
        } else if (column == 8) {
            return "End";
        } else if (column == 9) {
            return "Print?";
        }
        return "";
    }

    @Override
    public Class getColumnClass(int column) {
        if (column == 9) {
            return Boolean.class;
        }
        return String.class;
    }

    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        if (columnIndex == 9) {
            try {
                this.selectedEvents.set(rowIndex, (Boolean)value);
                super.fireTableDataChanged();
            } catch (Exception exe) {}
        }
    }
    
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Event currEvent = events.get(rowIndex);
        if (columnIndex == 0) {
            int branchId = 0;
            try {
                if (currEvent.getEmployeeId() != null && currEvent.getEmployeeId() != 0) {
                    Employee emp = myParent.fetchEmployeeData(currEvent.getEmployeeId());
                    branchId = emp.getBranchId();
                } else if (currEvent.getClientId() != null) {
                    Client cli = myParent.fetchClientData(currEvent.getClientId());
                    branchId = cli.getBranchId();
                }
                return myParent.fetchBranchData(branchId).getBranchName();
            } catch (Exception exe) {
                return "No Branch";
            }
        } else if (columnIndex == 1) {
            try {
                return myParent.getMyFormat().format(currEvent.getEnteredOn());
            } catch (Exception exe) {
            }
        } else if (columnIndex == 2) {
            if (currEvent.getEmployeeId() != null) {
                Employee emp = myParent.fetchEmployeeData(currEvent.getEmployeeId());
                return emp.getEmployeeFullName();
            } else {
                return "No Employee";
            }
        } else if (columnIndex == 3) {
            if (currEvent.getClientId() != null) {
                Client cli = myParent.fetchClientData(currEvent.getClientId());
                return cli.getClientName();
            } else {
                return "No Client";
            }
        } else if (columnIndex == 4) {
            try {
                return myParent.fetchEventTypeData(currEvent.getEventTypeId()).getEvent();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 5) {
            try {
                return myParent.fetchUserData(currEvent.getEnteredBy()).getUserFullName();
            } catch (Exception exe) {}
        } else if (columnIndex == 6) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                return myParent.getMyFormat().format(data.getDate());
            } catch (Exception exe) {
            }
        } else if (columnIndex == 7) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                if (currEvent.getOrigShiftStartTime() != null) {
                    data.setStartTime(currEvent.getOrigShiftStartTime());
                }
                return data.getStartTimeStr();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 8) {
            try {
                ScheduleData data = myParent.fetchSchedData(currEvent.getShiftId());
                if (currEvent.getOrigShiftEndTime() != null) {
                    data.setEndTime(currEvent.getOrigShiftEndTime());
                }
                return data.getEndTimeStr();
            } catch (Exception exe) {
            }
        } else if (columnIndex == 9) {
            try {
                return this.selectedEvents.get(rowIndex);
            } catch (Exception exe) {}
        }
        return "";
    }
}
