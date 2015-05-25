/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JOptionPane;
import javax.swing.table.AbstractTableModel;
import rmischedule.main.Main_Window;
import static rmischedule.schedule.Schedule_Main_Form.companyId;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.EventController;
import rmischeduleserver.control.IncidentReportController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventFollowup;
import schedfoxlib.model.EventType;
import schedfoxlib.model.IncidentReportType;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public class IncidentEventPanel extends SingleEventPanel {

    private ClientTableModel clientTabelModel;
    
    public IncidentEventPanel(EventLoggerMainPanel parent) {
        super(parent);

        clientTabelModel.setMyParent(parent);
    }
    
    @Override
    public ArrayList<EventType> getEventTypes() {
        try {
            IncidentReportController iController = new IncidentReportController(super.getMyParent().getCompanyId());
            ArrayList<IncidentReportType> iTypes = iController.getIncidentReportTypes();
            
            //EventController eController = new EventController(super.getMyParent().getCompanyId());
            //ArrayList<EventType> eTypes = eController.getEventTypes(1);
            
            ArrayList<EventType> types = new ArrayList<EventType>();
            
            EventType selectOne = new EventType();
            selectOne.setEvent("Select a type!");
            selectOne.setEventTypeId(-1);
            types.add(selectOne);
            for (int i = 0; i < iTypes.size(); i++) {
                IncidentReportType currType = iTypes.get(i);
                EventType type = new EventType();
                type.setEvent(currType.getReportType());
                type.setEventTypeId(currType.getIncidentReportTypeId() * -1);
                types.add(type);
            }
            
            //types.addAll(eTypes);
            //Collections.sort(types);
            
            return types;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void loadClientData(Client cli) {
        this.cli = cli;

        nameLbl.setText(cli.getClientName());
        phoneLbl.setText(cli.getClientPhone());
        cellLbl.setText(cli.getClientPhone2());
        addressLbl.setText(cli.getAddress1() + " " + cli.getCity() + ", " + cli.getState() + " " + cli.getZip());

        nonsavedfollowups = new ArrayList<EventFollowup>();
        FetchScheduleData fetchSched = new FetchScheduleData(null, cli, shiftCombo);
        new Thread(fetchSched).run();
    }
    
    @Override
    public void saveEvent() throws Exception {
        Event event = null;
        if (this.selectedEvent == null) {
            event = new Event();
        } else {
            event = this.selectedEvent;
        }
        try {
            ScheduleData schedData = (ScheduleData) shiftCombo.getSelectedItem();
            event.setShiftId(schedData.getScheduleId());
            event.setClientId(schedData.getClientId());
            event.setEmployeeId(schedData.getEmployeeId());
            event.setOrigShiftStartTime(schedData.getStartTime());
            event.setOrigShiftEndTime(schedData.getEndTime());
        } catch (Exception exe) {
        }
        try {
            event.setEventTypeId(((EventType) getEventComboModel().getSelectedItem()).getEventTypeId());
        } catch (Exception exe) {
        }
        try {
            event.setEmployeeId(emp.getEmployeeId());
        } catch (Exception exe) {
        }
        event.setEnteredBy(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
        event.setEventNotes(eventNotesTxt.getText());

        EventController eventController = EventController.getInstance(companyId);
        try {
            Integer eventId = eventController.saveEvent(event);
            event.setEventId(eventId);
        } catch (Exception exe) {
        }
        if (nonsavedfollowups != null) {
            for (int f = 0; f < this.nonsavedfollowups.size(); f++) {
                try {
                    EventFollowup eFollow = this.nonsavedfollowups.get(f);
                    eFollow.setEventId(event.getEventId());
                    eventController.saveEventFollowup(eFollow);
                } catch (Exception exe) {
                }
            }
        }
        super.getMyParent().reloadData();
        
        this.addFollowupPersonnelBtn.setEnabled(true);
        
        selectedEvent = event;
        
        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Event Saved", "Event Saved", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void selectObjectAt(int row) {
        cli = clientTabelModel.getClientAt(myTable.convertRowIndexToModel(row));
        loadClientData(cli);
    }

    @Override
    public AbstractTableModel getTableModel() {
        if (clientTabelModel == null) {
            clientTabelModel = new ClientTableModel();
        }
        return clientTabelModel;
    }

    @Override
    public String getNameLabel() {
        return "Incident Event";
    }

    @Override
    public void loadSearchResults(String searchParams, ArrayList<Integer> selBranches) {
        try {
            ClientController clientController = ClientController.getInstance(super.getMyParent().getCompanyId());
            ArrayList<Client> clients = clientController.searchClientsByParam(searchParams, selBranches);
            clientTabelModel.setClients(clients);
        } catch (Exception exe) {
        }
    }

    @Override
    public void loadEventData(Event event) {
        super.getMyParent().getTabbedPane().setSelectedComponent(this);
        try {
            ClientController cliController = ClientController.getInstance(this.getMyParent().getCompanyId());
            Client cli = cliController.getClientById(event.getClientId());
            this.loadClientData(cli);
        } catch (Exception exe) {
        }
    }

}
