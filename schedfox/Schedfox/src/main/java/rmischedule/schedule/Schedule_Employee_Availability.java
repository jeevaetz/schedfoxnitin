/*
 * Schedule_Employee_Availability.java
 *
 * Created on May 19, 2004, 9:37 AM
 */
package rmischedule.schedule;

import rmischedule.*;
import java.util.Hashtable;
import java.util.Vector;
import rmischedule.schedule.components.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import rmischedule.data_connection.Connection;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.main.Main_Window;
import rmischedule.schedule.components.AEmployee;
import rmischedule.schedule.components.AvailabilityComboBox;
import rmischedule.schedule.components.SEmployee;
import rmischedule.schedule.components.availability.AvailabilityFilteringOptions;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.employee.employee_list_types_query;

/**
 *
 * @author  jason.allen
 */
public class Schedule_Employee_Availability extends JPanel {

    private JPanel jpEmployees;
    private JSplitPane jsAvailable;
    public JPanel jpTrained;
    private JPanel jpUnTrained;
    private JTextField mySearchTextBox;
    public JPanel jpEmpTrained;
    private JPanel jpEmpUnTrained;
    private JPanel jpLblTrained;
    private JPanel jpLblUnTrained;
    private JTabbedPane mainAvailabilityTab;
    private JTextArea myAvailabilityStatusArea;
    private JPanel myEmployeeTypes;
    public JScrollPane js;
    private Hashtable myHash;
    public Schedule_View_Panel parent;
    public AvailabilityComboBox acb;
    private Vector<SEmployee> semp;
    private Hashtable<EmployeeType, JCheckBox> checkboxesOfTypes;
    private AvailabilityFilteringOptions myFilteringOptions;

    public Schedule_Employee_Availability(Schedule_View_Panel p) {
        parent = p;
        initComponents();

        employee_list_types_query listTypesQuery = new employee_list_types_query();
        Connection connection = new Connection();
        connection.setBranch(p.getBranch());
        connection.setCompany(p.getCompany());
        connection.prepQuery(listTypesQuery);
        Record_Set result = connection.executeQuery(listTypesQuery);
        checkboxesOfTypes = new Hashtable<EmployeeType, JCheckBox>();
        if (result.length() > 0) {
            do {
                try {
                    EmployeeType empType = new EmployeeType();
                    empType.setEmployeeType(result.getString("employee_type"));
                    empType.setEmployeeTypeId(result.getInt("employee_type_id"));
                    JCheckBox chkBox = new JCheckBox(empType.getEmployeeType());
                    chkBox.setSelected(true);
                    chkBox.addActionListener(new EmployeeTypeAction());
                    myEmployeeTypes.add(chkBox);

                    checkboxesOfTypes.put(empType, chkBox);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (result.moveNext());
        }
    }

    public void initComponents() {
        setLayout(new BorderLayout());

        //Set up the Employee panels
        jpEmployees = new JPanel(new BorderLayout());

        jpTrained = new JPanel(new BorderLayout());
        mySearchTextBox = new JTextField();

        mainAvailabilityTab = new JTabbedPane();

        myEmployeeTypes = new JPanel();
        myEmployeeTypes.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        myAvailabilityStatusArea = new JTextArea(3, 0);
        myAvailabilityStatusArea.setEditable(false);
        myAvailabilityStatusArea.setLineWrap(true);
        myAvailabilityStatusArea.setWrapStyleWord(true);
        myAvailabilityStatusArea.setBackground(new Color(213, 222, 242));
        myAvailabilityStatusArea.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        myAvailabilityStatusArea.setAlignmentX(JComponent.CENTER_ALIGNMENT);
        myAvailabilityStatusArea.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 11));

        setShiftDisplayingAvailabilityFor(null);

        jpLblTrained = new JPanel();
        jpLblTrained.setLayout(new BoxLayout(jpLblTrained, BoxLayout.Y_AXIS));

        JPanel myAvailabilityComboPanel = new JPanel();
        myAvailabilityComboPanel.setLayout(new BoxLayout(myAvailabilityComboPanel, BoxLayout.X_AXIS));

        myFilteringOptions =
                new AvailabilityFilteringOptions(this.parent, this);

        JLabel myIconLabel = new JLabel();
        myIconLabel.setPreferredSize(new Dimension(24, 24));
        myIconLabel.setIcon(Main_Window.FilterUsers);
        myIconLabel.addMouseListener(new MouseAdapter () {
            @Override
            public void mouseClicked(MouseEvent e) {
                myFilteringOptions.toggleDisplay();
            }
        }
        );

        //Ok here is the label we need to replace...
        acb = new AvailabilityComboBox(this, myFilteringOptions);
        myAvailabilityComboPanel.add(acb);
        if (parent.getAllEmployeeTypes().size() != 0 || parent.getAvailableCertificationsForSchedule().size() != 0) {
            myAvailabilityComboPanel.add(myIconLabel);
        }

        jpLblTrained.add(myAvailabilityComboPanel);
        jpLblTrained.add(mySearchTextBox);
        jpLblTrained.add(myFilteringOptions);

        jpLblTrained.setBackground(Schedule_View_Panel.blank_color);
        jpTrained.add(jpLblTrained, BorderLayout.NORTH);
        jpEmployees.add(jpTrained, BorderLayout.CENTER);
        jpEmpTrained = new JPanel();
        add(jpEmployees);

        mySearchTextBox.getDocument().addDocumentListener(new DocumentListener() {

            public void insertUpdate(DocumentEvent e) {
                SearchByText();
            }

            public void removeUpdate(DocumentEvent e) {
            	SearchByText();
            }

            public void changedUpdate(DocumentEvent e) {
            	SearchByText();
            }
            
        });
    }

    /**
     * Used to display useful text about what shifts you are displaying availability list for
     * send a null if you do not currently have a shift selected...
     */
    public void setShiftDisplayingAvailabilityFor(final UnitToDisplay shift) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                if (shift == null) {
                    myAvailabilityStatusArea.setText("Displaying all active employees.");
                } else {
                    myAvailabilityStatusArea.setText("Displaying employees that can work " + shift.getDateString() + " for " +
                            shift.getClient().getClientName());
                }
            }
        });
    }

    /**
     * Used when multiple shifts are selected for a availability list, to display information about
     * availability...
     */
    public void setShiftsForDisplayingAvailabilityForm(final int numShiftsSelected) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                myAvailabilityStatusArea.setText("Displaying employees that can work all of the " + numShiftsSelected + " shifts selected.");
            }
        });

    }

    public Schedule_View_Panel parent() {
        return parent;
    }

    public void doOnSelect() {
        parent.setEmployeeToFilter(-1);
        parent.orderClients(null);
    }

    public void repaint() {
        super.repaint();
        try {
            jpTrained.repaint();
            jpTrained.revalidate();
        } catch (Exception e) {
        }
    }

    /**
     * Used to refresh Employee Info Box with new Employee Array should only be used
     * on our sort functions....
     */
    public void refreshEmployeesList(Vector<SEmployee> se) {
        int i, c;
        c = se.size();
        jpEmpTrained.removeAll();
        semp = se;
        for (i = 1; i < c; i++) {
            if (se.get(i) != null && !se.get(i).isDeleted()) {
                jpEmpTrained.add(new AEmployee(se.get(i), parent));
            }
        }
        jpEmpTrained.add(new JPanel());
        js.validate();
        repaint();
    }

    /**
     * Used to redraw existing data, rather than creating new AEmployee objects...
     */
    public void redrawEmployeesList(final Vector<SEmployee> se, final int LastSortType) {
        final int c = se.size();
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                jpEmpTrained.removeAll();
                semp = se;
                for (int i = 1; i < c; i++) {
                    if (se.get(i) != null && !se.get(i).isDeleted() && se.get(i).getId() != 0) {
                        try {
                            jpEmpTrained.add(se.get(i).getAEmp());
                        } catch (Exception e) {
                        }

                    }
                }
                jpEmpTrained.add(new JPanel());
                js.validate();
                repaint();
            }
        });

    }

    /**
     * Returns array of employees used by AvailabilityComboBox
     */
    public Vector getEmployeesList() {
        return semp;
    }

    /**
     * Sets the entire List to Non Visible G....
     */
    public void setListToNonVisible() {
        for (int i = 0; i < this.semp.size(); i++) {
            semp.get(i).setVisible(false);
        }
    }

    public void setListToVisible() {
        for (int i = 0; i < this.semp.size(); i++) {
            semp.get(i).setShowBecauseOfAvailability(true);
            semp.get(i).setShowByClientList(true);
        }
        this.refreshEmployeeListByVisibility();
    }

    public void setGivenEmployeeToVisible(int cid) {
        ((SEmployee) myHash.get(cid)).setVisible(true);
    }

    public void buildEmployeesList(Vector<SEmployee> se) {
        int i, c;
        semp = se;
        c = se.size();
        myHash = new Hashtable(c * 2);

        jpEmpTrained = new JPanel();
        jpEmpTrained.setLayout(new BoxLayout(jpEmpTrained, BoxLayout.Y_AXIS));
        /*
         * '0' is the blank employee
         */
        for (i = 1; i < c; i++) {
            if (!se.get(i).isDeleted() && se.get(i).getId() != 0) {
                jpEmpTrained.add(new AEmployee(se.get(i), parent));

            }
            myHash.put(se.get(i).getId(), se.get(i));
        }

        refreshEmployeeListByVisibility();
        
        js = new JScrollPane(jpEmpTrained);
        jpTrained.add(js, BorderLayout.CENTER);
        js.getVerticalScrollBar().setUnitIncrement(30);
        jpEmpTrained.revalidate();
        repaint();
    }

    private void SearchByText() {
        String newText = mySearchTextBox.getText();

        for (int i = 0; i < jpEmpTrained.getComponentCount(); i++) {
            try {
                AEmployee myComp = ((AEmployee) jpEmpTrained.getComponent(i));
                String myString = myComp.myEmployee.getName();
                if (myString.regionMatches(true, 0, newText, 0, newText.length())) {
                    js.getViewport().setViewPosition(myComp.getLocation());
                    i = jpEmpTrained.getComponentCount();
                } else if (newText.length() == 0) {
                    js.getViewport().setViewPosition(new Point(0, 0));
                }
            } catch (Exception e) {
            }
        }
    }

    public Hashtable<EmployeeType, JCheckBox> getCheckboxesOfEmployeeType() {
        return checkboxesOfTypes;
    }

    public void refreshEmployeeListByVisibility() {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < jpEmpTrained.getComponentCount(); i++) {
                    try {
                        AEmployee myComp = ((AEmployee) jpEmpTrained.getComponent(i));
                        myComp.setVisible(
                                !myComp.myEmployee.isDeleted() &&
                                !myComp.myEmployee.isInvisible() &&
                                myComp.myEmployee.isShowBecauseOfAvailability() &&
                                myComp.myEmployee.isShowBecauseOfEmployeeType() &&
                                myComp.myEmployee.isShowBecauseOfBanning() &&
                                myComp.myEmployee.isShowBecauseOfClientTraining() &&
                                myComp.myEmployee.isShowByClientList() &&
                                myComp.myEmployee.isShowBecauseOfEmployeeCertifications() &&
                                myComp.myEmployee.isShowBecauseOfPartTimeHours());
                    } catch (Exception e) {
                    }
                }
                repaint();
            }
        });
    }

    private class EmployeeTypeAction implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            refreshEmployeeListByVisibility();
        }
    }
}
