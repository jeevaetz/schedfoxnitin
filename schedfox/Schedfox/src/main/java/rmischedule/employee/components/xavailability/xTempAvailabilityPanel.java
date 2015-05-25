/*
 * xTempAvailabilityPanel.java
 *
 * Created on August 19, 2005, 9:17 AM
 */
package rmischedule.employee.components.xavailability;

import java.awt.Rectangle;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.employee.xEmployeeEdit;
import rmischedule.main.Main_Window;
import rmischedule.schedule.components.notes.AddNoteClass;
import rmischeduleserver.control.AvailabilityController;
import rmischeduleserver.control.TimeOffController;

import rmischeduleserver.mysqlconnectivity.queries.availability.*;
import schedfoxlib.model.Availability;
import schedfoxlib.model.AvailabilityInterface;
import schedfoxlib.model.Employee;
import schedfoxlib.model.TimeOffCalc;

/**
 *
 * @author  Ira Juneau
 */
public class xTempAvailabilityPanel extends javax.swing.JPanel implements xFormInterface {

    private xAvailabilitySpecifyTimes myTimeDisplay;
    private String[] myMonths;
    private JLabel[] myDayLabel = new JLabel[7];
    private int[] myDaysOfWeek = new int[7];
    private Vector<xIndividualDayPanel> vectorOfDays;
    private Vector<JPanel> myTotalPanels;
    private Vector<AvailIconContainer> myButtons;
    private Vector<JMenuItem> myRightMenu;
    private JPopupMenu myPopMenu;
    private xIndividualDayPanel activePanel;
    private assemble_total_avail_query myGetTempAvailQuery;
    private String employee_id;
    private boolean dataIsLoading;
    private xEmployeeEdit employeeEdit;
    private static final int numOfYearsToDisplay = 10;
    private AvailIconContainer vacationDayIcon;
    private AvailIconContainer personalDayIcon;
    private AvailDayStats availStats;
    private HashMap<Integer, ArrayList<TimeOffCalc>> hoursCompensatedHash;
    private xMainAvailabilityPanel mainAvail;

    /** Creates new form xTempAvailabilityPanel */
    public xTempAvailabilityPanel(xEmployeeEdit employeeEdit, xMainAvailabilityPanel mainAvail) {
        initComponents();
        this.mainAvail = mainAvail;
        this.employeeEdit = employeeEdit;
        dataIsLoading = false;
        hoursCompensatedHash = new HashMap<Integer, ArrayList<TimeOffCalc>>();
        setUpHeaders();
        setUpYearCombo();
        MonthCombo.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        createAvailabilityTools();

        notePanel.add(new AddNoteClass(this.myLayeredPane, this.ContentPane));
        notePanel.add(new JLabel("Drag an E-Z Note"));

        availStats = new AvailDayStats();
        myLayeredPane.add(availStats, JLayeredPane.POPUP_LAYER);
        availStats.setVisible(false);
        availStats.setBounds(0, 0, 141, 220);

        myTimeDisplay = new xAvailabilitySpecifyTimes(this);
        myLayeredPane.add(myTimeDisplay, JLayeredPane.PALETTE_LAYER);
        myLayeredPane.setBounds(0, 0, myTimeDisplay.getMySize().width, myTimeDisplay.getMySize().height);
        myLayeredPane.moveToBack(myTimeDisplay);
        createPopupMenu();

        YearCombo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                firstMonthOrYearChanged(evt);
            }
        });
        MonthCombo.addItemListener(new java.awt.event.ItemListener() {

            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                firstMonthOrYearChanged(evt);
            }
        });
    }

    /**
     * Display Availability Stats
     */
    public void displayAvailStats(xIndividualDayPanel indDayPanel) {

        Rectangle rect = indDayPanel.getBounds();
        availStats.setBounds((int) rect.getX() + (int) rect.getWidth(), (int) rect.getY(), 141, 140);
        availStats.displayStats(indDayPanel.getAvailability());
        availStats.setVisible(true);
    }

    public void hideAvailStats() {
        availStats.setVisible(false);
    }

    public xEmployeeEdit getEmployeeEdit() {
        return this.employeeEdit;
    }

    private void setUpYearCombo() {
        Calendar myCal = Calendar.getInstance();
        myCal.add(Calendar.YEAR, -2);
        for (int i = 0; i < numOfYearsToDisplay; i++) {
            YearCombo.addItem(myCal.get(Calendar.YEAR));
            if (i == 2) {
                YearCombo.setSelectedIndex(i);
            }
            myCal.roll(Calendar.YEAR, 1);
        }

    }

    private void setUpHeaders() {
        Integer companyId = Integer.parseInt(employeeEdit.getConnection().myCompany);
        Calendar myCal = StaticDateTimeFunctions.getBegOfWeek(companyId);
        for (int i = 0; i < 7; i++) {
            myDayLabel[i] = new JLabel(StaticDateTimeFunctions.getDayOfWeek(myCal), JLabel.CENTER);
            myDaysOfWeek[i] = myCal.get(Calendar.DAY_OF_WEEK);
            myCal.add(Calendar.DAY_OF_YEAR, 1);
            HeaderPanel.add(myDayLabel[i]);
        }
    }

    /**
     * Builds our Calendar Object with Year and Month set by the ComboBoxes!
     */
    public void setUpCalendarForSelectedYearMonth() {
        Calendar currentCal = Calendar.getInstance();
        Calendar newCal = Calendar.getInstance();
        Calendar correctCalendar;
        newCal.set(Calendar.YEAR, ((Integer) YearCombo.getSelectedItem()).intValue());
        newCal.set(Calendar.MONTH, StaticDateTimeFunctions.getCalendarIntForMonth((String) MonthCombo.getSelectedItem()));
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        correctCalendar = StaticDateTimeFunctions.setCalendarTo(newCal);
        int monthToDisplay = correctCalendar.get(Calendar.MONTH);
        clearData();
        //Add in necessary blank spots...
        int numBlanks = (correctCalendar.get(Calendar.DAY_OF_WEEK) - myDaysOfWeek[0]);
        if (numBlanks < 0) {
            numBlanks = 7 - (numBlanks * -1);
        }
        correctCalendar.add(Calendar.DAY_OF_YEAR, -numBlanks);
        for (int i = 0; i < numBlanks; i++) {
            addSpacerPanel(correctCalendar);
            correctCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        do {
            xIndividualDayPanel myPanel = new xIndividualDayPanel(true, correctCalendar, this);
            if (currentCal.compareTo(correctCalendar) > 0) {
                myPanel.setToPast(true);
            }
            vectorOfDays.add(myPanel);
            myTotalPanels.add(myPanel);
            MonthDisplayPanel.add(myPanel);
            correctCalendar.add(Calendar.DAY_OF_YEAR, 1);
        } while (correctCalendar.get(Calendar.MONTH) == monthToDisplay);
        while (myTotalPanels.size() < 42) {
            addSpacerPanel(correctCalendar);
            correctCalendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        MonthDisplayPanel.revalidate();
    }

    /**
     * Creates our buttons to drag onto our availability...
     */
    public void createAvailabilityTools() {
        myButtons = new Vector<AvailIconContainer>();

        vacationDayIcon = new AvailIconContainer(this, "Vacation", "Full Day", "Half Day", Main_Window.Vac_Icon, Main_Window.Vac_Half_Icon, myToolBar.getBackground(), this, Main_Window.NON_AVAILABLE_VACATION, Main_Window.NON_AVAILABLE_HALF_VACATION);
        personalDayIcon = new AvailIconContainer(this, "Personal Day", "Full Day", "Half Day", Main_Window.Personal_Icon, Main_Window.Personal_Half_Icon, myToolBar.getBackground(), this, Main_Window.NON_AVAILABLE_PERSONAL, Main_Window.NON_AVAILABLE_HALF_PERSONAL);
        myButtons.add(new AvailIconContainer(this, "Non Availability", xAvailDragDropIcon.NONAVAIL, null, Main_Window.Generic_NA_Icon, null, myToolBar.getBackground(), this, Main_Window.NON_AVAILABLE, null));
        myButtons.add(vacationDayIcon);
        myButtons.add(personalDayIcon);

        for (int i = 0; i < myButtons.size(); i++) {
            myToolBar.add(myButtons.get(i));
        }
        myToolBar.revalidate();
    }

    /**
     * Grabs our Vector of all Buttons that can be dragged on, so we can list when right clicked...
     */
    public Vector<AvailIconContainer> getVectorOfValidActions() {
        return myButtons;
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQueryFormEmployee(String employeeId) {
        Calendar startMonth;
        Calendar endMonth;
        employee_id = employeeId;
        Calendar newCal = Calendar.getInstance();
        newCal.set(Calendar.YEAR, ((Integer) YearCombo.getSelectedItem()).intValue());
        newCal.set(Calendar.MONTH, StaticDateTimeFunctions.getCalendarIntForMonth((String) MonthCombo.getSelectedItem()));
        newCal.set(Calendar.DAY_OF_MONTH, 1);
        myGetTempAvailQuery = new assemble_total_avail_query();
        startMonth = StaticDateTimeFunctions.getBegOfMonth(StaticDateTimeFunctions.setCalendarTo(newCal));
        endMonth = StaticDateTimeFunctions.getEndOfMonth(StaticDateTimeFunctions.setCalendarTo(newCal));
        myGetTempAvailQuery.update(employee_id, StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startMonth), StaticDateTimeFunctions.convertCalendarToDatabaseFormat(endMonth));

        return myGetTempAvailQuery;
    }

    public void getData(String employeeid) {
        employee_id = employeeid;
        setUpCalendarForSelectedYearMonth();
    }

    public void loadData() {

    }

    /**
     * Refreshes our time off counts with vacation and personal days
     * @param personalDaysOffCalcs
     * @param vacationDaysOffCalcs
     */
    public void refreshCounts(ArrayList<TimeOffCalc> personalDaysOffCalcs, ArrayList<TimeOffCalc> vacationDaysOffCalcs, HashMap<Integer, Integer> integers) {
        double personalDaysOff = 0;
        int personalDaysAdjust = 0;
        double vacationDaysOff = 0;
        int vacationDaysAdjust = 0;
        TimeOffController timeController =
                TimeOffController.getInstance(employeeEdit.getConnection().myCompany);
        try {
            personalDaysOff = timeController.calcCalcuationsForEmployee(personalDaysOffCalcs);
            if (integers.get(1) != null) {
                personalDaysAdjust = integers.get(1);
            }
            hoursCompensatedHash.put(1, personalDaysOffCalcs);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            vacationDaysOff =
                    timeController.calcCalcuationsForEmployee(vacationDaysOffCalcs);
            if (integers.get(2) != null) {
                vacationDaysAdjust = integers.get(2);
            }
            hoursCompensatedHash.put(2, vacationDaysOffCalcs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        personalDayIcon.setCount(personalDaysOff, personalDaysAdjust);
        vacationDayIcon.setCount(vacationDaysOff, vacationDaysAdjust);
    }

    public void loadData(ArrayList<Availability> avails) {
        if (employee_id == null) {
            return;
        }
        setUpCalendarForSelectedYearMonth();
        dataIsLoading = true;

        for (int a = 0; a < avails.size(); a++) {
            Availability avail = avails.get(a);
            for (int d = 0; d < vectorOfDays.size(); d++) {
                if (vectorOfDays.get(d).equalsDoy(avail.getAvailDayOfYear())) {
                    vectorOfDays.get(d).setAvailability(avail);
                }
            }
        }
        dataIsLoading = false;

    }

    /**
     * Returns the time off calcs by time off type.
     * @param timeOffType
     * @return
     */
    public ArrayList<TimeOffCalc> getTimeOffCalcsForType(int timeOffType) {
        return hoursCompensatedHash.get(timeOffType);
    }

    /**
     * Adds a spacer Panel
     */
    private void addSpacerPanel(Calendar myCal) {
        xIndividualDayPanel spacerPanel = new xIndividualDayPanel(false, myCal, this);
        MonthDisplayPanel.add(spacerPanel);
        myTotalPanels.add(spacerPanel);
    }

    public void clearData() {

        vectorOfDays = new Vector(31);
        myTotalPanels = new Vector(42);
        MonthDisplayPanel.removeAll();
    }

    /**
     * Method run when day object is clicked
     */
    public void runOnClickDay(xIndividualDayPanel PanelTriggered) {
        myTimeDisplay.showMe(MonthAndControlPanel, PanelTriggered, "Edit Unavailability For " + PanelTriggered.getMyReadDate());

    }

    /**
     * Create our tooltip menu
     */
    public void createPopupMenu() {
        myRightMenu = new Vector();
        myPopMenu = new JPopupMenu("Unavailability");
        for (int i = 0; i < myButtons.size(); i++) {
            myRightMenu.add(new JMenuItem("Set this shift to " + myButtons.get(i).getValue()));
            myPopMenu.add(myRightMenu.get(i));
            final Integer val = (Integer) myButtons.get(i).getValue();
            myRightMenu.get(i).addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    runOnDrop(val, new MouseEvent(myPopMenu, 0, 0, 0, 0, 0, 0, true), null);
                }
            });
        }
    }

    private void runOnDrop(Integer val, MouseEvent evt, BufferedImage bi) {
        activePanel.runOnDrop(val, evt, bi);
    }

    /**
     * Called from our individualdaypanels when information on them is modified...explains how to save
     * our data...
     */
    public String saveDayInformation(xIndividualDayPanel mySavePanel) {
        Employee emp = (Employee) employeeEdit.getSelectedObject();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        AvailabilityInterface availInterface = mySavePanel.getAvailability();
        Availability avail = new Availability();
        if (availInterface != null && availInterface.getAvailabilityId() >= 0) {
            avail.setAvailId(availInterface.getAvailabilityId());
        } else if (availInterface != null && availInterface.getAvailabilityId() < 0) {
            //Overriding master shift
            avail.setAvailMasterRow(availInterface.getAvailabilityId());
        }
        try {
            avail.setAvailDayOfYear(dateFormat.parse(mySavePanel.getDBaseDate()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        double numberHoursToCompensate = 0;
        try {

            numberHoursToCompensate = mySavePanel.getHoursCompensated() / mySavePanel.getDaysAccrued();
        } catch (Exception e) {
        }
        try {
            avail.setHoursCompensated(new BigDecimal(numberHoursToCompensate));
        } catch (Exception e) {
            avail.setHoursCompensated(new BigDecimal(0));
        }
        avail.setAvailStartTime(Integer.parseInt(mySavePanel.getStartDBase()));
        avail.setAvailEndTime(Integer.parseInt(mySavePanel.getEndDBase()));
        avail.setEmployeeId(emp.getEmployeeId());
        avail.setAvailType(mySavePanel.getShiftType());
        avail.setCreatedBy(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
        try {
            AvailabilityController availController = AvailabilityController.getInstance(employeeEdit.getConnection().myCompany);
            return availController.saveAvailability(avail);
        } catch (Exception e) {
            return "";
        }
    }

    public xEmployeeEdit getMyParent() {
        return employeeEdit;
    }

    public JLayeredPane getLayeredPane() {
        return myLayeredPane;
    }

    public JPanel getContentPanel() {
        return ContentPane;
    }

    public void hideSpecifyTimes(xAvailabilitySpecifyTimes myForm) {
        myLayeredPane.setLayer(myTimeDisplay, JLayeredPane.PALETTE_LAYER);
        myLayeredPane.moveToBack(myTimeDisplay);
    }

    public JPopupMenu getMyMenu() {
        return myPopMenu;
    }

    public void setActiveDayForMenu(xIndividualDayPanel myActivePanel) {
        activePanel = myActivePanel;
    }

    public void deletePermenantDayInformation(xIndividualDayPanel myDelPanel) {
    }

    public boolean isGettingData() {
        return dataIsLoading;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        myLayeredPane = new javax.swing.JLayeredPane();
        ContentPane = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        ControlPanel = new javax.swing.JPanel();
        MonthChangePanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        myMonths = StaticDateTimeFunctions.getMonthsOfYear();
        MonthCombo = new javax.swing.JComboBox(myMonths);
        jPanel5 = new javax.swing.JPanel();
        YearChangePanel = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        YearCombo = new javax.swing.JComboBox();
        HeaderPanel = new javax.swing.JPanel();
        MonthAndControlPanel = new javax.swing.JPanel();
        MonthDisplayPanel = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        rightPanel = new javax.swing.JPanel();
        myToolBar = new javax.swing.JPanel();
        notePanel = new javax.swing.JPanel();

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(new javax.swing.BoxLayout(this, javax.swing.BoxLayout.Y_AXIS));

        myLayeredPane.setMinimumSize(null);
        myLayeredPane.setPreferredSize(new java.awt.Dimension(500, 500));
        myLayeredPane.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                resizeChild(evt);
            }
        });

        ContentPane.setMinimumSize(new java.awt.Dimension(500, 35));
        ContentPane.setPreferredSize(new java.awt.Dimension(500, 39));
        ContentPane.setLayout(new javax.swing.BoxLayout(ContentPane, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setMinimumSize(new java.awt.Dimension(10, 35));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.Y_AXIS));

        ControlPanel.setMaximumSize(new java.awt.Dimension(32767, 24));
        ControlPanel.setMinimumSize(new java.awt.Dimension(10, 24));
        ControlPanel.setPreferredSize(new java.awt.Dimension(10, 24));
        ControlPanel.setLayout(new javax.swing.BoxLayout(ControlPanel, javax.swing.BoxLayout.LINE_AXIS));

        MonthChangePanel.setLayout(new javax.swing.BoxLayout(MonthChangePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel1.setText("Month");
        MonthChangePanel.add(jLabel1);

        jPanel3.setMaximumSize(new java.awt.Dimension(10, 10));
        MonthChangePanel.add(jPanel3);

        MonthCombo.setMaximumSize(new java.awt.Dimension(32767, 18));
        MonthCombo.setMinimumSize(new java.awt.Dimension(25, 18));
        MonthCombo.setPreferredSize(new java.awt.Dimension(29, 20));
        MonthChangePanel.add(MonthCombo);

        jPanel5.setMaximumSize(new java.awt.Dimension(10, 10));
        MonthChangePanel.add(jPanel5);

        ControlPanel.add(MonthChangePanel);

        YearChangePanel.setLayout(new javax.swing.BoxLayout(YearChangePanel, javax.swing.BoxLayout.LINE_AXIS));

        jLabel2.setText("Year");
        YearChangePanel.add(jLabel2);

        jPanel4.setMaximumSize(new java.awt.Dimension(10, 10));
        YearChangePanel.add(jPanel4);

        YearCombo.setMaximumSize(new java.awt.Dimension(32767, 18));
        YearCombo.setMinimumSize(new java.awt.Dimension(25, 18));
        YearCombo.setPreferredSize(new java.awt.Dimension(29, 20));
        YearChangePanel.add(YearCombo);

        ControlPanel.add(YearChangePanel);

        jPanel2.add(ControlPanel);

        HeaderPanel.setMaximumSize(new java.awt.Dimension(32767, 15));
        HeaderPanel.setMinimumSize(new java.awt.Dimension(10, 15));
        HeaderPanel.setPreferredSize(new java.awt.Dimension(10, 15));
        HeaderPanel.setLayout(new java.awt.GridLayout(1, 7));
        jPanel2.add(HeaderPanel);

        MonthAndControlPanel.setLayout(new javax.swing.BoxLayout(MonthAndControlPanel, javax.swing.BoxLayout.LINE_AXIS));

        MonthDisplayPanel.setLayout(new java.awt.GridLayout(0, 7));
        MonthAndControlPanel.add(MonthDisplayPanel);

        jPanel2.add(MonthAndControlPanel);

        ContentPane.add(jPanel2);

        jPanel1.setMinimumSize(new java.awt.Dimension(220, 40));
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        rightPanel.setBackground(new java.awt.Color(246, 237, 225));
        rightPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(72, 117, 156)), new javax.swing.border.LineBorder(new java.awt.Color(72, 117, 156), 2, true)), "Drag and drop the icons."));
        rightPanel.setMaximumSize(new java.awt.Dimension(220, 32767));
        rightPanel.setMinimumSize(new java.awt.Dimension(220, 20));
        rightPanel.setOpaque(false);
        rightPanel.setPreferredSize(new java.awt.Dimension(220, 20));
        rightPanel.setLayout(new javax.swing.BoxLayout(rightPanel, javax.swing.BoxLayout.Y_AXIS));

        myToolBar.setBackground(new java.awt.Color(246, 237, 225));
        myToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        myToolBar.setMaximumSize(new java.awt.Dimension(20000, 32767));
        myToolBar.setMinimumSize(new java.awt.Dimension(150, 10));
        myToolBar.setPreferredSize(new java.awt.Dimension(150, 10));
        myToolBar.setLayout(new java.awt.GridLayout(0, 1));
        rightPanel.add(myToolBar);

        jPanel1.add(rightPanel);

        notePanel.setMaximumSize(new java.awt.Dimension(220, 28));
        notePanel.setMinimumSize(new java.awt.Dimension(10, 28));
        notePanel.setPreferredSize(new java.awt.Dimension(100, 28));
        notePanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 1));
        jPanel1.add(notePanel);

        ContentPane.add(jPanel1);

        ContentPane.setBounds(0, 0, 570, 370);
        myLayeredPane.add(ContentPane, javax.swing.JLayeredPane.PALETTE_LAYER);

        add(myLayeredPane);
    }// </editor-fold>//GEN-END:initComponents

    private void resizeChild(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_resizeChild
        ContentPane.setBounds(0, 0, myLayeredPane.getBounds().width, myLayeredPane.getBounds().height);
    }//GEN-LAST:event_resizeChild
    private void firstMonthOrYearChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_firstMonthOrYearChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            this.mainAvail.refreshCalculations();
        }
    }//GEN-LAST:event_firstMonthOrYearChanged
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ContentPane;
    private javax.swing.JPanel ControlPanel;
    private javax.swing.JPanel HeaderPanel;
    private javax.swing.JPanel MonthAndControlPanel;
    private javax.swing.JPanel MonthChangePanel;
    private javax.swing.JComboBox MonthCombo;
    private javax.swing.JPanel MonthDisplayPanel;
    private javax.swing.JPanel YearChangePanel;
    private javax.swing.JComboBox YearCombo;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JLayeredPane myLayeredPane;
    private javax.swing.JPanel myToolBar;
    private javax.swing.JPanel notePanel;
    private javax.swing.JPanel rightPanel;
    // End of variables declaration//GEN-END:variables

    public xMainAvailabilityPanel getMainAvailPanel() {
        return this.mainAvail;
    }
}
