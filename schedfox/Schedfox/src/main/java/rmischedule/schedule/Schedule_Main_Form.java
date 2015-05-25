/*
 * Schedule_Main_Form.java
 *
 * Created on July 13, 2005, 9:56 AM
 */
package rmischedule.schedule;

import schedfoxlib.model.ShiftTypeClass;
import com.creamtec.ajaxswing.AjaxSwingManager;
import java.awt.Container;
import java.awt.Dimension;
import java.util.Hashtable;
import java.util.ArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.util.Iterator;
import java.util.Vector;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPopupMenu;

import schedfoxlib.model.Company;
import rmischedule.schedule.components.*;
import rmischedule.main.Main_Window;
import rmischedule.messaging.xMessagingEdit;
import rmischedule.schedule.components.SShift;
import rmischedule.schedule.components.availability.Messaging_Availability;
import schedfoxlib.model.util.Record_Set;

/**
 *
 * @author  ira
 */
public class Schedule_Main_Form extends javax.swing.JInternalFrame {

    private Hashtable<String, Schedule_View_Panel> myOpenSchedulesHash;
    private Schedule_Main_Form thisobject;
    /**
     * Static SShift menu stuff put here so only created once for each Schedule rather than a bunch for each SShift,
     * speed issues resolved here...
     */
    private static JPopupMenu myRightClickMouseMenu = new JPopupMenu("Shift options");
    private static JMenuItem myConfirmMouseMenu = new JMenuItem("Confirm selected shifts");
    private static JMenuItem myDeleteMouseMenu = new JMenuItem("Delete selected shifts for this week");
    private static JMenuItem myPermDeleteMouseMenu = new JMenuItem("Delete selected shifts current and future weeks");
    private static JMenuItem overrideConflictMouseMenu = new JMenuItem("Mark this shift as not conflicted");
    private static SShift ShiftToRunMethodsOn = null;
    /**
     *  variables added by Jeffery Davis on 06/17/2010 to handle messaging
     *      from schedule
     */
    private static JMenuItem messagingMenu = new JMenuItem("Message for this shift");
    public static String companyId;
    public static String branchId;
    private ScheduleDashboardPanel scheduleDashboard = new ScheduleDashboardPanel(this);


    /**
     *  variable addtion by Jeffrey Davis on 06/17/2010 complete
     */
    /**
     *  private method added by Jeffrey Davis for messaging
     */
    private static void hideSortWindow() {
        if (Main_Window.sortParametersWindow != null && Main_Window.sortParametersWindow.isShowing()) {
            Main_Window.sortParametersWindow.reset();
            Main_Window.sortParametersWindow.setVisible(false);
            Main_Window.messagingEditWindow.resetSubForms();
        }
    }

    /** Creates new form Schedule_Main_Form */
    public Schedule_Main_Form() {
        initComponents();
        try {
            this.setMaximum(true);
        } catch (Exception e) {
        }
        thisobject = this;
        myOpenSchedulesHash = new Hashtable(16);
        Schedule_Main_Form.createShiftPopupMenu();
        setVisible(false);

        try {
            if (Main_Window.isClientLoggedIn() || Main_Window.isEmployeeLoggedIn()) {
                this.add(scheduleDashboard, 0);
            }
        } catch (Exception e) {
        }
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            this.setClosable(false);
            ((javax.swing.plaf.basic.BasicInternalFrameUI)
                this.getUI()).getNorthPane().setPreferredSize( new Dimension(0,0) );

        }
    }


    public ScheduleDashboardPanel getScheduleDashboard() {
        return this.scheduleDashboard;
    }

    public void checkForAndLoadSchedule(String company, String branch, java.util.Date st, java.util.Date ed) {
        /**
         *  variable assignments
         */
        this.companyId = company;
        this.branchId = branch;

        AjaxSwingManager.beginOperation();
        loadMyForm myThread = new loadMyForm(company, branch, st, ed);
        myThread.start();
    }

    /**
     * Static method to create our mouse menu only one time for our SShift objects put
     * here rather than running the damn thing multiple times...not needed...
     */
    public static void createShiftPopupMenu() {
        myRightClickMouseMenu.add(myConfirmMouseMenu);
        myRightClickMouseMenu.add(myDeleteMouseMenu);
        myRightClickMouseMenu.add(myPermDeleteMouseMenu);
        myRightClickMouseMenu.add(overrideConflictMouseMenu);
        //
        myRightClickMouseMenu.add(messagingMenu);
        //
        myConfirmMouseMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ShiftToRunMethodsOn.confirmAllSelectedShifts();
            }
        });
        myDeleteMouseMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ShiftToRunMethodsOn.deleteAllSelectedShifts(false);
            }
        });
        myPermDeleteMouseMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ShiftToRunMethodsOn.deleteAllSelectedShifts(true);
            }
        });
        overrideConflictMouseMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                ShiftToRunMethodsOn.setShiftToNotConflicted();
            }
        });
        /**
         *  Added by Jeffrey Davis on 06/10/2010 to handle Messaging
         */
        messagingMenu.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if (ShiftToRunMethodsOn.myShift instanceof DShift) {
                    Hashtable<String, Schedule_View_Panel> tempHash = Main_Window.parentOfApplication.myScheduleForm.getMyOpenSchedulesHash();
                    Schedule_View_Panel myTempSchedule = ((Schedule_View_Panel) tempHash.get(ShiftToRunMethodsOn.myShift.getParent().getBranch() + "," + ShiftToRunMethodsOn.myShift.getParent().getCompany()));
                    Messaging_Availability myMessagingAvailability = new Messaging_Availability(myTempSchedule);
                    if (myMessagingAvailability.getSShiftOfSelectedShifts().size() > 0) {
                        hideSortWindow();
                        if (Main_Window.messagingEditWindow == null) {
                            Main_Window.messagingEditWindow = new xMessagingEdit();
                            Main_Window.parentOfApplication.desktop.add(
                                    Main_Window.messagingEditWindow);
                        }
                        ArrayList<Integer> employeeIds = new ArrayList<Integer>();
                        Vector<SEmployee> emps = myMessagingAvailability.getFullEmployeeList();
                        for (int emp = 0; emp < emps.size(); emp++) {
                            if (emps.get(emp).isVisible()) {
                                employeeIds.add(emps.get(emp).getId());
                            }
                        }
                        
                        Main_Window.messagingEditWindow.setInformation(ShiftToRunMethodsOn.myShift.getParent().getCompany(),
                                ShiftToRunMethodsOn.myShift.getParent().getBranch(), Main_Window.parentOfApplication.getUser(),
                                (DShift)ShiftToRunMethodsOn.myShift, employeeIds);
                        try {
                            Main_Window.messagingEditWindow.setSelected(true);
                            Main_Window.messagingEditWindow.setVisible(true);
                        } catch (Exception exe) {
                        }
                    } else {
                        JOptionPane.showMessageDialog(Main_Window.parentOfApplication, "Please select a shift",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
        /**
         *     Addition by Jeffrey Davis on 06/10/2010 complete
         */
    }

    /**
     * Static method to go and grab our Mouse menu
     */
    public static JPopupMenu getMouseMenuForSShift(SShift shiftToCreateFor) {
        ShiftToRunMethodsOn = shiftToCreateFor;
        if (shiftToCreateFor.isPast()) {
            myPermDeleteMouseMenu.setVisible(false);
        } else {
            myPermDeleteMouseMenu.setVisible(true);
        }
        if (shiftToCreateFor.myShift != null) {
            if (shiftToCreateFor.myShift.getType().isShiftType(ShiftTypeClass.SHIFT_UNCONFIRMED)
                    && Main_Window.parentOfApplication.markUnconShifts()) {
                myConfirmMouseMenu.setVisible(true);
            } else {
                myConfirmMouseMenu.setVisible(false);
            }
            if (shiftToCreateFor.myShift.isMaster()) {
                myPermDeleteMouseMenu.setVisible(true);
            } else {
                myPermDeleteMouseMenu.setVisible(false);
            }
            if (shiftToCreateFor.myShift.hasConflict()) {
                overrideConflictMouseMenu.setVisible(true);
            } else {
                overrideConflictMouseMenu.setVisible(false);
            }
        }
        return myRightClickMouseMenu;
    }

    public void checkForAndLoadSchedule(String branch, String company) {
        checkForAndLoadSchedule(company, branch, null, null);
    }

    /**
     * Called from dispose() method of Schedule_View_Panel, should not be invoked any other way...
     */
    public void remove(Schedule_View_Panel panelToRemove) {
        try {
            ScheduleTab.remove(panelToRemove);
            myOpenSchedulesHash.remove(panelToRemove.getMyHashCode());
            if (myOpenSchedulesHash.size() == 0) {
                setVisible(false);
            }
            panelToRemove.dispose();
            panelToRemove = null;
            ShiftToRunMethodsOn = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Schedule_View_Panel getSchedulePanel(String br, String co) {
        return (Schedule_View_Panel) myOpenSchedulesHash.get(br + "," + co);
    }

    /**
     * Find Appropriate Schedule_View_Panel and pass Record_Set of avails
     */
    public void updateAvailabilityViaServer(ArrayList myList) {
        try {
            Record_Set myRecords = (Record_Set) myList.get(0);
            myRecords.decompressData();
            Schedule_View_Panel svp = (Schedule_View_Panel) myOpenSchedulesHash.get(myRecords.branch + "," + myRecords.company);
            if (svp.isDoneLoadingCompletely) {
                svp.placeAvailability(myRecords);
            }
        } catch (Exception e) {
        }
    }

    public void updateBanned(ArrayList myList) {
        try {
            Record_Set myRecords = (Record_Set) myList.get(0);
            myRecords.decompressData();
            Schedule_View_Panel svp = (Schedule_View_Panel) myOpenSchedulesHash.get(myRecords.branch + "," + myRecords.company);
            if (svp.isDoneLoadingCompletely) {
                svp.parseBannedEmployees(myRecords);
            }
        } catch (Exception e) {
        }
    }

    public void updateClientCerts(ArrayList myList) {
        try {
            Record_Set myRecords = (Record_Set) myList.get(0);
            myRecords.decompressData();
            Schedule_View_Panel svp = (Schedule_View_Panel) myOpenSchedulesHash.get(myRecords.branch + "," + myRecords.company);
            if (svp.isDoneLoadingCompletely) {
                svp.updateClientCertifications(myRecords);
            }
        } catch (Exception e) {
        }
    }

    public void updateEmpCerts(ArrayList myList) {
        try {
            Record_Set myRecords = (Record_Set) myList.get(0);
            myRecords.decompressData();
            Schedule_View_Panel svp = (Schedule_View_Panel) myOpenSchedulesHash.get(myRecords.branch + "," + myRecords.company);
            if (svp.isDoneLoadingCompletely) {
                svp.updateEmpCertifications(myRecords);
            }
        } catch (Exception ex) {
        }
    }

    /**
     * Find Appropriate Schedule_View_Panel....and pass Record_Set of updates
     */
    public void updateDataViaServer(ArrayList arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            ((Record_Set) arrayList.get(i)).decompressData();
        }
        Record_Set myRecords = (Record_Set) arrayList.get(0);
        try {
            ((Schedule_View_Panel) myOpenSchedulesHash.get(myRecords.branch + "," + myRecords.company)).shb.beginUpdate(arrayList);
        } catch (Exception e) {
        }
    }

    public void setVisible(boolean b) {
        super.setVisible(b);
        if (isVisible()) {
            try {
                setSelected(true);
            } catch (Exception ex) {
            }
        }
    }

    public Hashtable<String, Schedule_View_Panel> getMyOpenSchedulesHash() {
        return this.myOpenSchedulesHash;
    }

    /**
     * Gets the biggest height that the panel would need to be to show all schedules
     * at once (if the screen could be big enough).
     */
    public int getMaxSizeForMaxScheduleViewPanel() {
        Iterator<Schedule_View_Panel> svps = myOpenSchedulesHash.values().iterator();
        int maxHeight = 0;
        while (svps.hasNext()) {
            Schedule_View_Panel svp = svps.next();
            if (maxHeight < svp.getSizeToShowAllSchedules()) {
                maxHeight = svp.getSizeToShowAllSchedules();
            }
        }
        return maxHeight;
    }

    private class loadMyForm extends Thread {

        private String company;
        private String branch;
        private java.util.Date st;
        private java.util.Date ed;

        public loadMyForm(String co, String br, java.util.Date start, java.util.Date end) {
            company = co;
            branch = br;
            st = start;
            ed = end;
        }

        public void run() {
            try {
                Schedule_View_Panel myExistingSchedule = ((Schedule_View_Panel) myOpenSchedulesHash.get(branch + "," + company));
                ScheduleTab.setSelectedComponent(myExistingSchedule);
                scheduleDashboard.refreshScreen(myExistingSchedule);
                //myExistingSchedule.showFirstEmployeeInformation();
            } catch (Exception e) {
                Schedule_View_Panel myFirstPanel = new Schedule_View_Panel(thisobject, company, branch, st, ed);
                myOpenSchedulesHash.put(myFirstPanel.getMyHashCode(), myFirstPanel);
                Company myCompany = Main_Window.parentOfApplication.getCompanyById(company);
                String myTitle = "Schedule for " + myCompany.getName();
                if (myCompany.getBranches().size() > 1) {
                    myTitle = myTitle + " - " + Main_Window.parentOfApplication.getBranchById(company, branch).getBranchName();
                }
                try {
                    ScheduleTab.addTab(myTitle, myFirstPanel);
                    ScheduleTab.setSelectedComponent(myFirstPanel);
//                    myFirstPanel.showFirstEmployeeInformation();
//                    scheduleDashboard.refreshScreen(myFirstPanel);
                } catch (Exception exe) {
                }
            }

            ScheduleTab.revalidate();
            setVisible(true);
            toFront();
        }
    }

    private void doLayoutTree(Container cont) {
        cont.doLayout();
        for (int i = 0; i < cont.getComponentCount(); i++) {
            if (cont.getComponent(i) instanceof Container) {
                doLayoutTree((Container) cont.getComponent(i));
            }

        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        ScheduleTab = new javax.swing.JTabbedPane();
        jInternalFrame1 = new javax.swing.JInternalFrame();
        ScheduleTab1 = new javax.swing.JTabbedPane();

        setClosable(true);
        setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        setIconifiable(true);
        setTitle("Schedule");
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                removeAllSchedules(evt);
            }
        });
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        ScheduleTab.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        getContentPane().add(ScheduleTab);

        jInternalFrame1.setClosable(true);
        jInternalFrame1.setDefaultCloseOperation(javax.swing.WindowConstants.HIDE_ON_CLOSE);
        jInternalFrame1.setIconifiable(true);
        jInternalFrame1.setTitle("Schedule");
        jInternalFrame1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentHidden(java.awt.event.ComponentEvent evt) {
                jInternalFrame1removeAllSchedules(evt);
            }
        });
        jInternalFrame1.getContentPane().setLayout(new javax.swing.BoxLayout(jInternalFrame1.getContentPane(), javax.swing.BoxLayout.Y_AXIS));

        ScheduleTab1.setTabLayoutPolicy(javax.swing.JTabbedPane.SCROLL_TAB_LAYOUT);
        jInternalFrame1.getContentPane().add(ScheduleTab1);

        getContentPane().add(jInternalFrame1);

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-730)/2, (screenSize.height-590)/2, 730, 590);
    }// </editor-fold>//GEN-END:initComponents

    private void removeAllSchedules(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_removeAllSchedules
        Iterator<String> scheduleIt = myOpenSchedulesHash.keySet().iterator();
        while (scheduleIt.hasNext()) {
            Schedule_View_Panel schedView = myOpenSchedulesHash.get(scheduleIt.next());
            ScheduleTab.remove(schedView);
            schedView.dispose();
        }
        this.ShiftToRunMethodsOn = null;
    }//GEN-LAST:event_removeAllSchedules

    private void jInternalFrame1removeAllSchedules(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jInternalFrame1removeAllSchedules
        // TODO add your handling code here:
    }//GEN-LAST:event_jInternalFrame1removeAllSchedules

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTabbedPane ScheduleTab;
    private javax.swing.JTabbedPane ScheduleTab1;
    private javax.swing.JInternalFrame jInternalFrame1;
    // End of variables declaration//GEN-END:variables
}
