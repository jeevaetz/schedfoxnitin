/*
 * Schedule_View_Panel.java
 *
 * Created on July 13, 2005, 10:06 AM
 */
package rmischedule.schedule;

import schedfoxlib.model.ShiftTypeClass;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischeduleserver.util.xprint.xPrintData;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.text.DateFormat;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.regex.*;
import java.awt.event.*;
import java.awt.*;
import java.awt.Point;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.BorderLayout;
import java.awt.Rectangle;

import java.io.InputStream;
import javax.swing.*;
import net.sf.jasperreports.engine.JasperExportManager;

import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import schedfoxlib.model.util.Record_Set;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.*;
import rmischeduleserver.mysqlconnectivity.queries.employee.*;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx;
import rmischeduleserver.mysqlconnectivity.queries.schedule_data.training.*;
import rmischeduleserver.mysqlconnectivity.queries.reports.*;

import rmischedule.schedule.components.notes.*;
import rmischedule.schedule.schedulesizes.*;
import rmischedule.schedule.components.data_components.*;
import rmischedule.security.*;
import rmischedule.xprint.components.*;
import rmischedule.data_connection.Connection;
import rmischedule.main.Main_Window;
import rmischedule.schedule.components.*;
import rmischedule.employee.data_components.EmployeeType;
import rmischedule.ireports.viewer.IReportViewer;
import rmischeduleserver.control.AddressController;
import rmischeduleserver.control.BillingController;
import rmischeduleserver.control.ClientController;
import rmischeduleserver.control.EmployeeController;
import rmischeduleserver.control.ScheduleController;
import schedfoxlib.model.Client;
import schedfoxlib.model.Employee;
import schedfoxlib.model.RateCode;
import rmischeduleserver.mysqlconnectivity.queries.util.get_certifications_query;
import schedfoxlib.model.CalcedLocationDistance;

/**
 *
 * @author ira
 */
public class Schedule_View_Panel extends javax.swing.JPanel {

    private boolean isInitializing;
    private SearchPanel mySearchPanel;
    private scrollContentPanel myContentPane;
    //Ints for extracting Schedule information...
    int shift_id_pos = 0;
    int shift_master_id_pos = 0;
    int client_id_pos = 0;
    int employee_id_pos = 0;
    int schedule_date_pos = 0;
    int schedule_id_pos = 0;
    int week_no_pos = 0;
    int start_time_pos = 0;
    int end_time_pos = 0;
    int day_code_pos = 0;
    int override_pos = 0;
    int isDeleted_pos = 0;
    int type_pos = 0;
    //Parse Sched Variables...
    DShift ss;
    String shift_id = "0";
    String shift_master_id = "0";
    String schedule_date = "";
    String client_id = "0";
    String employee_id = "0";
    String schedule_id = "0";
    private calendar_week_class[] colCals;
    private ScheduleHeaderPanel Col_Headers;
    private Calendar tmpDate;
    public NewDShiftEdit pnShiftEdit;
    public static String currentDate;
    private boolean markUnconfirmedShifts;
    private boolean displayReconcile = false;
    private int wk_cnt = 0;
    int week_no = 0;
    int start_time = 0;
    int end_time = 0;
    int day_code = 0;
    int override = 0;
    long milliseconds = 0;
    boolean isDeleted = false;
    boolean filterByContainsClients;
    String type = "0";
    static int badshifts = 0;
    boolean hasInitializedpos = false;
    int moveViewWTF = 0;
    private boolean isMilitaryFormat;
    private int size;
    boolean haveNow = false;
    int currentWeek = 0;
    private Schedule_Employee_Availability_Split_Pane myAvailSplitPane;
    private Schedule_Employee_Availability myAvailability;
    private String Branch;
    private String Company;
    private String Employee;
    private Schedule_View_Panel thisObject;
    private Connection myConnection;
    private ScheduleToolBar myToolBar;
    public New_AEmployee_Information AEinfo;
    public String lastUpdated;
    public JPanel Row_Headers;
    public Hashtable<Integer, SClient> hash_clients;
    public Hashtable<Integer, SEmployee> hash_employees;
    public Hashtable<String, SourceOfConflict> hash_shifts;
    public Hashtable<String, calendar_week_class> dbaseDateToWeek;
    public Hashtable<String, Boolean> clientEmpBanned;
    //USED TO LOAD DATA FAST
    private Record_Set clientRecordSet;
    private Record_Set employeeRecordSet;
    private Record_Set scheduleRecordSet;
    private boolean doneLoadingData;
    public SchedulesMapClass mySchedules;
    private Vector<ScheduleThread> runningThreads;
    private Vector<SEmployee> employees;
    private Vector<SMainComponent> clients;
    public Schedule_Heart_Beat shb;
    //ICONS For Schedule
    public ImageIcon employeePrintIcon;
    public ImageIcon goodDropIcon;
    public final static int EMPLOYEE_NAME_LOCATION = 0;
    private int EMPLOYEE_SIZE = 230;
    //colors
    //public final static Color total_color = new Color(232,237,237);
    public final static Color total_color = new Color(204, 204, 204);
    public final static Color master_color = new Color(209, 230, 250);//jim blue
    //public final static Color client_color = new Color(234, 234, 249);//storeid and left side current
    public final static Color client_color = new Color(209, 230, 250);
    // public final static Color blank_color = Color.WHITE;
    //public final static Color blank_color =new Color(249,249,249);
    //public final static Color blank_color =new Color(240,240,240);
    public final static Color blank_color = new Color(235, 235, 235);
    ;
    //public final static Color blank_c olor = new Color(143, 143, 143);
    // public final static Color past_master_color = Color.BLUE;
    //public final static Color master_color = new Color(205, 205, 255);
    public final static Color sep_color = new Color(100, 100, 100);
    public final static Color shift_color = new Color(255, 255, 255);
    public final static Color trainer_color = new Color(179, 220, 245);
    public final static Color extra_coverage_color = new Color(118, 246, 179);
    public final static Color training_color = new Color(179, 179, 245);
    //public final static Color hcol_color = new Color(204, 204, 255);
    public final static Color hcol_color = new Color(249, 249, 249);//clor for header mondays,tue
    public final static Color employee_del_color = Color.lightGray;
    public final static Color past_master_color = new Color(146, 171, 203);
    private ShiftFilterClass myActiveShiftFilter;
    private ShiftFilterClass myActiveEmployeeFilter;
    private ShiftFilterClass myActiveClientFilter;
    private checkScheduleForConflictsThread myConflictThread;
    private LoadAllDatabaseInformation LoadDataThread;
    public Schedule_Main_Form myParent;
    private int currentSelectedWeek;
    private String currentSelectedWeekDateStart;
    private String currentSelectedWeekDateEnded;
    public int ViewPanelRowCount = 0;
    private int lastSearchedLocation;
    public boolean isDoneLoadingCompletely;
    private LegendPanel myLegend;
    public int myInt = 0;
    private double zoomScale = 1.00;
    private AddNewNote myNewNoteWindow;
    private NoteDisplayPanel myNoteDisplay;
    private JPanel scrollPanelX;
    private JPanel scrollPanelY;
    private HashMap<Integer, RateCode> rateCodes;
    /**
     * Yay Begin Filters!
     */
    private static ShiftFilterClass myNormalViewFilter = new ShiftFilterClass(true) {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            return true;
        }
    };
    private static ShiftFilterClass myExtraCoverageFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            if (shiftToShow.getType().isShiftType(ShiftTypeClass.SHIFT_EXTRA_COVERAGE_SHIFT)) {
                return true;
            }
            return false;
        }
    };
    private static ShiftFilterClass myUnreconciledFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            try {
                DShift shift = (DShift) shiftToShow;
                if (shift.mySShift.isLastWeek() && !shiftToShow.getType().isShiftType(ShiftTypeClass.SHIFT_RECONCILED)) {
                    return true;
                }
            } catch (Exception exe) {
                return false;
            }
            return false;
        }

        public boolean onlyShowByShiftNotSchedule() {
            return true;
        }
    };
    private static ShiftFilterClass myConflictShiftViewFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            if (shiftToShow.hasConflict()) {
                return true;
            }
            return false;
        }
    };
    private static ShiftFilterClass myOpenShiftViewFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            if (shiftToShow.getEmployee().getId() == 0) {
                return true;
            }
            return false;
        }
    };
    private static ShiftFilterClass myTrainShiftViewFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            if (shiftToShow.getType().isShiftType(ShiftTypeClass.SHIFT_TRAINING_SHIFT)) {
                return true;
            }
            return false;
        }

        public boolean showWholeSchedule() {
            return false;
        }
    };
    private static ShiftFilterClass myUnconfirmedShiftViewFilter = new ShiftFilterClass() {
        public boolean shouldShowMe(UnitToDisplay shiftToShow) {
            if (!shiftToShow.getType().isShiftType(ShiftTypeClass.SHIFT_CONFIRMED)
                    && shiftToShow.getEmployee().getId() > 0) {
                return true;
            }
            return false;
        }

        public boolean showWholeSchedule() {
            return false;
        }
    };

    /**
     * Creates new form Schedule_View_Panel -- With dates
     */
    public Schedule_View_Panel(Schedule_Main_Form myParentForm, String company, String branch) {
        build_Schedule_View_Panel(myParentForm, company, branch, null, null);
    }

    public Schedule_View_Panel(
            Schedule_Main_Form myParentForm,
            String company, String branch,
            java.util.Date bg, java.util.Date ed) {
        build_Schedule_View_Panel(myParentForm, company, branch, bg, ed);
    }

    public Schedule_Main_Form getParentForm() {
        return this.myParent;
    }

    /**
     * Creates new form Schedule_View_Panel
     */
    public void build_Schedule_View_Panel(
            Schedule_Main_Form myParentForm,
            String company, String branch,
            java.util.Date bg, java.util.Date ed) {

        Branch = branch;
        Company = company;
        doneLoadingData = false;
        thisObject = this;
        isDoneLoadingCompletely = false;
        isInitializing = true;
        hasInitializedpos = false;
        lastSearchedLocation = 0;
        myParent = myParentForm;
        myActiveShiftFilter = myNormalViewFilter;
        myActiveEmployeeFilter = myNormalViewFilter;
        myActiveClientFilter = myNormalViewFilter;
        size = 0;

        myConnection = new Connection();
        myConnection.setCompany(Company);
        myConnection.setBranch(Branch);

        myToolBar = new ScheduleToolBar(this);
        if (bg != null) {
            myToolBar.setDates(bg, ed);
        }

        onlyLoadData myLoadThread = new onlyLoadData();
        myLoadThread.start();
        initComponents();
        createColHeader();

        myNoteDisplay = new NoteDisplayPanel();
        myMainLayeredPanel.add(myNoteDisplay, JLayeredPane.DRAG_LAYER);
        myNoteDisplay.setVisible(false);
        myNoteDisplay.setBounds(0, 0, 0, getBounds().height);
        mySchedules = new SchedulesMapClass();
        ScheduleSplitPanel.setDividerLocation(new Double(java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth() - EMPLOYEE_SIZE).intValue());
        myContentPane = new scrollContentPanel();
        BoxLayout myContentLayout = new BoxLayout(myContentPane, BoxLayout.Y_AXIS);
        myContentPane.setLayout(myContentLayout);
        myScrollPanel.setViewportView(loadingPanel);
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            myScrollPanel.putClientProperty("ajaxswing.enableAjaxScrolling", Boolean.TRUE);
        }

        // Some non-sense to make the layouts work when the entire content fits inside the screen (no scrolling necessary)
        scrollPanelX = new JPanel();
        scrollPanelY = new JPanel();
        scrollPanelX.setLayout(new BoxLayout(scrollPanelX, BoxLayout.X_AXIS));
        scrollPanelY.setLayout(new BoxLayout(scrollPanelY, BoxLayout.Y_AXIS));
        JPanel spacerPanelX = new JPanel();
        JPanel spacerPanelY = new JPanel();
        spacerPanelX.setPreferredSize(new Dimension(0, 0));
        spacerPanelY.setPreferredSize(new Dimension(0, 0));

        scrollPanelX.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPanelX.setAlignmentY(Component.TOP_ALIGNMENT);
        scrollPanelY.setAlignmentX(Component.LEFT_ALIGNMENT);
        scrollPanelY.setAlignmentY(Component.TOP_ALIGNMENT);
        spacerPanelX.setAlignmentX(Component.LEFT_ALIGNMENT);
        spacerPanelX.setAlignmentY(Component.TOP_ALIGNMENT);
        spacerPanelY.setAlignmentX(Component.LEFT_ALIGNMENT);
        spacerPanelY.setAlignmentY(Component.TOP_ALIGNMENT);
        myContentPane.setAlignmentX(Component.LEFT_ALIGNMENT);
        myContentPane.setAlignmentY(Component.TOP_ALIGNMENT);

        scrollPanelY.add(myContentPane);
        scrollPanelY.add(spacerPanelY);
        scrollPanelX.add(scrollPanelY);
        scrollPanelX.add(spacerPanelX);


        lastUpdated = new String();
        runningThreads = new Vector();
        myLegend = new LegendPanel();

        ScheduleSplitPanel.setBounds(myMainLayeredPanel.getBounds());

        employeePrintIcon = Main_Window.Printer_Icon;
        goodDropIcon = Main_Window.Component_Icon_Green;
        Row_Headers = new EmployeeRowHeaders();

        initializeOtherComponents();
        myConflictThread = new checkScheduleForConflictsThread();

        LoadDataThread = new LoadAllDatabaseInformation(this);
        LoadDataThread.start();

        isMilitaryFormat = !Main_Window.parentOfApplication.is12HourFormat();
        currentDate = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance());
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('C'), "confirmShift");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('c'), "confirmShift");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("DELETE"), "deleteShift");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('+'), "addSize");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('='), "addSize");
        getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('-'), "subSize");

        if (Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
            getActionMap().put("confirmShift", new confirmAction());
            getActionMap().put("deleteShift", new deleteAction());
            getActionMap().put("addSize", new zoomInAction());
            getActionMap().put("subSize", new zoomOutAction());
        }
        markUnconfirmedShifts = Main_Window.parentOfApplication.markUnconShifts();
        myNewNoteWindow = new AddNewNote(this);

        this.myMainLayeredPanel.add(myNewNoteWindow);
        myMainLayeredPanel.setLayer(myNewNoteWindow, JLayeredPane.MODAL_LAYER);
        myNewNoteWindow.setVisible(false);
        if (AjaxSwingManager.isAjaxSwingRunning()) {
            AjaxSwingManager.endOperation();
        }

    }

    /**
     * Returns or initializes the rate codes.
     *
     * @return HashMap<Integer, RateCode>
     */
    public HashMap<Integer, RateCode> getRateCodes() {
        if (rateCodes == null) {
            BillingController billingController = BillingController.getInstance(Company);
            try {
                rateCodes = new HashMap<Integer, RateCode>();
                ArrayList<RateCode> rateCodesArray = billingController.getRateCodes();
                for (int r = 0; r < rateCodesArray.size(); r++) {
                    rateCodes.put(rateCodesArray.get(r).getRateCodeId(), rateCodesArray.get(r));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return rateCodes;
    }

    /**
     * Returns the rate code associated with the integer passed in.
     *
     * @param rateCodeInt
     * @return
     */
    public RateCode getRateCode(Integer rateCodeInt) {
        HashMap<Integer, RateCode> rates = getRateCodes();
        RateCode retVal = null;
        if (rates != null) {
            retVal = rates.get(rateCodeInt);
        }
        return retVal;
    }

    /**
     * Returns the size that it would take to show ALL the schedules on the
     * screen at once.
     *
     * @return
     */
    public int getSizeToShowAllSchedules() {
        int biggestYLocation = 0;
        System.out.println("Component Count: " + myContentPane.getComponents().length);
        for (int c = 0; c < myContentPane.getComponents().length; c++) {
            Component currComp = myContentPane.getComponents()[c];
            if (currComp instanceof SClient) {
                SClient currClient = (SClient) currComp;
                int shiftHeight = currClient.getRowCount() * ComponentDimensions.currentSizes.get("SShift").height;
                int otherHeight = ComponentDimensions.currentSizes.get("RowTotal").height + 30;
                biggestYLocation += shiftHeight + otherHeight;

                System.out.println("Size: " + biggestYLocation);
            }
        }
        return biggestYLocation;
    }

    /**
     * Shows NoteDisplayPanel and displays given notes
     */
    public void displayNotes(ArrayList<NoteData> notes, myNoteIconLabel iconOfOrigin) {
        if (notes.size() > 0) {
            myNoteDisplay.setBounds(0, getBounds().height - 220, getBounds().width, 220);
            myNoteDisplay.loadData(notes, iconOfOrigin);
        }
    }

    public NoteDisplayPanel getMyNoteDisplay() {
        return myNoteDisplay;
    }

    public scrollContentPanel getContentPane() {
        return myContentPane;
    }

    /**
     * Shows the first employee information so that it will display them in the
     *
     */
    public void showFirstEmployeeInformation() {
        try {
            Iterator<SSchedule> myIterator = mySchedules.getAllSchedules().iterator();
            if (myIterator.hasNext()) {
                myParent.getScheduleDashboard().setEmployeeInfo(myIterator.next().getEmployee().getEmployee(), this);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Show the first client information so that it will correct line up in the
     * dashboard
     */
    public void showFirstClientInformation() {
        try {
            Iterator<SSchedule> myIterator = mySchedules.getAllSchedules().iterator();
            if (myIterator.hasNext()) {
                myParent.getScheduleDashboard().setClientInfo(myIterator.next().getClient(), this);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Call to either set Note Window visible or non, with the object it was
     * dropped on
     */
    public void setNoteVisible(boolean setVisible, Object obj) {
        if (setVisible) {
            Rectangle myRect = thisObject.getBounds();
            myNewNoteWindow.setBounds(new Double((myRect.getWidth() - 380) / 2).intValue(), new Double((myRect.getHeight() - 250) / 2).intValue(), 380, 250);
        }
        myNewNoteWindow.setVisible(setVisible, obj);
    }

    public void scaleImageInMainWindow(ImageIcon myImageIcon, int amountToAdd) {
        Image newImage = myImageIcon.getImage();
        int newWidthHeight = newImage.getWidth(this) + amountToAdd;
        myImageIcon.setImage(newImage.getScaledInstance(newWidthHeight, newWidthHeight, Image.SCALE_SMOOTH));

    }

    /**
     * Should SShifts that are unconfirmed show up yellow?
     */
    public boolean shouldMarkUnconShifts() {
        return markUnconfirmedShifts;
    }

    /**
     * Shows the Note Adding Window...yay...
     */
    public void addNewNoteForObject(Object myObject) {
    }

    void displayConflictedShifts(boolean b) {
        //TO DO
        if (b) {
            myActiveShiftFilter = myConflictShiftViewFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    class confirmAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            confirmSelectedShifts();
        }
    }

    class deleteAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            deleteSelectedShifts(false);
        }
    }

    class zoomInAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            ComponentDimensions.zoomIn();
        }
    }

    class zoomOutAction extends AbstractAction {

        public void actionPerformed(ActionEvent e) {
            ComponentDimensions.zoomOut();
        }
    }

    public void confirmSelectedShifts() {
        if (myAvailSplitPane.parentWin.acb.getSelectedShifts().size() == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure you want to confirm all selected shifts?", "Confirm Shifts?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            AvailabilityComboBox.mySShiftVector selectedShift = myAvailSplitPane.parentWin.acb.getSelectedShifts();
            SShift tempShift;
            RunQueriesEx myQuery = new RunQueriesEx();
            for (int i = 0; i < selectedShift.size(); i++) {
                try {
                    tempShift = ((SShift) selectedShift.get(i));
                    if (tempShift.myShift instanceof DShift) {
                        if (!tempShift.myShift.isMaster()) {
                            ((DShift) tempShift.myShift).confirmShift();
                            myQuery.add(((DShift) tempShift.myShift).getQueryEx());
                        }
                    }
                } catch (Exception ex) {
                }
            }
            try {
                getConnection().executeQueryEx(myQuery);
            } catch (Exception exe) {
            }
            selectedShift.clear();
        }
    }

    public AvailabilityComboBox getAcb() {
        return myAvailSplitPane.parentWin.acb;
    }

    /**
     * Marks all selected, conflicted shifts as non conflicted...
     */
    public void unConflictSelectedShifts() {
        if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure you want to turn off conflicts on selected shifts?", "Shut off conflict on shifts?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            AvailabilityComboBox.mySShiftVector selectedShift = myAvailSplitPane.parentWin.acb.getSelectedShifts();
            SShift tempShift;
            RunQueriesEx myQuery = new RunQueriesEx();
            for (int i = 0; i < selectedShift.size(); i++) {
                try {
                    tempShift = ((SShift) selectedShift.get(i));
                    if (tempShift.myShift instanceof DShift) {
                        if (tempShift.myShift.hasConflict()) {
                            ((DShift) tempShift.myShift).deConflictShift();
                            myQuery.add(((DShift) tempShift.myShift).getQueryEx());
                        }
                    }
                } catch (Exception ex) {
                }
            }
            try {
                getConnection().executeQueryEx(myQuery);
            } catch (Exception exe) {
            }
            selectedShift.clear();
        }
    }

    public void deleteSelectedShifts(boolean deletePerm) {
        if (myAvailSplitPane.parentWin.acb.getSelectedShifts().size() == 0) {
            return;
        }
        if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, "Are you sure you want to delete all selected shifts?", "Confirm Delete", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            AvailabilityComboBox.mySShiftVector selectedShift = myAvailSplitPane.parentWin.acb.getSelectedShifts();
            RunQueriesEx myQuery = new RunQueriesEx();
            for (int i = selectedShift.size() - 1; i >= 0; i--) {
                try {
                    if (selectedShift.get(i).myShift instanceof DShift) {
                        QueryGenerateShift myDeleteShift = new QueryGenerateShift((DShift) selectedShift.get(i).myShift);
                        myDeleteShift.deleteShift(deletePerm);
                        myQuery.add(myDeleteShift.getMyQuery());
                        selectedShift.remove(i);
                    } else if (selectedShift.get(i).myShift instanceof DAvailability) {
                        //TODO: do this yo!
                    }
                } catch (Exception ex) {
                }
            }
            try {
                getConnection().executeQueryEx(myQuery);
            } catch (Exception exe) {
            }
        }
    }

    public New_AEmployee_Information getEmpInfo() {
        return AEinfo;
    }

    /**
     * Returns my Employee Availability Panel
     */
    public Schedule_Employee_Availability getAvail() {
        return myAvailability;
    }

    /**
     * Used by our AEmployee Information to provide the current selected week so
     * that we can show overtime for it...
     */
    public int getCurrentSelectedWeek() {
        return currentSelectedWeek;
    }

    public String getCurrentSelectedWeekStart() {
        if (currentSelectedWeekDateStart == null) {
            this.setCurrentSelectedWeek(currentWeek);
        }
        return currentSelectedWeekDateStart;
    }

    public String getCurrentSelectedWeekEnd() {
        if (currentSelectedWeekDateEnded == null) {
            this.setCurrentSelectedWeek(currentWeek);
        }
        return currentSelectedWeekDateEnded;
    }

    /**
     * Frontend method to our hashtable, returns null if no shift is found
     */
    public SourceOfConflict getShiftById(String id) {
        return hash_shifts.get(id);
    }

    /*
     * Keep our height up to date only SClient should call this
     */
    public void updateHeight(int old_height, int new_height) {
        if (!isInitialized()) {
            return;
        }
        ViewPanelRowCount = (ViewPanelRowCount - old_height) + new_height;
    }

    /**
     * Currently called when a shift is selected...from SSHift may want to
     * change to do by what week is dominating listview...
     */
    public void setCurrentSelectedWeek(int newVal) {
        if (colCals == null) {
            updateColCals();
        }
        if (newVal != currentSelectedWeek) {
            currentSelectedWeek = newVal;
            currentSelectedWeekDateStart = colCals[(newVal * 7)].onlyDayMonth;
            currentSelectedWeekDateEnded = colCals[(newVal * 7) + 6].onlyDayMonth;


        }
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < employees.size(); i++) {
                    try {
                        employees.get(i).getAEmp().setEmployeeLabelToAvailabilityColors();
                    } catch (Exception e) {
                    }
                }
            }
        });
    }

    /**
     * Returns String of the integer representing the Company
     */
    public String getCompany() {
        return Company;
    }

    /**
     * Returns String of the integer representing the Branch
     */
    public String getBranch() {
        return Branch;
    }

    public int getBufferValue() {
        return 0;
    }

    public int getWeekCount() {
        long diff = getEndWeek().getTime().getTime() - getBegWeek().getTime().getTime();
        wk_cnt = (int) Math.ceil((float) diff / (float) (1000 * 60 * 60 * 24 * 7));
        return wk_cnt;
    }

    private void updateColCals() {
        int a, i, c, w, wc;
        Calendar rightNow = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        int intToday = (today.get(Calendar.DATE))
                + (today.get(Calendar.MONTH) * 100)
                + (today.get(Calendar.YEAR) * 10000);
        w = 0;
        wc = getWeekCount();
        rightNow.setTime(getBegWeek().getTime());
        rightNow.add(Calendar.WEEK_OF_YEAR, -1);
        rightNow.add(Calendar.MONTH, -1);
        colCals = new calendar_week_class[(wc) * 7];
        dbaseDateToWeek = new Hashtable(wc * 7);
        for (a = 0; a <= wc; a++) {
            for (c = 0; c < 7; c++) {
                rightNow.add(Calendar.DATE, 1);
                colCals[(a * 7) + c] = new calendar_week_class();
                colCals[(a * 7) + c].setTime(rightNow.getTime(), a);
                dbaseDateToWeek.put(colCals[(a * 7) + c].dbaseFormat, colCals[(a * 7) + c]);
            }
        }
    }

    public void createColHeader() {
        Calendar rightNow = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        int i, w, wc;

        Col_Headers = new ScheduleHeaderPanel();

        w = 0;

        wc = getWeekCount();

        rightNow.setTime(getBegWeek().getTime());
        colCals = new calendar_week_class[((wc) * 7) + 1];
        dbaseDateToWeek = new Hashtable(wc * 7);
        for (int a = 0; a < wc; a++) {
            for (int c = 0; c < 7; c++) {
                if (StaticDateTimeFunctions.areCalendarsEqual(rightNow, today)) {
                    haveNow = true;
                    currentWeek = a;
                }

                individualHeaderPanel pnName = new individualHeaderPanel(StaticDateTimeFunctions.getDayOfWeek(rightNow),
                        DateFormat.getDateInstance(DateFormat.SHORT).format(rightNow.getTime()));

                colCals[(a * 7) + c] = new calendar_week_class();
                colCals[(a * 7) + c].setTime(rightNow.getTime(), a);
                dbaseDateToWeek.put(colCals[(a * 7) + c].dbaseFormat, colCals[(a * 7) + c]);
                Col_Headers.add(pnName);

                rightNow.add(Calendar.DATE, 1);
            }
            Total_Pane tp = new Total_Pane();
            Col_Headers.add(tp);
        }
        colCals[colCals.length - 1] = new calendar_week_class();
        colCals[colCals.length - 1].setTime(rightNow.getTime(), colCals[colCals.length - 2].Week + 1);
    }

    /**
     * Is Form initializing or has all data loaded
     */
    public boolean isInitialized() {
        return !isInitializing;
    }

    public void moveToShift(DShift activeShift) {
    }

    /**
     * Used to check all employees for conflicts...pass in empId to check
     * employee, or null if you want to check all Schedules...
     */
    public void CheckScheduleForConflicts(String empId) {
        myConflictThread.runMe(empId);
    }

    public void addClient(SMainComponent sc) {
        clients.add(sc);
        if (sc instanceof SClient) {
            hash_clients.put(sc.getClientData().getClientId(), (SClient) sc);
        }
    }

    /**
     * Currently called from our toolbar used to print out a schedule for the
     * current week...
     */
    public void printSchedule() {
        client_query myClientQuery = new client_query();
        employee_query myEmployeeQuery = new employee_query();
        generic_assemble_schedule_query myQuery = new Generic_Report_Schedule_Query();
        Calendar endDate = Calendar.getInstance();
        Calendar startDate = Calendar.getInstance();
        String startWeek = "";
        String endWeek = "";
        GetDatesForPrintDialog myDateDialog = new GetDatesForPrintDialog(Main_Window.parentOfApplication, true);
        Integer companyId = Integer.parseInt(this.Company);
        Calendar[] returnval = myDateDialog.getDatesFromDialog(companyId);
        startDate = returnval[0];
        endDate = returnval[1];
        if (startDate == null) {
            return;
        }
        startWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(startDate);
        endWeek = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(endDate);
        String employee = "";
        String header = "Schedule for " + Main_Window.parentOfApplication.getCompanyNameById(this.Company) + ", " + Main_Window.parentOfApplication.getBranchNameById(Branch);
        if (myActiveShiftFilter == myOpenShiftViewFilter) {
            employee = "0";
            header = "Open shifts for " + Main_Window.parentOfApplication.getCompanyNameById(this.Company) + ", " + Main_Window.parentOfApplication.getBranchNameById(Branch);
        } else if (myActiveShiftFilter == myTrainShiftViewFilter) {
            myQuery = new assemble_schedule_for_training_query();
            header = "Training shifts for " + Main_Window.parentOfApplication.getCompanyNameById(this.Company) + ", " + Main_Window.parentOfApplication.getBranchNameById(Branch);
        } else if (myActiveShiftFilter == myUnconfirmedShiftViewFilter) {
            myQuery = new assemble_schedule_for_unconfirmed_report();
            header = "Unconfirmed shifts for " + Main_Window.parentOfApplication.getCompanyNameById(this.Company) + ", " + Main_Window.parentOfApplication.getBranchNameById(Branch);
        }
        myClientQuery.update("", "", "");
        myEmployeeQuery.update("", true);
        myQuery.update("", employee, this.employee_id, startWeek, endWeek, "", "", true);

        xPrintData tableData = new xPrintData(myQuery, myClientQuery, myEmployeeQuery, startWeek, endWeek, myConnection.getServer(), myConnection.myCompany, myConnection.myBranch);
        tableData.setSortType(tableData.SORT_BY_CLIENT);
        try {

            InputStream reportStream = getClass().getResourceAsStream("/rmischedule/ireports/ClientSchedule.jasper");

            Hashtable parameters = new Hashtable();
            parameters.put("SUBREPORT_DIR", "rmischedule/ireports/");
            JasperPrint report = JasperFillManager.fillReport(reportStream, parameters, tableData);
            if (AjaxSwingManager.isAjaxSwingRunning()) {
                byte[] fileBytes = JasperExportManager.exportReportToPdf(report);
                ClientAgent.getCurrentInstance().print(fileBytes);
            } else {
                IReportViewer viewer = new IReportViewer(report);
                Main_Window.parentOfApplication.desktop.add(viewer);
                viewer.showForm();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean updateClient(Record_Set rs) {
        SClient myClient = hash_clients.get(rs.getInt("client_id"));
        boolean change = false;
        if (myClient != null) {
            myClient.setClientName(rs.getString("client_name"));
            myClient.setDeleted(rs.getInt("client_is_deleted"));
            myClient.getClientData().setClientEndDate(rs.getDate("client_end_date"));
        } else {
            SClient newClient = new SClient(this, rs);
            addClient(newClient);
            plantClients(newClient);
            orderClients(null);
            this.revalidate();
        }

        return change;
    }

    /**
     * Used by the heartbeat either adds new employee or updates existing one...
     */
    public boolean updateEmployee(SEmployee se) {
        SEmployee tempEmp = (SEmployee) hash_employees.get(se.getId());
        boolean change = false;
        if (tempEmp != null) {
            tempEmp.updateEmployee(se.getEmployee());
        } else {
            addEmployee(se);
            change = true;
        }

        return change;
    }

    /**
     * Should only be called by updateEmployee method if new Employee because it
     * will rebuild our avail list...and other list...
     */
    public void addEmployee(SEmployee se) {
        employees.add(se);
        hash_employees.put(se.getId(), se);
        myAvailability.refreshEmployeesList(employees);
        myAvailability.acb.processComboChange(myAvailability.acb.getSelectedIndex());
    }

    public void updateShift(final DShift d) {
        Runnable updateShiftRunnable = new Runnable() {
            public void run() {
                DShift myShift = null;

                myShift = (DShift) hash_shifts.get(d.getShiftId());
                if (myShift == null && d.isDeleted) {
                    myShift = (DShift) hash_shifts.get("-" + d.getRealMasterId() + "/" + d.getDatabaseDate());
                }
                if (myShift != null) {
                    if (d.isDeleted()) {
                        try {
                            myShift.mySShift.cleareInfo();
                        } catch (Exception ex) {
                        }
                    } else if ((myShift.getEmployee().getId() != d.getEmployee().getId())
                            || (!myShift.getType().equals(d.getType()))
                            || (myShift.getStartTime() != d.getStartTime())
                            || (myShift.getEndTime() != d.getEndTime())
                            || (myShift.isDeleted() != d.isDeleted()
                            || (myShift.getGroupId() != d.getGroupId()))) {
                        if (!myShift.getEmployee().equals(d.getEmployee())
                                || !myShift.getGroupId().equals(d.getGroupId())) {
                            myShift.updateShift(d);
                            myShift.getClient().addNewShiftToClient(myShift);

                        } else {
                            myShift.updateShift(d);
                            //Was causing crapout on certain heartbeats...keep in
                            if (myShift.mySShift != null) {
                                myShift.mySShift.updateInfo();
                            }
                        }
                    }
                } else {
                    if (!d.isDeleted()) {
                        if (d.getDatabaseFormatDate().compareTo(d.getEndDate()) <= 0) {
                            d.getClient().addNewShiftToClient(d);
                            addShiftToHash(d);
                        }
                    }
                }
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            updateShiftRunnable.run();
        } else {
            SwingUtilities.invokeLater(updateShiftRunnable);
        }
    }

    /**
     * Adds shift to Hash and in Shift Vector...
     */
    public void addShiftToHash(SourceOfConflict d) {
        hash_shifts.put(d.getShiftId(), d);
    }

    /**
     * Called on double click Client Header to Edit Client Info
     */
    public void editClient(Client client) {
        Main_Window.getClientEditWindow().setDeleted(client.getClientIsDeleted() + "");
        Main_Window.getClientEditWindow().setInformation(getCompany(), getBranch());
        Main_Window.getClientEditWindow().setVisible(true);
        Main_Window.getClientEditWindow().setSelected(client);
    }

    /**
     * Used to show our Edit Employee Window
     */
    public void editEmployee(String eid) {
        SEmployee myEmp = hash_employees.get(Integer.parseInt(eid));
        if (myEmp.isDeleted()) {
            Main_Window.getEmployeeEditWindow().setDeleted(true);
        } else {
            Main_Window.getEmployeeEditWindow().setDeleted(false);
        }
        Main_Window.getEmployeeEditWindow().setVisible(true);
        Main_Window.getEmployeeEditWindow().setInformation(getCompany(), getBranch(), eid);
    }

    public int getBegOfWeek() {
        return StaticDateTimeFunctions.getStartOfWeekInt(Integer.parseInt(Company));
    }

    public Calendar getBegWeek() {
        return myToolBar.getBegDate();
    }

    public Calendar getEndWeek() {
        return myToolBar.getEndDate();
    }

    public boolean shouldDisplayReconcile() {
        return displayReconcile;
    }

    public void setDisplayReconcile(boolean recon) {
        this.displayReconcile = recon;
    }

    public boolean getShouldDisplayClientsFilter() {
        return filterByContainsClients;
    }

    /**
     * Given a week and day will produce a date...in dbase format 2005-10-01
     */
    public String getDateByWeekDay(int weekNo, int day) {
        try {
            return colCals[(weekNo * 7) + (day)].dbaseFormat;
        } catch (Exception e) {
            return colCals[colCals.length - 1].dbaseFormat;
        }
    }

    public Calendar getDate(int w, int d) {
        if (colCals == null) {
            updateColCals();
        }
        try {
            return colCals[(w * 7) + d - 1].myDate;
        } catch (Exception e) {
            return colCals[colCals.length - 1].myDate;
        }
    }

    /**
     * The generated hash value to be used for this object.
     *
     * @return String
     */
    public String getMyHashCode() {
        return this.getBranch() + "," + this.getCompany();
    }

    /**
     * Takes in a Database Format Date, returns a day number via a hash
     */
    public int getDayNumber(String date) {
        try {
            return dbaseDateToWeek.get(date).getDayCode();
        } catch (Exception e) {
            badshifts++;
            return colCals.length + 100;
        }
    }

    /**
     * Method to return Week Id for given date
     */
    public int getWeekNo(String date) {
        try {
            return dbaseDateToWeek.get(date).getWeekNo();
        } catch (Exception e) {
            badshifts++;
            return colCals.length + 100;
        }
    }

    public String getRegCalendarByDate(String date) {
        for (int i = 0; i < colCals.length; i++) {
            if (colCals[i].equals(date)) {
                return colCals[i].readableDate;
            }
        }
        return "00/00/0000";
    }

    /**
     * Removes Shift from our shifts...should be called by cleareInfo...
     */
    public void removeShift(SourceOfConflict shiftToRemove) {
        hash_shifts.remove(shiftToRemove.getShiftId());
    }

    public boolean isMilitaryTimeFormat() {
        return isMilitaryFormat;
    }

    /**
     * Returns specified client....
     */
    public SClient getClient(Integer cid) {
        return hash_clients.get(cid);
    }

    /**
     * Returns specified employee....
     */
    public SEmployee getEmployee(String eid) {
        return (SEmployee) (hash_employees.get(Integer.parseInt(eid)));
    }

    /**
     * Returns my LayeredPanel.
     */
    public JLayeredPane getLayeredPane() {
        return myMainLayeredPanel;
    }

    /**
     * Returns my ScrollPane
     */
    public JScrollPane getScrollPane() {
        return myScrollPanel;
    }

    public Vector getAllOpenShifts() {
        return new Vector();
    }

    /**
     * Display open shifts or not depending on true or false passed in
     */
    public void displayOpenShiftsOnly(boolean val) {
        if (val) {
            myActiveShiftFilter = myOpenShiftViewFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    public void displayExtraCoverageShiftsOnly(boolean val) {
        if (val) {
            myActiveShiftFilter = myExtraCoverageFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    public void displayUnreconciledShiftsOnly(boolean val) {
        if (val) {
            myActiveShiftFilter = myUnreconciledFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    /**
     * Display only schedules with training shifts...
     */
    public void displayTrainingShiftsOnly(boolean val) {
        if (val) {
            myActiveShiftFilter = myTrainShiftViewFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    /**
     * Display only unconfirmed shifts...
     */
    public void displayUnconfirmedShiftsOnly(boolean val) {
        if (val) {
            myActiveShiftFilter = myUnconfirmedShiftViewFilter;
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    /**
     * Display only shifts modified in last twelve hours...
     */
    public void displayTwelveHourShiftsOnly(boolean val, final int numOfHoursToShow) {
        if (val) {
            myActiveShiftFilter = new ShiftFilterClass() {
                public boolean shouldShowMe(UnitToDisplay shiftToShow) {
                    Calendar currentTime = Calendar.getInstance();
                    Calendar twelveHour = Calendar.getInstance();
                    Calendar shiftTime = StaticDateTimeFunctions.setCalendarToWithHours(shiftToShow.getLastUpdateStr());
                    currentTime.add(Calendar.HOUR_OF_DAY, 2);
                    twelveHour.add(Calendar.HOUR_OF_DAY, -numOfHoursToShow);
                    if (StaticDateTimeFunctions.compareDatesWithHourMinute(currentTime, shiftTime) >= 0
                            && StaticDateTimeFunctions.compareDatesWithHourMinute(twelveHour, shiftTime) <= 0) {
                        return true;
                    }
                    return false;
                }

                public boolean showWholeSchedule() {
                    return false;
                }
            };
        } else {
            myActiveShiftFilter = myNormalViewFilter;
        }
        orderClients(null);
    }

    /**
     * Runs this schedule through all active filters to see if it should be
     * displayed or not used by order clients...
     */
    public boolean runScheduleThroughFilters(SSchedule schedToTest) {
        boolean shouldDisplay = true;
        boolean ranThroughNorm = false;

        if (myActiveClientFilter != myNormalViewFilter && shouldDisplay) {
            shouldDisplay = shouldDisplay && myActiveClientFilter.runFilterOnThisSchedule(schedToTest);
        } else if (!ranThroughNorm) {
            myActiveClientFilter.runFilterOnThisSchedule(schedToTest);
            ranThroughNorm = true;
        }
        if (myActiveEmployeeFilter != myNormalViewFilter && shouldDisplay) {
            shouldDisplay = shouldDisplay && myActiveEmployeeFilter.runFilterOnThisSchedule(schedToTest);
        } else if (!ranThroughNorm) {
            myActiveEmployeeFilter.runFilterOnThisSchedule(schedToTest);
            ranThroughNorm = true;
        }
        if (myActiveShiftFilter != myNormalViewFilter) {
            shouldDisplay = shouldDisplay && myActiveShiftFilter.runFilterOnThisSchedule(schedToTest);
        } else if (!ranThroughNorm) {
            myActiveShiftFilter.runFilterOnThisSchedule(schedToTest);
            ranThroughNorm = true;
        }
        return shouldDisplay;
    }

    /**
     * Gets the current schedule view filter
     */
    public ArrayList<ShiftFilterClass> getActiveFilter() {
        ArrayList<ShiftFilterClass> myFilters = new ArrayList();
        myFilters.add(myActiveShiftFilter);
        myFilters.add(myActiveEmployeeFilter);
        myFilters.add(myActiveClientFilter);
        return myFilters;
    }

    /**
     * Gets lists of employees....God kinda self explainatory
     */
    public Vector<SEmployee> getEmployeeList() {
        return employees;
    }

    /**
     * Used to keep track of all threads running
     */
    public void registerNewThread(ScheduleThread myThread) {
        runningThreads.add(myThread);
    }

    public Vector<SSchedule> getEmployeeSchedules(SEmployee employee) {
        return this.mySchedules.getEmployeeSchedules(employee);
    }

    /**
     * Trying to replace our search function, with the text field in the
     * DateDisplayPanel zooming to a specific Client...
     */
    public void searchClientAndEmployees(String newText, boolean reset) {
        boolean found = false;
        SMainComponent clientMatched;
        SSchedule empMatched;
        clientMatched = null;
        empMatched = null;
        int locationSoFar;
        for (int timesSearched = 0; timesSearched < 2; timesSearched++) {
            if (reset) {
                lastSearchedLocation = -1;
            }
            locationSoFar = 0;

            for (int i = 0; i < clients.size(); i++) {
                String myString = " " + clients.get(i).getFullClientName();
                Pattern p = Pattern.compile(".+\\p{Space}*" + newText.toLowerCase() + ".*");
                Matcher m = p.matcher(myString.toLowerCase());
                if (m.matches() && locationSoFar > lastSearchedLocation) {
                    clientMatched = clients.get(i);
                    lastSearchedLocation = locationSoFar;
                    break;
                }
                SortedSet myMap = mySchedules.getClientSchedules(clients.get(i));
                Iterator<SSchedule> myIterator = myMap.iterator();
                while (myIterator.hasNext()) {
                    SSchedule currentSchedule = myIterator.next();
                    String myEmp = " " + currentSchedule.getEmployee().getName();
                    locationSoFar++;
                    Pattern pat = Pattern.compile(".+\\p{Space}*" + newText.toLowerCase() + ".*");
                    Matcher mat = pat.matcher(myEmp.toLowerCase());
                    if (mat.matches() && empMatched == null && locationSoFar > lastSearchedLocation) {
                        try {
                            empMatched = currentSchedule;
                            lastSearchedLocation = locationSoFar;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
            if (clientMatched == null && empMatched == null) {
                reset = true;
            } else {
                timesSearched = 2;
            }
        }
        if (clientMatched != null) {
            Point rp = clientMatched.getLocation();
            Rectangle r = new Rectangle(
                    0,
                    -myScrollPanel.getViewport().getViewRect().y + rp.y,
                    myScrollPanel.getViewport().getWidth(),
                    myScrollPanel.getViewport().getHeight());
            myScrollPanel.getViewport().scrollRectToVisible(r);
            return;
        } else if (empMatched != null) {
            Point rp = empMatched.getMyLocation();
            Rectangle r = new Rectangle(
                    0,
                    -myScrollPanel.getViewport().getViewRect().y + rp.y,
                    myScrollPanel.getViewport().getWidth(),
                    myScrollPanel.getViewport().getHeight());
            myScrollPanel.getViewport().scrollRectToVisible(r);
            return;
        } else {
            myScrollPanel.getViewport().scrollRectToVisible(
                    new Rectangle(
                    0,
                    -myScrollPanel.getViewport().getViewRect().y,
                    myScrollPanel.getViewport().getWidth(),
                    myScrollPanel.getViewport().getHeight()));
        }
    }

    public String getLastUpdated() {
        return this.lastUpdated;
    }

    /**
     * These dispose methods are so very important... We have a massive memory
     * leak since it appears java's GC cannot handle all of our bidirection
     * references... Therefore it is very very important as you add classes that
     * you properly dispose all new private class, and remove all objects from
     * sub panels. Please verify from time to time in HEAP stack that this is
     * still working.
     */
    public void dispose() {
        this.myParent = null;
        for (int i = 0; i < runningThreads.size(); i++) {
            ScheduleThread currThread = runningThreads.get(i);
            currThread.killMe();
            currThread = null;
        }
        ComponentListener[] cl = this.getComponentListeners();
        for (int i = 0; i < cl.length; i++) {
            this.removeComponentListener(cl[i]);
        }
        this.removeAll();
        try {
            this.getContentPane().removeNotify();
        } catch (Exception e) {
        }
        try {
            this.getContentPane().removeAll();
        } catch (Exception e) {
        }
        this.myContentPane = null;

        this.myNoteDisplay.killThreads();
        this.myToolBar.removeListeners();

        if (this.getClientVector() != null) {
            for (int s = 0; s < this.getClientVector().size(); s++) {
                SMainComponent currentClient = this.getClientVector().get(s);
                currentClient.removeAll();
                currentClient.dispose();
                currentClient = null;
            }
        }

        if (this.getEmployeeList() != null) {
            for (int e = 0; e < this.getEmployeeList().size(); e++) {
                SEmployee currentEmployee = this.getEmployeeList().get(e);
                currentEmployee.dispose();
                currentEmployee = null;
            }
        }

        mySchedules.dispose();

        this.hash_shifts = null;
        this.clients = null;
        this.employees = null;
    }

    /**
     * Gets other components from the Schedule_view...
     */
    public void initializeOtherComponents() {
        Runnable initRunnable = new Runnable() {
            public void run() {
                myAvailability = new Schedule_Employee_Availability(thisObject);
                myAvailSplitPane = new Schedule_Employee_Availability_Split_Pane(myAvailability);

                myToolBar.setEnabled(false);
                if (!Main_Window.parentOfApplication.isEmployeeLoggedIn()
                        && !Main_Window.parentOfApplication.isClientLoggedIn()) {
                    ControlPanel.add(myToolBar);
                }
                AEinfo = new New_AEmployee_Information(thisObject);
                myMainLayeredPanel.add(AEinfo, JLayeredPane.DRAG_LAYER);
                AvailabilityPanel.add(myAvailSplitPane);

                if (Main_Window.parentOfApplication.isEmployeeLoggedIn()
                        || Main_Window.parentOfApplication.isClientLoggedIn()) {
                    AvailabilityPanel.setVisible(false);
                }

                SchedulePanel.add(myLegend, BorderLayout.SOUTH);
            }
        };
        if (SwingUtilities.isEventDispatchThread()) {
            initRunnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(initRunnable);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Schedule_Employee_Availability_Split_Pane getSeaContainer() {
        return myAvailSplitPane;
    }

    /**
     * Returns the Connection object
     */
    public Connection getConnection() {
        return myConnection;
    }

    public AvailabilityComboBox.mySShiftVector getSelectedShifts() {
        return myAvailability.acb.getSelectedShifts();
    }

    /*
     *  Shift Edit Code.  If the sshift does not have a DShift then one will be
     *  created if accepted.
     */
    public void editShift(SShift sp) {
        if (pnShiftEdit == null) {
            pnShiftEdit = new NewDShiftEdit(Main_Window.parentOfApplication, true, this, sp);

        } else {
            pnShiftEdit.updateValues(sp);
        }
        pnShiftEdit.setVisible(true);
    }

    /**
     * View only client given by client id passed in, pass in -1 to display
     * everything!
     */
    public void setClientToFilter(final int val) {
        if (val != -1) {
            myActiveClientFilter = new ShiftFilterClass() {
                public boolean shouldShowMe(UnitToDisplay shiftToShow) {
                    if (shiftToShow.getClient().getId().equals(val + "")) {
                        return true;
                    }
                    return false;
                }

                protected boolean showWholeSchedule(SSchedule mySched) {
                    if (mySched.getClient().getId().equals(val + "")) {
                        return true;
                    }
                    return false;
                }
            };
        } else {
            myActiveClientFilter = myNormalViewFilter;
        }
    }

    /**
     * View only emp given by emp id passed in, pass in -1 to display
     * everything!
     */
    public void setEmployeeToFilter(final int val) {
        if (val != -1) {
            myActiveEmployeeFilter = new ShiftFilterClass() {
                public boolean shouldShowMe(UnitToDisplay shift) {
                    if (shift.getEmployee().getId() == val) {
                        return true;
                    }
                    return false;
                }
            };
        } else {
            myActiveEmployeeFilter = myNormalViewFilter;
        }
    }

    public boolean getPuzzlePeicesShown() {
        return false;
    }

    public void sortClients() {
        java.util.Collections.sort(clients);
    }

    public void plantClients(SClient client) {
        int i, c, h;

        h = 0;
        c = clients.size();

        sortClients();
        for (i = 0; i < c; i++) {
            if (client == null || (client != null && client == clients.get(i))) {
                myContentPane.add(clients.get(i), i);
                clients.get(i).plantShifts();
            }
        }
    }

    /**
     * Pretty basic returns the Vector of SClients...
     */
    public Vector<SMainComponent> getClientVector() {
        return clients;
    }

    public boolean getClients() {
        Record_Set rs = clientRecordSet;
        int size = rs.length();

        clients = new Vector(size);
        hash_clients = new Hashtable<Integer, SClient>(size * 2);

        for (int i = 0; i < size; i++) {
            SClient client = new SClient(this, rs);
            clients.add(client);
            hash_clients.put(client.getClientData().getClientId(), client);
            rs.moveNext();
        }

        if (!Main_Window.isClientLoggedIn()) {
        //clients.add(new STimeOff(this, rs, "Non-Availability", Main_Window.NON_AVAILABLE * -1));
            clients.add(new STimeOff(this, rs, "Vacation", Main_Window.NON_AVAILABLE_VACATION * -1));
            clients.add(new STimeOff(this, rs, "Personal", Main_Window.NON_AVAILABLE_PERSONAL * -1));
        }

        return true;
    }

    public boolean getEmployees() {
        Record_Set rs = employeeRecordSet;

        if (rs != null) {
            int i, c;
            int size = rs.length() + 1;

            employees = new Vector(size);
            hash_employees = new Hashtable(size);

            // add blank employee
            Employee blankEmployee = new Employee(new Date());
            blankEmployee.setEmployeeId(0);
            employees.add(new SEmployee(blankEmployee, null, this, "f", "", null));
            employees.get(0).setAEmployee(new AEmployee(employees.get(0), this));
            hash_employees.put(0, employees.get(0));

            AddressController addressController = AddressController.getInstance(Company);
            HashMap<Integer, ArrayList<CalcedLocationDistance>> locationDistance = addressController.getAllDistanceInfoForBranch(Integer.parseInt(this.Branch));

            load_employee_to_stores_query employees_to_location = new load_employee_to_stores_query();
            Record_Set client_to_location = getConnection().executeQuery(employees_to_location);
            Hashtable<String, ArrayList<String>> clientToLocs =
                    new Hashtable<String, ArrayList<String>>();
            if (client_to_location.length() > 0) {
                do {
                    ArrayList<String> vals = clientToLocs.get(client_to_location.getString("employee_id"));
                    if (vals == null) {
                        clientToLocs.put(client_to_location.getString("employee_id"), new ArrayList<String>());
                        vals = clientToLocs.get(client_to_location.getString("employee_id"));
                    }
                    vals.add(client_to_location.getString("client_id"));
                } while (client_to_location.moveNext());
            }

            for (i = 1; i < size; i++) {
                Employee myEmployee = new Employee(new Date(), rs);
                SEmployee aEmployee = new SEmployee(
                        myEmployee,
                        locationDistance.get(myEmployee.getEmployeeId()),
                        this,
                        rs.getString("hasnotes"),
                        rs.getString("employee_types"),
                        clientToLocs.get(rs.getString("id")));
                try {
                    aEmployee.setAllow_sms_messaging(rs.getBoolean("sms_messaging"));
                } catch (Exception e) {
                    aEmployee.setAllow_sms_messaging(false);
                }

                employees.add(aEmployee);
                hash_employees.put(Integer.parseInt(rs.getString("id")), employees.get(i));
                rs.moveNext();
            }
        }
        return true;
    }

    /**
     * Method to load banned employees, clients combos into a hash, the key is
     * just the client id then space then employee id, this is just used for
     * quick lookups since every entry in this table is assumed to be banned,
     * therefore if an entry exists it is a banned combo...might want to
     * heartbeat this, I would suggest completely running this method rather
     * than investing further logic in this, since the data is quite small, only
     * two ids...
     */
    public void loadBannedEmployees() {
        schedule_client_banned_query myBannedQuery = new schedule_client_banned_query();
        myConnection.prepQuery(myBannedQuery);
        Record_Set bannedEmps = new Record_Set();
        try {
            bannedEmps = myConnection.executeQuery(myBannedQuery);
        } catch (Exception e) {
        }
        parseBannedEmployees(bannedEmps);
    }

    public void parseBannedEmployees(Record_Set bannedEmps) {
        Vector<SEmployee> checkEmps = new Vector();
        if (clientEmpBanned != null) {
            for (String s : clientEmpBanned.keySet()) {
                StringTokenizer st = new StringTokenizer(s);
                st.nextToken();
                SEmployee emp = this.hash_employees.get(Integer.parseInt(st.nextToken()));
                if (emp != null && !checkEmps.contains(emp)) {
                    checkEmps.add(emp);
                }
            }
        }

        clientEmpBanned = new Hashtable();
        for (int i = 0; i < bannedEmps.length(); i++) {
            clientEmpBanned.put(bannedEmps.getString("cid") + " " + bannedEmps.getString("eid"), true);
            SEmployee emp = this.hash_employees.get(Integer.parseInt(bannedEmps.getString("eid")));
            if (emp != null && !checkEmps.contains(emp)) {
                checkEmps.add(emp);
            }
            bannedEmps.moveNext();
        }

        for (SEmployee emp : checkEmps) {
            emp.checkConflicts();
        }
    }

    /**
     * Returns if given employee is banned from working for given client....
     */
    public boolean isClientEmpBanned(SMainComponent cli, SEmployee emp) {
        Boolean myVal = null;
        try {
            myVal = clientEmpBanned.get(cli.getId() + " " + emp.getId());
        } catch (Exception e) {
        }
        if (myVal != null) {
            return true;
        }
        return false;
    }

    public void parseSchedule(Record_Set rs, int index) {
        if (!hasInitializedpos) {
            shift_id_pos = rs.getPos("sid");
            shift_master_id_pos = rs.getPos("smid");
            client_id_pos = rs.getPos("cid");
            employee_id_pos = rs.getPos("eid");
            schedule_date_pos = rs.getPos("date");
            schedule_id_pos = rs.getPos("gp");
            start_time_pos = rs.getPos("start_time");
            end_time_pos = rs.getPos("end_time");
            day_code_pos = rs.getPos("dow");
            isDeleted_pos = rs.getPos("isdeleted");
            type_pos = rs.getPos("type");
            hasInitializedpos = true;
        }
        shift_id = rs.getString(shift_id_pos);
        shift_master_id = rs.getString(shift_master_id_pos);
        client_id = rs.getString(client_id_pos);
        employee_id = rs.getString(employee_id_pos);
        schedule_date = rs.getString(schedule_date_pos);
        if (schedule_date.indexOf(' ') > -1) {
            //If we are in the format YYYY-MM-DD HH:MM:SS, strip out remaining
            schedule_date = schedule_date.substring(0, schedule_date.indexOf(' '));
        }
        schedule_id = rs.getString(schedule_id_pos);
        start_time = rs.getInt(start_time_pos);
        end_time = rs.getInt(end_time_pos);
        day_code = rs.getInt(day_code_pos);
        isDeleted = rs.getBoolean(isDeleted_pos);
        type = rs.getString(type_pos);
        String trainerId = rs.getString("trainerid");

        SEmployee myEmp = hash_employees.get(Integer.parseInt(employee_id));
        SClient myCli = hash_clients.get(Integer.parseInt(client_id));
        if (myEmp != null && myCli != null) {
            ss = new DShift(
                    shift_id,
                    shift_master_id,
                    myCli,
                    myEmp,
                    start_time,
                    end_time,
                    day_code,
                    this,
                    schedule_id,
                    isDeleted,
                    type,
                    schedule_date,
                    rs.getString("edate"),
                    rs.getString("sdate"),
                    rs.getString("pay_opt"),
                    rs.getString("bill_opt"),
                    rs.getInt("rate_code_id"),
                    trainerId,
                    rs.getString("lu"),
                    rs.getString("hasnote"),
                    1);
            ss.getClient().addNewShiftToClient(ss);
        }
    }

    public void orderClients(final SClient sc) {
        sortClients();
        if (sc == null) {
            size = 0;
        }

        Runnable orderClientsRunnable = new Runnable() {
            public void run() {
                int c = clients.size();
                boolean shouldDisplayThisClient;
                for (int i = 0; i < c; i++) {
                    if (sc == null) {
                        shouldDisplayThisClient = clients.get(i).rePosShifts();
                        if (clients.get(i).getParent() != Row_Headers) {
                            Row_Headers.add(clients.get(i).getEmployeeListHeader(), i);
                        }
                        if (shouldDisplayThisClient) {
                            clients.get(i).setVisible(true);
                            size += clients.get(i).getRowCount();
                            clients.get(i).getEmployeeListHeader().setVisible(true);
                        } else {
                            clients.get(i).setVisible(false);
                            clients.get(i).getEmployeeListHeader().setVisible(false);

                        }

                    } else if (clients.get(i) == sc) {
                        shouldDisplayThisClient = clients.get(i).rePosShifts();
                        if (shouldDisplayThisClient) {
                            clients.get(i).setVisible(true);
                            size -= clients.get(i).getRowCount();
                            clients.get(i).getEmployeeListHeader();
                            size += clients.get(i).getRowCount();
                        } else {
                            /**
                             * I do this to reposition the rows, dirty but will
                             * work for now.
                             */
                            orderClients(null);
                            return;
                        }
                    }
                }
                int plus = new Double(size * .4).intValue();

                ViewPanelRowCount = size;
                size -= plus;
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            orderClientsRunnable.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(orderClientsRunnable);
            } catch (Exception e) {
                System.out.println("Could not order clients!");
                e.printStackTrace();
            }
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        loadingPanel = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        progressBar = new javax.swing.JProgressBar();
        jPanel1 = new javax.swing.JPanel();
        myMainLayeredPanel = new javax.swing.JLayeredPane();
        FloatContainerPanel = new javax.swing.JPanel();
        ControlPanel = new javax.swing.JPanel();
        ScheduleSplitPanel = new javax.swing.JSplitPane();
        this.ScheduleSplitPanel.setUI(new rmischedule.schedule.components.graphiccomponents.CustomSplitPaneUI());
        SchedulePanel = new javax.swing.JPanel();
        myScrollPanel = new javax.swing.JScrollPane();
        AvailabilityPanel = new javax.swing.JPanel();

        loadingPanel.setBackground(new java.awt.Color(255, 255, 255));
        loadingPanel.setLayout(new javax.swing.BoxLayout(loadingPanel, javax.swing.BoxLayout.LINE_AXIS));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        loadingPanel.add(jPanel2);

        progressBar.setBackground(new java.awt.Color(255, 255, 255));
        progressBar.setIndeterminate(true);
        progressBar.setMaximumSize(new java.awt.Dimension(200, 18));
        progressBar.setMinimumSize(new java.awt.Dimension(200, 18));
        progressBar.setPreferredSize(new java.awt.Dimension(200, 18));
        progressBar.setString("Loading...");
        progressBar.setStringPainted(true);
        loadingPanel.add(progressBar);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        loadingPanel.add(jPanel1);

        setBackground(new java.awt.Color(255, 255, 255));
        setPreferredSize(new java.awt.Dimension(500, 400));
        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                resizeLayeredPanel(evt);
            }
        });
        setLayout(new java.awt.GridLayout(1, 0));

        myMainLayeredPanel.setMinimumSize(new java.awt.Dimension(200, 200));
        myMainLayeredPanel.setPreferredSize(new java.awt.Dimension(500, 500));

        FloatContainerPanel.setBackground(new java.awt.Color(255, 255, 255));
        FloatContainerPanel.setPreferredSize(new java.awt.Dimension(500, 500));
        FloatContainerPanel.setLayout(new java.awt.BorderLayout());

        ControlPanel.setBackground(new java.awt.Color(255, 255, 255));
        ControlPanel.setLayout(new javax.swing.BoxLayout(ControlPanel, javax.swing.BoxLayout.Y_AXIS));
        FloatContainerPanel.add(ControlPanel, java.awt.BorderLayout.NORTH);

        ScheduleSplitPanel.setBackground(new java.awt.Color(255, 255, 255));
        ScheduleSplitPanel.setResizeWeight(1.0);
        ScheduleSplitPanel.setOneTouchExpandable(true);

        SchedulePanel.setBackground(new java.awt.Color(255, 255, 255));
        SchedulePanel.setLayout(new java.awt.BorderLayout());

        myScrollPanel.setBackground(new java.awt.Color(255, 255, 255));
        myScrollPanel.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        myScrollPanel.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        SchedulePanel.add(myScrollPanel, java.awt.BorderLayout.CENTER);

        ScheduleSplitPanel.setLeftComponent(SchedulePanel);

        AvailabilityPanel.setBackground(new java.awt.Color(255, 255, 255));
        AvailabilityPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        AvailabilityPanel.setPreferredSize(new java.awt.Dimension(0, 0));
        AvailabilityPanel.setLayout(new java.awt.GridLayout(1, 0));
        ScheduleSplitPanel.setRightComponent(AvailabilityPanel);

        FloatContainerPanel.add(ScheduleSplitPanel, java.awt.BorderLayout.CENTER);

        FloatContainerPanel.setBounds(0, 0, -1, -1);
        myMainLayeredPanel.add(FloatContainerPanel, javax.swing.JLayeredPane.DEFAULT_LAYER);

        add(myMainLayeredPanel);
    }// </editor-fold>//GEN-END:initComponents
    private void resizeLayeredPanel(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_resizeLayeredPanel
        FloatContainerPanel.setBounds(myMainLayeredPanel.getBounds());
        revalidate();
    }//GEN-LAST:event_resizeLayeredPanel

    public boolean isFilterActive() {
        return (myActiveShiftFilter != myNormalViewFilter && myActiveEmployeeFilter != myNormalViewFilter && myActiveClientFilter != myNormalViewFilter);
    }

    public ScheduleDashboardPanel getScheduleDashboard() {
        return myParent.getScheduleDashboard();
    }

    /**
     * Threaded conflict checking...aint it wonderful...looks complicated not
     * really just gets recordset check hashes for either master or sched adds
     * them to a vector to keep track of what shifts are conflicted and sets
     * their graphical properties...
     */
    private class checkScheduleForConflictsThread extends ScheduleThread {

        private boolean runMe;
        private boolean killMe;
        private ArrayBlockingQueue<String> employeesToRunOn;

        public checkScheduleForConflictsThread() {
            employeesToRunOn = new ArrayBlockingQueue(80);
            registerMe(thisObject);
        }

        public void killMe() {
            killMe = true;
        }

        public void runMe(String empId) {
            runMe = true;
            if (empId != null && !employeesToRunOn.contains(empId)) {
                employeesToRunOn.add(empId);
            }
        }

        public void run() {
            this.setPriority(Thread.MAX_PRIORITY);
            while (!killMe) {
                if (runMe) {
                    //If initial scan, do all employees
                    if (employeesToRunOn.size() == 0) {
                        for (int i = 0; i < employees.size(); i++) {
                            employees.get(i).checkConflicts();
                            runMe = false;
                        }
                    } else {
                        do {
                            hash_employees.get(Integer.parseInt(employeesToRunOn.poll())).checkConflicts();
                        } while (employeesToRunOn.size() > 0);
                        runMe = false;
                    }
                }
                try {
                    sleep(300);
                } catch (Exception e) {
                }
            }
            employeesToRunOn = null;
        }
    }

    public void zoomToShift(String id) {
        try {
            if (hash_shifts.get(id) instanceof DShift) {
                SShift zShift = ((DShift) hash_shifts.get(id)).mySShift;
                myScrollPanel.getViewport().scrollRectToVisible(new Rectangle(zShift.getX(), zShift.myRow.myWeek.mySched.getMyLocation().y + myScrollPanel.getViewport().getBounds().height, 10, 10));
                zShift.selectThisShift(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This class should only load the data nothing else because much of the
     * time is wasted just waiting for the server decided to make this method...
     */
    public class onlyLoadData extends Thread {

        public void run() {
            setPriority(MAX_PRIORITY);
            schedule_client_query myClientQuery = new schedule_client_query();
            only_use_this_query_for_the_schedule_query mySchedQuery = new only_use_this_query_for_the_schedule_query();
            employee_query myEmpQuery = new employee_query();
            RunQueriesEx myQueryEx = new RunQueriesEx();
            ArrayList<Record_Set> myList = new ArrayList(4);

            String bw = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getBegWeek());
            String ew = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getEndWeek());

            int employee_id = 0;
            int client_id = 0;

            String employeeIncludeList = "";
            String clientIncludeList = "";

            if (Main_Window.isEmployeeLoggedIn()) {
                employee_id = Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId());
            } else if (Main_Window.isClientLoggedIn()) {
                try {
                    ClientController clientController = ClientController.getInstance(Company);
                    ArrayList<Client> clients = clientController.getClientsByDuplicateLogin(Integer.parseInt(Main_Window.parentOfApplication.getUser().getUserId()));
                    if (clients.size() > 0) {
                        clientIncludeList = "";
                        for (int c = 0; c < clients.size(); c++) {
                            if (c > 0) {
                                clientIncludeList += ",";
                            }
                            clientIncludeList += clients.get(c).getClientId();
                        }
                        clientIncludeList += "";
                    }
                } catch (Exception exe) {
                }
            }

            if (employee_id != 0) {
                employeeIncludeList = "(" + employee_id + ")";
            }

            mySchedQuery.update(clientIncludeList, employeeIncludeList, bw, ew, "", "", false);
            myClientQuery.update(employee_id, bw, ew);
            myEmpQuery.update("", employee_id, true);

            myConnection.prepQuery(mySchedQuery);
            myConnection.prepQuery(myClientQuery);
            myConnection.prepQuery(myEmpQuery);

            myQueryEx.update(mySchedQuery, myClientQuery, myEmpQuery);

            /**
             * Doing avail seperately to let it run faster rather than tie shit
             * together...
             */
            try {
                long startTime = System.currentTimeMillis();
                myList = myConnection.executeQueryEx(myQueryEx);
            } catch (Exception e) {
            }
            clientRecordSet = new Record_Set();
            employeeRecordSet = new Record_Set();
            scheduleRecordSet = new Record_Set();

            scheduleRecordSet = myList.get(myList.size() - 3);
            clientRecordSet = myList.get(myList.size() - 2);
            employeeRecordSet = myList.get(myList.size() - 1);
            if (clientRecordSet.length() == 0 || employeeRecordSet.length() == 0) {
                if (Main_Window.isEmployeeLoggedIn()) {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                            "You don't appear to have a schedule for the next two weeks, please "
                            + "contact your scheduler(s) if you believe this is an error.",
                            "Can not view schedule",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(Main_Window.parentOfApplication,
                            "You have " + clientRecordSet.length() + " locations entered and "
                            + "" + employeeRecordSet.length() + " employees entered. \nPlease enter at least "
                            + "one client and one employee before loading the schedule. ", "Can Not Load Schedule!", JOptionPane.ERROR_MESSAGE);
                }
                myParent.remove(thisObject);
                return;
            }
            doneLoadingData = true;
        }
    }

    public class LoadAllDatabaseInformation extends Thread {

        private Schedule_View_Panel parent;
        public Record_Set availabilityRecordSet;
        public boolean killMe;

        public LoadAllDatabaseInformation(Schedule_View_Panel p) {
            killMe = false;
            parent = p;
        }

        @Override
        public void run() {
            this.setPriority(Thread.MAX_PRIORITY);

            assemble_availability_for_schedule_query myAvailabilityQuery = new assemble_availability_for_schedule_query();
            myConnection.prepQuery(myAvailabilityQuery);
            String bw = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getBegWeek());
            String ew = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(getEndWeek());
            myAvailabilityQuery.update("", bw, ew);

            while (!doneLoadingData && !killMe) {
                try {
                    sleep(100);
                } catch (Exception e) {
                }
            }
            getEmployees();
            getClients();

            myAvailability.buildEmployeesList(employees);

            scheduleRecordSet.moveToFront();
            hash_shifts = new Hashtable(scheduleRecordSet.length() * 2);
            lastUpdated = scheduleRecordSet.lu;

            try {
                SwingUtilities.invokeAndWait(new Runnable() {
                    public void run() {
                        for (int i = 0; i < scheduleRecordSet.length(); i++) {
                            parseSchedule(scheduleRecordSet, i);
                            scheduleRecordSet.moveNext();
                        }

                        plantClients(null);
                        if (Main_Window.isEmployeeLoggedIn()) {
                            JLabel warningLabel = new JLabel("* Please note: Shifts in future weeks are subject to change!");
                            warningLabel.setForeground(Color.RED);
                            warningLabel.setFont(new Font("sansserif", Font.BOLD, 14));
                            myContentPane.add(warningLabel);
                        }

                        mySearchPanel = new SearchPanel(thisObject);

                        myScrollPanel.setCorner(myScrollPanel.UPPER_LEFT_CORNER, mySearchPanel);
                        myScrollPanel.setHorizontalScrollBarPolicy(myScrollPanel.HORIZONTAL_SCROLLBAR_ALWAYS);
                        myScrollPanel.setVerticalScrollBarPolicy(myScrollPanel.VERTICAL_SCROLLBAR_ALWAYS);
                        myScrollPanel.setAutoscrolls(true);
                        myScrollPanel.getVerticalScrollBar().setUnitIncrement(60);
                        myScrollPanel.getHorizontalScrollBar().setUnitIncrement(60);

                        isInitializing = false;

                        orderClients(null);
                    }
                });


            } catch (Exception e) {
            }

            Thread loadTrainingInformation = new Thread() {
                public void run() {
                    try {
                        get_training_for_schedule_by_client_query trainingQuery = new get_training_for_schedule_by_client_query();
                        getConnection().prepQuery(trainingQuery);
                        Calendar start = Calendar.getInstance();
                        start.add(Calendar.DAY_OF_YEAR, -1);
                        String startOfSchedule = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(start);
                        start.add(Calendar.DAY_OF_YEAR, -365);
                        String howFarBack = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(start);
                        trainingQuery.update(startOfSchedule, howFarBack);
                        Record_Set trainingList = getConnection().executeQuery(trainingQuery);
                        SClient currClient = null;
                        for (int t = 0; t < trainingList.length(); t++) {
                            if (currClient == null || !currClient.getId().equals(trainingList.getString("cid"))) {
                                currClient = hash_clients.get(trainingList.getInt("cid"));
                            }
                            try {
                                currClient.loadTrainingInfo(trainingList.getString("eid"),
                                        Double.parseDouble(trainingList.getString("time")), trainingList.getBoolean("is_trained"));
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            trainingList.moveNext();
                        }

                        myConflictThread.start();
                        CheckScheduleForConflicts(null);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            loadTrainingInformation.start();

            myScrollPanel.setBorder(BorderFactory.createLineBorder(Color.black, 1));

            RunQueriesEx myQuery = new RunQueriesEx();
            myQuery.add(myAvailabilityQuery);

            Record_Set myAvail = myConnection.executeQuery(myAvailabilityQuery);
            placeAvailability(myAvail);

//            myLoadAvailabilityThread myAvailThread = new myLoadAvailabilityThread(myAvailabilityQuery);
//            myAvailThread.start();

            shb = new Schedule_Heart_Beat(thisObject);

            loadCertifications();
            loadBannedEmployees();
            for (int i = 0; i < employees.size(); i++) {
                try {
                    employees.get(i).getAEmp().setEmployeeLabelToAvailabilityColors();
                } catch (Exception e) {
                }
            }

            myToolBar.setEnabled(true);
            scrollPanelX.setVisible(true);
            Row_Headers.setVisible(false);
            Col_Headers.setVisible(false);
            myScrollPanel.setRowHeaderView(Row_Headers);
            myScrollPanel.setColumnHeaderView(Col_Headers);
            myScrollPanel.setViewportView(scrollPanelX);
            isDoneLoadingCompletely = true;

            Thread scrollBarThread = new Thread() {
                public void run() {
                    setupScrollBar();
                }
            };
            scrollBarThread.start();

        }
    }

    private void setupScrollBar() {

        if (myContentPane.getComponentCount() > 0) {
            SClient lastClient = null;

            for (int i = myContentPane.getComponentCount() - 1; i >= 0; i--) {
                if (myContentPane.getComponent(i) instanceof SClient && lastClient == null) {
                    lastClient = (SClient) myContentPane.getComponent(i);
                }
            }

            while (!lastClient.isValid() && this.myParent != null) {
                try {
                    Thread.sleep(300);
                } catch (Exception ex) {
                }
            }

            if (this.myParent != null) {
                java.awt.EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        myScrollPanel.getViewport().setViewPosition(new Point(0, 0));
                        Row_Headers.setVisible(true);
                        Col_Headers.setVisible(true);
                        scrollPanelX.setVisible(true);
                        doLayout();
                        if (AjaxSwingManager.isAjaxSwingRunning()) {
                            ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(myParent, true);
                            AjaxSwingManager.endOperation();
                        }
                        if (Main_Window.isEmployeeLoggedIn()
                                || Main_Window.isClientLoggedIn()) {
                            showFirstEmployeeInformation();
                            showFirstClientInformation();
                            try {
                                myParent.getScheduleDashboard().refreshScreen(thisObject);
                            } catch (Exception e) {
                            }
                        }
                    }
                });
            }
        }
    }

    /**
     * Seperate thread to load availability upon load...
     */
    public class myLoadAvailabilityThread extends Thread {

        private assemble_availability_for_schedule_query myavailquery;

        public myLoadAvailabilityThread(assemble_availability_for_schedule_query myAvailQuery) {
            myavailquery = myAvailQuery;
        }

        public void run() {
            try {
                ArrayList myAvail = new ArrayList();
                RunQueriesEx myQuery = new RunQueriesEx();
                myQuery.add(myavailquery);

                myAvail = myConnection.executeQueryEx(myQuery);
                placeAvailability((Record_Set) myAvail.get(0));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Rather self explainatory I would hope...
     */
    public void loadCertifications() {
        get_certifications_for_employee_branch_query myEmpCertQuery = new get_certifications_for_employee_branch_query();
        get_certifications_for_client_branch_query myClientCertQuery = new get_certifications_for_client_branch_query();
        myConnection.prepQuery(myEmpCertQuery);
        myConnection.prepQuery(myClientCertQuery);
        Record_Set empCertRS = null;
        Record_Set cliCertRS = null;
        try {
            empCertRS = myConnection.executeQuery(myEmpCertQuery);
            cliCertRS = myConnection.executeQuery(myClientCertQuery);
        } catch (Exception e) {
        }

        updateEmpCertifications(empCertRS);
        updateClientCertifications(cliCertRS);
    }

    /**
     * Returns the certifications that are available for this Schedule
     *
     * @return Vector<CertificationClass>
     */
    public Vector<CertificationClass> getAvailableCertificationsForSchedule() {
        Vector<CertificationClass> retVal = new Vector<CertificationClass>();

        get_certifications_query certificationQuery = new get_certifications_query();
        certificationQuery.update("", true);
        Record_Set rst = myConnection.executeQuery(certificationQuery);

        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new CertificationClass(rst));
            rst.moveNext();
        }

        return retVal;
    }

    public Vector<EmployeeType> getAllEmployeeTypes() {
        Vector<EmployeeType> retVal = new Vector<EmployeeType>();

        get_all_employee_types_query typeQuery = new get_all_employee_types_query();
        Record_Set rst = myConnection.executeQuery(typeQuery);

        for (int r = 0; r < rst.length(); r++) {
            retVal.add(new EmployeeType(rst));
            rst.moveNext();
        }

        return retVal;
    }

    public void updateEmpCertifications(Record_Set empCertRS) {
        if (empCertRS.length() > 0) {
            for (SEmployee emp : this.employees) {
                emp.clearCertifications();
            }

            SEmployee myEmp;
            if (empCertRS != null) {
                for (int i = 0; i < empCertRS.length(); i++) {
                    myEmp = hash_employees.get(Integer.parseInt(empCertRS.getString("eid")));
                    if (myEmp != null) {
                        CertificationClass myCert = new CertificationClass(empCertRS.getString("certid"), empCertRS.getString("cert_name"), empCertRS.getString("cert_desc"), empCertRS.getString("renewal"), empCertRS.getString("acquired"), empCertRS.getString("expired"), Boolean.parseBoolean(empCertRS.getString("iscert")));
                        myEmp.addCertification(myCert);
                    }
                    empCertRS.moveNext();
                }
            }
        }
    }

    public void updateClientCertifications(Record_Set cliCertRS) {
        for (SMainComponent cl : this.clients) {
            if (cl instanceof SClient) {
                ((SClient) cl).clearCertifications();
            }
        }

        SClient myCli;
        if (cliCertRS != null) {
            for (int i = 0; i < cliCertRS.length(); i++) {
                myCli = hash_clients.get(cliCertRS.getInt("cid"));
                if (myCli != null) {
                    CertificationClass myCert = new CertificationClass(cliCertRS.getString("certid"), cliCertRS.getString("cert_name"), cliCertRS.getString("cert_desc"), cliCertRS.getString("renewal"), cliCertRS.getString("acquired"), cliCertRS.getString("expired"), Boolean.parseBoolean(cliCertRS.getString("iscert")));
                    myCli.addCertification(myCert);
                }
                cliCertRS.moveNext();
            }
        }
    }

    /**
     * Processes and places our availability...
     */
    public void placeAvailability(final Record_Set availabilityRecordSet) {
        Runnable availRunnable = new Runnable() {
            public void run() {

                for (int i = 0; i < availabilityRecordSet.length(); i++) {
                    String stringEid = availabilityRecordSet.getString("employee_id");
                    SEmployee myEmployee = null;
                    try {
                        myEmployee = hash_employees.get(Integer.parseInt(stringEid));
                    } catch (Exception ex) {
                    }

                    if (myEmployee != null) {
                        final DAvailability myAvail = new DAvailability(myEmployee, availabilityRecordSet, thisObject);
                        final SEmployee emp = myEmployee;
                        //Place in the "client" for vacation / time off

                        for (int c = 0; c < clients.size(); c++) {
                            if (clients.get(c) instanceof STimeOff && !emp.isDeleted()) {
                                STimeOff currentTimeOffClient = (STimeOff) clients.get(c);
                                if (currentTimeOffClient.getIdInt() == myAvail.getAvailType() * -1) {
                                    myAvail.setClient(currentTimeOffClient);
                                    currentTimeOffClient.addNewShiftToClient(myAvail);
                                } else if (myAvail.getAvailType() == Main_Window.AVAILABLE) {
                                    SourceOfConflict avail = hash_shifts.get(myAvail.getShiftId() + (- 1 * currentTimeOffClient.getIdInt()));
                                    if (avail != null) {
                                        try {
                                            ((DAvailability) avail).getShift().cleareInfo();
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }

                        myEmployee.placeAvailability(myAvail);
                        try {
                            myEmployee.checkConflicts();
                        } catch (Exception e) {
                        }
                    }
                    availabilityRecordSet.moveNext();
                }
                plantClients(null);
            }
        };
        try {
            if (SwingUtilities.isEventDispatchThread()) {
                availRunnable.run();
            } else {
                SwingUtilities.invokeAndWait(availRunnable);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main header class for our schedule...fun fun...
     */
    private class ScheduleHeaderPanel extends JPanel implements ZoomListener {

        public ScheduleHeaderPanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(myLayout);
        }

        public void zoomPerformed() {
        }

        public void zoomFinished() {
            this.revalidate();
        }
    }

    public class individualHeaderPanel extends ZoomablePanel {

        private String myDay;
        private String myDate;

        public individualHeaderPanel(String day, String date) {
            setLayout(new BorderLayout());
            setBackground(hcol_color);
            myDay = day;
            myDate = date;
        }

        /**
         * Needed by CreamTec
         *
         * @return
         */
        public String getMyDay() {
            return myDay;
        }

        /**
         * Needed by CreamTec
         *
         * @return
         */
        public String getMyDate() {
            return myDate;
        }

        public void paintComponentCustom(java.awt.Graphics g) {
            g.setFont(Main_Window.header_font);
            //g.setColor(new Color(102,102,155));//modified newline current color
            g.setColor(new Color(0, 0, 0));

            int y = 5 + g.getFont().getSize();
            g.drawString(myDay, (60 - g.getFontMetrics().getStringBounds(myDay, g).getBounds().width) / 2, y);
            g.setFont(Main_Window.date_font);
            //g.setColor(new Color(102,102,155));//modified newline current nice color
            g.setColor(new Color(0, 0, 0));
            y += g.getFont().getSize() + 4;
            g.drawString(myDate, (60 - g.getFontMetrics().getStringBounds(myDate, g).getBounds().width) / 2, y);
        }

        public String getSizeKey() {
            return "individualHeaderPanel";
        }
    }

    /**
     * Class used to contain column header classes, and tie together week and
     * calendar info....
     */
    private class calendar_week_class {

        private Calendar myDate;
        private String readableDate;
        private int Week;
        private int DayCode;
        private String dbaseFormat;
        public String onlyDayMonth;

        public calendar_week_class() {
            myDate = Calendar.getInstance();
        }

        public void setTime(Date date, int week) {
            myDate.setTime(date);
            DayCode = myDate.get(Calendar.DAY_OF_WEEK);
            dbaseFormat = StaticDateTimeFunctions.convertCalendarToDatabaseFormatWithOutAdjust(myDate);
            DayCode = myDate.get(Calendar.DAY_OF_WEEK) - 1;
            if (DayCode == 0) {
                DayCode = 7;
            }
            readableDate = StaticDateTimeFunctions.convertCalendarToReadableFormat(myDate);

            StringTokenizer toDayMonth = new StringTokenizer(readableDate, "/");
            onlyDayMonth = toDayMonth.nextToken() + "/" + toDayMonth.nextToken();
            Week = week;
        }

        public int getWeekNo() {
            return Week;
        }

        public int getDayCode() {
            return DayCode;
        }

        public Calendar getDate() {
            return myDate;
        }

        public boolean equals(String d) {
            if (d.equals(dbaseFormat)) {
                return true;
            }
            return false;
        }
    }

    /**
     * Used to hold the vertical panel of totals for employees
     */
    class Total_Pane extends ZoomablePanel {

        public Total_Pane() {
            super();
            setLayout(new BorderLayout());
            setBackground(total_color);
            // setBackground(Color.RED);
            //setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            //setBorder(BorderFactory.createLineBorder(new Color(102,102,155), 2));//mod
        }

        public void paintComponentCustom(Graphics g) {
            g.setColor(new Color(0, 0, 0));
            g.setFont(Main_Window.totals_font);
            g.drawString("Total", (50 - g.getFontMetrics().getStringBounds("Total", g).getBounds().width) / 2, (50 + g.getFont().getSize()) / 2);
        }

        public String getSizeKey() {
            return "Total_Pane";
        }
    }

    public void scrollRectToVisible(Rectangle r) {
        myContentPane.scrollRectToVisible(r);
    }

    /**
     * Ahh another class to ensure that it will resize properly according to row
     * count and the row size set in Schedule_View_Panel
     */
    public class scrollContentPanel extends JPanel {

        public scrollContentPanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(myLayout);
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }
    }

    public class EmployeeRowHeaders extends JPanel {

        public EmployeeRowHeaders() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(myLayout);
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel AvailabilityPanel;
    private javax.swing.JPanel ControlPanel;
    public javax.swing.JPanel FloatContainerPanel;
    private javax.swing.JPanel SchedulePanel;
    private javax.swing.JSplitPane ScheduleSplitPanel;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel loadingPanel;
    private javax.swing.JLayeredPane myMainLayeredPanel;
    private javax.swing.JScrollPane myScrollPanel;
    private javax.swing.JProgressBar progressBar;
    // End of variables declaration//GEN-END:variables
}
