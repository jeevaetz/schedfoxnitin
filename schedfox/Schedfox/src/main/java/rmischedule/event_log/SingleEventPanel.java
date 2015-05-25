/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.AbstractTableModel;
import rmischedule.main.Main_Window;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.EventController;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventFollowup;
import schedfoxlib.model.EventType;
import schedfoxlib.model.Group;
import schedfoxlib.model.ScheduleData;

/**
 *
 * @author ira
 */
public abstract class SingleEventPanel extends javax.swing.JPanel {

    protected ArrayList<EventFollowup> nonsavedfollowups;

    protected Client cli;
    protected Employee emp;
    protected Event selectedEvent;
    private boolean fromSchedule = false;

    private EventTypeComboModel eventComboModel = new EventTypeComboModel();

    private EventLoggerMainPanel myParent;

    /**
     * Creates new form SingleEventPanel
     */
    public SingleEventPanel(EventLoggerMainPanel parent) {
        this.myParent = parent;

        initComponents();
        
        followupPanel.setVisible(false);

        myTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                selectedEvent = null;

                eventNotesTxt.setEnabled(true);
                shiftCombo.setEnabled(true);
                searchShiftsBtn.setEnabled(true);
                personnelTypeCmb.setEnabled(true);

                addFollowupPersonnelBtn.setEnabled(false);

                eventNotesTxt.setText("");
                shiftCombo.removeAllItems();
                personnelTypeCmb.setSelectedIndex(0);

                selectObjectAt(myTable.getSelectedRow());
            }
        });

        try {
            ArrayList<EventType> types = getEventTypes();
            for (int t = 0; t < types.size(); t++) {
                eventComboModel.addEventType(types.get(t));
                parent.getEventTypeHash().put(types.get(t).getEventTypeId(), types.get(t));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSchedData(String data) {
        if (data.indexOf("/") > -1) {
            data = data.substring(0, data.indexOf("/"));
        }

        try {
            ScheduleController schedController = new ScheduleController(myParent.getCompanyId());
            ClientController clientController = ClientController.getInstance(myParent.getCompanyId());
            ScheduleData sched = schedController.getScheduleByIdentifier(Integer.parseInt(data));

            if (myParent.getClientHash().get(sched.getClientId()) == null) {
                myParent.getClientHash().put(sched.getClientId(), clientController.getClientById(sched.getClientId()));
            }

            Client currClient = myParent.getClientHash().get(sched.getClientId());
            try {
                Employee currEmployee = fetchEmployeeData(sched.getEmployeeId());
                sched.setEmployee(currEmployee);
            } catch (Exception exe) {
            }
            sched.setClient(currClient);

            shiftCombo.removeAllItems();
            shiftCombo.addItem(sched);
        } catch (Exception exe) {
        }

    }

    public EventLoggerMainPanel getMyParent() {
        return myParent;
    }

    protected Employee fetchEmployeeData(Integer employeeId) {
        Employee retVal = null;
        if (employeeId != null && employeeId != 0) {
            if (myParent.getEmployeeHash().get(employeeId) == null) {
                try {
                    EmployeeController empController = EmployeeController.getInstance(myParent.getCompanyId());
                    retVal = empController.getEmployeeById(employeeId);
                    myParent.getEmployeeHash().put(employeeId, retVal);
                } catch (Exception exe) {
                }
            }
        } else {
            return new Employee();
        }
        return myParent.getEmployeeHash().get(employeeId);
    }

    public abstract ArrayList<EventType> getEventTypes();

    public abstract void saveEvent() throws Exception;

    public abstract void selectObjectAt(int row);

    public abstract AbstractTableModel getTableModel();

    public abstract String getNameLabel();

    public abstract void loadEventData(Event event);

    private void addFollowup() {
        AddEventFollowupDialog addDialog = new AddEventFollowupDialog(Main_Window.parentOfApplication, true, myParent.getCompanyId(), 0);
        addDialog.setVisible(true);
        try {
            EventFollowup follow = addDialog.getEventFollowup();
            if (follow != null && this.selectedEvent == null) {
                this.nonsavedfollowups.add(follow);
            } else if (follow != null && this.selectedEvent != null) {
                EventController eventController = EventController.getInstance(this.getMyParent().getCompanyId());
                follow.setEventId(this.selectedEvent.getEventId());
                eventController.saveEventFollowup(follow);
                this.loadEvent(selectedEvent);
                this.getMyParent().reloadData();
            }
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Saved Followup!", "Saved Followup!", JOptionPane.OK_OPTION);
        } catch (Exception exe) {
        }
    }
    
    protected void addFollowupsForEvent(Event event) {
        EventController eController = EventController.getInstance(this.getMyParent().getCompanyId());

        try {
            ArrayList<Group> groups = eController.getGroupsForEventType(event.getEventTypeId());
            for (int g = 0; g < groups.size(); g++) {
                EventFollowup eventFollowup = new EventFollowup();
                Group selectedGroup = groups.get(g);
                try {

                    EventFollowup follow = eController.getNonClosedFollowupsForUser(this.selectedEvent.getEventId(), Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                    if (follow != null) {
                        eventFollowup = follow;
                    }
                } catch (Exception exe) {
                }
                eventFollowup.setEventId(event.getEventId());
                eventFollowup.setFollowupRequestedBy(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                eventFollowup.setFollowupRequestGroup(selectedGroup.getGroupId());

                try {
                    EventController eventController = EventController.getInstance(this.getMyParent().getCompanyId());
                    eventController.saveEventFollowup(eventFollowup);
                } catch (Exception exe) {
                }

            }
        } catch (Exception exe) {
        }
    }

    public void loadEvent(Event event) {
        EventController eventController = EventController.getInstance(this.getMyParent().getCompanyId());
        try {
            ArrayList<EventFollowup> followups = eventController.getEventFollowup(event.getEventId());
            eventDataPanel.removeAll();
            for (int f = 0; f < followups.size(); f++) {
                EventFollowup follow = followups.get(f);
                if (follow.getFollowupProcessedBy() != null && follow.getFollowupProcessedBy() != 0) {
                    EventFollowupPanel followupPanel = new EventFollowupPanel(followups.get(f), this, this.getMyParent().getCompanyId());
                    eventDataPanel.add(followupPanel);
                }
            }
            if (followups.size() > 0) {
                followupPanel.setVisible(true);
            }
        } catch (Exception exe) {
        }

        try {
            eventNotesTxt.setText(event.getEventNotes());
        } catch (Exception exe) {
        }
        try {
            EventType type = myParent.getEventTypeHash().get(event.getEventTypeId());
            personnelTypeCmb.setSelectedItem(type);
        } catch (Exception exe) {
        }
        try {
            shiftCombo.removeAllItems();
            ScheduleData data = myParent.fetchSchedData(event.getShiftId());
            try {
                data.setEmployee(this.myParent.fetchEmployeeData(data.getEmployeeId()));
            } catch (Exception exe) {
            }
            try {
                data.setClient(this.myParent.fetchClientData(data.getClientId()));
            } catch (Exception exe) {
            }
            shiftCombo.addItem(data);
            shiftCombo.setSelectedItem(data);
        } catch (Exception exe) {
        }
        this.loadEventData(event);

        this.selectedEvent = event;
        this.eventNotesTxt.setEnabled(false);
        this.shiftCombo.setEnabled(false);
        this.searchShiftsBtn.setEnabled(false);
        this.personnelTypeCmb.setEnabled(false);

        this.addFollowupPersonnelBtn.setEnabled(true);
    }

    public abstract void loadSearchResults(String searchParams, ArrayList<Integer> selBranches);

    /**
     * @return the eventComboModel
     */
    public EventTypeComboModel getEventComboModel() {
        return eventComboModel;
    }

    /**
     * @param eventComboModel the eventComboModel to set
     */
    public void setEventComboModel(EventTypeComboModel eventComboModel) {
        this.eventComboModel = eventComboModel;
    }

    /**
     * @return the fromSchedule
     */
    public boolean isFromSchedule() {
        return fromSchedule;
    }

    /**
     * @param fromSchedule the fromSchedule to set
     */
    public void setFromSchedule(boolean fromSchedule) {
        this.searchPanel.setVisible(!fromSchedule);
        this.fromSchedule = fromSchedule;
    }

    /**
     * @param myParent the myParent to set
     */
    public void setMyParent(EventLoggerMainPanel myParent) {
        this.myParent = myParent;
    }

    protected class FetchScheduleData implements Runnable {

        private Employee employee;
        private Client client;
        private JComboBox combo;

        public FetchScheduleData(Employee employee, Client client, JComboBox combo) {
            this.employee = employee;
            this.client = client;
            this.combo = combo;
        }

        @Override
        public void run() {
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();

            startCal.add(Calendar.DAY_OF_YEAR, -1);
            endCal.add(Calendar.DAY_OF_YEAR, 1);

            try {
                ScheduleController schedController = new ScheduleController(getMyParent().getCompanyId());
                ClientController clientController = ClientController.getInstance(getMyParent().getCompanyId());
                ArrayList<ScheduleData> schedules = schedController.getSchedule(startCal.getTime(), endCal.getTime(), this.employee, this.client);
                combo.removeAllItems();
                combo.addItem("Do not associate event with a shift.");
                for (int s = 0; s < schedules.size(); s++) {
                    ScheduleData data = schedules.get(s);
                    if (getMyParent().getClientHash().get(data.getClientId()) == null) {
                        getMyParent().getClientHash().put(data.getClientId(), clientController.getClientById(data.getClientId()));
                    }
                    if (cli != null) {
                        data.setDisplayEmployee(true);
                    }
                    Client currClient = getMyParent().getClientHash().get(data.getClientId());
                    try {
                        Employee currEmployee = fetchEmployeeData(data.getEmployeeId());
                        data.setEmployee(currEmployee);
                    } catch (Exception exe) {
                    }
                    data.setClient(currClient);

                    combo.addItem(data);
                }
            } catch (Exception exe) {
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        followupGroup = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        searchPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        searchTxt = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        myTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        nameLbl = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        phoneLbl = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        cellLbl = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        addressLbl = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        personnelTypeCmb = new javax.swing.JComboBox();
        jPanel13 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        shiftCombo = new javax.swing.JComboBox();
        searchShiftsBtn = new javax.swing.JButton();
        jPanel14 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        eventNotesTxt = new javax.swing.JTextArea();
        jPanel15 = new javax.swing.JPanel();
        jPanel22 = new javax.swing.JPanel();
        addFollowupPersonnelBtn = new javax.swing.JButton();
        jPanel18 = new javax.swing.JPanel();
        jPanel43 = new javax.swing.JPanel();
        saveEventBtn = new javax.swing.JButton();
        followupPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        eventDataPanel = new javax.swing.JPanel();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        searchPanel.setMaximumSize(new java.awt.Dimension(280, 32767));
        searchPanel.setMinimumSize(new java.awt.Dimension(280, 0));
        searchPanel.setPreferredSize(new java.awt.Dimension(280, 505));
        searchPanel.setLayout(new javax.swing.BoxLayout(searchPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel2.setPreferredSize(new java.awt.Dimension(451, 35));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        searchTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                searchTxtKeyTyped(evt);
            }
        });
        jPanel2.add(searchTxt);

        searchPanel.add(jPanel2);

        jPanel7.setLayout(new javax.swing.BoxLayout(jPanel7, javax.swing.BoxLayout.Y_AXIS));

        myTable.setAutoCreateRowSorter(true);
        myTable.setModel(getTableModel());
        myTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(myTable);

        jPanel7.add(jScrollPane2);

        searchPanel.add(jPanel7);

        jPanel1.add(searchPanel);

        jPanel4.setLayout(new javax.swing.BoxLayout(jPanel4, javax.swing.BoxLayout.Y_AXIS));

        jPanel3.setLayout(new javax.swing.BoxLayout(jPanel3, javax.swing.BoxLayout.Y_AXIS));

        jPanel9.setMaximumSize(new java.awt.Dimension(300000, 28));
        jPanel9.setLayout(new javax.swing.BoxLayout(jPanel9, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText(getNameLabel());
        jLabel1.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel9.add(jLabel1);

        nameLbl.setMaximumSize(new java.awt.Dimension(180, 28));
        nameLbl.setMinimumSize(new java.awt.Dimension(120, 28));
        jPanel9.add(nameLbl);

        jLabel2.setText("Phone");
        jLabel2.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        jLabel2.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel9.add(jLabel2);

        phoneLbl.setMaximumSize(new java.awt.Dimension(180, 28));
        phoneLbl.setMinimumSize(new java.awt.Dimension(120, 28));
        jPanel9.add(phoneLbl);

        jPanel3.add(jPanel9);

        jPanel16.setMaximumSize(new java.awt.Dimension(90096, 28));
        jPanel16.setMinimumSize(new java.awt.Dimension(216, 28));
        jPanel16.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.LINE_AXIS));

        jLabel3.setText("Cell");
        jLabel3.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        jLabel3.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel3.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel3.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel16.add(jLabel3);

        cellLbl.setMaximumSize(new java.awt.Dimension(180, 28));
        cellLbl.setMinimumSize(new java.awt.Dimension(120, 28));
        cellLbl.setPreferredSize(new java.awt.Dimension(120, 28));
        jPanel16.add(cellLbl);

        jLabel4.setText("Address");
        jLabel4.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        jLabel4.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel4.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel4.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel16.add(jLabel4);

        addressLbl.setMaximumSize(new java.awt.Dimension(180, 28));
        addressLbl.setMinimumSize(new java.awt.Dimension(120, 28));
        jPanel16.add(addressLbl);

        jPanel3.add(jPanel16);

        jPanel4.add(jPanel3);

        jPanel8.setMaximumSize(new java.awt.Dimension(32767, 70));
        jPanel8.setMinimumSize(new java.awt.Dimension(0, 70));
        jPanel8.setPreferredSize(new java.awt.Dimension(612, 70));
        jPanel8.setLayout(new javax.swing.BoxLayout(jPanel8, javax.swing.BoxLayout.LINE_AXIS));

        jPanel17.setMaximumSize(new java.awt.Dimension(32767, 60));
        jPanel17.setMinimumSize(new java.awt.Dimension(172, 60));
        jPanel17.setLayout(new java.awt.GridLayout(2, 0));

        jPanel11.setLayout(new javax.swing.BoxLayout(jPanel11, javax.swing.BoxLayout.LINE_AXIS));

        jLabel5.setText("Description");
        jLabel5.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        jLabel5.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel5.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel5.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel11.add(jLabel5);

        personnelTypeCmb.setModel(eventComboModel);
        personnelTypeCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                personnelTypeCmbActionPerformed(evt);
            }
        });
        jPanel11.add(personnelTypeCmb);

        jPanel17.add(jPanel11);

        jPanel13.setLayout(new javax.swing.BoxLayout(jPanel13, javax.swing.BoxLayout.LINE_AXIS));

        jLabel6.setText("Search for Shift");
        jLabel6.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 4));
        jLabel6.setMaximumSize(new java.awt.Dimension(96, 16));
        jLabel6.setMinimumSize(new java.awt.Dimension(96, 16));
        jLabel6.setPreferredSize(new java.awt.Dimension(96, 16));
        jPanel13.add(jLabel6);

        jPanel13.add(shiftCombo);

        searchShiftsBtn.setText("...");
        searchShiftsBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchShiftsBtnActionPerformed(evt);
            }
        });
        jPanel13.add(searchShiftsBtn);

        jPanel17.add(jPanel13);

        jPanel8.add(jPanel17);

        jPanel4.add(jPanel8);

        jPanel14.setPreferredSize(new java.awt.Dimension(60, 138));
        jPanel14.setLayout(new javax.swing.BoxLayout(jPanel14, javax.swing.BoxLayout.LINE_AXIS));

        jPanel23.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 0, 1, 1));
        jPanel23.setMaximumSize(new java.awt.Dimension(96, 32767));
        jPanel23.setPreferredSize(new java.awt.Dimension(96, 138));
        jPanel23.setLayout(new javax.swing.BoxLayout(jPanel23, javax.swing.BoxLayout.Y_AXIS));

        jLabel8.setText("Notes");
        jPanel23.add(jLabel8);

        jPanel14.add(jPanel23);

        eventNotesTxt.setColumns(20);
        eventNotesTxt.setLineWrap(true);
        eventNotesTxt.setRows(5);
        eventNotesTxt.setWrapStyleWord(true);
        eventNotesTxt.setMaximumSize(new java.awt.Dimension(80, 2147483647));
        eventNotesTxt.setPreferredSize(new java.awt.Dimension(80, 94));
        jScrollPane3.setViewportView(eventNotesTxt);

        jPanel14.add(jScrollPane3);

        jPanel4.add(jPanel14);

        jPanel15.setMaximumSize(new java.awt.Dimension(32767, 28));
        jPanel15.setMinimumSize(new java.awt.Dimension(409, 35));
        jPanel15.setPreferredSize(new java.awt.Dimension(600, 28));
        jPanel15.setLayout(new javax.swing.BoxLayout(jPanel15, javax.swing.BoxLayout.LINE_AXIS));

        jPanel22.setMaximumSize(new java.awt.Dimension(90000, 32767));
        jPanel22.setMinimumSize(new java.awt.Dimension(96, 100));
        jPanel22.setPreferredSize(new java.awt.Dimension(96, 20));

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 387, Short.MAX_VALUE)
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel22);

        addFollowupPersonnelBtn.setText("Add Followup");
        addFollowupPersonnelBtn.setEnabled(false);
        addFollowupPersonnelBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFollowupPersonnelBtnActionPerformed(evt);
            }
        });
        jPanel15.add(addFollowupPersonnelBtn);

        jPanel18.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel18.setPreferredSize(new java.awt.Dimension(5, 75));

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel18);

        jPanel43.setMaximumSize(new java.awt.Dimension(5, 32767));
        jPanel43.setPreferredSize(new java.awt.Dimension(5, 75));

        javax.swing.GroupLayout jPanel43Layout = new javax.swing.GroupLayout(jPanel43);
        jPanel43.setLayout(jPanel43Layout);
        jPanel43Layout.setHorizontalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 5, Short.MAX_VALUE)
        );
        jPanel43Layout.setVerticalGroup(
            jPanel43Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 33, Short.MAX_VALUE)
        );

        jPanel15.add(jPanel43);

        saveEventBtn.setText("Save Event");
        saveEventBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveEventBtnActionPerformed(evt);
            }
        });
        jPanel15.add(saveEventBtn);

        jPanel4.add(jPanel15);

        followupPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Followups"));
        followupPanel.setMaximumSize(new java.awt.Dimension(32767, 260));
        followupPanel.setMinimumSize(new java.awt.Dimension(0, 260));
        followupPanel.setPreferredSize(new java.awt.Dimension(674, 260));
        followupPanel.setLayout(new java.awt.GridLayout(1, 0));

        eventDataPanel.setLayout(new javax.swing.BoxLayout(eventDataPanel, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane4.setViewportView(eventDataPanel);

        followupPanel.add(jScrollPane4);

        jPanel4.add(followupPanel);

        jPanel1.add(jPanel4);

        add(jPanel1);
    }// </editor-fold>//GEN-END:initComponents

    private void searchTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_searchTxtKeyTyped
        if (searchTxt.getText().length() > 3) {
            ArrayList<Integer> myBranches = this.myParent.getBranches();

            this.loadSearchResults(searchTxt.getText() + evt.getKeyChar(), myBranches);
        }
    }//GEN-LAST:event_searchTxtKeyTyped

    private void myTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myTableMouseClicked

    }//GEN-LAST:event_myTableMouseClicked

    private void personnelTypeCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_personnelTypeCmbActionPerformed
        String selectedType = ((EventType) personnelTypeCmb.getSelectedItem()).getEvent();
        if (selectedType.equals("Call Off") || selectedType.equals("No Call No Show") || selectedType.equals("Post Abandonment")
                || selectedType.equals("Sleeping") || selectedType.equals("Late Checkin")) {

        }
    }//GEN-LAST:event_personnelTypeCmbActionPerformed

    private void searchShiftsBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchShiftsBtnActionPerformed
        Integer empId = null;
        try {
            empId = emp.getEmployeeId();
        } catch (Exception exe) {
        }

        Integer cliId = null;
        try {
            cliId = cli.getClientId();
        } catch (Exception exe) {
        }
        ArrayList<Integer> branches = this.myParent.getBranches();

        EventSearchShiftsDialog searchDialog = new EventSearchShiftsDialog(Main_Window.parentOfApplication, true, myParent.getCompanyId(), empId, cliId, branches);
        searchDialog.setVisible(true);

        try {
            ScheduleData sched = searchDialog.getScheduleData();
            Client currClient = myParent.getClientHash().get(sched.getClientId());
            if (currClient == null) {
                ClientController clientController = ClientController.getInstance(myParent.getCompanyId());
                currClient = clientController.getClientById(sched.getClientId());
            }
            sched.setClient(currClient);
            boolean contains = false;
            for (int c = 0; c < shiftCombo.getItemCount(); c++) {
                try {
                    contains = contains || ((ScheduleData) shiftCombo.getItemAt(c)).getScheduleId().equals(sched.getScheduleId());
                } catch (Exception exe) {
                }
            }
            if (!contains) {
                shiftCombo.addItem(sched);
            }
            shiftCombo.setSelectedItem(sched);
        } catch (Exception exe) {
        }
    }//GEN-LAST:event_searchShiftsBtnActionPerformed

    private void addFollowupPersonnelBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFollowupPersonnelBtnActionPerformed
        try {
            AddEventFollowupDialog dialog = new AddEventFollowupDialog(Main_Window.parentOfApplication, true, myParent.getCompanyId(), selectedEvent.getEventId());
            dialog.setVisible(true);
            EventFollowup followup = dialog.getEventFollowup();
            followup.setFollowupProcessedOn(new Date());
            EventController eController = EventController.getInstance(myParent.getCompanyId());
            eController.saveEventFollowup(followup);
            this.loadEvent(selectedEvent);
        } catch (Exception exe) {}
    }//GEN-LAST:event_addFollowupPersonnelBtnActionPerformed

    private void saveEventBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveEventBtnActionPerformed
        try {
            this.saveEvent();
        } catch (Exception exe) {
            exe.printStackTrace();
        }
    }//GEN-LAST:event_saveEventBtnActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    protected javax.swing.JButton addFollowupPersonnelBtn;
    protected javax.swing.JLabel addressLbl;
    protected javax.swing.JLabel cellLbl;
    private javax.swing.JPanel eventDataPanel;
    protected javax.swing.JTextArea eventNotesTxt;
    private javax.swing.ButtonGroup followupGroup;
    protected javax.swing.JPanel followupPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel43;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    protected javax.swing.JTable myTable;
    protected javax.swing.JLabel nameLbl;
    private javax.swing.JComboBox personnelTypeCmb;
    protected javax.swing.JLabel phoneLbl;
    private javax.swing.JButton saveEventBtn;
    private javax.swing.JPanel searchPanel;
    private javax.swing.JButton searchShiftsBtn;
    private javax.swing.JTextField searchTxt;
    protected javax.swing.JComboBox shiftCombo;
    // End of variables declaration//GEN-END:variables
}
