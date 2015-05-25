/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import rmischedule.main.Main_Window;
import rmischedule.personnel.PersonnelChangeForm;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.EventController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventFollowup;
import schedfoxlib.model.EventType;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public class PersonnelEventPanel extends SingleEventPanel {

    private EmployeeTableModel employeeTabelModel;
    private EventLoggerMainPanel myParent;

    public PersonnelEventPanel(EventLoggerMainPanel parent) {
        super(parent);

        this.myParent = parent;
        employeeTabelModel.setMyParent(parent);
    }

    public void loadEmployeeData(Employee emp, Boolean loadScheduleData) {
        this.emp = emp;

        nameLbl.setText(emp.getEmployeeFullName());
        phoneLbl.setText(emp.getEmployeePhone());
        cellLbl.setText(emp.getEmployeeCell());
        addressLbl.setText(emp.getAddress1() + " " + emp.getCity() + ", " + emp.getState() + " " + emp.getZip());

        nonsavedfollowups = new ArrayList<EventFollowup>();
        if (loadScheduleData) {
            FetchScheduleData fetchSched = new FetchScheduleData(emp, null, shiftCombo);
            new Thread(fetchSched).run();
        }
    }

    @Override
    public ArrayList<EventType> getEventTypes() {
        try {
            EventController eController = EventController.getInstance(super.getMyParent().getCompanyId());
            ArrayList<EventType> types = eController.getEventTypes(1);
            EventType selectOne = new EventType();
            selectOne.setEvent("Select a type!");
            selectOne.setEventTypeId(-1);
            types.add(0, selectOne);
            return types;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void selectObjectAt(int row) {
        emp = employeeTabelModel.getEmployeeAt(myTable.convertRowIndexToModel(row));
        loadEmployeeData(emp, true);
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (employeeTabelModel == null) {
            employeeTabelModel = new EmployeeTableModel();
        }
        return employeeTabelModel;
    }

    @Override
    public String getNameLabel() {
        return "Employee Name";
    }

    @Override
    public void loadSearchResults(String searchParams, ArrayList<Integer> selBranches) {
        try {
            EmployeeController empController = EmployeeController.getInstance(super.getMyParent().getCompanyId());
            ArrayList<Employee> employees = empController.getEmployeesByParam(searchParams, selBranches);
            this.employeeTabelModel.setEmployees(employees);
        } catch (Exception exe) {
        }
    }

    @Override
    public void saveEvent() throws Exception {
        Event event = null;
        boolean isInsert = true;
        if (this.selectedEvent == null) {
            event = new Event();
        } else {
            event = this.selectedEvent;
            isInsert = false;
        }
        try {
            ScheduleData schedData = (ScheduleData) shiftCombo.getSelectedItem();
            event.setShiftId(schedData.getScheduleId());
            event.setClientId(schedData.getClientId());
            if (emp == null || emp.getEmployeeId() == null || emp.getEmployeeId() == 0) {
                event.setEmployeeId(schedData.getEmployeeId());
            }
        } catch (Exception exe) {
        }

        EventType type = ((EventType) getEventComboModel().getSelectedItem());
        if (type.getEventTypeId() == -1) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Select an event type!", "Select an event type!", JOptionPane.INFORMATION_MESSAGE);
        } else {
            try {
                event.setEventTypeId(type.getEventTypeId());
            } catch (Exception exe) {
            }
            try {
                ScheduleData data = (ScheduleData) shiftCombo.getSelectedItem();
                event.setShiftId(data.getScheduleId());
                event.setClientId(data.getClientId());
                event.setOrigShiftStartTime(data.getStartTime());
                event.setOrigShiftEndTime(data.getEndTime());
            } catch (Exception exe) {
            }
            try {
                if (emp.getEmployeeId() != null && emp.getEmployeeId() != 0)  {
                    event.setEmployeeId(emp.getEmployeeId());
                }
            } catch (Exception exe) {
            }
            event.setEnteredBy(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
            event.setEventNotes(eventNotesTxt.getText());

            EventController eventController = EventController.getInstance(this.getMyParent().getCompanyId());
            try {
                Integer eventId = eventController.saveEvent(event);
                event.setEventId(eventId);
            } catch (Exception exe) {
            }

            super.addFollowupsForEvent(event);
            
            super.getMyParent().reloadData();

            this.addFollowupPersonnelBtn.setEnabled(true);

            selectedEvent = event;

            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Event Saved", "Event Saved", JOptionPane.INFORMATION_MESSAGE);
            
            if (super.isFromSchedule()) {
                this.getMyParent().getMyParent().dispose();
            }
            
            if (event.getEventTypeId() == 13 && isInsert) {
                try {
                    int confirm = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Do you want to mark the employee as banned?", 
                            "Do you want to mark the employee as banned?", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        EmployeeController.getInstance(this.myParent.getCompanyId()).banEmployeeFromPost(emp.getEmployeeId(), cli.getClientId(), true);
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Marked as banned!", "Marked as banned!", JOptionPane.INFORMATION_MESSAGE);
                    }
                } catch (Exception exe) {}
            } else if ((event.getEventTypeId() == 14 || event.getEventTypeId() == 15) && isInsert) {
                try {
                    ScheduleData data = (ScheduleData) shiftCombo.getSelectedItem();
                    Client myClient = ClientController.getInstance(this.myParent.getCompanyId()).getClientById(data.getClientId());
                    PersonnelChangeForm form = new PersonnelChangeForm(Main_Window.parentOfApplication, myClient, emp, Integer.parseInt(this.myParent.getCompanyId()), emp.getBranchId(), true);
                    form.setVisible(true);
                } catch (Exception exe) {}
            }
        }
    }

    @Override
    public void loadEventData(Event event) {
        super.getMyParent().getTabbedPane().setSelectedComponent(this);
        try {
            EmployeeController empController = EmployeeController.getInstance(this.getMyParent().getCompanyId());
            Employee emp = empController.getEmployeeById(event.getEmployeeId());
            this.loadEmployeeData(emp, false);
        } catch (Exception exe) {
        }
    }

}
