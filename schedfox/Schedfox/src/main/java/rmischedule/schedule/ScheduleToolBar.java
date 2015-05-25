/*
 * ScheduleToolBar.java
 *
 * Created on July 1, 2005, 10:28 AM
 */
package rmischedule.schedule;

import javax.swing.border.*;
import java.awt.Dimension;
import java.awt.Color;
import javax.swing.*;
import javax.swing.JPanel;
import javax.swing.JComboBox;
import javax.swing.Box;
import java.awt.event.*;
import java.awt.Font;
import java.util.*;

import rmischedule.main.Main_Window;
import rmischedule.components.graphicalcomponents.*;
import rmischedule.schedule.components.*;
import rmischedule.components.graphicalcomponents.myToolBarIcons;
import rmischedule.schedule.components.DateDisplayPanel;
import rmischedule.schedule.components.ScheduleLoadingProgressBar;
import rmischedule.schedule.components.data_components.AlertModel;
import rmischedule.schedule.schedulesizes.ComponentDimensions;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;

/**
 *
 * @author ira
 */
public class ScheduleToolBar extends JToolBar {

    private Schedule_View_Panel myParent;
    private static final String VIEW_ALL_CONFLICTED_SHIFTS = "View all conflicted shifts";
    private static final String VIEW_ALL_SHIFTS = "View all schedules";
    private static final String VIEW_OPEN_SHIFTS = "View open shifts";
    private static final String VIEW_TRAINING_SHIFTS = "View training shifts";
    private static final String VIEW_UNCON_SHIFTS = "View unconfirmed shifts";
    private static final String VIEW_EXTRA_COVERAGE_SHITS = "View extra coverage shifts";
    private static final String VIEW_LAST_12_HOURS = "View By modified date";
    private static final String VIEW_NON_CONFIRMED_SHIFTS = "View unreconciled shifts for past week.";
    private Border myToolBarOverBorder = null;
    private myToolBarIcons myRefreshLabel;
    private myToolBarIcons myReplaceLabel;
    private myToolBarIcons myPrinterLabel;
    private myToolBarIcons myAlertLabel;
    private myToolBarIcons myCloseLabel;
    private myToolBarIcons myHelpLabel;
    private DateDisplayPanel myReloadDates;
    private myToolBarIcons myZoomInIcon;
    private myToolBarIcons myZoomOutIcon;
    private JPanel spacerPanel;
    private ScheduleLoadingProgressBar myProgBar;
    private JComboBox myViewSelecterCombo;
    private static int maxDateRange = 180;
    private String myLimitDateRange = "Please limit your date range to a maximum of " + maxDateRange + " days at a time!";
    private String myRanegOfDate = "Your Initial Date is over the End Date";
    private SmallComboBox myClientFilter;
    private SmallComboBox myEmployeeFilter;
    private SmallComboBox myShiftFilter;
    private HashMap<Employee, Client> employeeAndClient;
    private AlertThread alertThread;

    /**
     * Creates a new instance of ScheduleToolBar
     */
    public ScheduleToolBar(Schedule_View_Panel myparent) {
        myParent = myparent;
        BoxLayout myLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        setLayout(myLayout);
        setFloatable(false);

        myClientFilter = new SmallComboBox();
        myEmployeeFilter = new SmallComboBox();
        myShiftFilter = new SmallComboBox();

        myClientFilter.setFont(new Font("Dialog", Font.BOLD, 12));
        myEmployeeFilter.setFont(new Font("Dialog", Font.BOLD, 12));
        myShiftFilter.setFont(new Font("Dialog", Font.BOLD, 12));

        setMinimumSize(new Dimension(40, 40));
        setPreferredSize(new Dimension(400, 40));
        setMaximumSize(new Dimension(15000, 1000));
        myProgBar = null;
        initComponents();

        //if (AjaxSwingManager.isAjaxSwingRunning()) {
        myZoomInIcon.setVisible(false);
        myZoomOutIcon.setVisible(false);
        myAlertLabel.setVisible(false);
        //}
    }

    public void setEnabled(boolean b) {
        super.setEnabled(b);

        myRefreshLabel.setEnabled(b);
        myReplaceLabel.setEnabled(b);
        myPrinterLabel.setEnabled(b);
        myCloseLabel.setEnabled(b);
        myHelpLabel.setEnabled(b);
        myReloadDates.setEnabled(b);
        myZoomInIcon.setEnabled(b);
        myZoomOutIcon.setEnabled(b);
        myViewSelecterCombo.setEnabled(b);

        myClientFilter.setEnabled(b);
        myEmployeeFilter.setEnabled(b);
        myShiftFilter.setEnabled(b);
    }

    public void removeListeners() {
        myRefreshLabel.removeListeners();
        myReplaceLabel.removeListeners();
        myPrinterLabel.removeListeners();
        myCloseLabel.removeListeners();
        myHelpLabel.removeListeners();
        myZoomInIcon.removeListeners();
        myZoomOutIcon.removeListeners();
        this.myParent = null;
    }

    /**
     * Used to add our initializing Progress Bar...
     */
    public void addLoadBar() {
        myProgBar = new ScheduleLoadingProgressBar();
    }

    public DateDisplayPanel getDatePanel() {
        return myReloadDates;
    }

    public Calendar getBegDate() {
        return myReloadDates.getBegDate();
    }

    public Calendar getEndDate() {
        return myReloadDates.getEndDate();
    }

    public void setDates(java.util.Date bg, java.util.Date ed) {
        myReloadDates.setBegDate(bg);
        myReloadDates.setEndDate(ed);
        myReloadDates.setUpCalendars();
    }

    public void initComponents() {

        myReloadDates = new DateDisplayPanel(myParent);
        spacerPanel = new JPanel();

        spacerPanel.setMaximumSize(new java.awt.Dimension(10000, 10));

        myRefreshLabel = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    myRefreshLabel.setEnabled(false);
                    refreshSchedule();
                }
            }
        };

        myReplaceLabel = new myToolBarIcons();

        myPrinterLabel = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    printSchedule();
                }
            }
        };

        myAlertLabel = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    AlertDialog alertDialog = new AlertDialog(Main_Window.parentOfApplication, true, myParent, alertThread, ScheduleToolBar.this);
                    ArrayList<AlertModel> alertModels = new ArrayList<AlertModel>();
                    Iterator<Employee> empIterator = getEmployeeAndClient().keySet().iterator();
                    while (empIterator.hasNext()) {
                        AlertModel alertModel = new AlertModel();
                        final Employee emp = empIterator.next();
                        final Client client = getEmployeeAndClient().get(emp);
                        alertModel.setAlertText(emp.getFullName() + " has been removed from " + client.getName());
                        alertModel.setClient(client);
                        alertModel.setEmployee(emp);
                        alertModels.add(alertModel);
                    }
                    alertDialog.refreshData();
                    alertDialog.setVisible(true);
                }
            }
        };

        myHelpLabel = new myToolBarIcons();
        myCloseLabel = new myToolBarIcons() {
            @Override
            protected void runOnClick() {
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    CloseScheduleWindow();
                }
            }
        };
        myZoomInIcon = new myToolBarIcons();
        myZoomOutIcon = new myToolBarIcons();

        myRefreshLabel.setToolTipText(
                "Refreshes The Schedule");
        myZoomOutIcon.setToolTipText(
                "Zoom In Schedule");
        myZoomInIcon.setToolTipText(
                "Zoom Out Schedule");
        myReplaceLabel.setToolTipText(
                "Change Date And Refresh Schedule");
        myPrinterLabel.setToolTipText(
                "Print Schedule For Current Week");
        myHelpLabel.setToolTipText(
                "Display Help");
        myCloseLabel.setToolTipText(
                "Close This Schedule");

        myRefreshLabel.setIcon(Main_Window.RecycleRefreshIcon);

        myZoomInIcon.setIcon(Main_Window.Zoom_In_Shift_Icon);

        myZoomOutIcon.setIcon(Main_Window.Zoom_Out_Shift_Icon);

        myAlertLabel.setIcon(Main_Window.Alert_Animated_Icon);

        myReplaceLabel.setIcon(Main_Window.ReplaceIcon);

        myPrinterLabel.setIcon(Main_Window.PrintForToolbarIcon);

        myHelpLabel.setIcon(Main_Window.Help_32x32_Icon);

        myCloseLabel.setIcon(Main_Window.Close_Window_Icon);
        myViewSelecterCombo = new JComboBox();

        myViewSelecterCombo.setPreferredSize(
                new Dimension(210, 22));
        myViewSelecterCombo.setMinimumSize(
                new Dimension(210, 22));
        myViewSelecterCombo.setMaximumSize(
                new Dimension(210, 22));
        myViewSelecterCombo.addItem(VIEW_ALL_SHIFTS);

        myViewSelecterCombo.addItem(VIEW_OPEN_SHIFTS);

        myViewSelecterCombo.addItem(VIEW_ALL_CONFLICTED_SHIFTS);

        myViewSelecterCombo.addItem(VIEW_TRAINING_SHIFTS);

        myViewSelecterCombo.addItem(VIEW_EXTRA_COVERAGE_SHITS);

        myViewSelecterCombo.addItem(VIEW_UNCON_SHIFTS);

        myViewSelecterCombo.addItem(VIEW_LAST_12_HOURS);

        myViewSelecterCombo.addItem(VIEW_NON_CONFIRMED_SHIFTS);
        String[] myShiftFilterOptions = new String[8];
        myShiftFilterOptions[0] = VIEW_ALL_SHIFTS;
        myShiftFilterOptions[1] = VIEW_OPEN_SHIFTS;
        myShiftFilterOptions[2] = VIEW_TRAINING_SHIFTS;
        myShiftFilterOptions[3] = VIEW_UNCON_SHIFTS;
        myShiftFilterOptions[4] = VIEW_LAST_12_HOURS;
        myShiftFilterOptions[5] = VIEW_ALL_CONFLICTED_SHIFTS;
        myShiftFilterOptions[6] = VIEW_EXTRA_COVERAGE_SHITS;
        myShiftFilterOptions[7] = VIEW_NON_CONFIRMED_SHIFTS;

        myShiftFilter.addItems(myShiftFilterOptions);

        myShiftFilter.setText(
                "Show shifts of type");
        loadEmployeeClients myThread = new loadEmployeeClients();

        myThread.start();
        alertThread = new AlertThread();

        alertThread.start();

        add(myReloadDates);

        add(Box.createHorizontalStrut(3));
        add(myRefreshLabel);

        add(Box.createHorizontalStrut(3));
        add(myZoomOutIcon);

        add(Box.createHorizontalStrut(3));
        add(myZoomInIcon);

        add(Box.createHorizontalStrut(3));
        add(myPrinterLabel);

        add(Box.createHorizontalStrut(3));
        add(spacerPanel);
        PrettyTitleBar myFilterTitleBar = new PrettyTitleBar(new Color(168, 182, 214), new Color(213, 222, 242), "", "");

        myFilterTitleBar.setBorder(
                new BevelBorder(BevelBorder.LOWERED));
        add(myFilterTitleBar);

        if (!Main_Window.parentOfApplication.isEmployeeLoggedIn()
                && !Main_Window.parentOfApplication.isClientLoggedIn()) {
            myFilterTitleBar.add(myShiftFilter);
            myFilterTitleBar.add(myEmployeeFilter);
            myFilterTitleBar.add(myClientFilter);
        }

        if (Main_Window.parentOfApplication.isUserAMemberOfGroups(myParent.getConnection(), "ADMIN", "Payroll")) {
            JCheckBox reconCheckbox = new JCheckBox("Display Reconcile Info");
            reconCheckbox.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    myParent.setDisplayReconcile(((JCheckBox) e.getSource()).isSelected());
                    myParent.orderClients(null);
                }
            });
            myFilterTitleBar.add(reconCheckbox);
        }

        add(myAlertLabel);

        add(Box.createHorizontalStrut(3));
        add(myCloseLabel);

        myZoomInIcon.addMouseListener(
                new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent e) {
                        if (!isEnabled()) {
                            return;
                        }
                        if (myParent.isDoneLoadingCompletely) {
                            ComponentDimensions.zoomOut();
                            myParent.repaint();
                        }
                    }
                });

        myZoomOutIcon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
//                System.out.println("*****----->>>>ScheduleToolBar.addMouseListener4:");
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    ComponentDimensions.zoomIn();
                    myParent.repaint();
                }
            }
        });

        myShiftFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("*****----->>>>ScheduleToolBar.actionPerformed:");
                if (myShiftFilter.getSelectedItem() == VIEW_OPEN_SHIFTS) {
                    myParent.displayOpenShiftsOnly(true);
                } else if (myShiftFilter.getSelectedItem() == VIEW_ALL_SHIFTS) {
                    myParent.displayOpenShiftsOnly(false);
                    myEmployeeFilter.setIndexOfCombo(0);
                    myClientFilter.setIndexOfCombo(0);
                } else if (myShiftFilter.getSelectedItem() == VIEW_TRAINING_SHIFTS) {
                    myParent.displayTrainingShiftsOnly(true);
                } else if (myShiftFilter.getSelectedItem() == VIEW_EXTRA_COVERAGE_SHITS) {
                    myParent.displayExtraCoverageShiftsOnly(true);
                } else if (myShiftFilter.getSelectedItem() == VIEW_UNCON_SHIFTS) {
                    myParent.displayUnconfirmedShiftsOnly(true);
                } else if (myShiftFilter.getSelectedItem() == VIEW_LAST_12_HOURS) {
                    String[] myOptions = {"6 hours", "12 hours", "24 hours", "48 hours", "72 hours"};
                    Object selection = JOptionPane.showInputDialog(Main_Window.parentOfApplication, "Show all shifts that have been changed in the last", "Select One", JOptionPane.INFORMATION_MESSAGE, null, myOptions, myOptions[0]);
                    int numHours = 6;
                    if (selection == myOptions[1]) {
                        numHours = 12;
                    } else if (selection == myOptions[2]) {
                        numHours = 24;
                    } else if (selection == myOptions[3]) {
                        numHours = 48;
                    } else if (selection == myOptions[4]) {
                        numHours = 72;
                    }
                    myParent.displayTwelveHourShiftsOnly(true, numHours);
                } else if (myShiftFilter.getSelectedItem() == VIEW_ALL_CONFLICTED_SHIFTS) {
                    //TO DO:
                    myParent.displayConflictedShifts(true);
                } else if (myShiftFilter.getSelectedItem() == VIEW_NON_CONFIRMED_SHIFTS) {
                    myParent.displayUnreconciledShiftsOnly(true);
                }
            }
        });


        myEmployeeFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("*****----->>>>ScheduleToolBar.actionPerformed5:");
                try {
                    myParent.setEmployeeToFilter(((SEmployee) myEmployeeFilter.getSelectedItem()).getId());
                } catch (Exception ex) {
                    myParent.setEmployeeToFilter(-1);
                }
                myParent.orderClients(null);
            }
        });

        myClientFilter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
//                System.out.println("*****----->>>>ScheduleToolBar.actionPerformed6:");
                try {
                    myParent.setClientToFilter(Integer.parseInt(((SClient) myClientFilter.getSelectedItem()).getId()));
                } catch (Exception ex) {
                    myParent.setClientToFilter(-1);
                }
                myParent.orderClients(null);
            }
        });

        myHelpLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
//                System.out.println("*****----->>>>ScheduleToolBar.actionPerformed8:");
                if (!isEnabled()) {
                    return;
                }
                if (myParent.isDoneLoadingCompletely) {
                    rmischedule.schedule.help.BareBonesBrowserLaunch.openURL(Main_Window.LOCATION_OF_HELP_FILES);
                }
            }
        });

    }

    public void refreshSchedule() {
        if (myParent.isDoneLoadingCompletely) {
            myReloadDates.setEnabled(false);

            String companyToLoad = myParent.getCompany();
            String branchToLoad = myParent.getBranch();
            Calendar begginingDate = getBegDate();
            Calendar endDate = getEndDate();
            if (endDate.compareTo(begginingDate) < 0) {
                endDate.add(Calendar.WEEK_OF_YEAR, 1);
            }

            myParent.myParent.remove(myParent);

            int yeardiff = endDate.get(Calendar.YEAR) - begginingDate.get(Calendar.YEAR);
            int daydiff = endDate.get(Calendar.DAY_OF_YEAR) - begginingDate.get(Calendar.DAY_OF_YEAR);

            daydiff += (yeardiff * 365);
            if (daydiff > maxDateRange) {
                JOptionPane.showMessageDialog(null, myLimitDateRange, "Error refreshing schedule", JOptionPane.ERROR_MESSAGE);
                myRefreshLabel.setEnabled(true);
                return;
            }

            if (getEndDate().compareTo(getBegDate()) < 1) {
                JOptionPane.showMessageDialog(null, myRanegOfDate, "Error refreshing schedule", JOptionPane.ERROR_MESSAGE);
                myRefreshLabel.setEnabled(true);
                return;
            }

            Main_Window.myScheduleForm.checkForAndLoadSchedule(companyToLoad, branchToLoad, begginingDate.getTime(), endDate.getTime());
        }

    }

    public void printSchedule() {
        myParent.printSchedule();
    }

    public void CloseScheduleWindow() {
        myParent.myParent.remove(myParent);
    }

    /**
     * Used to find who has schedules... and only list them...
     */
    public void buildEmployeeComboBox() {
        myEmployeeFilter.clearMyComboBox();
        myEmployeeFilter.addItem("Show all employees");
        Vector myTotalEmployees = myParent.getEmployeeList();
        Vector<SEmployee> empsWithShifts = new Vector();
        for (int i = 0; i < myTotalEmployees.size(); i++) {
            Vector<SSchedule> empSchedule = (myParent.mySchedules.getEmployeeSchedules(((SEmployee) myTotalEmployees.get(i))));
            if (empSchedule != null) {
                empsWithShifts.add((SEmployee) myTotalEmployees.get(i));
            }
        }
        myEmployeeFilter.addItems(empsWithShifts);
        myEmployeeFilter.setText("Show employee");
    }

    /**
     * Used to find what clients have schedules...and only list them
     */
    public void buildClientComboBox() {
        myClientFilter.clearMyComboBox();
        myClientFilter.addItem("Show all clients");
        Vector<SMainComponent> myTotalClients = myParent.getClientVector();
        Vector<SMainComponent> clientsWithShifts = new Vector<SMainComponent>();
        for (int i = 0; i < myTotalClients.size(); i++) {
            int myCliSchedSize = (myParent.mySchedules.getClientSchedules((myTotalClients.get(i)))).size();
            if (myCliSchedSize > 1 || myTotalClients.get(i) instanceof STimeOff) {
                clientsWithShifts.add(myTotalClients.get(i));
            }
        }
//        myClientFilter.addItems(clientsWithShifts);
        myClientFilter.addItems(myTotalClients);
        myClientFilter.setText("Show client");
    }

    /**
     * @return the employeeAndClient
     */
    public HashMap<Employee, Client> getEmployeeAndClient() {
        return employeeAndClient;
    }

    /**
     * @param employeeAndClient the employeeAndClient to set
     */
    public void setEmployeeAndClient(HashMap<Employee, Client> employeeAndClient) {
        this.employeeAndClient = employeeAndClient;
    }

    public class AlertThread extends Thread {

        private boolean shouldRefreshSoon = false;
        
        public AlertThread() {
        }

        public void setRefreshSoon() {
            shouldRefreshSoon = true;
        }
        
        @Override
        public void run() {
            ScheduleController scheduleController = new ScheduleController(myParent.getConnection().myCompany);
            while (myParent != null) {
                try {
                    for (int i = 0; i < 120; i++) {
                        if (shouldRefreshSoon) {
                            i = 120;
                            shouldRefreshSoon = false;
                        }
                        sleep(1000);
                    }
                    setEmployeeAndClient(scheduleController.getEmployeesAndClientsNoLongerWorking(Integer.parseInt(myParent.getConnection().myBranch)));
                    SwingUtilities.invokeAndWait(new Runnable() {
                        public void run() {
                            myAlertLabel.setVisible(getEmployeeAndClient().size() > 0);
                        }
                    });
                } catch (Exception e) {
                }
            }
        }
    }

    

    /**
     * Used to load employees, clients into our filter drop downs...fun fun
     */
    private class loadEmployeeClients extends Thread {

        public loadEmployeeClients() {
        }

        public void run() {
            while (myParent != null && !myParent.isDoneLoadingCompletely) {
                try {
                    sleep(500);
                } catch (Exception e) {
                }
            }
            if (myParent != null) {
                buildEmployeeComboBox();
                buildClientComboBox();
            }
        }
    }
}
