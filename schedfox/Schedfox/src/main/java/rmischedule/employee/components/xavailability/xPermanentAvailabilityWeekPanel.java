/*
 * xPermanentAvailabilityMainPanel.java
 *
 * Created on August 17, 2005, 6:10 PM
 */
package rmischedule.employee.components.xavailability;

import java.util.Vector;
import java.util.Calendar;
import java.awt.Dimension;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.*;

import rmischedule.main.*;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.data_connection.Connection;
import rmischedule.employee.xEmployeeEdit;
import rmischedule.schedule.schedulesizes.ComponentDimensions;

import rmischeduleserver.mysqlconnectivity.queries.availability.*;
import schedfoxlib.model.util.Record_Set;
import schedfoxlib.model.AvailabilityInterface;
import schedfoxlib.model.AvailabilityMaster;
import schedfoxlib.model.Employee;

/**
 *
 * @author  Ira Juneau
 */
public class xPermanentAvailabilityWeekPanel extends javax.swing.JPanel implements xFormInterface {

    private xAvailabilitySpecifyTimes myTimeDisplay;
    private Vector<xIndividualDayPanel> myDaysOfWeek;
    private xDayAvailabilityPanel myDispAvail;
    private Vector<JLabel> myHeaderLabels;
    private Vector<AvailIconContainer> myButtons;
    private Vector<JMenuItem> myRightMenu;
    private JPopupMenu myPopMenu;
    private xIndividualDayPanel activePanel;
    private master_availability_by_range_query myGetMasterAvailQuery;
    private String empid;
    private boolean dataIsLoading;
    private xEmployeeEdit employeeEdit;
    private xMainAvailabilityPanel mainPanel;
    private static final Dimension myShiftDimension = ComponentDimensions.defaultSizes.get("SShift");

    /** Creates new form xPermanentAvailabilityMainPanel */
    public xPermanentAvailabilityWeekPanel(xEmployeeEdit employeeEdit, xMainAvailabilityPanel mainPanel) {
        initComponents();
        this.employeeEdit = employeeEdit;
        this.mainPanel = mainPanel;
        dataIsLoading = true;
        myButtons = new Vector();
        myDispAvail = new xDayAvailabilityPanel();
        ItemizedDisplayPanel.add(myDispAvail);
        myHeaderLabels = new Vector();
        myHeaderLabels.add(Day1);
        myHeaderLabels.add(Day2);
        myHeaderLabels.add(Day3);
        myHeaderLabels.add(Day4);
        myHeaderLabels.add(Day5);
        myHeaderLabels.add(Day6);
        myHeaderLabels.add(Day7);
        empid = "0";
        setUpWeekPanel();
        createAvailabilityTools();
        myTimeDisplay = new xAvailabilitySpecifyTimes(this);
        myLayeredPane.add(myTimeDisplay, JLayeredPane.PALETTE_LAYER);
        myLayeredPane.moveToBack(myTimeDisplay);
        myTimeDisplay.setBounds(100, 100, 100, 100);

        createPopupMenu();
        repaint();
        ContentPane.setBounds(0, 0, myLayeredPane.getBounds().width, myLayeredPane.getBounds().height);
    }

    /**
     * Private method to lay out and initialize our week view...
     */
    private void setUpWeekPanel() {
        Integer companyId = Integer.parseInt(employeeEdit.getConnection().myCompany);
        Calendar weekCal = StaticDateTimeFunctions.getBegOfWeek(companyId);
        myDaysOfWeek = new Vector();
        for (int i = 0; i < 7; i++) {
            xIndividualDayPanel myPanel = new xIndividualDayPanel(weekCal.get(Calendar.DAY_OF_WEEK), this);
            myPanel.setPreferredSize(myShiftDimension);
            myPanel.setMinimumSize(myShiftDimension);
            myPanel.setMaximumSize(myShiftDimension);
            myDaysOfWeek.add(myPanel);
            DisplayPanel.add(myDaysOfWeek.get(i));
            weekCal.add(Calendar.DAY_OF_YEAR, 1);
        }
        weekCal = StaticDateTimeFunctions.getBegOfWeek(companyId);
        for (int i = 0; i < 7; i++) {
            myHeaderLabels.get(i).setText(StaticDateTimeFunctions.getDayOfWeek(weekCal));
            myHeaderLabels.get(i).setPreferredSize(new Dimension(myShiftDimension.width, 15));
            myHeaderLabels.get(i).setMinimumSize(new Dimension(myShiftDimension.width, 15));
            myHeaderLabels.get(i).setMaximumSize(new Dimension(myShiftDimension.width, 15));
            weekCal.add(Calendar.DAY_OF_YEAR, 1);
        }
        DayDisplayPanel.setPreferredSize(new Dimension(100, myShiftDimension.height));
        DayDisplayPanel.setMinimumSize(new Dimension(0, myShiftDimension.height));
        DayDisplayPanel.setMaximumSize(new Dimension(10000, myShiftDimension.height));
    }

    public void clearData() {
        for (int x = 0; x < myDaysOfWeek.size(); x++) {
            myDaysOfWeek.get(x).setStartEndTimes(Main_Window.AVAILABLE, null, null, "0", "0");
            myDaysOfWeek.get(x).clear();
        }
    }

    public void loadData() {
    }

    /**
     * Loads Data into our damn form...
     */
    public void loadData(ArrayList<AvailabilityMaster> avails) {
        dataIsLoading = true;
        for (int a = 0; a < avails.size(); a++) {
            AvailabilityMaster avail = avails.get(a);
            for (int x = 0; x < myDaysOfWeek.size(); x++) {
                try {
                    if (myDaysOfWeek.get(x).equalsDow(avail.getDow())) {
                        myDaysOfWeek.get(x).setAvailability(avail);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        dataIsLoading = false;
    }

    public rmischeduleserver.mysqlconnectivity.queries.GeneralQueryFormat getQueryFormEmployee(String employeeId) {
        empid = employeeId;
        Integer companyId = Integer.parseInt(employeeEdit.getConnection().myCompany);
        myGetMasterAvailQuery = new master_availability_by_range_query();
        String start = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(StaticDateTimeFunctions.getBegOfWeek(companyId));
        String end = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(StaticDateTimeFunctions.getEndOfWeek(companyId));
        myGetMasterAvailQuery.update(employeeId, start, end);
        return myGetMasterAvailQuery;
    }

    public void getData(String eid) {
    }

    /**
     * Creates our buttons to drag onto our availability...
     */
    public void createAvailabilityTools() {
        myButtons = new Vector<AvailIconContainer>();

        myButtons.add(new AvailIconContainer(this, "Non Availability", xAvailDragDropIcon.NONAVAIL, null, Main_Window.Generic_NA_Icon, null, myToolBar.getBackground(), null, Main_Window.NON_AVAILABLE, null));
        for (int i = 0; i < myButtons.size(); i++) {
            myInnerToolBar.add(myButtons.get(i));
        }
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

    /**
     * Called from our individualdaypanels when information on them is modified...explains how to save
     * our data...
     */
    public String saveDayInformation(xIndividualDayPanel mySavePanel) {
        if (mySavePanel.getShiftType() != Main_Window.AVAILABLE) {
            master_availability_save_query mySaveQuery = new master_availability_save_query();

            Employee emp = (Employee) employeeEdit.getSelectedObject();

            AvailabilityInterface availInterface = mySavePanel.getAvailability();

            AvailabilityMaster availMaster = new AvailabilityMaster();
            availMaster.setAvailMDateStarted(new Date(new Connection().getServerTimeMillis()));
            availMaster.setAvailMDateEnded(new Date("10/10/2100"));
            availMaster.setAvailMTimeStarted(Integer.parseInt(mySavePanel.getStartDBase()));
            availMaster.setAvailMTimeEnded(Integer.parseInt(mySavePanel.getEndDBase()));
            availMaster.setAvailMDayOfWeek(Integer.parseInt(mySavePanel.getDayOfWeek()));
            availMaster.setEmployeeId(emp.getEmployeeId());
            availMaster.setAvailMRow(0);
            if (availInterface != null) {
                availMaster.setAvailMId(availInterface.getAvailabilityId());
            }
            mySaveQuery.update(availMaster);

            Record_Set rs = null;
            try {
                rs = Main_Window.getEmployeeEditWindow().getConnection().executeQuery(mySaveQuery);
                return rs.getString("avail_id");
            } catch (Exception e) {
                return "";
            }

        } else {
            deletePermenantDayInformation(mySavePanel);
            return "";
        }
    }

    private void runOnDrop(Integer val, MouseEvent evt, BufferedImage bi) {
        activePanel.runOnDrop(val, evt, bi);
    }

    /**
     * Method run when day object is clicked
     */
    public void runOnClickDay(xIndividualDayPanel PanelTriggered) {
        myTimeDisplay.showMe(DayDisplayPanel, PanelTriggered, "Edit Permanent Unavailability");
    }

    public JLayeredPane getLayeredPane() {
        return myLayeredPane;
    }

    public JPanel getContentPanel() {
        return ContentPane;
    }

    public Vector<AvailIconContainer> getVectorOfValidActions() {
        return myButtons;
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
        master_availability_delete_query myDeleteQuery = new master_availability_delete_query();
        Integer companyId = Integer.parseInt(employeeEdit.getConnection().myCompany);
        Calendar myDateDelCal = StaticDateTimeFunctions.getBegOfWeek(companyId);
        myDeleteQuery.update(myDelPanel.getShiftId(), StaticDateTimeFunctions.convertCalendarToDatabaseFormat(myDateDelCal));
        try {
            Main_Window.getEmployeeEditWindow().getConnection().prepQuery(myDeleteQuery);
            Main_Window.getEmployeeEditWindow().getConnection().executeQuery(myDeleteQuery);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isGettingData() {
        return dataIsLoading;
    }

    public xEmployeeEdit getMyParent() {
        return employeeEdit;
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
        jPanel1 = new javax.swing.JPanel();
        SpacerPanelOne = new javax.swing.JPanel();
        BufferPanelThree = new javax.swing.JPanel();
        WeekHeaderDisplay = new javax.swing.JPanel();
        Day1 = new javax.swing.JLabel();
        Day2 = new javax.swing.JLabel();
        Day3 = new javax.swing.JLabel();
        Day4 = new javax.swing.JLabel();
        Day5 = new javax.swing.JLabel();
        Day6 = new javax.swing.JLabel();
        Day7 = new javax.swing.JLabel();
        BufferPanelFour = new javax.swing.JPanel();
        DayDisplayPanel = new javax.swing.JPanel();
        BufferPanelOne = new javax.swing.JPanel();
        DisplayPanel = new javax.swing.JPanel();
        BufferPanelTwo = new javax.swing.JPanel();
        ItemizedDisplayPanel = new javax.swing.JPanel();
        myToolBar = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jTextArea1 = new javax.swing.JTextArea();
        myInnerToolBar = new javax.swing.JPanel();

        setBackground(new java.awt.Color(186, 186, 222));
        setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3), javax.swing.BorderFactory.createEtchedBorder()));
        setLayout(new java.awt.GridLayout(1, 0));

        myLayeredPane.addHierarchyBoundsListener(new java.awt.event.HierarchyBoundsListener() {
            public void ancestorMoved(java.awt.event.HierarchyEvent evt) {
            }
            public void ancestorResized(java.awt.event.HierarchyEvent evt) {
                resizeChild(evt);
            }
        });

        ContentPane.setLayout(new javax.swing.BoxLayout(ContentPane, javax.swing.BoxLayout.LINE_AXIS));

        jPanel1.setOpaque(false);
        jPanel1.setLayout(new javax.swing.BoxLayout(jPanel1, javax.swing.BoxLayout.Y_AXIS));

        SpacerPanelOne.setMaximumSize(new java.awt.Dimension(32767, 30));
        SpacerPanelOne.setMinimumSize(new java.awt.Dimension(10, 30));
        SpacerPanelOne.setOpaque(false);
        SpacerPanelOne.setPreferredSize(new java.awt.Dimension(10, 30));
        SpacerPanelOne.setLayout(new javax.swing.BoxLayout(SpacerPanelOne, javax.swing.BoxLayout.LINE_AXIS));

        BufferPanelThree.setMaximumSize(new java.awt.Dimension(10, 32767));
        BufferPanelThree.setOpaque(false);
        SpacerPanelOne.add(BufferPanelThree);

        WeekHeaderDisplay.setOpaque(false);
        WeekHeaderDisplay.setLayout(new javax.swing.BoxLayout(WeekHeaderDisplay, javax.swing.BoxLayout.LINE_AXIS));

        Day1.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day1.setText("jLabel1");
        WeekHeaderDisplay.add(Day1);

        Day2.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day2.setText("jLabel2");
        WeekHeaderDisplay.add(Day2);

        Day3.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day3.setText("jLabel3");
        WeekHeaderDisplay.add(Day3);

        Day4.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day4.setText("jLabel4");
        WeekHeaderDisplay.add(Day4);

        Day5.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day5.setText("jLabel5");
        WeekHeaderDisplay.add(Day5);

        Day6.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day6.setText("jLabel6");
        WeekHeaderDisplay.add(Day6);

        Day7.setFont(new java.awt.Font("Microsoft Sans Serif", 1, 12));
        Day7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Day7.setText("jLabel7");
        WeekHeaderDisplay.add(Day7);

        SpacerPanelOne.add(WeekHeaderDisplay);

        BufferPanelFour.setMaximumSize(new java.awt.Dimension(1000, 32767));
        BufferPanelFour.setOpaque(false);
        SpacerPanelOne.add(BufferPanelFour);

        jPanel1.add(SpacerPanelOne);

        DayDisplayPanel.setMaximumSize(new java.awt.Dimension(32767, 2000));
        DayDisplayPanel.setOpaque(false);
        DayDisplayPanel.setPreferredSize(new java.awt.Dimension(10, 50));
        DayDisplayPanel.setLayout(new javax.swing.BoxLayout(DayDisplayPanel, javax.swing.BoxLayout.LINE_AXIS));

        BufferPanelOne.setMaximumSize(new java.awt.Dimension(10, 32767));
        BufferPanelOne.setOpaque(false);
        DayDisplayPanel.add(BufferPanelOne);

        DisplayPanel.setOpaque(false);
        DisplayPanel.setLayout(new javax.swing.BoxLayout(DisplayPanel, javax.swing.BoxLayout.LINE_AXIS));
        DayDisplayPanel.add(DisplayPanel);

        BufferPanelTwo.setMaximumSize(new java.awt.Dimension(1000, 32767));
        BufferPanelTwo.setOpaque(false);
        DayDisplayPanel.add(BufferPanelTwo);

        jPanel1.add(DayDisplayPanel);

        ItemizedDisplayPanel.setOpaque(false);
        ItemizedDisplayPanel.setLayout(new java.awt.GridLayout(1, 0));
        jPanel1.add(ItemizedDisplayPanel);

        ContentPane.add(jPanel1);

        myToolBar.setBackground(new java.awt.Color(246, 237, 225));
        myToolBar.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createCompoundBorder(new javax.swing.border.LineBorder(new java.awt.Color(72, 117, 156), 2, true), javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(72, 117, 156))), "Non Availability"));
        myToolBar.setMaximumSize(new java.awt.Dimension(120, 32767));
        myToolBar.setMinimumSize(new java.awt.Dimension(120, 10));
        myToolBar.setPreferredSize(new java.awt.Dimension(120, 10));
        myToolBar.setLayout(new javax.swing.BoxLayout(myToolBar, javax.swing.BoxLayout.Y_AXIS));

        jPanel6.setMaximumSize(new java.awt.Dimension(32767, 40));
        jPanel6.setMinimumSize(new java.awt.Dimension(10, 40));
        jPanel6.setOpaque(false);
        jPanel6.setPreferredSize(new java.awt.Dimension(10, 40));
        jPanel6.setLayout(new java.awt.GridLayout(1, 0));

        jTextArea1.setEditable(false);
        jTextArea1.setFont(new java.awt.Font("Courier", 1, 12));
        jTextArea1.setText("Drag and drop\n  the icons.");
        jTextArea1.setMargin(new java.awt.Insets(0, 3, 0, 0));
        jPanel6.add(jTextArea1);

        myToolBar.add(jPanel6);

        myInnerToolBar.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 5, 0, 0));
        myInnerToolBar.setOpaque(false);
        myInnerToolBar.setLayout(new java.awt.GridLayout(0, 1));
        myToolBar.add(myInnerToolBar);

        ContentPane.add(myToolBar);

        ContentPane.setBounds(0, 0, 650, 410);
        myLayeredPane.add(ContentPane, javax.swing.JLayeredPane.PALETTE_LAYER);

        add(myLayeredPane);
    }// </editor-fold>//GEN-END:initComponents

    private void resizeChild(java.awt.event.HierarchyEvent evt) {//GEN-FIRST:event_resizeChild
        ContentPane.setBounds(0, 0, myLayeredPane.getBounds().width, myLayeredPane.getBounds().height);
    }//GEN-LAST:event_resizeChild
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BufferPanelFour;
    private javax.swing.JPanel BufferPanelOne;
    private javax.swing.JPanel BufferPanelThree;
    private javax.swing.JPanel BufferPanelTwo;
    private javax.swing.JPanel ContentPane;
    private javax.swing.JLabel Day1;
    private javax.swing.JLabel Day2;
    private javax.swing.JLabel Day3;
    private javax.swing.JLabel Day4;
    private javax.swing.JLabel Day5;
    private javax.swing.JLabel Day6;
    private javax.swing.JLabel Day7;
    private javax.swing.JPanel DayDisplayPanel;
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JPanel ItemizedDisplayPanel;
    private javax.swing.JPanel SpacerPanelOne;
    private javax.swing.JPanel WeekHeaderDisplay;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JPanel myInnerToolBar;
    private javax.swing.JLayeredPane myLayeredPane;
    private javax.swing.JPanel myToolBar;
    // End of variables declaration//GEN-END:variables

    public void displayAvailStats(xIndividualDayPanel indDayPanel) {
    }

    public void hideAvailStats() {
    }

    public xMainAvailabilityPanel getMainAvailPanel() {
        return this.mainPanel;
    }
}
