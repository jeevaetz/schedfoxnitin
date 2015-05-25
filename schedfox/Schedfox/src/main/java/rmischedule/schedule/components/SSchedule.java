/*
 * SSchedule.java
 *
 * Created on April 14, 2004, 11:38 AM
 */
package rmischedule.schedule.components;

import com.creamtec.ajaxswing.AjaxSwingConstants;
import com.creamtec.ajaxswing.AjaxSwingManager;
import com.creamtec.ajaxswing.core.AjaxSwingProperties;
import com.creamtec.ajaxswing.core.ClientAgent;
import java.awt.geom.Rectangle2D;
import rmischedule.schedule.Schedule_View_Panel;
import javax.swing.*;
import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import rmischedule.main.Main_Window;
import rmischedule.security.*;
import rmischedule.components.graphicalcomponents.DragAndDropContainer;
import rmischedule.components.graphicalcomponents.DragAndDropLabel;
import rmischeduleserver.util.StaticDateTimeFunctions;
import rmischedule.schedule.schedulesizes.*;

/**
 *
 * @author  jason.allen
 */
public class SSchedule extends JPanel {

    private SWeek[] Weeks;
    public SEmployee myEmployee;
    private SMainComponent myClient;
    private mySchedShiftCount myShifts;
    public Schedule_View_Panel myParent;
    private RowHeader pnRowName;
    private boolean isBlankSchedule;
    private boolean highlightSchedule;
    public myEmployeeInfo pnTmp;
    private String scheduleId;
    public int row_count;
    private SSchedule thisObject;
    
    private boolean shouldShowThisSchedule;

    public SSchedule(SEmployee employee, SMainComponent client, int week_size, Schedule_View_Panel Parent, long schedId) {
        thisObject = this;
        myEmployee = employee;
        myClient = client;
        myParent = Parent;
        week_size = myParent.getWeekCount();
        myShifts = new mySchedShiftCount();
        Weeks = new SWeek[week_size];
        for (int i = 0; i < week_size; i++) {
            Weeks[i] = new SWeek(i, this);
        }
        scheduleId = schedId + "";
        if (employee.getId() != 0) {
            scheduleId = "0";
        }
        if (schedId == Long.MAX_VALUE) {
            isBlankSchedule = true;
        }
        highlightSchedule = false;
        row_count = 1;
        initializeHeader();
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        Parent.mySchedules.addSchedule(this);
    }
    /**
     * Dummy constructor used for SchedulesMapClass
     */
    public SSchedule(SEmployee employee) {
        myEmployee = employee;
        
        myShifts = new mySchedShiftCount();
        scheduleId = "0";
    }

    public void setClient(SMainComponent client) {
        myClient = client;
    }

    /**
     * These dispose methods are so very important... We have a massive memory leak
     * since it appears java's GC cannot handle all of our bidirection references...
     * Therefore it is very very important as you add classes that you properly
     * dispose all new private class, and remove all objects from sub panels. Please
     * verify from time to time in HEAP stack that this is still working.
     */
    public void dispose() {
        myEmployee = null;
        myClient = null;
        myShifts = null;
        myParent = null;
        pnRowName.dispose();
        pnRowName = null;
        
        pnTmp.dispose();
        pnTmp = null;
        thisObject = null;

        for (int w = 0; w < getWeeks().length; w++) {
            getWeeks()[w].dispose();
        }
        Weeks = null;

        this.removeAll();
    }

    /**
     * Does this schedule have a schedule id of LONG.MAX_VALUE if so then it is
     * the blank schedule!
     */
    public boolean isBlankSchedule() {
        return isBlankSchedule;
    }

    /**
     * Initializes the client header, so it doesn't have to do it every time, only
     * on initiation....
     */
    public void initializeHeader() {
        pnRowName = new RowHeader(this);
        pnTmp = new myEmployeeInfo(myParent.getLayeredPane(), myParent.FloatContainerPanel, this);
        pnTmp.setSched(this);
        pnRowName.add(pnTmp);
        pnRowName.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
    }

    public Point getMyLocation() {
        Point norm = this.getLocation();
        if (norm.y > 0) {
            //This is a hack, we somehow always get the correct client positioning, so if its
            //zero it is correct, however for employees it always shows half of the prior
            //row, not sure why prolly a getPosition wrong somewhere in the mix...
            norm.y = norm.y + (ComponentDimensions.currentSizes.get("RowHeader").height / 2);
        }
        Point cli = getClient().getLocation();
        return new Point(norm.x, norm.y + cli.y);
    }

    public int getHeaderHeight() {
        return pnRowName.getHeight();
    }

    public void setSelected(boolean t) {
        if (t) {
            pnRowName.setBackground(Color.yellow);
            //pnRowName.setBackground(Color.black);
        } else {
            pnRowName.setBackground(Color.WHITE);
        }
    }

    /**
     * Use this method to compare Schedule Id's, will make certain that the blank 
     * Schedule is ALWAYS on the bottom!
     */
    public String getSortingScheduleId() {
        return scheduleId;
    }

    public String getInsertScheduleId(Long currentTime) {
        if (Long.parseLong(scheduleId) == Long.MAX_VALUE) {
            return (currentTime + "");
        }
        return scheduleId;
    }

    /**
     * Use this method to get the real Id, if you are in the Blank schedule it will 
     * generate a new Schedule ID for you...
     */
    public String getInsertScheduleId() {
        return this.getInsertScheduleId(System.currentTimeMillis());
    }

    /**
     * Overload setVisible method to ensure that whenever the row is not visible the name
     * header won't be either and vice versa...G,...
     */
    public void setVisible(boolean val) {
        super.setVisible(val);
        try {
            pnRowName.setVisible(val);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    public void adjustHeader() {
        pnRowName.setBackground(Color.WHITE);
        pnTmp.setBackground(Color.WHITE);
        pnTmp.setBackground(Color.WHITE);
        if (myEmployee.isDeleted()) {
            if (myEmployee.getTerm().compareTo(Calendar.getInstance().getTime()) <= 0) {
                pnRowName.setBackground(Schedule_View_Panel.employee_del_color);
                pnTmp.setBackground(Schedule_View_Panel.employee_del_color);
            }
        }

        //Display reconc shit in weeks...
        for (int i = 0; i < getWeeks().length; i++) {
            getWeeks()[i].checkToDisplayReconcIcon();
        }

        try {
            if (myParent.isInitialized()) {
                this.myParent.Row_Headers.revalidate();
                this.myParent.Row_Headers.repaint();
            }
        } catch (Exception e) {
        }

    }

    /**
     * Method used to create a new ASchedule box returns a panel of employee info....wonderful...
     */
    public JPanel getHeader() {
        adjustHeader();
        return pnRowName;
    }

    public SMainComponent getClient() {
        return myClient;
    }

    public SEmployee getEmployee() {
        return myEmployee;
    }

    public SWeek getWeek(int i) {
        return getWeeks()[i];
    }

    public int getWeekSize() {
        return getWeeks().length;
    }

    public void addDShift(UnitToDisplay sp) {
        if (sp.getWeekNo() < getWeeks().length) {
            getWeeks()[sp.getWeekNo()].addDShift(sp);
        }
    }

    /**
     * Tests if this schedule has the employee....
     */
    public boolean containsEmployee(SEmployee se) {
        if (se == this.getEmployee()) {
            return true;
        }
        return false;
    }

    public boolean isBlank() {
        return (myShifts.size() == 0);
    }

    public void plantWeeks() {
        balanceWeeks();

        SwingUtilities.invokeLater(new Runnable() {

            public void run() {
                for (int i = 0; i < getWeeks().length; i++) {
                    final int ii = i;
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            add(getWeeks()[ii]);
                            getWeeks()[ii].plantRows();
                            if (AjaxSwingManager.isAjaxSwingRunning() && myParent.isDoneLoadingCompletely) {
                                ClientAgent.getCurrentInstance().getHTMLPage().setComponentDirty(thisObject, true);
                            }
                        }
                    });
                }
            }
        });

    }

    /**
     * ALWAYS USE THIS METHOD TO ADD ROWS!
     * This way all componenets know how many rows they have so that they all can
     * calculate their size properly!
     */
    public void addRowsToSchedule(int numToAdd) {
        row_count += numToAdd;
        for (int i = 0; i < getWeeks().length; i++) {
            getWeeks()[i].addRow(numToAdd);
        }
        if (numToAdd > 0) {
            myClient.rePosShifts();
        }
    }

    /**
     * ALWAYS USE THIS METHOD TO SUBTRACT ROWS!
     * This way all components know how many rows they have so that they all can calculate
     * their size properly!
     */
    public void removeRowsFromSchedule(int numToTake) {
        row_count -= numToTake;
        for (int i = 0; i < getWeeks().length; i++) {
            getWeeks()[i].removeRow(numToTake);
        }
        if (numToTake > 0) {
            myClient.rePosShifts();
        }
    }

    /**
     * Method responsible for making certain that weeks are long enough to hold the max
     * week length, loops through all weeks checking size then takes max weeks and adds
     * or subtracts weeks...
     */
    public int balanceWeeks() {
        int max = 1;
        int old_height = row_count;
        int usedRowSize = 0;
        for (int i = 0; i < getWeeks().length; i++) {
            usedRowSize = getWeeks()[i].getUsedRowSize();
            if (usedRowSize > max) {
                max = usedRowSize;
            }
        }

        if (max > row_count) {
            addRowsToSchedule(max - row_count);
        } else if (max < row_count) {
            removeRowsFromSchedule(row_count - (max));
        }
        return max;
    }

    /**
     * Use to get the row count for this particular schedule, fast as hell
     */
    public int getRowCount() {
        return row_count;
    }

    /**
     * Used for sizing our schedule
     */
    public int getRowCountForSize() {
        if (row_count == 0) {
            return 1;
        }
        return row_count;
    }

    public Point getMyPosition() {
        int x, y;
        x = getX() + myClient.getX();
        y = getY() + myClient.getY();
        return new Point(x, y);
    }

    /**
     * Used to generate a String to allow the schedules to be sorted by first employee name, then
     * by open schedule id's with shifts, then finally by blank schedule...
     * sets first letter to a if employee name exists
     * sets first letter to b if open schedule but has shifts
     * sets first letter to c if open with no shifts.
     */
    public String[] getMyCompareString() {
        String[] myReturn = new String[3];
        try {
            //Use id too in case duplicate names exist for clients
            myReturn[0] = getClient().getFullClientName();
            myReturn[1] = getClient().getId() + "";
            if (getEmployee().getId() == -1) {
                myReturn[2] = "a" + getEmployee().getName();
            } else if (getEmployee().getId() == -2) {
                myReturn[2] = "z" + getEmployee().getName();
            } else if (getEmployee().getId() > 0) {
                myReturn[2] = "B" + getEmployee().getName() + getEmployee().getId();
            } else if (!isBlankSchedule()) {
                myReturn[2] = "C" + getSortingScheduleId();
            } else {
                myReturn[2] = "D" + getSortingScheduleId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myReturn;
    }

    public void setHighlight(boolean b) {
        this.highlightSchedule = b;
        this.repaint();
    }

    //Somewhat hacky way of doing a selection border.  If we actually set the border,
    //it changes the size of the component and jacks up our layout a little bit
    public void paint(Graphics g) {
        super.paint(g);
        if (this.highlightSchedule) {
            Graphics2D g2d = (Graphics2D) g;
            Stroke oldStroke = g2d.getStroke();
            Paint oldPaint = g2d.getPaint();

            g2d.setPaint(Color.GREEN);
            g2d.setStroke(new BasicStroke(6.0f));
            g2d.draw(new Rectangle2D.Float(0, 0, this.getWidth(), this.getHeight()));

            g2d.setPaint(oldPaint);
            g2d.setStroke(oldStroke);
        }
    }

    /**
     * @return the Weeks
     */
    public SWeek[] getWeeks() {
        return Weeks;
    }

    /**
     * @return the shouldShowThisSchedule
     */
    public boolean isShouldShowThisSchedule() {
        return shouldShowThisSchedule;
    }

    /**
     * @param shouldShowThisSchedule the shouldShowThisSchedule to set
     */
    public void setShouldShowThisSchedule(boolean shouldShowThisSchedule) {
        this.shouldShowThisSchedule = shouldShowThisSchedule;
    }

    public class RowHeader extends JPanel implements ZoomListener {

        private SSchedule parentSched;

        public RowHeader(SSchedule mySched) {
            ComponentDimensions.addZoomListener(this);
            parentSched = mySched;
            setLayout(new GridLayout());
        }

        public void dispose() {
            this.removeAll();
            parentSched = null;
        }

        // Yeah this is kind of naughty, but it's the only easy way to do it
        public Dimension getPreferredSize() {
            Dimension d = ComponentDimensions.currentSizes.get("RowHeader");
            return new Dimension(d.width, d.height * parentSched.getRowCount());
        }

        public Dimension getMaximumSize() {
            return getPreferredSize();
        }

        public Dimension getMinimumSize() {
            return getPreferredSize();
        }

        public void zoomPerformed() {
            this.revalidate();
        }

        public void zoomFinished() {
        }
    }

    public class myEmployeeInfo extends DragAndDropLabel implements DragAndDropContainer {

        SSchedule Parent;

        public myEmployeeInfo(JLayeredPane panelToDisplayOn, Container panelContainingDropContainers, Object objectToPassOnDrop) {
            super(panelToDisplayOn, panelContainingDropContainers, objectToPassOnDrop);
            this.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_CLICK);
            this.putClientProperty(AjaxSwingProperties.COMPONENT_MOUSE_LEFT_EVENT_LISTENER, AjaxSwingConstants.MOUSE_ON_MOUSE_DOWN);
            BoxLayout myLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
            setLayout(myLayout);
             if (!Main_Window.parentOfApplication.checkSecurity(security_detail.MODULES.SCHEDULING_EDIT, security_detail.ACCESS.MODIFY)) {
                super.setDragEnabled(false);
            }
            try {
                SEmployee e = (SEmployee) objectToPassOnDrop;
                if (e.getId() == 0) {
                    super.setDragEnabled(false);
                }
            } catch (Exception e) {
            }
        }

        public void dispose() {
            Parent = null;
            this.removeAll();
        }

        public SEmployee getMyEmployee() {
            return Parent.getEmployee();
        }

        public void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            int rectWidth = ComponentDimensions.currentSizes.get("RowHeader").width;
            int rectHeight = ComponentDimensions.currentSizes.get("RowHeader").height;
            Rectangle bounds = this.getBounds();

            g.setColor(Schedule_View_Panel.client_color);


            if (!myEmployee.isDeleted()) {
                g.fillRect(0, 0, rectWidth, rectHeight);
            }
            g.setColor(new Color(249, 249, 249));
            g.fillRect(0, rectHeight / 3, rectWidth, rectHeight * 2 / 3);
            g2d.scale(ComponentDimensions.getScaleValue(), ComponentDimensions.getScaleValue());
            g.setColor(new Color(54, 54, 54));//current
            g.setColor(Color.BLACK);
            g.setFont(Main_Window.employee_font);
            int y = bounds.y + Main_Window.employee_font.getSize();
            g.drawString(myEmployee.getName(), bounds.x + 2, y);
            g.setFont(Main_Window.employee_info_font);
            y += Main_Window.employee_info_font.getSize() + 8;
            if (!Main_Window.isClientLoggedIn()) {
                g.drawString(myEmployee.getPhone(), bounds.x + 2, y);
            }
            y += Main_Window.employee_info_font.getSize() + 8;
            if (myEmployee.isDeleted()) {
                g.setColor(Color.RED);
                g.drawString("Terminated (" + myEmployee.getReadableTerm() + ")", bounds.x + 2, y);
            } else {
                if (!Main_Window.isClientLoggedIn()) {
                    if (myEmployee.getCell() != null && myEmployee.getCell().trim().length() > 0) {
                        g.drawString(myEmployee.getCell(), bounds.x + 2, y);
                    } else {
                        g.drawString(myEmployee.getPhone2(), bounds.x + 2, y);
                    }
                }
            }
        }

        public void setSched(SSchedule parent) {
            Parent = parent;
        }

        public void highlightMe(boolean highlightMe, Object myObj, MouseEvent evt) {
            SEmployee myEmp;
            try {
                myEmp = (SEmployee) myObj;
            } catch (Exception e) {
                try {
                    myEmp = ((SSchedule) myObj).getEmployee();
                } catch (Exception exe) {
                }
            }
            if (highlightMe) {
                thisObject.setHighlight(true);
            } else {
                thisObject.setHighlight(false);
            }
        }

        public void runOnDrop(Object objectToPassIn, MouseEvent evt, BufferedImage bi) {
            SEmployee employeeToMoveShiftsTo = null;
            SEmployee employeeToMoveShiftsFrom = null;
            SSchedule scheduleToMoveShiftsFrom = null;
            Hashtable<String, DShift> masters = new Hashtable();
            rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx meQuery = new rmischeduleserver.mysqlconnectivity.queries.RunQueriesEx();
            try {
                employeeToMoveShiftsTo = (SEmployee) objectToPassIn;
                employeeToMoveShiftsFrom = getEmployee();
                if (employeeToMoveShiftsFrom.getName().equals("")) {
                    scheduleToMoveShiftsFrom = Parent;
                } else {
                    scheduleToMoveShiftsFrom = myParent.mySchedules.getScheduleByClientEmployee(employeeToMoveShiftsFrom, (SClient)getClient(), "0");
                }
            } catch (Exception e) {
                try {
                    scheduleToMoveShiftsFrom = ((SSchedule) objectToPassIn);
                    employeeToMoveShiftsTo = Parent.getEmployee();
                    employeeToMoveShiftsFrom = scheduleToMoveShiftsFrom.getEmployee();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            if (employeeToMoveShiftsTo == employeeToMoveShiftsFrom) {
                return;
            }
            if (myParent.isClientEmpBanned((SClient)getClient(), employeeToMoveShiftsTo)) {
                JOptionPane.showMessageDialog(Main_Window.parentOfApplication, employeeToMoveShiftsTo.getName() + " has been banned from working for this location!", "ERROR!", JOptionPane.OK_OPTION);
                return;
            }
            String dialogText = "Move " + employeeToMoveShiftsFrom.getName() + "'s shifts to " + employeeToMoveShiftsTo.getName() + " permanently?";
            if (employeeToMoveShiftsFrom.getName().equals("")) {
                dialogText = "Move all open shifts to " + employeeToMoveShiftsTo.getName();
            } else if (employeeToMoveShiftsTo.getName().equals("")) {
                dialogText = "Take " + employeeToMoveShiftsFrom.getName() + " off of this schedule and mark shifts as open?";
            }
            String currentTime = StaticDateTimeFunctions.convertCalendarToDatabaseFormat(Calendar.getInstance());
            if (JOptionPane.showConfirmDialog(Main_Window.parentOfApplication, dialogText, "Confirm Shift Change", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                for (int w = 0; w < scheduleToMoveShiftsFrom.getWeeks().length; w++) {
                    for (int r = 0; r < scheduleToMoveShiftsFrom.getWeeks()[w].getRowSize(); r++) {
                        for (int d = 0; d < 7; d++) {
                            if (scheduleToMoveShiftsFrom.getWeeks()[w].getRow(r).getSShift(d).hasData()) {
                                if (currentTime.compareTo(scheduleToMoveShiftsFrom.getWeeks()[w].getRow(r).getSShift(d).myShift.getDatabaseDate()) <= 0) {
                                    if (scheduleToMoveShiftsFrom.getWeeks()[w].getRow(r).getSShift(d).myShift instanceof DShift) {
                                        DShift currShift = (DShift)scheduleToMoveShiftsFrom.getWeeks()[w].getRow(r).getSShift(d).myShift;
                                        if (masters.get(currShift.getMasterId()) == null || !currShift.isMaster()) {
                                            QueryGenerateShift myQueryShift = new QueryGenerateShift(currShift);
                                            myQueryShift.moveShift(getClient().getId(), employeeToMoveShiftsTo.getId() + "", getSortingScheduleId(), true);
                                            masters.put(currShift.getMasterId(), currShift);
                                            meQuery.add(myQueryShift.getMyQuery());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                try {
                    myParent.getConnection().executeUpdate(meQuery);
                } catch (Exception e) {
                }
            }
        }

        @Override
        protected void methodToRunOnClick(MouseEvent event) {
            if (myParent.getScheduleDashboard() != null && (Main_Window.isClientLoggedIn() || Main_Window.isEmployeeLoggedIn())) {
                myParent.getScheduleDashboard().setEmployeeInfo(getEmployee().getEmployee(), myParent);
            } else {
                if (event.getClickCount() > 1) {
                    myParent.editEmployee(getEmployee().getId() + "");
                }
            }
        }
    }

    public class mySchedShiftCount {

        private int myCount;

        public mySchedShiftCount() {
            myCount = 0;
        }

        public void add() {
            if (myCount++ <= 0) {
                myClient.rePosShifts();
            }
        }

        public void sub() {
            myCount--;
            if (myCount < 0) {
                myCount = 0;
            }

            if (myCount == 0 && getEmployee().getId() == 0) {
                myClient.removeSchedule(thisObject);
            }
        }

        public int size() {
            return myCount;
        }
    }

    /**
     * Get the number of shifts that this schedule has.
     */
    public int getShiftCount() {
        if (myClient.getMyBlankSchedule() == this && myShifts.size() == 0) {
            return 1;
        }
        return myShifts.size();
    }

    /**
     * This method should only be called from SRow whenever a new SShift with data
     * is added to the SRow this ensures the schedule knows exactly how many shifts
     * it has on it, not only that but which id's...greatness...
     */
    public void addShiftToSchedule() {
        myShifts.add();
    }

    /**
     * This method should only be called from SRow whenever a SShift is removed from it
     */
    public void removeShiftFromSchedule() {
        myShifts.sub();
    }
}
