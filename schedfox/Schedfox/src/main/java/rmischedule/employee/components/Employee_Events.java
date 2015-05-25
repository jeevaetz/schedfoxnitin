/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rmischedule.employee.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.InputStream;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import rmischedule.components.graphicalcomponents.GenericEditSubForm;
import rmischedule.components.jcalendar.JCalendarComboBox;
import rmischedule.employee.xEmployeeEdit;
import rmischedule.event_log.CachedEventData;
import rmischedule.event_log.PersonnelEventTableModel;
import rmischedule.ireports.viewer.IReportViewer;
import rmischedule.main.Main_Window;
import static rmischedule.schedule.Schedule_Main_Form.branchId;
import rmischeduleserver.RMIScheduleServerImpl;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.CompanyController;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.EventController;
import rmischeduleserver.control.ScheduleController;
import rmischeduleserver.control.UserController;
import rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat;
import rmischeduleserver.mysqlconnectivity.queries.event.get_events_for_employee_query;
import schedfoxlib.model.Branch;
import schedfoxlib.model.Client;
import schedfoxlib.model.Company;
import schedfoxlib.model.Employee;
import schedfoxlib.model.Event;
import schedfoxlib.model.EventType;
import schedfoxlib.model.ScheduleData;
import schedfoxlib.model.User;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author ira
 */
public class Employee_Events extends GenericEditSubForm implements CachedEventData {

    private PersonnelEventTableModel eventTabelModel = new PersonnelEventTableModel(this);
    private SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yy");
    private SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd");

    private ArrayList<Branch> branches;
    private HashMap<Integer, Branch> branchHash;
    private HashMap<Integer, EventType> eventTypeHash;
    private HashMap<Integer, Client> clientHash;
    private HashMap<Integer, Employee> employeeHash;
    private HashMap<String, ScheduleData> schedHash;
    private HashMap<Integer, User> userHash;
    private HashMap<Integer, JCheckBox> eventTypeChkHash;

    private JCalendarComboBox begCal;
    private JCalendarComboBox endCal;

    private xEmployeeEdit myParent;

    /**
     * Creates new form Employee_Events
     */
    public Employee_Events(xEmployeeEdit myParent) {
        initComponents();

        this.myParent = myParent;

        eventTypeChkHash = new HashMap<Integer, JCheckBox>();
        branchHash = new HashMap<Integer, Branch>();
        eventTypeHash = new HashMap<Integer, EventType>();
        clientHash = new HashMap<Integer, Client>();
        employeeHash = new HashMap<Integer, Employee>();
        schedHash = new HashMap<String, ScheduleData>();
        userHash = new HashMap<Integer, User>();

        try {
            EventController eventController = EventController.getInstance(myParent.cpny);
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
                eventTypeHash.put(types.get(t).getEventTypeId(), types.get(t));
                eventTypeChkHash.put(types.get(t).getEventTypeId(), eventTypeCheck);
            }
        } catch (Exception exe) {
        }

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
    }

    @Override
    public GeneralQueryFormat getQuery(boolean isSelected) {
        get_events_for_employee_query eventsQuery = new get_events_for_employee_query();
        Iterator<Integer> keys = eventTypeChkHash.keySet().iterator();
        ArrayList<Integer> selectedKeys = new ArrayList<Integer>();
        while (keys.hasNext()) {
            Integer key = keys.next();
            if (eventTypeChkHash.get(key).isSelected()) {
                selectedKeys.add(key);
            }
        }
        eventsQuery.update(selectedKeys, this.dbFormat.format(begCal.getCalendar().getTime()), this.dbFormat.format(endCal.getCalendar().getTime()), ((Employee) myParent.getSelectedObject()).getEmployeeId());
        return eventsQuery;
    }

    @Override
    public GeneralQueryFormat getSaveQuery(boolean isNewData) {
        return null;
    }

    @Override
    public void loadData(Record_Set rs) {
        ArrayList<Event> events = new ArrayList<Event>();
        for (int r = 0; r < rs.length(); r++) {
            events.add(new Event(rs));
            rs.moveNext();
        }
        eventTabelModel.setEvents(events);
    }

    @Override
    public boolean needsMoreRecordSets() {
        return false;
    }

    @Override
    public String getMyTabTitle() {
        return "Personnel Events";
    }

    @Override
    public JPanel getMyForm() {
        return this;
    }

    @Override
    public void doOnClear() {

    }

    @Override
    public boolean userHasAccess() {
        return true;
    }

    @Override
    public Employee fetchEmployeeData(Integer employeeId) {
        Employee retVal = null;
        if (employeeId != null && employeeId != 0) {
            if (employeeHash.get(employeeId) == null) {
                try {
                    EmployeeController empController = EmployeeController.getInstance(this.myparent.getConnection().myCompany);
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
    public Client fetchClientData(Integer clientId) {
        Client retVal = null;
        if (clientId != null && clientId != 0) {
            if (clientHash.get(clientId) == null) {
                try {
                    ClientController clientController = ClientController.getInstance(this.myparent.getConnection().myCompany);
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

    public ScheduleData fetchSchedData(String scheduleId) {
        ScheduleData retVal = null;
        if (scheduleId != null && scheduleId.length() > 0) {
            if (schedHash.get(scheduleId) == null) {
                ScheduleController schedController = new ScheduleController(this.myparent.getConnection().myCompany);
                if (scheduleId.indexOf("/") > -1) {
                    try {
                        Date shiftDate = dbFormat.parse(scheduleId.substring(scheduleId.indexOf("/") + 1));
                        retVal = schedController.getScheduleByIdentifier(Integer.parseInt(scheduleId.substring(0, scheduleId.indexOf("/"))));
                        if (retVal == null) {
                            retVal = new ScheduleData();
                        }
                        retVal.setDate(shiftDate);
                        schedHash.put(scheduleId, retVal);
                    } catch (Exception exe) {
                    }
                } else {
                    try {
                        retVal = schedController.getScheduleByIdentifier(Integer.parseInt(scheduleId));
                        if (retVal == null) {
                            retVal = new ScheduleData();
                        }
                        schedHash.put(scheduleId, retVal);
                    } catch (Exception exe) {
                    }
                }
            }
        } else {
            return new ScheduleData();
        }
        return schedHash.get(scheduleId);
    }

    @Override
    public User fetchUserData(Integer userId) {
        User retVal = null;
        if (userId != null && userId != 0) {
            if (userHash.get(userId) == null) {
                try {
                    UserController userController = new UserController(this.myparent.getConnection().myCompany);
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

    @Override
    public Branch fetchBranchData(Integer branchId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EventType fetchEventTypeData(Integer eventType) {
        return eventTypeHash.get(eventType);
    }

    @Override
    public SimpleDateFormat getMyFormat() {
        return myFormat;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        checkPanel = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        startPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        endPanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        eventTable = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        printSelectedEvents = new javax.swing.JButton();

        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        checkPanel.setPreferredSize(new java.awt.Dimension(200, 72));
        checkPanel.setLayout(new java.awt.GridLayout(0, 5, 2, 0));
        jPanel2.add(checkPanel);

        jPanel3.setMaximumSize(new java.awt.Dimension(200, 32767));
        jPanel3.setMinimumSize(new java.awt.Dimension(200, 10));
        jPanel3.setPreferredSize(new java.awt.Dimension(200, 100));
        jPanel3.setLayout(new java.awt.GridLayout(2, 1));

        startPanel.setLayout(new javax.swing.BoxLayout(startPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Start");
        jLabel1.setMaximumSize(new java.awt.Dimension(35, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(35, 16));
        jLabel1.setPreferredSize(new java.awt.Dimension(35, 16));
        startPanel.add(jLabel1);

        jPanel3.add(startPanel);

        endPanel.setLayout(new javax.swing.BoxLayout(endPanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("End");
        jLabel2.setMaximumSize(new java.awt.Dimension(35, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(35, 16));
        jLabel2.setPreferredSize(new java.awt.Dimension(35, 16));
        endPanel.add(jLabel2);

        jPanel3.add(endPanel);

        jPanel2.add(jPanel3);

        add(jPanel2);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        eventTable.setModel(eventTabelModel);
        jScrollPane1.setViewportView(eventTable);

        jPanel1.add(jScrollPane1);

        add(jPanel1);

        jPanel4.setMaximumSize(new java.awt.Dimension(32767, 35));
        jPanel4.setMinimumSize(new java.awt.Dimension(10, 35));
        jPanel4.setPreferredSize(new java.awt.Dimension(100, 35));
        jPanel4.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        printSelectedEvents.setText("Print Event Reports");
        printSelectedEvents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                printSelectedEventsActionPerformed(evt);
            }
        });
        jPanel4.add(printSelectedEvents);

        add(jPanel4);
    }// </editor-fold>//GEN-END:initComponents

    private void printSelectedEventsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_printSelectedEventsActionPerformed
        ArrayList<Event> selectedEvents = eventTabelModel.getSelectedEvents();
        if (selectedEvents.isEmpty()) {
            JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Select Events To Print!", "No Events Selected!", JOptionPane.ERROR_MESSAGE);
        } else {
            try {
                int confirm = JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Do you want to include followups in the report?", "Include Followup", JOptionPane.YES_NO_OPTION);
                
                CompanyController compController = CompanyController.getInstance();
                Company myCompany = compController.getCompanyById(Integer.parseInt(this.myParent.getConnection().myCompany));
                
                InputStream reportStream
                        = getClass().getResourceAsStream("/rmischedule/ireports/event_report.jasper");
                
                ArrayList<Integer> selectedEventInts = new ArrayList<Integer>();
                for (int s = 0; s < selectedEvents.size(); s++) {
                    selectedEventInts.add(selectedEvents.get(s).getEventId());
                }
                
                Hashtable parameters = new Hashtable();
                parameters.put("showFollowup", confirm == JOptionPane.YES_OPTION);
                parameters.put("schema", myCompany.getDB());
                parameters.put("events", selectedEventInts);
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
    }//GEN-LAST:event_printSelectedEventsActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel checkPanel;
    private javax.swing.JPanel endPanel;
    private javax.swing.JTable eventTable;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton printSelectedEvents;
    private javax.swing.JPanel startPanel;
    // End of variables declaration//GEN-END:variables

}
