/*
 * SClient.java
 *
 * Created on April 13, 2004, 2:02 PM
 */
package rmischedule.schedule.components;

import schedfoxlib.model.util.Record_Set;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.ClientAgent;
import rmischedule.schedule.Schedule_View_Panel;
import rmischedule.options.*;
import rmischedule.options.optiontypeclasses.*;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import rmischedule.main.Main_Window;
import rmischeduleserver.mysqlconnectivity.queries.client.*;
import rmischeduleserver.data_connection_types.*;
import rmischedule.schedule.components.notes.*;
import rmischedule.schedule.schedulesizes.*;
import schedfoxlib.model.Client;

/**
 *
 * @author jason.allen
 */
public abstract class SMainComponent extends JPanel implements Comparable {

    public int lastScheduleId;
    public int client_row_count = 0;
    private boolean shouldShowShiftCountInTotals;

    protected Schedule_View_Panel parent;
    private ClientNameAboveEmployeeHeader pnRowName;
    private JLabel lblRowTotal;
    private JPanel pnRowTotal;
    protected JPanel pnRowHeader;
    private JComboBox shiftTotalDetails;
    private String[] shiftDetailString = {"Hours", "Shifts"};
    private TotalPanel[] myTotals;
    protected SMainComponent thisObj;
    private mySchedulePanel schedulePanel;
    private totalClientHeaderPanel myHeader;
    private JPanel totalPanel;
    private EmployeeHoldPanel employeeListHoldingPanel;

    public SMainComponent(String Id) {
        thisObj = this;
    }

    /**
     * Our Constructor
     */
    public SMainComponent(Schedule_View_Panel myParent, Record_Set rs) {
        thisObj = this;
        parent = myParent;

        myHeader = new totalClientHeaderPanel();
        schedulePanel = new mySchedulePanel();
        totalPanel = new myLowerListOfTotalsPanel();
        lastScheduleId = 0;

        this.shouldShowShiftCountInTotals = this.getShouldDisplayNumberOfShiftsFromOptions();
        initializeEmployeeHeader();
        initializeTotals();
        setClientHeader();
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        super.add(myHeader);
        super.add(schedulePanel);
        super.add(totalPanel);

    }

    public void initializeComponent() {
        addMyBlankSchedule();
    }

    /**
     * Abstract function to add your blank schedule.
     */
    public abstract void addMyBlankSchedule();

    /**
     * Abstract function to define the header
     */
    public abstract String generateClientName();

    /**
     * Abstract function to plant all shifts / availability.
     */
    public abstract void plantShifts();

    public abstract boolean isEmployeeBanned(String eid);

    /**
     * Returns if we should have the blank schedule in our client.
     *
     * @return
     */
    public abstract boolean shouldHaveBlankSchedule();

    /**
     * Abstract function to load training info.
     *
     * @param employeeId
     * @param trainingTime
     * @param override
     */
    public abstract void loadTrainingInfo(String employeeId, double trainingTime, boolean override);

    /**
     * Gets the Client headers mouse adapter.
     *
     * @return
     */
    public abstract MouseAdapter getClientMouseAdapter();

    /**
     * Gets the action for the printer panel.
     *
     * @return
     */
    public abstract MouseListener getPrinterMouseListener();

    /**
     * Sets the client header
     *
     * @return
     */
    public abstract void setClientHeader();

    /**
     * Returns the blank schedule
     *
     * @return
     */
    public abstract SSchedule getMyBlankSchedule();

    public abstract boolean isDefaultToNonBillable();

    public abstract Client getClientData();

    public abstract boolean hasNotes();

    public abstract Integer getRate();

    public abstract boolean isDeleted();

    public abstract double getTrainingTime();

    public abstract String getClientName();

    public abstract String getFullClientName();

    public abstract void setClientName(String clientName);

    public abstract void setDeleted(Integer del);

    public abstract int getIdInt();

    public abstract String getId();

    public abstract SortedSet getSchedules();

    public abstract Color getMyColor();

    /**
     * Method to grab new data for header and return JPanel...
     */
    public JPanel getEmployeeListHeader() {
        SortedSet myMap = parent.mySchedules.getClientSchedules(this);
        Iterator myIterator = myMap.iterator();
        int c = myMap.size();
        for (int i = 0; i < c; i++) {
            if (myIterator.hasNext()) {
                SSchedule tempSched = (SSchedule) myIterator.next();
                try {
                    if (tempSched.isVisible() && (tempSched.getHeader().getParent() != getEmployeeHoldingPanel())) {
                        int realPos = i;
                        if (realPos > getEmployeeHoldingPanel().getComponentCount()) {
                            realPos = getEmployeeHoldingPanel().getComponentCount();
                        }
                        getEmployeeHoldingPanel().add(tempSched.getHeader(), realPos);
                    }
                } catch (Exception e) {
                    System.out.println("IMPORTANT ERROR COULD NOT PLACE HEADER FOR EMPLOYEE " + tempSched.getEmployee().getName());
                }
            }
        }
        return pnRowHeader;
    }

    /**
     * Returns true if this client should be shown as defined as the filter
     * allows...does it have at least one shift that satifies current filter...
     */
    public boolean rePosShifts() {
        int temporaryRowCount = 1;
        SortedSet myMap = getSchedules();
        int c = myMap.size();
        boolean showThisClient = false;
        Iterator myIterator = myMap.iterator();
        SSchedule tempSched = null;
        if (this instanceof STimeOff) {
            showThisClient = true;
        }

        final ArrayList<SSchedule> scheds = new ArrayList<SSchedule>();

        for (int i = 0; i < c; i++) {
            try {
                if (myIterator.hasNext()) {
                    tempSched = (SSchedule) myIterator.next();
                    if (parent.runScheduleThroughFilters(tempSched)) {
                        showThisClient = true;
                        tempSched.setShouldShowThisSchedule(true);
                        temporaryRowCount += tempSched.getRowCount();
                    } else {
                        tempSched.setShouldShowThisSchedule(false);
                    }
                    scheds.add(tempSched);
                }
            } catch (Exception exe) {
                exe.printStackTrace();
            }
        }
        
        final int finalTempRowCount = temporaryRowCount;

        Runnable refresh = new Runnable() {
            @Override
            public void run() {
                int myVisPos = 0;
                for (int i = 0; i < scheds.size(); i++) {
                    try {
                        SSchedule tempSched = scheds.get(i);
                        tempSched.setVisible(tempSched.isShouldShowThisSchedule());
                        if ((tempSched.getParent() == null && tempSched.isVisible())) {
                            add(tempSched, myVisPos);
                        }
                        myVisPos++;
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }

                forceUpdateHeight(client_row_count, finalTempRowCount);
                
                revalidate();
                repaint();
                if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                    ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObj, true);
                }
                
                
            }
        };

        if (SwingUtilities.isEventDispatchThread()) {
            refresh.run();
        } else {
            SwingUtilities.invokeLater(refresh);
        }

        return showThisClient;
    }

    public Schedule_View_Panel getMyParent() {
        return this.parent;
    }

    public JPanel getEmployeeHoldingPanel() {
        return employeeListHoldingPanel;
    }

    public JPanel getHeader() {
        return this.myHeader;
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
        schedulePanel.removeAll();
        myHeader.removeAll();
        totalPanel.removeAll();
        pnRowHeader.removeAll();
        parent = null;
        pnRowName = null;
        lblRowTotal = null;
        pnRowTotal = null;
        pnRowHeader = null;
        shiftTotalDetails = null;
        shiftDetailString = null;
        myTotals = null;
        thisObj = null;
        schedulePanel = null;
        myHeader = null;
        totalPanel = null;
        employeeListHoldingPanel.removeAll();
        employeeListHoldingPanel = null;
    }

    /**
     * Gets the initial value for if we should display number of shifts rather
     * than hours from our options values first.
     *
     * @return boolean
     */
    public boolean getShouldDisplayNumberOfShiftsFromOptions() {
        try {
            boolean doesOptionShowNumberOfShifts
                    = (((Vector) ((ComboOptionClass) Main_Window.newOptions.getOptionByName(OptionsDataClass.DisplayTotalShiftsOrTimes)).read()).get(0).equals("Number Of Shifts"));
            return doesOptionShowNumberOfShifts;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Method to initialize our panels and labels...
     */
    public void initializeTotals() {
        int weekCnt = (parent.getWeekCount()) * 8;
        myTotals = new TotalPanel[weekCnt];
        for (int i = 0; i < weekCnt; i++) {
            myTotals[i] = new TotalPanel(i, myTotals, shiftTotalDetails);
            totalPanel.add(myTotals[i]);
        }

        if (this.shouldShowShiftCountInTotals) {
            shiftTotalDetails.setSelectedIndex(1);
        } else {
            shiftTotalDetails.setSelectedIndex(0);
        }
    }

    /**
     * Method to set up our header for employees so that all we have to do from
     * now on is add and remove the actual headers not everything else...
     */
    public void initializeEmployeeHeader() {
        pnRowHeader = new JPanel();
        shiftTotalDetails = new JComboBox(shiftDetailString);

        shiftTotalDetails.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                for (int i = 0; i < myTotals.length; i++) {
                    myTotals[i].displayTotal(0);
                }
            }
        });

        pnRowHeader.setLayout(new BoxLayout(pnRowHeader, BoxLayout.Y_AXIS));
        pnRowName = new ClientNameAboveEmployeeHeader();
        pnRowTotal = new RowTotalPanel();
        BoxLayout myRowTotalLayout = new BoxLayout(pnRowTotal, BoxLayout.X_AXIS);
        pnRowTotal.setLayout(myRowTotalLayout);
        lblRowTotal = new JLabel(" Total");

        pnRowTotal.add(lblRowTotal);
        JPanel spacer = new JPanel();
        spacer.setMaximumSize(new Dimension(10, 10));
        spacer.setMinimumSize(new Dimension(10, 10));
        spacer.setPreferredSize(new Dimension(10, 10));
        spacer.setOpaque(false);
        pnRowTotal.add(spacer);
        if (!Main_Window.isEmployeeLoggedIn()) {
            pnRowTotal.add(shiftTotalDetails);
        }
        pnRowTotal.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        pnRowTotal.setBackground(Schedule_View_Panel.total_color);
        employeeListHoldingPanel = new EmployeeHoldPanel();
        pnRowHeader.add(pnRowName);
        pnRowHeader.add(employeeListHoldingPanel);
        pnRowHeader.add(pnRowTotal);
    }

    private class RowTotalPanel extends ZoomablePanel {

        public String getSizeKey() {
            return "RowTotalPanel";
        }
    }

    /**
     * Overloaded method to add to the schedulePanel...
     */
    public void add(final SSchedule myComp) {
        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                schedulePanel.add(myComp);
                if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                    ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(schedulePanel, true);
                }
            }
        });
    }

    @Override
    public void removeAll() {
        if (this.parent.isDoneLoadingCompletely) {
            schedulePanel.removeAll();
            if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(schedulePanel, true);
            }
        }
    }

    public void add(final SSchedule myComp, int index) {
        try {
            if (index > schedulePanel.getComponentCount()) {
                index = schedulePanel.getComponentCount();
            }
            final int ii = index;
            Runnable graphicsManipThread = new Runnable() {

                public void run() {
                    schedulePanel.add(myComp, ii);
                    if (AjaxSwingManager.isAjaxSwingRunning() && parent.isDoneLoadingCompletely) {
                        ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(schedulePanel, true);
                    }
                }
            };
            if (SwingUtilities.isEventDispatchThread()) {
                graphicsManipThread.run();
            } else {
                SwingUtilities.invokeAndWait(graphicsManipThread);
            }
        } catch (Exception e) {
            System.out.println("IMPORTANT ERROR COULD NOT PLACE SCHEDULE FOR " + myComp.getEmployee().getName());
        }
    }

    public void setSelected(boolean s) {
        if (s) {
            pnRowName.setBackground(Color.yellow);
        } else {
            pnRowName.setBackground(this.getMyColor());
            //pnRowName.setBackground(Color.RED);
        }
    }

    /**
     * Removes any blank schedule from our list...fun fun...
     */
    public void removeSchedule(SSchedule schedToRemove) {
        if (!schedToRemove.isBlankSchedule() && parent.isDoneLoadingCompletely) {
            schedulePanel.remove(schedToRemove);
            employeeListHoldingPanel.remove(schedToRemove.getHeader());
            parent.mySchedules.removeSchedule(schedToRemove);
            rePosShifts();
        }
    }

    /**
     * Method used to add a new DShift to this Client...
     */
    public void addNewShiftToClient(UnitToDisplay newShift) {
        if (newShift.isDeleted()) {
            return;
        }
        SSchedule mySchedule = parent.mySchedules.getScheduleByDShift(newShift);
        if (mySchedule == null) {
            mySchedule = new SSchedule(newShift.getEmployee(), newShift.getClient(), parent.getWeekCount(), parent, Long.parseLong(newShift.getGroupId()));
            if (this.parent.isDoneLoadingCompletely) {
                mySchedule.plantWeeks();
            }
        }
        mySchedule.addDShift(newShift);
    }

    /**
     * Method to tell if Row Headers have a parent, ie do they need to be added
     * for that particular client?
     */
    public java.awt.Container getEmployeeListHeadersParent() {
        try {
            return pnRowHeader.getParent();
        } catch (Exception e) {
            return null;
        }
    }

    public void addTotal(UnitToDisplay val, double total) {
        try {

            myTotals[val.getWeekNo() * 8 + val.getShift().getOffset()].addTotal(total);
        } catch (Exception e) {
            System.out.println("addTotal failed for week " + val.getWeekNo() + " and day " + (val.getDayCode() - 1));
        }
    }

    public void updateHeight(int old_row_count, int new_row_count) {
        if (!this.parent.isDoneLoadingCompletely) {
            return;
        }
        forceUpdateHeight(old_row_count, new_row_count);

    }

    public int getRowCount() {
        return client_row_count;
    }

    public void forceUpdateHeight(int old_row_count, int new_row_count) {
        client_row_count = client_row_count - old_row_count + new_row_count;
        parent.updateHeight(old_row_count, new_row_count);
        getEmployeeListHeader();
    }

    @Override
    public String toString() {
        return getClientName();
    }

    @Override
    public int compareTo(Object obj) {
        if (obj instanceof SClient && this instanceof SClient) {
            SMainComponent myCompare = ((SMainComponent) obj);
            return (this.getFullClientName().compareToIgnoreCase(myCompare.getFullClientName()));
        } else if (this instanceof SClient && obj instanceof STimeOff) {
            return -1;
        } else if (this instanceof STimeOff && obj instanceof SClient) {
            return 1;
        } else if (this instanceof STimeOff && obj instanceof STimeOff) {
            return this.getClientName().compareToIgnoreCase(((STimeOff) obj).getClientName());
        }
        return 0;
    }

    /**
     * *************************************************************************
     * BELOW THIS LINE ARE ALL OF THE INDIVIDUAL CLASSES FOR THE CLIENT, THESE
     * WERE MAINLY CREATED TO ALLOW THE SCHEDULE TO RESIZE PROPERLY...MOST
     * CLASSES ARE VERY BASIC
     * *************************************************************************
     */
    class totalClientHeaderPanel extends JPanel {

        public totalClientHeaderPanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(myLayout);
        }
    }

    class EmployeeHoldPanel extends JPanel {

        public EmployeeHoldPanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(myLayout);
        }
    }

    /**
     * Class file for the Panel which contains only the schedule, not the header
     * and not the totals makes it easier to add and remove schedules without
     * screwing with those componenets....
     */
    private class mySchedulePanel extends JPanel {

        public mySchedulePanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(myLayout);
        }
    }

    /**
     * Class file for the Client Name above the Employee List Panel
     */
    private class ClientNameAboveEmployeeHeader extends ZoomablePanel {

        public ClientNameAboveEmployeeHeader() {
            super();
            setLayout(new BorderLayout());
            setBackground(getMyColor());
            addMouseListener(thisObj.getClientMouseAdapter());
            add(new ClientNote());
        }

        public String getSizeKey() {
            return "employeeListHeaderSize";
        }
    }

    /**
     * Class file for the totals listed on the bottom of each Client...
     */
    public class myLowerListOfTotalsPanel extends JPanel {

        public myLowerListOfTotalsPanel() {
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.X_AXIS);
            setLayout(myLayout);
        }
    }

    private class ClientNote extends myNoteIconLabel {

        public ClientNote() {
            super(parent);
        }

        public boolean hasNote() {
            return hasNotes();
        }

        public ArrayList<NoteData> getNotes() {
            get_client_notes_query myQuery = new get_client_notes_query();
            myQuery.update(getId() + "");
            Record_Set rs = new Record_Set();
            ArrayList<NoteData> retVal = new ArrayList<NoteData>();
            try {
                rs = parent.getConnection().executeQuery(myQuery);
                for (int r = 0; r < rs.length(); r++) {
                    NoteData currNote = new NoteData();
                    currNote.setDate(rs.getDate("notes_date_time"));
                    currNote.setNote(rs.getString("notes"));
                    currNote.setNoteType(rs.getString("note_type_name"));
                    currNote.setUserLogin(rs.getString("user_login"));
                    retVal.add(currNote);
                    rs.moveNext();
                }
                return retVal;
            } catch (Exception e) {
            }
            return null;
        }
    }

    protected class EditClientListener extends MouseAdapter {

        public EditClientListener() {
        }

        public void mouseClicked(MouseEvent event) {
            if (event.getClickCount() > 1) {
                parent.editClient(getClientData());
            }
        }
    }
}
