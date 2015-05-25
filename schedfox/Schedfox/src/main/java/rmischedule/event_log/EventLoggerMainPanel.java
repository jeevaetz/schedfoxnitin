/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.event_log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.main.Main_Window;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.EventController;
import rmischeduleserver.control.IncidentReportController;
import rmischeduleserver.control.ScheduleController;
import rmischeduleserver.control.UserController;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.Company;
import schedfoxlib.model.Employee;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventType;
import schedfoxlib.model.IncidentReportType;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.User;

/**
 *
 * @author ira
 */
public class EventLoggerMainPanel extends javax.swing.JPanel implements CachedEventData {

    private String companyId;
    private ArrayList<Branch> branches;
    private HashMap<Integer, Branch> branchHash;
    private HashMap<Integer, EventType> eventTypeHash;
    private HashMap<Integer, Client> clientHash;
    private HashMap<Integer, Employee> employeeHash;
    private HashMap<String, ScheduleData> schedHash;
    private HashMap<Integer, User> userHash;
    private HashMap<Integer, JCheckBox> eventTypeChkHash;
    private SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy HH:mm");

    private EventTableModel eventTabelModel = new EventTableModel(this);

    private PersonnelEventPanel eventPersonnelPanel;
    private IncidentEventPanel eventIncidentPanel;

    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;
    private EventLoggerInternalFrame myParent;

    /**
     * Creates new form EventLoggerMainPanel
     */
    public EventLoggerMainPanel(String companyId, EventLoggerInternalFrame eventPanel) {
        initComponents();

        this.companyId = companyId;
        eventTable.setRowHeight(35);
        this.myParent = eventPanel;

        begCal = new JCalendarComboBox();
        endCal = new JCalendarComboBox();

        begCal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                reloadData();
            }
        });

        endCal.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                reloadData();
            }
        });

        startPanel.add(begCal);
        endPanel.add(endCal);

        Calendar startCal = Calendar.getInstance();
        startCal.add(Calendar.DAY_OF_MONTH, -2);
        begCal.setCalendar(startCal);

        try {
            CompanyController compController = new CompanyController();
            branches = compController.getBranchesForCompany(Integer.parseInt(companyId));
            branchHash = new HashMap<Integer, Branch>();
            clientHash = new HashMap<Integer, Client>();
            userHash = new HashMap<Integer, User>();
            schedHash = new HashMap<String, ScheduleData>();
            eventTypeHash = new HashMap<Integer, EventType>();
            employeeHash = new HashMap<Integer, Employee>();
            for (int b = 0; b < branches.size(); b++) {
                branchHash.put(branches.get(b).getBranchId(), branches.get(b));
            }
        } catch (Exception exe) {
        }

        eventPersonnelPanel = new PersonnelEventPanel(this);
        mainTabbedPanel.addTab("Personnel Event", eventPersonnelPanel);

        eventIncidentPanel = new IncidentEventPanel(this);
        mainTabbedPanel.addTab("Incident Event", eventIncidentPanel);

        mainTabbedPanel.addTab("Follow Up", eventsFollowupPanel);

        this.loadEventTypes();
        reloadData();

        eventTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        eventTable.getColumnModel().getColumn(0).setMinWidth(80);
        eventTable.getColumnModel().getColumn(0).setMaxWidth(80);

        eventTable.getColumnModel().getColumn(1).setPreferredWidth(110);
        eventTable.getColumnModel().getColumn(1).setMinWidth(110);
        eventTable.getColumnModel().getColumn(1).setMaxWidth(110);

        eventTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        eventTable.getColumnModel().getColumn(4).setMinWidth(100);
        eventTable.getColumnModel().getColumn(4).setMaxWidth(100);

        eventTable.getColumnModel().getColumn(5).setPreferredWidth(120);
        eventTable.getColumnModel().getColumn(5).setMinWidth(120);
        eventTable.getColumnModel().getColumn(5).setMaxWidth(120);

        eventTable.getColumnModel().getColumn(6).setPreferredWidth(70);
        eventTable.getColumnModel().getColumn(6).setMinWidth(70);
        eventTable.getColumnModel().getColumn(6).setMaxWidth(70);

        eventTable.getColumnModel().getColumn(7).setPreferredWidth(45);
        eventTable.getColumnModel().getColumn(7).setMinWidth(45);
        eventTable.getColumnModel().getColumn(7).setMaxWidth(45);

        eventTable.getColumnModel().getColumn(8).setPreferredWidth(45);
        eventTable.getColumnModel().getColumn(8).setMinWidth(45);
        eventTable.getColumnModel().getColumn(8).setMaxWidth(45);

        eventTable.getColumnModel().getColumn(9).setPreferredWidth(45);
        eventTable.getColumnModel().getColumn(9).setMinWidth(45);
        eventTable.getColumnModel().getColumn(9).setMaxWidth(45);

        final String localCompanyId = companyId;

        eventTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = eventTable.getColumnModel().getColumnIndexAtX(e.getX());
                int row = e.getY() / eventTable.getRowHeight();
                if (row < eventTable.getRowCount() && row >= 0) {
                    Event event = eventTabelModel.getEventAt(eventTable.convertRowIndexToModel(row));
                    if (column == 9) {
                        super.mouseClicked(e);
                    } else if (e.getClickCount() > 1) {
                        if (event.getEventTypeId() > 0) {
                            eventPersonnelPanel.loadEvent(event);
                        } else {
                            eventIncidentPanel.loadEvent(event);
                        }
                    }
                }
            }
        });
    }

    private void loadEventTypes() {
        try {
            eventTypeChkHash = new HashMap<Integer, JCheckBox>();
            checkPanel.removeAll();
            if (eventViewCmb.getSelectedIndex() == 0) {
                EventController eventController = EventController.getInstance(companyId);
                ArrayList<EventType> types = eventController.getEventTypes(1);
                for (int t = 0; t < types.size(); t++) {
                    JCheckBox eventTypeCheck = new JCheckBox(types.get(t).getEvent());
                    eventTypeCheck.setSelected(true);
                    eventTypeCheck.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            reloadData();
                        }
                    });
                    checkPanel.add(eventTypeCheck);
                    eventTypeChkHash.put(types.get(t).getEventTypeId(), eventTypeCheck);
                }
            } else {
                IncidentReportController incidentController = new IncidentReportController(companyId);
                ArrayList<IncidentReportType> types = incidentController.getIncidentReportTypes();
                for (int t = 0; t < types.size(); t++) {
                    JCheckBox eventTypeCheck = new JCheckBox(types.get(t).getReportType());
                    eventTypeCheck.setSelected(true);
                    eventTypeCheck.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            reloadData();
                        }
                    });
                    checkPanel.add(eventTypeCheck);
                    eventTypeChkHash.put(types.get(t).getIncidentReportTypeId() * -1, eventTypeCheck);
                }
            }
        } catch (Exception exe) {
        }
    }

    public void loadDataForEmployee(Integer employeeId, String scheduleId) {
        try {
            EmployeeController employeeController = EmployeeController.getInstance(this.companyId);
            Employee emp = employeeController.getEmployeeById(employeeId);
            eventPersonnelPanel.loadEmployeeData(emp, Boolean.FALSE);
            eventPersonnelPanel.loadSchedData(scheduleId);

            this.eventPersonnelPanel.setFromSchedule(true);
            this.eventIncidentPanel.setFromSchedule(true);
        } catch (Exception exe) {
        }
    }

    public HashMap<Integer, Branch> getCachedBranches() {
        return branchHash;
    }

    public JTabbedPane getTabbedPane() {
        return this.mainTabbedPanel;
    }

    @Override
    public Client fetchClientData(Integer clientId) {
        Client retVal = null;
        if (clientId != null && clientId != 0) {
            if (clientHash.get(clientId) == null) {
                try {
                    ClientController clientController = ClientController.getInstance(companyId);
                    retVal = clientController.getClientById(clientId);
                    clientHash.put(clientId, retVal);
                } catch (Exception exe) {
                }
            }
        } else {
            return new Client();
        }
        return clientHash.get(clientId);
    }

    @Override
    public Employee fetchEmployeeData(Integer employeeId) {
        Employee retVal = null;
        if (employeeId != null && employeeId != 0) {
            if (employeeHash.get(employeeId) == null) {
                try {
                    EmployeeController empController = EmployeeController.getInstance(companyId);
                    retVal = empController.getEmployeeById(employeeId);
                    employeeHash.put(employeeId, retVal);
                } catch (Exception exe) {
                }
            }
        } else {
            return new Employee();
        }
        return employeeHash.get(employeeId);
    }

    @Override
    public User fetchUserData(Integer userId) {
        User retVal = null;
        if (userId != null && userId != 0) {
            if (userHash.get(userId) == null) {
                try {
                    UserController userController = new UserController(companyId);
                    retVal = userController.getUserById(userId);
                    userHash.put(userId, retVal);
                } catch (Exception exe) {
                }
            }
        } else {
            return new User();
        }
        return userHash.get(userId);
    }

    /**
     * @return the companyId
     */
    public String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    /**
     * @return the clientHash
     */
    public HashMap<Integer, Client> getClientHash() {
        return clientHash;
    }

    /**
     * @param clientHash the clientHash to set
     */
    public void setClientHash(HashMap<Integer, Client> clientHash) {
        this.clientHash = clientHash;
    }

    /**
     * @return the employeeHash
     */
    public HashMap<Integer, Employee> getEmployeeHash() {
        return employeeHash;
    }

    /**
     * @param employeeHash the employeeHash to set
     */
    public void setEmployeeHash(HashMap<Integer, Employee> employeeHash) {
        this.employeeHash = employeeHash;
    }

    /**
     * @return the schedHash
     */
    public HashMap<String, ScheduleData> getSchedHash() {
        return schedHash;
    }

    /**
     * @param schedHash the schedHash to set
     */
    public void setSchedHash(HashMap<String, ScheduleData> schedHash) {
        this.schedHash = schedHash;
    }

    /**
     * @return the eventTypeHash
     */
    public HashMap<Integer, EventType> getEventTypeHash() {
        return eventTypeHash;
    }

    @Override
    public Branch fetchBranchData(Integer branchId) {
        return this.branchHash.get(branchId);
    }

    @Override
    public EventType fetchEventTypeData(Integer eventType) {
        return this.eventTypeHash.get(eventType);
    }

    /**
     * @param eventTypeHash the eventTypeHash to set
     */
    public void setEventTypeHash(HashMap<Integer, EventType> eventTypeHash) {
        this.eventTypeHash = eventTypeHash;
    }

    /**
     * @return the myFormat
     */
    public SimpleDateFormat getMyFormat() {
        return myFormat;
    }

    /**
     * @param myFormat the myFormat to set
     */
    public void setMyFormat(SimpleDateFormat myFormat) {
        this.myFormat = myFormat;
    }

    /**
     * @return the branchHash
     */
    public HashMap<Integer, Branch> getBranchHash() {
        return branchHash;
    }

    /**
     * @param branchHash the branchHash to set
     */
    public void setBranchHash(HashMap<Integer, Branch> branchHash) {
        this.branchHash = branchHash;
    }

    public ScheduleData fetchSchedData(String scheduleId) {
        ScheduleData retVal = null;
        if (scheduleId != null && scheduleId.length() > 0) {
            if (getSchedHash().get(scheduleId) == null) {
                ScheduleController schedController = new ScheduleController(getCompanyId());
                if (scheduleId.indexOf("/") > -1) {
                    try {
                        Date shiftDate = dbFormat.parse(scheduleId.substring(scheduleId.indexOf("/") + 1));
                        retVal = schedController.getScheduleByIdentifier(Integer.parseInt(scheduleId.substring(0, scheduleId.indexOf("/"))));
                        if (retVal == null) {
                            retVal = new ScheduleData();
                        }
                        retVal.setDate(shiftDate);
                        getSchedHash().put(scheduleId, retVal);
                    } catch (Exception exe) {
                    }
                } else {
                    try {
                        retVal = schedController.getScheduleByIdentifier(Integer.parseInt(scheduleId));
                        if (retVal == null) {
                            retVal = new ScheduleData();
                        }
                        getSchedHash().put(scheduleId, retVal);
                    } catch (Exception exe) {
                    }
                }
            }
        } else {
            return new ScheduleData();
        }
        return getSchedHash().get(scheduleId);
    }

    public ArrayList<Integer> getBranches() {
        ArrayList<Integer> myBranches = new ArrayList<Integer>();

        Vector<Company> companies = Main_Window.getActiveListOfCompanies();
        for (int c = 0; c < companies.size(); c++) {
            if (companies.get(c).getCompId().equals(this.companyId)) {
                Vector<Branch> branches = companies.get(c).getBranches();
                for (int b = 0; b < branches.size(); b++) {
                    myBranches.add(branches.get(b).getBranchId());
                }
            }
        }
        return myBranches;
    }

    public void reloadData() {
        try {
            Calendar startCal = this.begCal.getCalendar();
            Calendar endCal = this.endCal.getCalendar();

            Iterator<Integer> keys = eventTypeChkHash.keySet().iterator();
            ArrayList<Integer> selectedTypes = new ArrayList<Integer>();
            while (keys.hasNext()) {
                Integer key = keys.next();
                if (eventTypeChkHash.get(key).isSelected()) {
                    selectedTypes.add(key);
                }
            }

            Vector<Company> companies = Main_Window.getActiveListOfCompanies();
            ArrayList<Integer> branchAccess = new ArrayList<Integer>();
            for (int c = 0; c < companies.size(); c++) {
                if (companies.get(c).getId().equals(companyId)) {
                    Vector<Branch> branches = companies.get(c).getBranches();
                    for (int b = 0; b < branches.size(); b++) {
                        branchAccess.add(branches.get(b).getBranchId());
                    }
                }
            }

            EventController eventController = EventController.getInstance(companyId);
            ArrayList<Event> events = eventController.getEventsWithNoFollowup(branchAccess, false, startCal.getTime(), endCal.getTime(), selectedTypes);

            ArrayList<Integer> clientIds = new ArrayList<Integer>();
            try {
                for (int e = 0; e < events.size(); e++) {
                    clientIds.add(events.get(e).getClientId());
                }
                ClientController clientController = ClientController.getInstance(companyId);
                ArrayList<Client> clients = clientController.getClientsById(clientIds);
                for (int c = 0; c < clients.size(); c++) {
                    clientHash.put(clients.get(c).getClientId(), clients.get(c));
                }
            } catch (Exception exe) {
            }

            ArrayList<Integer> employeeIds = new ArrayList<Integer>();
            try {
                for (int e = 0; e < events.size(); e++) {
                    employeeIds.add(events.get(e).getEmployeeId());
                }
                EmployeeController employeeController = EmployeeController.getInstance(companyId);
                ArrayList<Employee> employees = employeeController.getEmployeesById(employeeIds);
                for (int c = 0; c < employees.size(); c++) {
                    employeeHash.put(employees.get(c).getEmployeeId(), employees.get(c));
                }
            } catch (Exception exe) {
            }

            ArrayList<Integer> scheduleIds = new ArrayList<Integer>();
            try {
                for (int e = 0; e < events.size(); e++) {
                    try {
                        String scheduleId = events.get(e).getShiftId();
                        if (scheduleId.indexOf("/") > -1) {
                            scheduleIds.add(Integer.parseInt(scheduleId.substring(0, scheduleId.indexOf("/"))));
                        } else {
                            scheduleIds.add(Integer.parseInt(scheduleId));
                        }
                    } catch (Exception exe) {
                    }
                }
                ScheduleController scheduleController = new ScheduleController(companyId);
                ArrayList<ScheduleData> schedules = scheduleController.getSchedulesByIdentifiers(scheduleIds);
                for (int c = 0; c < schedules.size(); c++) {
                    schedHash.put(schedules.get(c).getScheduleId(), schedules.get(c));
                }
            } catch (Exception exe) {
            }

            mainTabbedPanel.setTitleAt(2, "Event Reports (" + events.size() + ")");

            eventTabelModel.setEvents(events);
        } catch (Exception exe) {
            exe.printStackTrace();
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

        followUpGroup = new javax.swing.ButtonGroup();
        followUpClientGroup = new javax.swing.ButtonGroup();
        eventsFollowupPanel = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        eventViewCmb = new javax.swing.JComboBox();
        jPanel1 = new javax.swing.JPanel();
        checkPanel = new javax.swing.JPanel();
        jPanel44 = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        endPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        selectUnselectChk = new javax.swing.JCheckBox();
        eventOrderCmb = new javax.swing.JComboBox();
        jPanel4 = new javax.swing.JPanel();
        printSelectedBtn = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        mainTabbedPanel = new javax.swing.JTabbedPane();

        eventsFollowupPanel.setLayout(new javax.swing.BoxLayout(eventsFollowupPanel, javax.swing.BoxLayout.Y_AXIS));

        jPanel16.setMaximumSize(new java.awt.Dimension(32767, 100));
        jPanel16.setPreferredSize(new java.awt.Dimension(954, 100));
        jPanel16.setLayout(new javax.swing.BoxLayout(jPanel16, javax.swing.BoxLayout.Y_AXIS));

        jPanel17.setMaximumSize(new java.awt.Dimension(23000, 32));
        jPanel17.setLayout(new javax.swing.BoxLayout(jPanel17, javax.swing.BoxLayout.LINE_AXIS));

        eventViewCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Display all personnel events", "Display all incident events" }));
        eventViewCmb.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eventViewCmbActionPerformed(evt);
            }
        });
        jPanel17.add(eventViewCmb);

        jPanel16.add(jPanel17);

        jPanel1.setMinimumSize(new java.awt.Dimension(200, 40));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.LINE_AXIS));

        checkPanel.setMaximumSize(new java.awt.Dimension(20000, 100));
        checkPanel.setMinimumSize(new java.awt.Dimension(10, 100));
        checkPanel.setPreferredSize(new java.awt.Dimension(10, 100));
        checkPanel.setLayout(new java.awt.GridLayout(0, 5, 2, 0));
        jPanel1.add(checkPanel);

        jPanel44.setMaximumSize(new java.awt.Dimension(200, 1000));
        jPanel44.setMinimumSize(new java.awt.Dimension(200, 40));
        jPanel44.setPreferredSize(new java.awt.Dimension(200, 40));
        jPanel44.setLayout(new java.awt.GridLayout(2, 0));

        startPanel.setLayout(new javax.swing.BoxLayout(startPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Start");
        jLabel1.setMaximumSize(new java.awt.Dimension(35, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(35, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(35, 16));
        startPanel.add(jLabel1);

        jPanel44.add(startPanel);

        endPanel.setLayout(new javax.swing.BoxLayout(endPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("End");
        jLabel2.setMaximumSize(new java.awt.Dimension(35, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(35, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(35, 16));
        endPanel.add(jLabel2);

        jPanel44.add(endPanel);

        jPanel1.add(jPanel44);

        jPanel16.add(jPanel1);

        eventsFollowupPanel.add(jPanel16);

        eventTable.setAutoCreateRowSorter(true);
        eventTable.setModel(eventTabelModel);
        eventTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                eventTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(eventTable);

        eventsFollowupPanel.add(jScrollPane1);

        jPanel2.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel2.setMinimumSize(new java.awt.Dimension(100, 35));
        jPanel2.setPreferredSize(new java.awt.Dimension(954, 35));
        jPanel2.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        selectUnselectChk.setText("Select / Unselect All Events");
        selectUnselectChk.setMaximumSize(new java.awt.Dimension(220, 25));
        selectUnselectChk.setMinimumSize(new java.awt.Dimension(220, 25));
        selectUnselectChk.setPreferredSize(new java.awt.Dimension(220, 25));
        selectUnselectChk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                selectUnselectChkActionPerformed(evt);
            }
        });
        jPanel2.add(selectUnselectChk);

        eventOrderCmb.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Print with most recent events on top", "Print with most recent events on bottom" }));
        jPanel2.add(eventOrderCmb);

        jPanel4.setMaximumSize(new java.awt.Dimension(10, 15));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 100));
        jPanel4.setPreferredSize(new java.awt.Dimension(10, 5));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 10, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jPanel2.add(jPanel4);

        printSelectedBtn.setText("Print Selected Events");
        printSelectedBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedBtnActionPerformed(evt);
            }
        });
        jPanel2.add(printSelectedBtn);

        eventsFollowupPanel.add(jPanel2);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 954, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 748, Short.MAX_VALUE)
        );

        setMinimumSize(new java.awt.Dimension(750, 512));
        setLayout(new java.awt.GridLayout(1, 0));
        add(mainTabbedPanel);
    }// </editor-fold>//GEN-END:initComponents

    private void eventTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_eventTableMouseClicked

    }//GEN-LAST:event_eventTableMouseClicked

    private void eventViewCmbActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eventViewCmbActionPerformed
        this.loadEventTypes();
        this.reloadData();
    }//GEN-LAST:event_eventViewCmbActionPerformed

    private void printSelectedBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedBtnActionPerformed
        ArrayList<Event> selectedEvents = eventTabelModel.getSelectedEvents();
        if (selectedEvents.isEmpty()) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Select Events To Print!", "No Events Selected!", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int confirm = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Do you want to include followups in the report?", "Include Followup", JOptionPane.YES_NO_OPTION);

                CompanyController compController = CompanyController.getInstance();
                Company myCompany = compController.getCompanyById(Integer.parseInt(this.companyId));

                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/event_report.jasper");

                ArrayList<Integer> selectedEventInts = new ArrayList<Integer>();
                for (int s = 0; s < selectedEvents.size(); s++) {
                    selectedEventInts.add(selectedEvents.get(s).getEventId());
                }

                Hashtable parameters = new Hashtable();
                parameters.put("schema", myCompany.getDB());
                parameters.put("events", selectedEventInts);
                parameters.put("showFollowup", confirm == JOptionPane.YES_OPTION);
                
                if (eventOrderCmb.getSelectedIndex() == 0) {
                    parameters.put("orderby", "DESC");
                } else {
                    parameters.put("orderby", "ASC");
                }
                
                java.sql.Connection myConnect = RMIScheduleServerImpl.getConnection().generateConnection();
                try {
                    PreparedStatement pstmt = myConnect.prepareStatement("SET search_path='" + myCompany.getDB().replaceAll("'", "''") + "'");
                    pstmt.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, myConnect);
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
    }//GEN-LAST:event_printSelectedBtnActionPerformed

    private void selectUnselectChkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_selectUnselectChkActionPerformed
        try {
            eventTabelModel.selectDeselectAll(selectUnselectChk.isSelected());
        } catch (Exception exe) {
        }
    }//GEN-LAST:event_selectUnselectChkActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel checkPanel;
    private javax.swing.JPanel endPanel;
    private javax.swing.JComboBox eventOrderCmb;
    private javax.swing.JTable eventTable;
    private javax.swing.JComboBox eventViewCmb;
    private javax.swing.JPanel eventsFollowupPanel;
    private javax.swing.ButtonGroup followUpClientGroup;
    private javax.swing.ButtonGroup followUpGroup;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel44;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTabbedPane mainTabbedPanel;
    private javax.swing.JButton printSelectedBtn;
    private javax.swing.JCheckBox selectUnselectChk;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the myParent
     */
    public EventLoggerInternalFrame getMyParent() {
        return myParent;
    }

    /**
     * @param myParent the myParent to set
     */
    public void setMyParent(EventLoggerInternalFrame myParent) {
        this.myParent = myParent;
    }

}
